package spring.board.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import spring.board.security.CustomUserDetailImpl;
import spring.board.security.jwt.TokenProvider;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailImpl customUserDetail;
    private final TokenProvider jwtProvider;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetail)
                .and().build();
    }

//    @Bean
//    public SimpleCorsFilter corsFilter() {
//        return new SimpleCorsFilter();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.GET,"/image/**", "/api/login", "/logout", "/api").permitAll()
                                .requestMatchers("/answer/**").hasRole("ADMIN")
                                .requestMatchers("/question/**").hasRole("USER")).httpBasic(withDefaults());
//                ).formLogin(form -> form.loginPage("/api/login").permitAll()
//                        .defaultSuccessUrl("/question/list", true)
//                        .loginProcessingUrl("/api/login").defaultSuccessUrl("/question/list", true))
//                .logout(logout -> logout.deleteCookies("JSESSIONID", "remember-me") // 로그아웃 후 쿠키 삭제
//                        .logoutUrl("/api/logout").logoutSuccessUrl("/api/login")) // 로그아웃 성공 후 이동페이지
//                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class); // set jwt filter
//.httpBasic().disable()
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}user").roles("USER").build();
        UserDetails admin = User.withUsername("admin").password("{noop}admin").roles("ADMIN", "USER").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
