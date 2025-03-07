package com.kh.mvc.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.kh.mvc.model.dto.UserDTO;

/**
 * DAO(Data Access Object)
 * 
 * 데이터베이스 관련된 작업(CRUD)을 전문적으로 담당하는 객체
 * DAO 안에 모든 메소드들은 데이터베이스와 관련된 기능으로 만들 것
 * 
 * Controller를 통해 호출된 기능을 수행
 * DB에 직접 접근한 후 SQL문을 수행하고 결과받기(JDBC)
 */
public class UserDAO {
	
	/*
	 * JDBC용 객체
	 * - Connection: DB와의 연결 정보를 담고 있는 객체(IP주소, Port, 사용자명, 비번)
	 * - Statement: Connection이 가지고 있는 연결 정보 DB에 
	 * 							SQL문을 전달하고 실행하고 결과도 받아오는 객체
	 * - ResultSet: 실행한 SQL문이 SELECT문일 경우 조회된 결과가 처음 담기는 객체
	 * 
	 * - PreparedStatement: SQL문을 미리 준비하는 개념
	 *                      ?(placeholder)로 확보해놓은 공간을 
	 *                      사용자가 입력한 값들과 바인딩해서 SQL문을 수정
	 * 
	 * ** 처리 절차 **
	 * 1) JDBC Driver 등록: 해당 DBMC에서 제공하는 클래스를 동적으로 등록
	 * 2) Connection 객체 생성: 접속하고자 하는 DB의 정보를 입력해서 생성
	 * 													(URL, 사용자이름, 비밀번호)
	 * 3-1) PreparedStatement 객체 생성: Connection 객체를 이용해서 생성
	 * 																	 (미완성된 SQL문을 미리 전달)
	 * 3-2) 미완성된 SQL문을 완성 형태로 만들어 주어야 함
	 * 		→ 미완성된 경우에만 해당 / 완성된 경우에는 생략
	 * 4) SQL문을 실행: executeXXX() → SQL을 인자로 전달하지 않음
	 *                  > SELECT(DQL): executeQuery()
	 *                  > DML:         executeUpdate()
	 * 5) 결과받기: 
	 * 							> SELECT: ResultSet 타입 객체(조회 데이터 담김)
	 * 							> DML: 		int (처리된 행의 개수)
	 * 6-1) ResultSet에 담겨있는 데이터들을 하나하나씩 뽑아서 DTO 객체 필드에
	 *      옮겨 담은 후 조회 결과가 여러 행일 경우 List로 관리
	 * 6-2) 트랜잭션 처리 
	 * 7) (생략될 수 있음) 
	 * 		 자원반납(close) → 생성의 역순으로
	 * 8) 결과 반환 → Controller
	 * 								SELECT > 6-1에서 만든 것
	 * 								DML    > 처리된 행의 개수
	 * 
	 * 
	 */
	
	private final String URL = "jdbc:oracle:thin:@112.221.156.34:12345:XE";
	private final String USERNAME = "KH24_JAR";
	private final String PASSWORD = "KH1234";
	
	// static: 메모리 영역 별개, 프로그램 처음 실행할 때 딱 한 번만 수행
	
	// 프로그램 수행 순서
	// 1. static 블록
	// 2. 부모클래스 생성자
	// 3. 초기화 블록
	// 4. 자식 생성자
	
	
	// static 블록
	static {
		
		// 프로그램 실행 중에 여러 문제 발생 가능 → 예외처리로 오류 방지
		try {
			// 프로그램 실행 중에 딱 한 번만 수행되면 됨 → static 블록
			// ClassNotFoundException 오류 발생 → 예외처리
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
				System.out.println("ojdbc 잘 넣었나요?"
												 + "\n 오타 안 났나요?");
			e.printStackTrace();
		}
		
	}

