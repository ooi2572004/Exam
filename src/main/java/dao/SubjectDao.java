package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Subject;

public class SubjectDao extends Dao {

    public List<Subject> findAll(String schoolCd) throws Exception {

        List<Subject> list = new ArrayList<>();

        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(
                "select * from subject where school_cd = ? order by subject_cd"
            );
            statement.setString(1, schoolCd);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Subject s = new Subject();
                s.setSchoolCd(resultSet.getString("school_cd"));
                s.setSubjectCd(resultSet.getString("subject_cd"));
                s.setSubjectName(resultSet.getString("subject_name"));
                list.add(s);
            }

        } catch (Exception e) {
            throw e;

        } finally {
            if (resultSet != null) {
                try { resultSet.close(); } catch (SQLException e) { throw e; }
            }
            if (statement != null) {
                try { statement.close(); } catch (SQLException e) { throw e; }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { throw e; }
            }
        }

        return list;
    }

    public Subject find(String schoolCd, String subjectCd) throws Exception {

        Subject s = null;

        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(
                "select * from subject where school_cd = ? and subject_cd = ?"
            );
            statement.setString(1, schoolCd);
            statement.setString(2, subjectCd);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                s = new Subject();
                s.setSchoolCd(resultSet.getString("school_cd"));
                s.setSubjectCd(resultSet.getString("subject_cd"));
                s.setSubjectName(resultSet.getString("subject_name"));
            }

        } catch (Exception e) {
            throw e;

        } finally {
            if (resultSet != null) {
                try { resultSet.close(); } catch (SQLException e) { throw e; }
            }
            if (statement != null) {
                try { statement.close(); } catch (SQLException e) { throw e; }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { throw e; }
            }
        }

        return s;
    }

    public boolean insert(Subject subject) throws Exception {

        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            statement = connection.prepareStatement(
                "insert into subject(school_cd, subject_cd, subject_name) values(?, ?, ?)"
            );
            statement.setString(1, subject.getSchoolCd());
            statement.setString(2, subject.getSubjectCd());
            statement.setString(3, subject.getSubjectName());

            count = statement.executeUpdate();

        } catch (Exception e) {
            throw e;

        } finally {
            if (statement != null) {
                try { statement.close(); } catch (SQLException e) { throw e; }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException e) { throw e; }
            }
        }

        return count > 0;
    }
}