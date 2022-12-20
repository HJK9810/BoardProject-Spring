package spring.board.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.security.TokenRequestDto;
import spring.board.security.UserResponseDto;
import spring.board.security.UserTokenDto;
import spring.board.security.jwt.AuthService;
import spring.board.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/user/{email}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String email) {
        return new ResponseEntity<UserResponseDto>(userService.findUserByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody UserResponseDto user) {
        log.info("login start");
        log.info("user={}", user);
        UserTokenDto login = authService.login(user);
        log.info("login token={}", login);
        return new ResponseEntity<>(login, HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<UserTokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
