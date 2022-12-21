package spring.board.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.security.jwt.AuthService;
import spring.board.service.UserService;
import spring.board.web.dto.UserResponseDto;
import spring.board.web.dto.UserTokenDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/user/{email}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(@RequestBody UserResponseDto user) {
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/reissue")
    public ResponseEntity<UserTokenDto> reissue(@RequestBody UserTokenDto tokenDto) {
        return ResponseEntity.ok(authService.reissue(tokenDto));
    }

    @GetMapping("/logout/{user}")
    public ResponseEntity<UserResponseDto> logout(@PathVariable String user) {
        return ResponseEntity.ok(userService.logout(user));
    }
}
