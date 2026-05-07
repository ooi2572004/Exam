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

        // 学校コード取得
        String schoolCd = teacher.getSchool().getSchoolCd();

        // パラメータ取得
        String subjectCd = req.getParameter("subject_cd");
        String subjectName = req.getParameter("subject_name");

        // Subject インスタンス生成
        Subject subject = new Subject();
        subject.setSchoolCd(schoolCd);
        subject.setSubjectCd(subjectCd);
        subject.setSubjectName(subjectName);

        // DAO 呼び出し
        SubjectDao dao = new SubjectDao();
        boolean result = dao.insert(subject);

        // 結果に応じて JSP へ
        if (result) {
            req.getRequestDispatcher("subject_create_done.jsp").forward(req, res);
        } else {
            req.setAttribute("error", "科目の登録に失敗しました");
            req.getRequestDispatcher("subject_create.jsp").forward(req, res);
        }
    }
}