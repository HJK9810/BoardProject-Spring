package spring.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.board.domain.Users;
import spring.board.repository.UserRepository;
import spring.board.security.UserResponseDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserResponseDto findUserByEmail(String email) {
        UserResponseDto dto = new UserResponseDto();
        Users user = repository.findByEmail(email).get();
        dto.setEmail(user.getEmail());

        return dto;
    }
}