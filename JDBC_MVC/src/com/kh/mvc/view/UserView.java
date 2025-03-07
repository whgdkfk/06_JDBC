package com.kh.mvc.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.kh.mvc.controller.UserController;
import com.kh.mvc.model.dto.UserDTO;

/**
 * MemberView 클래스는 JDBC 실습을 위해 생성하였으며,
 * 사용자에게 입력 및 출력을 수행하는 메소드를 제공합니다.
 * 
 * @author: 종로 C 강의장
 * @version: 0.1
 * @date: 2025-03-04
 */
public class UserView {
	
	private Scanner sc = new Scanner(System.in);
	private UserController userController = new UserController();
	
	/**
	 * 프로그램 시작 시 사용자에게 보여줄 
	 * 메인 화면을 출력해주는 메소드입니다.
	 */
	public void mainMenu() {
		
		while(true) {
			
			System.out.println("--- USER 테이블 관리 프로그램 ---");
			System.out.println("1. 회원 전체 조회");
			
			// 한 명의 회원 정보 추가는 DB에 한 행을 넣겠다는 의미
			// == 5개 값 필요(USER_NO, USER_ID, USER_PW, USER_NAME, ENROLL_DATE)
			// → 필요한 값을 사용자가 입력할 수 있도록 유도
			System.out.println("2. 회원 추가");			
			
			System.out.println("9. 프로그램 종료");
			System.out.print("이용할 메뉴를 선택해주세요 > ");
			
			int menuNo = 0;
			
			// 숫자 외 다른 문자를 적어도 오류 발생X
			try {
			menuNo = sc.nextInt();
			
			} catch(InputMismatchException e) {
			
				sc.nextLine();
				continue;
			}
			sc.nextLine();
			
			switch(menuNo) {
				case 1: 
					findAll(); // 회원 전체 조회
					break;
				case 2:
					insertUser();
					break;
				case 9: 
					System.out.println("프로그램 종료~👻"); 
					return;
				default: 
					System.out.println("잘못된 메뉴 선택입니다.");
				
			}
			
		}
		
	}
	
	// 회원 전체 정보를 조회해주는 기능
	private void findAll() {
		
		System.out.println("\n--- 회원 전체 목록입니다. ---");
		
		// Controller야, 저기 DB 가서 회원 전체 목록 좀 가져와줘
		// 가지고 온 데이터들을 출력해주려고
		List<UserDTO> list = userController.findAll();
		
		System.out.println("\n조회된 총 회원의 수는 " + list.size() + "명입니다.");
		
		// isEmpty() vs size 
		// → isEmpty()가 의미 전달이 명확(비어있는지 확인)하므로 더 좋음
		// 부정적 의미로 조건식 만들어야 함 → !
		if( ! (list.isEmpty()) ) {
			
			System.out.println("==========================================================");
			for(UserDTO user : list) {
				System.out.print(user.getUserName() + "님의 정보");
				System.out.print("\n아이디: " + user.getUserId());
				System.out.print("\t가입일: " + user.getEnrollDate());
				System.out.println();
			}
			System.out.println("==========================================================");
			
		} else {
			System.out.println("회원이 존재하지 않습니다.");
		}
		
	}

	// 객체 지향 프로그래밍 
	// 현실 세계에 존재하는 객체를
	// 속성과 행위로 만들어서
	// 객체간의 상호작용(메서드 호출)을 통해
	// 프로그래밍 하는 기법
	
	/**
	 * 2. 회원 추가
	 * TB_USER에 INSERT할 값을 사용자에게 입력받도록 유도하는 화면
	 */
	private void insertUser() {
		
		System.out.println("--- 회원 추가 페이지입니다. ---");
		
		// System.out.print("회원 번호를 입력하세요. > ");
		// int userNo = sc.nextInt();
		System.out.print("아이디를 입력하세요. > ");

		// UNIQUE 했다고 치고 입력받은 아이디 가지고 
		// DB 가서 WHERE 조건절에다가 사용자가 입력한 아이디 넣어서
		// 조회 결과 있으면 혼쭐내주기
		String userId = sc.nextLine();
		
		/*
		while(true) {
			String userId = sc.nextLine();
			break;
		
			if(조회결과 중복 없음) {
				SELECT USER_ID FROM TB_USER
				WHERE USER_ID = 사용자가 입력한 아이디값
				break;
			}
			System.out.println("중복된 아이디가 존재합니다. 다른 아이디를 입력해주세요.");
			
			// userId.matches("^[a-zA-z]")
			if(userId.length() > 30) {
				System.out.println("아이디는 30자 이내로 입력해주세요.");
			}
		}
		*/
		
		
		System.out.print("비밀번호를 입력하세요. > ");
		String userPw = sc.nextLine();
		System.out.print("이름을 입력하세요. > ");
		String userName = sc.nextLine();
		
													 								// argument(인자값)
																					// 값
		int result = userController.insertUser(userId, userPw, userName);
		
		if(result > 0) {
			System.out.println(userName + "님 가입에 성공하셨습니다.");
		} else {
			System.out.println("회원 추가에 실패했습니다. 다시 시도해주세요.");
		}

	}
	
}
