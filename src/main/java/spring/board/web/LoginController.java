package spring.board.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.board.domain.Users;
import spring.board.repository.UserRepository;
import spring.board.security.CustomUserDetailImpl;
import spring.board.security.UserDetailsVO;
import spring.board.security.UserDto;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailImpl customUserDetail;
    private final UserRepository repository;

    @PostMapping(value="/login")
    public UserDetailsVO login(@RequestBody UserDto dto, HttpSession session) {
        String username = dto.getEmail();
        String password = dto.getPassword();
        log.info("username={}", username);
        log.info("passwd={}", password);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        UserDetails userDetail = customUserDetail.loadUserByUsername(username);
        log.info("userDetail={}", userDetail);
        log.info("userName={}", userDetail.getUsername());
        log.info("passwd={}", userDetail.getPassword());
        log.info("authorities={}", userDetail.getAuthorities());
        Users user = repository.findByEmail(userDetail.getUsername()).get();
        log.info("user={}", user);
        return (UserDetailsVO) userDetail;
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
