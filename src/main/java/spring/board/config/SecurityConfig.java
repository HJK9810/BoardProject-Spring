package spring.board.config;

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
import spring.board.security.SimpleCorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailImpl customUserDetail;

    public SecurityConfig(CustomUserDetailImpl customUserDetail) {
        this.customUserDetail = customUserDetail;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(customUserDetail)
                .and().build();
    }

    @Bean
    public SimpleCorsFilter corsFilter() {
        return new SimpleCorsFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable().csrf().disable()
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.GET,"/image/**", "/login", "/logout").permitAll()
                                .requestMatchers("/answer/**").hasRole("ADMIN")
                                .requestMatchers("/question/**").hasRole("USER")
                ).formLogin(form -> form.loginPage("/login").permitAll()
                        .defaultSuccessUrl("/question/list", true)
                        .loginProcessingUrl("/login").defaultSuccessUrl("/question/list", true))
                .logout(logout -> logout.deleteCookies("JSESSIONID", "remember-me") // 로그아웃 후 쿠키 삭제
                        .logoutUrl("/logout").logoutSuccessUrl("/login")); // 로그아웃 성공 후 이동페이지
//.httpBasic(withDefaults())
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
