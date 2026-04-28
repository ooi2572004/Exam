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

	private List<TestListStudent> postFilter(ResultSet resultSet) throws Exception {
		Map<String, TestListStudent> map = new LinkedHashMap<>();
		try {
			while (resultSet.next()) {
				String studentNo   = resultSet.getString("student_no");
				String subjectCd   = resultSet.getString("subject_cd");
				int    point       = resultSet.getInt("point");
				String classNum    = resultSet.getString("class_num");

				TestListStudent tts = map.get(studentNo);
				if (tts == null) {
					tts = new TestListStudent();
					tts.setStudentNo(studentNo);
					tts.setClassNum(classNum);
					tts.setPoints(new LinkedHashMap<>());
					// 学生名はstudentテーブルからJOINして取得!
					try {
						tts.setStudentName(resultSet.getString("student_name"));
					} catch (Exception e) {
						tts.setStudentName("");
					}
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
	 * クラスと科目を条件に学生別成績一覧を取得
	 * subject が null の場合は全科目対象
	 */
	public List<TestListStudent> filter(String classNum, Subject subject,
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
				"where t.school_cd = ? and t.class_num = ?"
			);
			if (subject != null) {
				sql.append(" and t.subject_cd = ?");
			}
			sql.append(" order by t.student_no, t.subject_cd");

			statement = connection.prepareStatement(sql.toString());
			statement.setString(1, school.getSchoolCd());
			statement.setString(2, classNum);
			if (subject != null) {
				statement.setString(3, subject.getSubjectCd());
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
}