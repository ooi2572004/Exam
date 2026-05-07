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
import bean.TestListStudent.TestRecord;

public class TestListStudentDao extends Dao {

	/**
	 * 学生番号で検索（学生毎検索）
	 * 科目名・科目コード・回数・点数のリストを返す
	 */
	public TestListStudent filterByStudent(String studentNo, School school) throws Exception {

		TestListStudent tts = null;
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			// testテーブルとstudentテーブル・subjectテーブルをJOIN
			String sql =
				"select t.student_no, s.student_name, t.class_num, s.ent_year, " +
				"       sub.subject_name, t.subject_cd, t.no, t.point " +
				"from test t " +
				"join student s on t.student_no = s.student_no " +
				"join subject sub on t.subject_cd = sub.subject_cd and t.school_cd = sub.school_cd " +
				"where t.school_cd = ? and t.student_no = ? " +
				"order by t.subject_cd, t.no";

			statement = connection.prepareStatement(sql);
			statement.setString(1, school.getSchoolCd());
			statement.setString(2, studentNo);
			resultSet = statement.executeQuery();

			List<TestRecord> records = new ArrayList<>();

			while (resultSet.next()) {
				if (tts == null) {
					// 最初の行で学生情報をセット
					tts = new TestListStudent();
					tts.setStudentNo(resultSet.getString("student_no"));
					tts.setStudentName(resultSet.getString("student_name"));
					tts.setClassNum(resultSet.getString("class_num"));
					tts.setEntYear(resultSet.getInt("ent_year"));
				}
				// 1行分の成績レコードを追加
				TestRecord record = new TestRecord();
				record.setSubjectName(resultSet.getString("subject_name"));
				record.setSubjectCd(resultSet.getString("subject_cd"));
				record.setNo(resultSet.getInt("no"));
				record.setPoint(resultSet.getInt("point"));
				records.add(record);
			}

			if (tts != null) {
				tts.setRecords(records);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException sqle) { throw sqle; }
			if (connection != null) try { connection.close(); } catch (SQLException sqle) { throw sqle; }
		}
		return tts;
	}

	/**
	 * 入学年度＋クラス＋科目で検索（科目・クラス毎検索）
	 */
	public List<TestListStudent> filter(int entYear, String classNum, Subject subject,
			School school, List<Subject> subjects) throws Exception {

		List<TestListStudent> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			String sql =
				"select t.student_no, s.student_name, t.class_num, s.ent_year, t.no, t.point " +
				"from test t " +
				"join student s on t.student_no = s.student_no " +
				"where t.school_cd = ? and t.class_num = ? and s.ent_year = ? and t.subject_cd = ? " +
				"order by t.student_no, t.no";

			statement = connection.prepareStatement(sql);
			statement.setString(1, school.getSchoolCd());
			statement.setString(2, classNum);
			statement.setInt(3, entYear);
			statement.setString(4, subject.getSubjectCd());

			resultSet = statement.executeQuery();

			// 科目毎検索用：学生番号をキーにまとめる
			Map<String, TestListStudent> map = new LinkedHashMap<>();
			while (resultSet.next()) {
				String sNo = resultSet.getString("student_no");
				TestListStudent tts = map.get(sNo);
				if (tts == null) {
					tts = new TestListStudent();
					tts.setStudentNo(sNo);
					tts.setStudentName(resultSet.getString("student_name"));
					tts.setClassNum(resultSet.getString("class_num"));
					tts.setEntYear(resultSet.getInt("ent_year"));
					// 科目毎検索では points（回数→点数）のMapを使う
					tts.setRecords(new ArrayList<>());
					map.put(sNo, tts);
				}
				TestRecord record = new TestRecord();
				record.setSubjectCd(subject.getSubjectCd());
				record.setSubjectName(subject.getSubjectName());
				record.setNo(resultSet.getInt("no"));
				record.setPoint(resultSet.getInt("point"));
				tts.getRecords().add(record);
			}
			list.addAll(map.values());

		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException sqle) { throw sqle; }
			if (connection != null) try { connection.close(); } catch (SQLException sqle) { throw sqle; }
		}
		return list;
	}
}