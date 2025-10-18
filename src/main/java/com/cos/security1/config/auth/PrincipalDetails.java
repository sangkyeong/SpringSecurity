package com.cos.security1.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.model.User;

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
public class PrincipalDetails implements UserDetails{

	private User user; //컴포지션

	public PrincipalDetails(User user) {
		this.user = user;
	}

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
}