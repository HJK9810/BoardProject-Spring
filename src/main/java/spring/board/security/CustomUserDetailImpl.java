package spring.board.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.board.domain.Users;
import spring.board.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> object = userRepository.findByEmail(username);
        Users user = null;

        if (object.isPresent()) user = object.get();
        if (user == null) throw new UsernameNotFoundException("User not authorized.");

        return new UserDetailsVO(user);
    }
}
