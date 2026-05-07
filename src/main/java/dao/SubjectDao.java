package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {

	public Subject get(String subjectCd, School school) throws Exception {
		Subject subject = new Subject();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"select * from subject where subject_cd = ? and school_cd = ?");
			statement.setString(1, subjectCd);
			statement.setString(2, school.getSchoolCd());
			ResultSet rSet = statement.executeQuery();
			SchoolDao schoolDao = new SchoolDao();
			if (rSet.next()) {
				subject.setSubjectCd(rSet.getString("subject_cd"));
				subject.setSubjectName(rSet.getString("subject_name"));
				subject.setSchool(schoolDao.get(rSet.getString("school_cd")));
			} else {
				subject = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException sqle) { throw sqle; }
			if (connection != null) try { connection.close(); } catch (SQLException sqle) { throw sqle; }
		}
		return subject;
	}

	public List<Subject> filter(School school) throws Exception {
		List<Subject> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		try {
			statement = connection.prepareStatement(
				"select * from subject where school_cd = ? order by subject_cd");
			statement.setString(1, school.getSchoolCd());
			ResultSet rSet = statement.executeQuery();
			SchoolDao schoolDao = new SchoolDao();
			while (rSet.next()) {
				Subject subject = new Subject();
				subject.setSubjectCd(rSet.getString("subject_cd"));
				subject.setSubjectName(rSet.getString("subject_name"));
				subject.setSchool(schoolDao.get(rSet.getString("school_cd")));
				list.add(subject);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException sqle) { throw sqle; }
			if (connection != null) try { connection.close(); } catch (SQLException sqle) { throw sqle; }
		}
		return list;
	}
>>>>>>> branch 'master' of https://github.com/ooi2572004/Exam.git
}