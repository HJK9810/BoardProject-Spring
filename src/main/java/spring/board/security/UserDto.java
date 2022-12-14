package spring.board.security;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import spring.board.domain.AdminCheck;

@Data
public class UserDto { // 토큰 저장된 필요정보들

    private String email;

    @Enumerated(EnumType.STRING)
    private AdminCheck role;
}
