package kr.co.lotteon.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        // 로그인 설정
        httpSecurity.formLogin(login -> login
                                        .loginPage("/member/login")
                                        .defaultSuccessUrl("/")
                                        .failureUrl("/member/login?success=100")
                                        .usernameParameter("uid")
                                        .passwordParameter("pass"));

        ///////////자동로그인 설정 구현예정///////////


        // 로그아웃 설정
        httpSecurity.logout(logout -> logout
                                        .invalidateHttpSession(true)
                                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                        .logoutSuccessUrl("/member/login?success=300"));

        ///////////배포전 인가 수정하기/////////
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                                                        .requestMatchers("/").permitAll()
                                                        .requestMatchers("/member/**").permitAll()
                                                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                                        .anyRequest().permitAll());

        // 사이트 위변조 방지 설정
        httpSecurity.csrf(CsrfConfigurer::disable);

        return httpSecurity.build();
    }

    // Security 인증 암호화 인코더 설정
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
