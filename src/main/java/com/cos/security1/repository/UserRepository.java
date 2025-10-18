package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

//CRUD 함수를 Jpa가 들고 있음
// @Repository 안해도 IoC됨 JpaRepository를 상속했기 때문에
public interface UserRepository extends JpaRepository<User, Integer>{

	public User findByUsername(String username);

}
