package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller	//view를 리턴하겠다
@RequiredArgsConstructor
public class IndexController {

//	@Autowired
//	private UserRepository userRepository;

//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;

	//생성자 주입방식으로 변경
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	@GetMapping("/test/login")
	public @ResponseBody String loginTest(
			Authentication authentication,
			@AuthenticationPrincipal UserDetails userDetails //DI의존성 주입
			) {
		System.out.println("test/login===========");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal(); //소셜로그인은 들고오지못함

		//아래 2가지 방식으로 로그인 사용자 정보를 조회가능
		System.out.println("authentication : "+principalDetails);
		//authentication : PrincipalDetails(user=User(id=1, username=test, password=***, email=test@naver.com, role=ROLE_USER, provider=null, providerId=null, createDate=2025-10-19 00:53:54.797028))

		System.out.println("userDetails : "+userDetails);
		//userDetails : PrincipalDetails(user=User(id=1, username=test, password=$2a$10$0kR3e4kLMbP.8CyToBy7ceYw1s8o0d7bALsZ1DfzfMWFKSdqnUy7S, email=test@naver.com, role=ROLE_USER, provider=null, providerId=null, createDate=2025-10-19 00:53:54.797028))
		return "세션 정보";
	};

	@GetMapping("/test/oauth/login")
	public @ResponseBody String loginOauthTest(
			Authentication authentication,
			@AuthenticationPrincipal OAuth2User oauth //DI의존성 주입
			) {
		System.out.println("test/oauth/login===========");
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

		//아래 2가지 방식으로 로그인 사용자 정보를 조회가능
		System.out.println("authentication : "+oAuth2User.getAttributes());
		System.out.println("oauth : "+oauth.getAttributes());
		return "oauth 세션 정보";
	};

	@GetMapping({"","/"})
	public String index() {
		// 머스테치 기본 폴터 src/main/resources/
		//뷰 리졸버 설정 : templetes(prefix), .mustache(suffix) 생략 -> pom.xml에 머스테치 의존성 등록함
		// application.yml에 아래 등록되어 있음 생략해도 상관 x
//		  mvc:
//			    view:
//			      prefix: /templates/
//			      suffix: .mustache

		return "index";
	}

	//일반로그인, 소셜 모두 PrincipalDetails로 받을 수 있음!
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails : "+principalDetails.getUser());
		return "user";
	}

	@GetMapping("/admin")
	public String admin() {
		return "admin";
	}

	@GetMapping("/manager")
	public String manager() {
		return "manager";
	}

	//SpringSecurity에서 동일한 주소가 있어 따로 설정필요 - SecurityConfig 파일 생성 후 작동안함.
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}

	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}

	@PostMapping("/join")
	public String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		user.setPassword(encPassword);

		userRepository.save(user);
		return "redirect:/loginForm";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}

	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 메소드 실행되기 직전에 실행, 여러 권한을 확인할 때 사용
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터 정보";
	}
}
