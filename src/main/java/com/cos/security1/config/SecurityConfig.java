package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured, prePostEnabled 어노테이션 활성화
public class SecurityConfig {
//
//	@Autowired
//	private PrincipalOauth2UserService principalOauth2UserService;

	@Bean //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해줌
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			PrincipalOauth2UserService principalOauth2UserService // 메서드 파라미터 주입
			) throws Exception {
		http
        	.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/user/**").authenticated()
                    .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER") // ROLE_ 생략
                    .requestMatchers("/admin/**").hasRole("ADMIN")                 // ROLE_ 생략
                    .anyRequest().permitAll()
            )
        	.formLogin(form -> form
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true) // true면 항상 이동
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/loginForm")
                .userInfoEndpoint(userInfo -> userInfo.userService(principalOauth2UserService))
            );
//		http.authorizeRequests()
//				.requestMatchers("/user/**").authenticated()
//				.requestMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
//				// .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
//				.requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
//				.anyRequest().permitAll()
//				.and()
//				.formLogin()
//				.loginPage("/loginForm")
//				.loginProcessingUrl("/login") // /login 주소 호출 시 시큐리티가 낚아채서 대신 로그인 진행
//				.defaultSuccessUrl("/") //로그인 성공 시 메인 페이지로 이동
//				.and()
//				.oauth2Login()
//				.loginPage("/loginForm") // 구글 로그인 완료 후 후처리 필요!!
//				.userInfoEndpoint()
//				.userService(principalOauth2UserService); // 로그인 완료 시 코드를 받는게 아니고 액세스 토큰+사용자 프로필정보를 받음
////				.and()
////				.oauth2Login()
////				.loginPage("/login")
////				.userInfoEndpoint()
////				.userService(principalOauth2UserService);

		return http.build();
	}
}