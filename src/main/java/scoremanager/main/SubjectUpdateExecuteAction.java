package scoremanager.main;
 
import java.util.HashMap;
import java.util.Map;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
public class SubjectUpdateExecuteAction extends Action {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");
 
		String cd = req.getParameter("cd");
		String name = req.getParameter("name");
 
		SubjectDao subjectDao = new SubjectDao();
		Map<String, String> errors = new HashMap<>();
 
		// 科目が他画面等ですでに削除されていないか確認
		if (subjectDao.get(cd, teacher.getSchool()) == null) {
			errors.put("cd", "科目が存在していません");
		}
 
		if (!errors.isEmpty()) {
			req.setAttribute("errors", errors);
			// エラー時は入力された名前を保持して画面へ戻す
			Subject subject = new Subject();
			subject.setSubjectCd(cd);
			subject.setSubjectName(name);
			req.setAttribute("subject", subject);
			req.getRequestDispatcher("subject_update.jsp").forward(req, res);
			return;
		}
 
		Subject subject = new Subject();
		subject.setSubjectCd(cd);
		subject.setSubjectName(name);
		subject.setSchool(teacher.getSchool());
		subjectDao.save(subject);
 
		req.getRequestDispatcher("subject_update_done.jsp").forward(req, res);
	}
}