package spring.board.service;

import spring.board.security.UserResponseDto;

public interface UserService {
    UserResponseDto findUserByEmail(String email);
}