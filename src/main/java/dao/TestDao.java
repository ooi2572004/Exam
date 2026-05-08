package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

	private String baseSql = "SELECT * FROM TEST ";

	// 1件取得
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

	// 検索条件での絞り込み（成績が存在する人のみ）
	public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
		List<Test> list = new ArrayList<>();
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

	// 複数の一括保存
	public boolean save(List<Test> list) throws Exception {
		boolean result = true;
		try (Connection con = getConnection()) {
			con.setAutoCommit(false);
			try {
				for (Test test : list) {
					if (!save(test, con)) {
						result = false;
					}
				}
				con.commit();
			} catch (Exception e) {
				con.rollback();
				throw e;
			}
		}
		return result;
	}

	// 1件の保存（UPDATE or INSERT）
	private boolean save(Test test, Connection connection) throws Exception {
		Test existingTest = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());
		if (existingTest != null) {
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

	// 空欄にされた成績を一括削除
	public boolean delete(List<Test> list) throws Exception {
		boolean result = true;
		try (Connection con = getConnection()) {
			con.setAutoCommit(false);
			try {
				for (Test test : list) {
					String sql = "DELETE FROM TEST WHERE STUDENT_NO = ? AND SUBJECT_CD = ? AND SCHOOL_CD = ? AND NO = ?";
					try (PreparedStatement pstmt = con.prepareStatement(sql)) {
						pstmt.setString(1, test.getStudent().getStudentNo());
						pstmt.setString(2, test.getSubject().getSubjectCd());
						pstmt.setString(3, test.getSchool().getSchoolCd());
						pstmt.setInt(4, test.getNo());
						pstmt.executeUpdate();
					}
				}
				con.commit();
			} catch (Exception e) {
				con.rollback();
				throw e;
			}
		}
		return result;
	}
}
