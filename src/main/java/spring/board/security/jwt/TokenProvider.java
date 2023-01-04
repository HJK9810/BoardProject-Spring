package spring.board.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import spring.board.exception.ApiExceptions;
import spring.board.exception.ErrorCode;
import spring.board.security.UserDetailsVO;
import spring.board.web.dto.UserTokenDto;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.Jwts.parserBuilder;

@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 6;            // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;  // 7일

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public UserTokenDto generateTokenDto(UserDetailsVO authentication) {
        // 권한들 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = builder()
                .setSubject(authentication.getUsername())   // payload "sub": username
                .claim(AUTHORITIES_KEY, authorities)        // payload "auth": role
                .setExpiration(accessTokenExpiresIn)        // payload "exp": 만료 시간
                .signWith(key, SignatureAlgorithm.HS512)    // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        String refreshToken = builder().setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS512).compact();

        return UserTokenDto.builder().grantType(BEARER_TYPE).accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken).build();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String accessToken) {
        if (accessToken.equals("undefined")) throw new ApiExceptions(ErrorCode.MEMBER_NOT_FOUND);
//        Claims claims = parseClaims(accessToken); // 토큰 복호화
        Map<String, Object> claims = decodeToken(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) throw new ApiExceptions(ErrorCode.INVALID_AUTH_TOKEN);

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.get("sub").toString(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String token) {
        try {
            parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            throw new ApiExceptions(ErrorCode.WRONG_JWT_SIGNATURE);
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            throw new ApiExceptions(ErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new ApiExceptions(ErrorCode.JWT_TOKEN_NOT_WORK);
        } catch (IllegalArgumentException e) {
            throw new ApiExceptions(ErrorCode.WRONG_JWT_TOKEN);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            throw new ApiExceptions(ErrorCode.WRONG_JWT_SIGNATURE);
        }
    }

    private Map<String, Object> decodeToken(String accessToken) {
        String payload = new String(Base64.getDecoder().decode(accessToken.split("\\.")[1]));

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(payload, Map.class);
        } catch (JsonProcessingException e) {
            throw new ApiExceptions(ErrorCode.MISMATCH_REFRESH_TOKEN);
        }
    }
}
