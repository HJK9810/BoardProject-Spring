package spring.board.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.board.domain.Users;
import spring.board.repository.RefreshTokenRepository;
import spring.board.repository.UserRepository;
import spring.board.security.TokenRequestDto;
import spring.board.security.UserDetailsVO;
import spring.board.security.UserResponseDto;
import spring.board.security.UserTokenDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public UserTokenDto login(UserResponseDto userResponseDto) {
        log.info("login start");
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
         Users user = userRepository.findByEmail(userResponseDto.getEmail()).get();
         UserDetailsVO userDetailsVO = new UserDetailsVO(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userResponseDto.getEmail(), userResponseDto.getPassword(), userDetailsVO.getAuthorities());
        log.info("create token={}", authenticationToken);

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        UserDetailsVO authentication = new UserDetailsVO(user);
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("auth check={}", authentication);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        UserTokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        log.info("create jwt token={}", tokenDto);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getUsername())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        log.info("save refresh token={}", refreshToken);

        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public UserTokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 User ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 User ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName());
        if (refreshToken == null) throw new RuntimeException("로그아웃 된 사용자입니다.");

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");

        // 5. 새로운 토큰 생성
        Users user = userRepository.findByEmail(authentication.getName()).get();
        UserTokenDto tokenDto = tokenProvider.generateTokenDto(new UserDetailsVO(user));
        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}
