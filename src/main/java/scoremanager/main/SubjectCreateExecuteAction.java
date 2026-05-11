package scoremanager.main;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectCreateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        String subjectCd = req.getParameter("subject_cd");
        String subjectName = req.getParameter("subject_name");

        // ① 科目コード文字数チェック
        if (subjectCd.length() != 3) {
            req.setAttribute("error", "科目コードは３文字で入力してください");
            returnError(req, res);
            return;
        }

        SubjectDao dao = new SubjectDao();

        // ② 重複チェック（get を使う）
        if (dao.get(subjectCd, teacher.getSchool()) != null) {
            req.setAttribute("error", "科目コードが重複しています");
            returnError(req, res);
            return;
        }

        // ③ 登録処理
        Subject subject = new Subject();
        subject.setSubjectCd(subjectCd);
        subject.setSubjectName(subjectName);
        subject.setSchool(teacher.getSchool());  
        boolean result = dao.insert(subject); 
        

        if (result == true) {

            req.getRequestDispatcher("subject_create_done.jsp").forward(req, res);
        } else {
            req.setAttribute("error", "登録に失敗しました");
            returnError(req, res);
        }
    }

    private void returnError(HttpServletRequest req, HttpServletResponse res) throws Exception {
        req.setAttribute("title", "得点管理システム");
        req.setAttribute("content", "subject_create_done.jsp");
        req.getRequestDispatcher("/common/base.jsp").forward(req, res);
    }
}