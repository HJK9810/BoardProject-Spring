package spring.board.security;

import lombok.Data;

@Data
public class UserResponseDto { // 토큰 저장된 필요정보들

    private String email;
    private String password;
}