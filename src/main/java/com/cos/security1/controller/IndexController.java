package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller	//view를 리턴하겠다
public class IndexController {

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
}
