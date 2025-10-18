package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //secured, prePostEnabled 어노테이션 활성화
public class SecurityConfig {

	@Bean //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해줌
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("deprecation")
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests()
				.requestMatchers("/user/**").authenticated()
				.requestMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
				// .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') and hasRole('ROLE_USER')")
				.requestMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
				.anyRequest().permitAll()
				.and()
				.formLogin()
				.loginPage("/loginForm")
				.loginProcessingUrl("/login") // /login 주소 호출 시 시큐리티가 낚아채서 대신 로그인 진행
				.defaultSuccessUrl("/"); //로그인 성공 시 메인 페이지로 이동
//				.and()
//				.oauth2Login()
//				.loginPage("/login")
//				.userInfoEndpoint()
//				.userService(principalOauth2UserService);

		return http.build();
	}
}