package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.board.repository.RefreshTokenRepository;
import spring.board.web.dto.UserResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final RefreshTokenRepository repository;

    @Override
    public UserResponseDto findUserByEmail(String email) {
        UserResponseDto dto = new UserResponseDto();
        dto.setEmail(email);

        return dto;
    }

    @Override
    public UserResponseDto logout(String email) {
        repository.deleteRefreshToken(email);
        return new UserResponseDto();
    }
}