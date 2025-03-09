package com.kh.mvc.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.kh.mvc.controller.UserController;
import com.kh.mvc.model.dao.UserDAO;
import com.kh.mvc.model.dto.UserDTO;
import com.kh.mvc.model.service.UserService;

public class UserView {
	
	/**
	 * Memberview 클래스는 JDBC 실습을 위해 생성하였으며,
	 * 사용자에게 입력 및 출력을 수행하는 메소드를 제공합니다.
	 */
		
		private Scanner sc = new Scanner(System.in);
		private UserController userController = new UserController();
		
		/**
		 * 프로그램 시작 시 사용자에게 보여줄
		 * 메인 화면을 출력해주는 메소드입니다.
		 */
		public void mainMenu() {
			while(true) {
				System.out.println("USER 테이블 관리 프로그램");
				System.out.println("1. 회원 전체 조회");
				
				// 한 명의 회원 정보 추가는 DB에 한 행을 넣겠다는 의미
				// == 5개 값 필요(USER_NO, USER_ID, USER_PW, USER_NAME, ENROLL_DATE)
				// -> 필요한 값을 사용자가 입력할 수 있도록 유도
				System.out.println("2. 회원 추가");
				System.out.println("9. 프로그램 종료");
				System.out.print("이용할 메뉴를 선택해주세요. > ");
				
				// 값을 초기화해야 뒤에서 쓸 수 있다.
				int menuNo = 0;
				
				// 숫자 외 다른 문자를 적어도 오류 발생하지 않도록 예외 처리
				try {
				menuNo = sc.nextInt();
				} catch(InputMismatchException e) {
					// 입력버퍼에 입력받은 값이 남아있으면 안 되니 nextLine으로 지워줌
					sc.nextLine();
					
					// 다음 반복으로 돌아감
					continue;
				}
				sc.nextLine();
				
				switch(menuNo) {
					case 1: 
						findAllTbUser(); 
						// return: 메서드를 호출한 곳으로 돌아감
						// break: 가장 가까운 스위치문에서 나가는 것
						break; 
					case 2: 
						insertTbUser(); 
						break;
					case 9: 
						System.out.println("프로그램 종료"); 
						return;
					default:
						System.out.println("잘못된 메뉴 선택입니다.");
					
				} // switch
				
			} // while
			
		} // main
		
		public void findAllTbUser() {

			// UserController 에 
			// findAllTbUser 메서드 호출
			UserController uc = new UserController();
			List<UserDTO> list = uc.findAllTbUser();
			
			for(int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getUserId());
				System.out.println(list.get(i).getUserPw());
			}
			
			
			
			// List, ArrayList는 클래스
			// 컬렉션 프레임워크: Map, Set, List(배열 업그레이드 버전, 길이 알아서 늘었다 줄음)
			// List 만드는 법 외우기
			
			
		}
		
		public void insertTbUser() {
			UserController uc = new UserController();
			List<UserDTO> list = uc.insertTbUser();
			
			for(int i=0; i<list.size(); i++) {
				System.out.println(list.get(i).getUserId());
				System.out.println(list.get(i).getUserPw());
			}
		}	
		
		
}
