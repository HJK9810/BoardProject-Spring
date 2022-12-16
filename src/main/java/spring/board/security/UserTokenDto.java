package spring.board.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTokenDto {
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
}
