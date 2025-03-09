package com.kh.mvc.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.kh.mvc.model.dto.UserDTO;

public class UserDAO {
	
	public List<UserDTO> findAllTbUser() {
		
		// ArrayList 에 아이디랑 비밀번호가 김관민, 김관민짱  조아라, 조아라짱 인 userDTO 두개를 담아
		// 리턴시키기 
		UserDTO udto = new UserDTO();
		UserDTO udto2 = new UserDTO();
		udto.setUserId("김관민");
		udto.setUserPw("김관민짱");
		udto2.setUserId("조아라");
		udto2.setUserPw("조아라짱");
		
		List<UserDTO> list = new ArrayList<UserDTO>();
		list.add(udto);
		list.add(udto2);

		return list;
	}
	
	public List<UserDTO> insertTbUser() {
		UserDTO udto = new UserDTO();
		UserDTO udto2 = new UserDTO();
		UserDTO udto3 = new UserDTO();
		udto.setUserId("김관민");
		udto.setUserPw("김관민짱");
		udto2.setUserId("조아라");
		udto2.setUserPw("조아라짱");
		udto3.setUserId("백종엽");
		udto3.setUserPw("백종엽짱");
		
		System.out.println("아무거나");
		
		List<UserDTO> list = new ArrayList<UserDTO>();
		list.add(udto);
		list.add(udto2);
		list.add(udto3);
		
		return list;
	}
}
