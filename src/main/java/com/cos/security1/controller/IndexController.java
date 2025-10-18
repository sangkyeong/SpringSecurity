package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller	//view를 리턴하겠다
public class IndexController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

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

	@GetMapping("/user")
	public String user() {
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
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
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
		return "개인정보";
	}
}
