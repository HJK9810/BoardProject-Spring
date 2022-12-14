//package spring.board.security;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import spring.board.domain.User;
//import spring.board.repository.UserRepository;
//
//@Service
//@RequiredArgsConstructor
//public class UserDetailImpl implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(username).get();
//
//        if (user == null) throw new UsernameNotFoundException("User not authorized.");
//
//        return new UserDetailsVO(user);
//    }
//}
