package scoremanager.main;

import java.time.LocalDate;
import java.util.List;

import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");

		ClassNumDao classNumDao = new ClassNumDao();
		List<String> list = classNumDao.filter(teacher.getSchool());
		
		// ローカル変数の指定 1
		LocalDate todaysDate = LocalDate.now(); // LocalDateインスタンスを取得
		int year = todaysDate.getYear(); // 現在の年を取得
		
		
		
	}
}