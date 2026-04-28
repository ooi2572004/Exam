package scoremanager.main;

import java.util.ArrayList;
import java.util.List;

import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestRegistExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");

		SubjectDao subjectDao = new SubjectDao();
		TestDao testDao = new TestDao();

		// 共通パラメーターの受け取り
		String subjectCd = req.getParameter("subject_cd");
		int num = Integer.parseInt(req.getParameter("num"));
		int count = Integer.parseInt(req.getParameter("count")); // 学生の人数
		
		Subject subject = subjectDao.get(subjectCd, teacher.getSchool());
		List<Test> testList = new ArrayList<>();

		// 画面から送られてきた人数分ループしてデータをBeanに詰める
		for (int i = 0; i < count; i++) {
			String studentNo = req.getParameter("student_no_" + i);
			String pointStr = req.getParameter("point_" + i);
			
			// 点数が入力されている場合のみ保存対象とする
			if (pointStr != null && !pointStr.isEmpty()) {
				Test test = new Test();
				// 学生情報のセット（DAO側でStudentNoを使って更新/登録するため最低限セット）
				bean.Student student = new bean.Student();
				student.setStudentNo(studentNo);
				test.setStudent(student);
				
				test.setSubject(subject);
				test.setSchool(teacher.getSchool());
				test.setNo(num);
				test.setPoint(Integer.parseInt(pointStr));
				test.setClassNum(req.getParameter("f2")); // 必要に応じて
				
				testList.add(test);
			}
		}

		// まとめてデータベースへ保存（DAOにsaveメソッド等を作成して対応）
		testDao.save(testList);

		// 完了画面へフォワード
		req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
	}
}