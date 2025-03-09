package com.kh.mvc.controller;

import java.util.List;

import com.kh.mvc.model.dto.UserDTO;
import com.kh.mvc.model.service.UserService;

public class UserController {
	
	public List<UserDTO> findAllTbUser() {

		// UserService 에 
		// findAllTbUser 메서드 호출
		UserService us = new UserService();
		List<UserDTO> list = us.findAllTbUser();
		return list;
	}
	
	public List<UserDTO> insertTbUser() {
		
		UserService us = new UserService();
		List<UserDTO> list = us.insertTbUser();
		return list;
	}
	
	
}
