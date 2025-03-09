package com.kh.mvc.model.service;

import java.util.List;

import com.kh.mvc.model.dao.UserDAO;
import com.kh.mvc.model.dto.UserDTO;

public class UserService {
	
	public List<UserDTO> findAllTbUser() {
		
		// UserDAO 자료형
		// userDao 변수명
		// new 새로운 객체 생성하겠다
		// UserDAO() 객체
		
		// UserDAO 에 
		// findAllTbUser 메서드 호출
		UserDAO udao = new UserDAO();
		List<UserDTO> list = udao.findAllTbUser();
		return list;
	}
	
	public List<UserDTO> insertTbUser() {
		
		UserDAO udao = new UserDAO();
		List<UserDTO> list = udao.insertTbUser();
		return list;
	}
}
