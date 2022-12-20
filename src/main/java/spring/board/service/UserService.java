package spring.board.service;

import spring.board.web.dto.UserResponseDto;

public interface UserService {
    UserResponseDto findUserByEmail(String email);

    UserResponseDto logout(String emil);
}