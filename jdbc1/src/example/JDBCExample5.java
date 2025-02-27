package example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample5 {
	public static void main(String[] args) {
		// 부서명을 입력받아
		// 해당 부서에 근무하는 사원의
		// 사번, 이름, 부서명, 직급명을
		// 직급코드 내림차순으로 조회
		
//		SELECT E.EMP_ID, E.EMP_NAME, D.DEPT_TITLE, J.JOB_NAME
//		FROM EMPLOYEE E
//		JOIN DEPARTMENT D ON (E.DEPT_CODE = D.DEPT_ID)
//		JOIN JOB J ON (E.JOB_CODE = J.JOB_CODE)
//		WHERE D.DEPT_TITLE = '총무부'
//		ORDER BY J.JOB_CODE DESC;
		
		Scanner sc = new Scanner(System.in); 
		
		// 1. JDBC 객체 참조 변수 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			// 2. DriverManager 객체를 이용해 Connection 객체 생성
			
			// Oracle JDBC Driver를 메모리에 로드(적재) == 객체로 만듦
			Class.forName("oracle.jdbc.OracleDriver");
					
			// DB 연결 정보
			String type = "jdbc:oracle:thin:@";
			String host = "112.221.156.34";
			String port = ":12345";
			String dbName = ":XE";
			String userName = "KH24_JAR";
			String password = "KH1234";
			
			// Connection 객체 생성
			conn = DriverManager.getConnection(
						type + host + port + dbName, // DB URL 
						userName, 
						password
					);
			
			// 범위 입력받기
			System.out.println("=== 해당 부서에 근무하는 직원 조회 ===");
			
			System.out.print("부서명 입력: ");
			String dept = sc.next();		
			
//		SELECT E.EMP_ID, E.EMP_NAME, D.DEPT_TITLE, J.JOB_NAME
//		FROM EMPLOYEE E
//		JOIN DEPARTMENT D ON (E.DEPT_CODE = D.DEPT_ID)
//		JOIN JOB J ON (E.JOB_CODE = J.JOB_CODE)
//		WHERE D.DEPT_TITLE = '총무부'
//		ORDER BY E.JOB_CODE DESC;
			
			// 3. SQL 작성
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT E.EMP_ID, E.EMP_NAME, D.DEPT_TITLE, J.JOB_NAME  ");
			sb.append("FROM EMPLOYEE E  ");
			sb.append("JOIN DEPARTMENT D ON (E.DEPT_CODE = D.DEPT_ID)  ");
			sb.append("JOIN JOB J ON (E.JOB_CODE = J.JOB_CODE)  ");
			sb.append("WHERE D.DEPT_TITLE =  ");
			sb.append("'" + dept + "'");
			sb.append("  ORDER BY E.JOB_CODE ASC");
			String sql = sb.toString();
			
			// 4. sql을 전달하고 결과를 받아올 Statement 객체 생성
			stmt = conn.createStatement();
			
			// 5. Statement 객체를 이용해서 SQL을 전달 후 수행
			rs = stmt.executeQuery(sql);
			
			// 6. 조회 결과가 저장된 ResultSet에 1행씩 접근하여 각 행에 기록된 컬럼값 얻어오기
			while(rs.next()) {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s \n",
													empId, empName, deptTitle, jobName);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
