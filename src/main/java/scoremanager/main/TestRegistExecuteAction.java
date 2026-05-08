package scoremanager.main;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
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
		Map<String, String> errors = new HashMap<>();
 
		String subjectCd = req.getParameter("f3");
		int num = Integer.parseInt(req.getParameter("f4"));
		String classNum = req.getParameter("f2");
		int count = Integer.parseInt(req.getParameter("count")); 
		Subject subject = subjectDao.get(subjectCd, teacher.getSchool());
		// 保存用と削除用のリストを分ける
		List<Test> saveList = new ArrayList<>();
		List<Test> deleteList = new ArrayList<>();
 
		for (int i = 0; i < count; i++) {
			String studentNo = req.getParameter("student_no_" + i);
			String pointStr = req.getParameter("point_" + i);
			Test test = new Test();
			Student student = new Student();
			student.setStudentNo(studentNo);
			test.setStudent(student);
			test.setSubject(subject);
			test.setSchool(teacher.getSchool());
			test.setNo(num);
			test.setClassNum(classNum);
			if (pointStr != null && !pointStr.isEmpty()) {
				int point = Integer.parseInt(pointStr);
				// 念のためサーバー側でも0〜100のチェック
				if (point < 0 || point > 100) {
					errors.put("point", "0～100の範囲で入力してください");
					break; // 1つでもエラーがあれば処理を中断
				}
				test.setPoint(point);
				saveList.add(test);
			} else {
				// 空欄の場合は削除リストに入れる
				deleteList.add(test);
			}
		}
 
		// エラーがあった場合は元の画面（TestRegistAction）に戻す
		if (!errors.isEmpty()) {
			req.setAttribute("errors", errors);
			req.getRequestDispatcher("TestRegist.action").forward(req, res);
			return;
		}
 
		// エラーがなければ一括保存 ＆ 一括削除
		if (!saveList.isEmpty()) {
			testDao.save(saveList);
		}
		if (!deleteList.isEmpty()) {
			testDao.delete(deleteList);
		}
 
		req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
	}
}