package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");

		// 1. ローカル変数の指定
		ClassNumDao classNumDao = new ClassNumDao();
		SubjectDao subjectDao = new SubjectDao();
		TestDao testDao = new TestDao();
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();
		
		// 検索用パラメーターの受け取り
		String entYearStr = req.getParameter("f1");
		String classNum = req.getParameter("f2");
		String subjectCd = req.getParameter("f3");
		String numStr = req.getParameter("f4");

		// 2. プルダウン用リストの準備
		List<String> classList = classNumDao.filter(teacher.getSchool());
		List<Subject> subjectList = subjectDao.filter(teacher.getSchool());
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}
		List<Integer> numSet = new ArrayList<>();
		for (int i = 1; i <= 2; i++) { // 成績の回数（例として1〜2回）
			numSet.add(i);
		}

		// 3. 検索ボタンが押された場合のデータ取得処理
		if (entYearStr != null && classNum != null && subjectCd != null && numStr != null) {
			int entYear = Integer.parseInt(entYearStr);
			int num = Integer.parseInt(numStr);
			Subject subject = subjectDao.get(subjectCd, teacher.getSchool());
			
			// 指定された条件に合致する学生の成績リストを取得
			List<Test> tests = testDao.filter(entYear, classNum, subject, num, teacher.getSchool());
			req.setAttribute("tests", tests);
			
			// 検索条件の保持
			req.setAttribute("f1", entYear);
			req.setAttribute("f2", classNum);
			req.setAttribute("f3", subjectCd);
			req.setAttribute("f4", num);
		}

		// 4. レスポンス値をセットしてフォワード
		req.setAttribute("class_num_set", classList);
		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("subject_set", subjectList);
		req.setAttribute("num_set", numSet);

		req.getRequestDispatcher("test_regist.jsp").forward(req, res);
	}
}