package spring.board.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import spring.board.domain.Users;
import spring.board.repository.UserRepository;
import spring.board.security.CustomUserDetailImpl;
import spring.board.security.UserDetailsVO;
import spring.board.security.jwt.TokenProvider;
import spring.board.security.userResponseDto;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailImpl customUserDetail;
    private final UserRepository repository;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public userResponseDto login(@RequestBody userResponseDto user, HttpServletResponse response) {
        Users member = repository.findByEmail(user.getEmail()).get();
//        if (!passwordEncoder.matches(user.getPassword(), member.getPassword())) {
//            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
//        }
        log.info("member={}", member.getEmail());
        UserDetailsVO userDetailsVO = new UserDetailsVO(member);
        List<String> roles = new ArrayList<>();
        userDetailsVO.getAuthorities().iterator().forEachRemaining(role -> {
            roles.add(String.valueOf(role));
        });

        String token = tokenProvider.createToken(userDetailsVO.getUsername(), roles);
        response.setHeader("X-AUTH-TOKEN", token);

        Cookie cookie = new Cookie("X-AUTH-TOKEN", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return user;
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserTokenDto> login(@RequestBody userResponseDto requestDto) {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(requestDto.getEmail(), requestDto.getPassword());
//        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
//        UserTokenDto tokenDto = tokenProvider.createToken(authentication).login(requestDto);
//        return ResponseEntity.ok(tokenDto);
//    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }
}
