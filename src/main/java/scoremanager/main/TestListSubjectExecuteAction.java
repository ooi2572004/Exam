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

public class TestListSubjectExecuteAction extends Action {

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
		String entYearStr = req.getParameter("ent_year");
		String classNum   = req.getParameter("class_num");
		String subjectCd  = req.getParameter("subject_cd");

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
		int maxNo = 0; // 試験回数の最大値
		Subject selectedSubject = null;

		// 入学年度・クラス・科目のいずれかが未選択の場合（代替フロー①）
		if (entYearStr == null || entYearStr.equals("0")
				|| classNum == null || classNum.equals("0")
				|| subjectCd == null || subjectCd.equals("0")) {
			errorMsg = "入学年度とクラスと科目を選択してください";
		} else {
			int entYear = Integer.parseInt(entYearStr);
			selectedSubject = subjectDao.get(subjectCd, school);
			testList = testToStudentDao.filter(entYear, classNum, selectedSubject, school, subjectList);

			if (testList == null || testList.isEmpty()) {
				// 成績情報が無かった場合（代替フロー②）
				errorMsg = "学生情報が存在しませんでした";
			} else {
				// 試験回数の最大値を求める（列ヘッダー「1回・2回…」の表示に使用）
				for (TestListStudent tts : testList) {
					if (tts.getRecords() != null) {
						for (TestListStudent.TestRecord record : tts.getRecords()) {
							if (record.getNo() > maxNo) maxNo = record.getNo();
						}
					}
				}
			}
		}

		// レスポンス値をセット 6
		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("class_num_set", classNumList);
		req.setAttribute("subject_list", subjectList);
		req.setAttribute("test_list", testList);
		req.setAttribute("error_msg", errorMsg);
		req.setAttribute("sel_ent_year", entYearStr);
		req.setAttribute("sel_class_num", classNum);
		req.setAttribute("sel_subject_cd", subjectCd);
		req.setAttribute("selected_subject", selectedSubject);
		req.setAttribute("max_no", maxNo);

		// JSPへフォワード 7
		req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
	}
}