package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao {

	// クラス図: private baseSql
	// ※基本となるSELECT文を定義しておき、各メソッドで使い回す設計です
	private String baseSql = "SELECT * FROM TEST ";

	// データベース接続用の共通メソッド（過去のDAOと同じです）
	private Connection getConnection() throws Exception {
		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		DataSource ds = (DataSource) envContext.lookup("jdbc/book");
		return ds.getConnection();
	}

	// クラス図: public get(...)
	// 1件の成績データを取得するメソッド
	public Test get(Student student, Subject subject, School school, int no) throws Exception {
		Test test = null;
		String sql = baseSql + "WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND SCHOOL_CD = ? AND NO = ?";
		
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setString(1, student.getStudentNo());
			pstmt.setString(2, subject.getSubjectCd());
			pstmt.setString(3, school.getSchoolCd());
			pstmt.setInt(4, no);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					// 取得した結果をTestオブジェクトに詰める
					test = new Test();
					test.setStudent(student);
					test.setSubject(subject);
					test.setSchool(school);
					test.setNo(rs.getInt("NO"));
					test.setPoint(rs.getInt("POINT"));
					test.setClassNum(rs.getString("CLASS_NUM"));
				}
			}
		}
		return test;
	}

	// クラス図: private postFilter(...)
	// DBから取得したResultSetを、List<Test>に変換する共通の裏方メソッド
	private List<Test> postFilter(ResultSet rSet, School school) throws Exception {
		List<Test> list = new ArrayList<>();
		
		// ここでResultSetを回してBeanに詰め、listにaddしていく処理を書きます
		// （※StudentDAOやSubjectDAOを使って、紐づくBeanもセットしてあげる必要があります）
		
		return list;
	}

	// クラス図: public filter(...)
	// 入学年度、クラス番号、科目、回数、学校で成績一覧を絞り込むメソッド（Actionから呼ばれます）
	public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
		List<Test> list = new ArrayList<>();
		
		// TESTテーブルとSTUDENTテーブルを結合して、指定の条件で絞り込みます
		String sql = "SELECT * FROM TEST t JOIN STUDENT s ON t.STUDENT_NO = s.STUDENT_NO "
				   + "WHERE s.ENT_YEAR = ? AND s.CLASS_NUM = ? AND t.SUBJECT_CD = ? AND t.NO = ? AND t.SCHOOL_CD = ?";
		
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			
			pstmt.setInt(1, entYear);
			pstmt.setString(2, classNum);
			pstmt.setString(3, subject.getSubjectCd());
			pstmt.setInt(4, num);
			pstmt.setString(5, school.getSchoolCd());
			
			try (ResultSet rs = pstmt.executeQuery()) {
				// 本来ならここで先ほどの private postFilter を呼び出してリスト化します
				while (rs.next()) {
					Test test = new Test();
					
					Student student = new Student();
					student.setStudentNo(rs.getString("STUDENT_NO"));
					student.setStudentName(rs.getString("STUDENT_NAME"));
					student.setEntYear(rs.getInt("ENT_YEAR"));
					
					test.setStudent(student);
					test.setSubject(subject);
					test.setSchool(school);
					test.setNo(rs.getInt("NO"));
					test.setPoint(rs.getInt("POINT"));
					test.setClassNum(rs.getString("CLASS_NUM"));
					
					list.add(test);
				}
			}
		}
		return list;
	}

	// クラス図: public save(List<Test>)
	// Actionから渡された「複数人の成績リスト」を受け取り、ループして保存するメソッド
	public boolean save(List<Test> list) throws Exception {
		boolean result = true;
		try (Connection con = getConnection()) {
			// オートコミットをオフにして、全件成功した時だけ保存（コミット）する安全対策
			con.setAutoCommit(false);
			
			try {
				for (Test test : list) {
					// 1件ずつ、下の private な save メソッドに処理を任せる
					boolean isSaved = save(test, con);
					if (!isSaved) {
						result = false;
					}
				}
				con.commit(); // 全て成功したら確定
			} catch (Exception e) {
				con.rollback(); // エラーが起きたら元に戻す
				throw e;
			}
		}
		return result;
	}

	// クラス図: private save(Test, Connection)
	// 実際に1件のデータをDBに保存（INSERT または UPDATE）する裏方メソッド
	private boolean save(Test test, Connection connection) throws Exception {
		// すでに成績が存在するかチェックする（getメソッドを使用）
		Test existingTest = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());
		
		if (existingTest != null) {
			// 存在する場合は上書き（UPDATE）
			String sql = "UPDATE TEST SET POINT = ?, CLASS_NUM = ? WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND NO = ? AND SCHOOL_CD = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setInt(1, test.getPoint());
				pstmt.setString(2, test.getClassNum());
				pstmt.setString(3, test.getStudent().getStudentNo());
				pstmt.setString(4, test.getSubject().getSubjectCd());
				pstmt.setInt(5, test.getNo());
				pstmt.setString(6, test.getSchool().getSchoolCd());
				pstmt.executeUpdate();
			}
		} else {
			// 存在しない場合は新規登録（INSERT）
			String sql = "INSERT INTO TEST (STUDENT_NO, SUBJECT_CD, SCHOOL_CD, NO, POINT, CLASS_NUM) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setString(1, test.getStudent().getStudentNo());
				pstmt.setString(2, test.getSubject().getSubjectCd());
				pstmt.setString(3, test.getSchool().getSchoolCd());
				pstmt.setInt(4, test.getNo());
				pstmt.setInt(5, test.getPoint());
				pstmt.setString(6, test.getClassNum());
				pstmt.executeUpdate();
			}
		}
		return true;
	}
}