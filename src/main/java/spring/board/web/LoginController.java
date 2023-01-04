package spring.board.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.board.security.jwt.AuthService;
import spring.board.service.UserService;
import spring.board.web.dto.UserResponseDto;
import spring.board.web.dto.UserTokenDto;

@Tag(name = "Login API", description = "로그인 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/user/{email}")
    @Operation(summary = "유저정보 보기")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<UserTokenDto> login(@RequestBody UserResponseDto user) {
        log.info("사용자가 로그인을 시도 하였습니다.");
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/reissue")
    @Operation(summary = "로그인 연장")
    public ResponseEntity<UserTokenDto> reissue(@RequestBody UserTokenDto tokenDto) {
        log.info("토큰을 갱신하겠습니다.");
        return ResponseEntity.ok(authService.reissue(tokenDto));
    }

    @GetMapping("/logout/{user}")
    @Operation(summary = "로그아웃")
    public ResponseEntity<UserResponseDto> logout(@PathVariable("user") String user) {
        log.info("사용자 {}가 로그아웃 하였습니다.", user);
        return ResponseEntity.ok(userService.logout(user));
    }
}
