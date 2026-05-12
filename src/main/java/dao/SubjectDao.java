package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;
 
public class SubjectDao extends Dao {
 
	// 1件取得（重複チェックや変更・削除の事前確認に使用）
	public Subject get(String subjectCd, School school) throws Exception {
		Subject subject = null;
		String sql = "SELECT * FROM SUBJECT WHERE SUBJECT_CD = ? AND SCHOOL_CD = ?";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, subjectCd);
			pstmt.setString(2, school.getSchoolCd());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					subject = new Subject();
					subject.setSubjectCd(rs.getString("SUBJECT_CD"));
					subject.setSubjectName(rs.getString("SUBJECT_NAME"));
					subject.setSchool(school);
				}
			}
		}
		return subject;
	}
 
	// 一覧取得（ログインしている先生の学校の科目を全件取得）
	public List<Subject> filter(School school) throws Exception {
		List<Subject> list = new ArrayList<>();
		String sql = "SELECT * FROM SUBJECT WHERE SCHOOL_CD = ? ORDER BY SUBJECT_CD ASC";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, school.getSchoolCd());
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Subject subject = new Subject();
					subject.setSubjectCd(rs.getString("SUBJECT_CD"));
					subject.setSubjectName(rs.getString("SUBJECT_NAME"));
					subject.setSchool(school);
					list.add(subject);
				}
			}
		}
		return list;
	}
 
	// 保存（存在すればUPDATE、なければINSERT）
	public boolean save(Subject subject) throws Exception {
		int count = 0;
		try (Connection con = getConnection()) {
			Subject old = get(subject.getSubjectCd(), subject.getSchool());
			if (old == null) {
				// 新規登録
				String sql = "INSERT INTO SUBJECT (SUBJECT_CD, SUBJECT_NAME, SCHOOL_CD) VALUES (?, ?, ?)";
				try (PreparedStatement pstmt = con.prepareStatement(sql)) {
					pstmt.setString(1, subject.getSubjectCd());
					pstmt.setString(2, subject.getSubjectName());
					pstmt.setString(3, subject.getSchool().getSchoolCd());
					count = pstmt.executeUpdate();
				}
			} else {
				// 変更（更新）
				String sql = "UPDATE SUBJECT SET SUBJECT_NAME = ? WHERE SUBJECT_CD = ? AND SCHOOL_CD = ?";
				try (PreparedStatement pstmt = con.prepareStatement(sql)) {
					pstmt.setString(1, subject.getSubjectName());
					pstmt.setString(2, subject.getSubjectCd());
					pstmt.setString(3, subject.getSchool().getSchoolCd());
					count = pstmt.executeUpdate();
				}
			}
		}
		return count > 0;
	}
 
	// 削除
	public boolean delete(Subject subject) throws Exception {
		int count = 0;
		String sql = "DELETE FROM SUBJECT WHERE SUBJECT_CD = ? AND SCHOOL_CD = ?";
		try (Connection con = getConnection();
			 PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, subject.getSubjectCd());
			pstmt.setString(2, subject.getSchool().getSchoolCd());
			count = pstmt.executeUpdate();
		}
		return count > 0;
	}
}