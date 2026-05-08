package scoremanager.main;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
public class TestRegistAction extends Action {
 
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		try {
			HttpSession session = req.getSession();
			Teacher teacher = (Teacher)session.getAttribute("user");
 
			ClassNumDao classNumDao = new ClassNumDao();
			SubjectDao subjectDao = new SubjectDao();
			TestDao testDao = new TestDao();
			StudentDao studentDao = new StudentDao();
			LocalDate todaysDate = LocalDate.now();
			int year = todaysDate.getYear();
			String entYearStr = req.getParameter("f1");
			String classNum = req.getParameter("f2");
			String subjectCd = req.getParameter("f3");
			String numStr = req.getParameter("f4");
 
			List<String> classList = classNumDao.filter(teacher.getSchool());
			List<Subject> subjectList = subjectDao.filter(teacher.getSchool());
			List<Integer> entYearSet = new ArrayList<>();
			for (int i = year - 10; i < year + 1; i++) {
				entYearSet.add(i);
			}
			List<Integer> numSet = new ArrayList<>();
			for (int i = 1; i <= 2; i++) {
				numSet.add(i);
			}
 
			// 検索ボタンが押された時の処理
			if (entYearStr != null && classNum != null && subjectCd != null && numStr != null) {
				int entYear = Integer.parseInt(entYearStr);
				int num = Integer.parseInt(numStr);
				Subject subject = subjectDao.get(subjectCd, teacher.getSchool());
				// 1. そのクラスの「在学生（isAttend=true）」を全員取得する
				List<Student> students = studentDao.filter(teacher.getSchool(), entYear, classNum, true);
				// 2. そのクラスの「登録済み成績」を取得する
				List<Test> tests = testDao.filter(entYear, classNum, subject, num, teacher.getSchool());
				// 3. 成績を「学生番号」をキーにしてMapに詰める（JSPで探しやすくするため）
				Map<String, Test> testMap = new HashMap<>();
				for (Test t : tests) {
					testMap.put(t.getStudent().getStudentNo(), t);
				}
				// 4. JSPに渡す
				req.setAttribute("students", students);
				req.setAttribute("testMap", testMap);
				req.setAttribute("f1", entYearStr);
				req.setAttribute("f2", classNum);
				req.setAttribute("f3", subjectCd);
				req.setAttribute("f4", numStr);
			}
 
			req.setAttribute("class_num_set", classList);
			req.setAttribute("ent_year_set", entYearSet);
			req.setAttribute("subject_set", subjectList);
			req.setAttribute("num_set", numSet);
 
			req.getRequestDispatcher("test_regist.jsp").forward(req, res);
 
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}