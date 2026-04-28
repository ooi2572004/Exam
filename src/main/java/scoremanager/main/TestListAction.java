package scoremanager.main;

import java.util.List;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.TestListStudent;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestListStudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession();
		Teacher teacher = (Teacher) session.getAttribute("user");
		School school = teacher.getSchool();

		SubjectDao subjectDao = new SubjectDao();
		ClassNumDao classNumDao = new ClassNumDao();
		TestListStudentDao testToStudentDao = new TestListStudentDao();

		String classNum  = req.getParameter("class_num");
		String subjectCd = req.getParameter("subject_cd");

		List<String> classNumList = classNumDao.filter(school);
		List<Subject> subjectList = subjectDao.filter(school);

		List<TestListStudent> testList = null;
		String errorMsg = null;

		if (classNum != null && !classNum.equals("0")) {

			Subject subject = null;
			if (subjectCd != null && !subjectCd.equals("0")) {
				subject = subjectDao.get(subjectCd, school);
			}

			testList = testToStudentDao.filter(classNum, subject, school, subjectList);

			if (testList == null || testList.isEmpty()) {
				errorMsg = "成績情報が存在しませんでした";
			}
		}
//
		req.setAttribute("class_num_set", classNumList);
		req.setAttribute("subject_list", subjectList);
		req.setAttribute("test_list", testList);
		req.setAttribute("error_msg", errorMsg);
		req.setAttribute("sel_class_num", classNum);
		req.setAttribute("sel_subject_cd", subjectCd);
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}