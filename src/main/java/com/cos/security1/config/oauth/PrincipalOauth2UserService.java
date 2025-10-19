package com.cos.security1.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 소셜 로그인 후 후처리하는 곳
 */
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;

//	@Autowired
//	private UserRepository userRepository;

	//생성자 주입방식으로 변경
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	//구글로 부터 받은 userRequest데이터에 대한 후처리 함수
	//함수 종료 시 @AuthenticationPrincipal 어노테이션이 만들어진다.
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : "+ userRequest.getClientRegistration());
		System.out.println("getAccessToken : "+ userRequest.getAccessToken());
		OAuth2User oauth2User = super.loadUser(userRequest);
		System.out.println("loadUser : "+ oauth2User.getAttributes());

		String provider = userRequest.getClientRegistration().getClientId(); // google
		String providerId = oauth2User.getAttribute("sub");
		String username = provider+"_"+providerId; //google_1234567
		String password = passwordEncoder.encode("get??");
		String email = oauth2User.getAttribute("email");
		String role = "ROLE_USER";

		User userEntity = userRepository.findByUsername(username);
		if(userEntity == null) {
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(provider)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);
		}else {
			System.out.println("이미 가입한 회원입니다.");
		}
		return new PrincipalDetails(userEntity, oauth2User.getAttributes());
	}
}
