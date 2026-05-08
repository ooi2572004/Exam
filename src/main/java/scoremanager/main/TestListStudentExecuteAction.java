package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.TestListStudent;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestListStudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListStudentExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ローカル変数の指定 1
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");
		School school = teacher.getSchool();

		SubjectDao subjectDao = new SubjectDao();
		ClassNumDao classNumDao = new ClassNumDao();
		TestListStudentDao testToStudentDao = new TestListStudentDao();
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();

		// リクエストパラメーターの取得 2
		String studentNo = req.getParameter("student_no");

		// DBからデータ取得 3
		List<String> classNumList = classNumDao.filter(school);
		List<Subject> subjectList = subjectDao.filter(school);

		// ビジネスロジック 4
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}

		TestListStudent testToStudent = null;
		String errorMsg = null;

		// 入力された学生番号の成績情報を取得
		testToStudent = testToStudentDao.filterByStudent(studentNo, school);

		// 成績情報が無かった場合（代替フロー②）
		if (testToStudent == null || testToStudent.getRecords() == null
				|| testToStudent.getRecords().isEmpty()) {
			// 学生が存在しない or 成績が無い場合
			// 学生名を取得して表示するために学生情報だけ取得
			StudentDao studentDao = new StudentDao();
			Student student = studentDao.get(studentNo);
			if (student != null) {
				// 学生は存在するが成績なし
				testToStudent = new TestListStudent();
				testToStudent.setStudentNo(student.getStudentNo());
				testToStudent.setStudentName(student.getStudentName());
				testToStudent.setRecords(new ArrayList<>());
			}
			errorMsg = "成績情報が存在しませんでした";
		}

		// レスポンス値をセット 6
		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("class_num_set", classNumList);
		req.setAttribute("subject_list", subjectList);
		req.setAttribute("test_to_student", testToStudent);
		req.setAttribute("error_msg", errorMsg);
		req.setAttribute("sel_student_no", studentNo);

		// JSPへフォワード 7
		req.getRequestDispatcher("test_list_student.jsp").forward(req, res);
	}
}