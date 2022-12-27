package spring.board.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.board.domain.Users;
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

        UserTokenDto tokenDto = null;
        try {
            // 인증 정보를 기반으로 JWT 토큰 생성
            tokenDto = tokenProvider.generateTokenDto(authentication);

            // RefreshToken 저장
            RefreshToken refreshToken = RefreshToken.builder().key(authentication.getUsername()).value(tokenDto.getRefreshToken()).build();
            refreshTokenRepository.save(refreshToken);
        } catch (NullPointerException e) {
            throw new NullPointerException("등록되지 않은 사용자입니다.");
        }

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public UserTokenDto reissue(UserTokenDto oldToken) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(oldToken.getRefreshToken())) {
            log.warn("Refresh Token 이 유효하지 않습니다.");
            return null;
        }

        // 2. Access Token 에서 User 정보 가져오기
        Authentication authentication = tokenProvider.getAuthentication(oldToken.getAccessToken());

        // 3. 저장소에서 User email 을 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName());

        // 4. Refresh Token 일치하는지 검사
        if (refreshToken == null) throw new RuntimeException("로그아웃 된 사용자입니다.");
        else if (!refreshToken.getValue().equals(oldToken.getRefreshToken())) throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");

        // 5. 새로운 토큰 생성
        Users user = userRepository.findByEmail(authentication.getName()).get();
        UserTokenDto tokenDto = tokenProvider.generateTokenDto(new UserDetailsVO(user));
        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);
        log.info("토큰이 갱신되었습니다.");

        // 토큰 발급
        return tokenDto;
    }
}
