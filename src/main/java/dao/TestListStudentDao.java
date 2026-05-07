package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.School;
import bean.Subject;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {

	/** ResultSetからTestToStudentリストを組み立てる共通処理 */
	private List<TestListStudent> postFilter(ResultSet resultSet) throws Exception {
		Map<String, TestListStudent> map = new LinkedHashMap<>();
		try {
			while (resultSet.next()) {
				String studentNo   = resultSet.getString("student_no");
				String subjectCd   = resultSet.getString("subject_cd");
				int    point       = resultSet.getInt("point");
				String classNum    = resultSet.getString("class_num");
				String studentName = resultSet.getString("student_name");

				TestListStudent tts = map.get(studentNo);
				if (tts == null) {
					tts = new TestListStudent();
					tts.setStudentNo(studentNo);
					tts.setStudentName(studentName);
					tts.setClassNum(classNum);
					tts.setPoints(new LinkedHashMap<>());
					map.put(studentNo, tts);
				}
				tts.getPoints().put(subjectCd, point);
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(map.values());
	}

	/**
	 * 入学年度＋クラス＋科目で検索（科目・クラス毎検索）
	 * studentテーブルをJOINしてent_yearで絞り込む
	 */
	public List<TestListStudent> filter(int entYear, String classNum, Subject subject,
			School school, List<Subject> subjects) throws Exception {

		List<TestListStudent> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			StringBuilder sql = new StringBuilder(
				"select t.student_no, s.student_name, t.class_num, t.subject_cd, t.point " +
				"from test t " +
				"join student s on t.student_no = s.student_no " +
				"where t.school_cd = ? and t.class_num = ? and s.ent_year = ?"
			);
			if (subject != null) {
				sql.append(" and t.subject_cd = ?");
			}
			sql.append(" order by t.student_no, t.subject_cd");

			statement = connection.prepareStatement(sql.toString());
			statement.setString(1, school.getSchoolCd());
			statement.setString(2, classNum);
			statement.setInt(3, entYear);
			if (subject != null) {
				statement.setString(4, subject.getSubjectCd());
			}

			resultSet = statement.executeQuery();
			list = postFilter(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException sqle) { throw sqle; }
			if (connection != null) try { connection.close(); } catch (SQLException sqle) { throw sqle; }
		}
		return list;
	}

	/**
	 * 学生番号で検索（学生毎検索）
	 */
	public List<TestListStudent> filterByStudent(String studentNo, School school,
			List<Subject> subjects) throws Exception {

		List<TestListStudent> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			String sql =
				"select t.student_no, s.student_name, t.class_num, t.subject_cd, t.point " +
				"from test t " +
				"join student s on t.student_no = s.student_no " +
				"where t.school_cd = ? and t.student_no = ? " +
				"order by t.student_no, t.subject_cd";

			statement = connection.prepareStatement(sql);
			statement.setString(1, school.getSchoolCd());
			statement.setString(2, studentNo);

			resultSet = statement.executeQuery();
			list = postFilter(resultSet);

		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException sqle) { throw sqle; }
			if (connection != null) try { connection.close(); } catch (SQLException sqle) { throw sqle; }
		}
		return list;
	}
}