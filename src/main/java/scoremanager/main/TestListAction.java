package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
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
		String searchType  = req.getParameter("search_type"); // "class" or "student"
		String entYearStr  = req.getParameter("ent_year");
		String classNum    = req.getParameter("class_num");
		String subjectCd   = req.getParameter("subject_cd");
		String studentNo   = req.getParameter("student_no");

		// DBからデータ取得 3
		List<String> classNumList = classNumDao.filter(school);
		List<Subject> subjectList = subjectDao.filter(school);

		// ビジネスロジック 4
		// 入学年度リストを生成
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}

		List<TestListStudent> testList = null;
		String errorMsg = null;

		if ("class".equals(searchType)) {
			// 科目・クラス毎検索
			if (entYearStr == null || entYearStr.equals("0")
					|| classNum == null || classNum.equals("0")
					|| subjectCd == null || subjectCd.equals("0")) {
				// 入学年度・クラス・科目のいずれかが未選択（代替フロー①）
				errorMsg = "入学年度とクラスと科目を選択してください";
			} else {
				int entYear = Integer.parseInt(entYearStr);
				Subject subject = subjectDao.get(subjectCd, school);
				testList = testToStudentDao.filter(entYear, classNum, subject, school, subjectList);
				if (testList == null || testList.isEmpty()) {
					// 成績情報が無かった場合（代替フロー②）
					errorMsg = "学生情報が存在しませんでした";
				}
			}
		} else if ("student".equals(searchType)) {
			// 学生毎検索
			if (studentNo == null || studentNo.trim().isEmpty()) {
				// 学生番号が未入力（代替フロー①）
				errorMsg = "学生番号を入力してください";
			} else {
				testList = testToStudentDao.filterByStudent(studentNo, school, subjectList);
				if (testList == null || testList.isEmpty()) {
					// 成績情報が無かった場合（代替フロー②）
					errorMsg = "成績情報が存在しませんでした";
				}
			}
		}

		// レスポンス値をセット 6
		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("class_num_set", classNumList);
		req.setAttribute("subject_list", subjectList);
		req.setAttribute("test_list", testList);
		req.setAttribute("error_msg", errorMsg);
		req.setAttribute("search_type", searchType);
		req.setAttribute("sel_ent_year", entYearStr);
		req.setAttribute("sel_class_num", classNum);
		req.setAttribute("sel_subject_cd", subjectCd);
		req.setAttribute("sel_student_no", studentNo);

		// JSPへフォワード 7
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}