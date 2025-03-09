package com.kh.mvc.run;

import com.kh.mvc.view.UserView;

public class Run {
	public static void main(String[] args) {
		// 객체 만들면서 동시에 메서드 호출
		// 한 개 메서드만 쓸 때
		new UserView().mainMenu();
		
		// 객체 만들고 변수에 객체 주소를 담음
		// 여러 메서드 쓸 때
		// UserView uv = new UserView();
		// uv.mainMenu();
	}
}