	public List<UserDTO> findAll(Connection conn) {
		// DB 가야지
		/* 
		 * VO / DTO / Entity
		 * 
		 * 1명의 회원의 정보는 1개의 UserDTO객체의 필드에 값을 담아야 겠다.
		 * 문제점: userDTO가 몇 개가 나올지 알 수 없음
		 */
		
		// 자바: 값을 속성과 행위를 가지고 있는 객체로 관리하자
		// RDBMS: 값을 각각의 테이블 사이의 관계를 가지고 관리하자 
		// 자바 & RDBMS → 패러다임 불일치
		// 테이블에 현실 세계의 개체 값 저장
		// VO: 값의 불변을 위해서
		// DTO: 주로 계층 간 또는 네트워크를 통해 데이터를 전송할 때
		// Entity: 테이블 형태를 똑같이 맞추기 위해서
		// => 테이블에 한 행의 데이터를 담기 위해서 사용

		List<UserDTO> list = new ArrayList();
		String sql = "SELECT "
									+		"USER_NO"
									+ ", USER_ID"
									+ ", USER_PW"
									+ ", USER_NAME"
									+ ", ENROLL_DATE "
								+ "FROM "
									+ 	"TB_USER "
								+ "ORDER "
									 + "BY "
									 	+ "ENROLL_DATE DESC";
		
		// 남색: heap에 올라가는 변수(비어있을 수 없음)
		// 황갈색: stack에 올라가는 변수 → 지역변수(텅텅 비어있음)
		// 메서드 영역에 선언됨 → 지역변수
		// 지역변수 사용하려면 초기화해야 함(초기화 null밖에 못함)
		// Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try {
			// 지역변수 텅텅 비어있으니 예외 발생 가능 → catch에서 예외 잡음
			// conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery();
			
			// 호출했을 때 반환값 
			// true → 조회결과 있음, false → 조회결과 없음
			while(rset.next()) {
				// 조회 결과 컬럼 값을 DTO 필드에 담는 작업 및 리스트에 요소로 추가
				// 값을 담을 공간 생성
				UserDTO user = new UserDTO();
				// user: 지역변수(선언된 while 블록 안에서만 사용)
				// new: heap 영역에 메모리 생성
				// heap에서 메모리가 계속 살아있기 위한 전제조건
				// → 누군가 메모리 주소를 가리키고 있어야 함
				// 아니면 Garbage Collector가 정리함
				
				user.setUserNo(rset.getInt("USER_NO"));
				user.setUserId(rset.getString("USER_ID"));
				user.setUserPw(rset.getString("USER_PW"));
				user.setUserName(rset.getString("USER_NAME"));
				user.setEnrollDate(rset.getDate("ENROLL_DATE"));
				
				// ArrayList의 본질: 10칸짜리 Object형 배열
				// Object: 모든 클래스의 최상위 객체(부모)
				// == 모든 참조 자료형 객체는 Object 타입을 쓸 수 있음
				// 상속 구조에서 제일 중요한 것: 자식 객체는 부모 객체의 자료형을 쓸 수 있음
				// 															 (필드, 메서드는 자료형에 포함됨)
				
				// → List<UserDTO>: UserDTO만 들어갈 수 있는 Object형 배열
				
				// list란? 
				// 데이터를 어떻게 관리해야 효율적이지? → 상상속의 자료형 생성 → 그 중 하나가 List
				// List: 순서를 가지고 나열시키자
				// List가 가져야 할 요소들 개념적으로, 추상적으로 정의
				// 상상 속 자료형을 실제로 코드를 가지고 구현함
				// List 추상적 개념이니 interface로 생성
				// List 실제로 구현할 클래스 → ArrayList(배열 가지고 구현한 List), LinkedList
				
				// Object형 배열에 값을 저장
				// 누군가 메모리주소를 가리키고 있으니 이 블록이 끝나도 살아남음
				list.add(user);
				
			}
			
					
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("오타가 나지 않았나요? 확인하셨나요? 두 번 봤나요?");
		
		} finally {
		
			try {
				// The local variable rset may not have been initialized
				// 지역변수 초기화 안 되어있다.
				// → ResultSet rset = null;
				// rest이 null이 아닐 때만 닫음
				if(rset != null) {
					rset.close();	
				}
			} catch (SQLException e) {
				System.out.println("몰라 DB서버 이상해");
				e.printStackTrace();
			}
			
			try {
				
				if(pstmt != null) {
					pstmt.close();
				}
				
			} catch (SQLException e) {
					System.out.println("PreparedStatement 이상해요");
			}
			
			try {
				
				if(conn != null) {
					conn.close();
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return list;
		
	}
	
	/**
	 * @param user 사용자가 입력한 아이디 / 비밀번호 / 이름이 각각 필드에 대입되어 있음
	 * @return 아직 무엇을 돌려줄지 안 정함
	 */
	public int insertUser(UserDTO user) {
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		String sql = "INSERT INTO TB_USER "
							 + "VALUES (SEQ_USER_NO.NEXTVAL, "
							 + "?, ?, ?, SYSDATE)";
		
		int result = 0;
		
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			
			// AutoCommit 끄기
			// conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			result = pstmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			
				try {
					// 숏 서킷(Short-Circuit)
					// 조건문에서 논리 연산자 && (AND)와 || (OR)를 사용할 때 
					// 왼쪽 피연산자의 결과만으로 전체 연산의 결과가 확정되면 
					// 오른쪽 피연산자의 평가를 생략하는 최적화 기법
					if(pstmt != null && !pstmt.isClosed()) pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
				try {
					if(conn != null) conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
		}
		
		// Controller로 돌아감
		return result;
		
	}

}
