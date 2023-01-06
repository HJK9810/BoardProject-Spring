package spring.board.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.board.domain.Users;
import spring.board.exception.ApiExceptions;
import spring.board.exception.ErrorCode;
import spring.board.repository.RefreshTokenRepository;
import spring.board.repository.UserRepository;
import spring.board.security.UserDetailsVO;
import spring.board.web.dto.RefreshToken;
import spring.board.web.dto.UserResponseDto;
import spring.board.web.dto.UserTokenDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserTokenDto login(UserResponseDto userResponseDto) {

        // Login ID/PW 를 기반으로 인증 체크
        UserDetailsVO authentication = userRepository.findByEmail(userResponseDto.getEmail()).map(UserDetailsVO::new).orElse(null);

        // 인증 정보를 기반으로 JWT 토큰 생성
        if (authentication == null) throw new ApiExceptions(ErrorCode.MEMBER_NOT_FOUND);
        UserTokenDto tokenDto =  tokenProvider.generateTokenDto(authentication);

        // RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder().key(authentication.getUsername()).value(tokenDto.getRefreshToken()).build();
        refreshTokenRepository.save(refreshToken);

        log.info("사용자 {}가 로그인 하였습니다.", userResponseDto.getEmail());
        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public UserTokenDto reissue(UserTokenDto oldToken) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(oldToken.getRefreshToken())) {
            throw new ApiExceptions(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 2. Access Token 에서 User 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(oldToken.getAccessToken());

        // 3. 저장소에서 User email 을 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName());

        // 4. Refresh Token 일치하는지 검사
        if (refreshToken == null) throw new ApiExceptions(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        else if (!refreshToken.getValue().equals(oldToken.getRefreshToken())) throw new ApiExceptions(ErrorCode.MISMATCH_REFRESH_TOKEN);

        // 5. 새로운 토큰 생성
        Users user = userRepository.findByEmail(authentication.getName()).orElse(null);
        UserTokenDto tokenDto = tokenProvider.generateTokenDto(new UserDetailsVO(user));
        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        log.info("토큰이 갱신되었습니다.");

        // 토큰 발급
        return tokenDto;
    }
}
