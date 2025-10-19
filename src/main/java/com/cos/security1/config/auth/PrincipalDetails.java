package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security1.model.User;

import lombok.Data;

/**
 * 시큐리티가 /login을 낚아채서 로그인 진행
 * 로그인 진행이 완료 시 세션을 만들어 줌 -> Security ContextHolder에 세션정보를 저장시킴
 * 세션정보안에 들어가야 할 오브젝트가 정해져 있음
 * 	- Authentication 타입객체
 * 		- user정보가 있어야 됨
 * 		- user오브젝트 타입
 * 			- UserDetails타입 객체
 *
 * 시큐리티 세션 객체 => Authentication 객체 -> UserDetails
 *
 */
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private User user; //컴포지션

	//일반 로그인 시 받는 곳
	public PrincipalDetails(User user) {
		this.user = user;
	}

	//구글 로그인 시 받는 곳
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	private Map<String, Object> attributes; //Oauth2User안에 들어있는 객체를 받기 위한 변수

	//해당 유저의 권한을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {

			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정 만료여부 확인
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//계정 잠김여부 확인
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//비밀번호 만료 여부 확인
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//계정 확인여부 확인
	@Override
	public boolean isEnabled() {
		/**
		 * 예) 1년이 지나면 휴먼계정으로 전환
		 * User객체에 로그인 날짜 필요
		 * 현재시간 - 로그인 시간 = 1년 초과 시 return false;
		 */

		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return (String) attributes.get("sub");
	}
}