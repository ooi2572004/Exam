package scoremanager.main;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.SubjectDao;
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
 
		ClassNumDao classNumDao = new ClassNumDao();
		SubjectDao subjectDao = new SubjectDao();
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();
 
		// DBからデータ取得 3
		// ユーザーが所属している学校のクラスデータを取得
		List<String> classNumList = classNumDao.filter(school);
		// ユーザーが所属している学校の科目データを取得
		List<Subject> subjectList = subjectDao.filter(school);
 
		// ビジネスロジック 4
		// 入学年度リストを生成
		List<Integer> entYearSet = new ArrayList<>();
		for (int i = year - 10; i < year + 1; i++) {
			entYearSet.add(i);
		}
 
		// レスポンス値をセット 6
		req.setAttribute("ent_year_set", entYearSet);
		req.setAttribute("class_num_set", classNumList);
		req.setAttribute("subject_list", subjectList);
 
		// JSPへフォワード 7
		req.getRequestDispatcher("test_list.jsp").forward(req, res);
	}
}