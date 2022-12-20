package spring.board.web.dto;

import lombok.Data;

@Data
public class UserResponseDto { // for login

    private String email;
    private String password;
}
