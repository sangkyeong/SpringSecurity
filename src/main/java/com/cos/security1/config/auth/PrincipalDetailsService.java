package com.cos.security1.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

//시큐리티 설정에서 .loginProcessingUrl("/login")
// /login요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어있는 loadUserByUsername 함수 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//로그인 화면단에서 사용자 이름 name변수명이 username이어야 잘 받아짐!
		// 커스텀 하려면 시큐리티 설정에서 .usernameParameter("username2") 추가
		System.out.println("username : "+username);
		User userEntity = userRepository.findByUsername(username);
		if(userEntity != null) {
			return new PrincipalDetails(userEntity); //시큐리티 세션 객체(내부 Authentication 객체(내부 UserDetails))에 들어감
		}
		return null;
	}

}
