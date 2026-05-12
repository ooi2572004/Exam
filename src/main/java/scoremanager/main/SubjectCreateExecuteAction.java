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
 
public class SubjectCreateExecuteAction extends Action {
 
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
 
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
 
        // ※HTMLフォームの name 属性が "cd", "name" の場合に合わせて取得
        // （画面設計に合わせる必要があります。以前提示されたJSPでは cd, name でした）
        String subjectCd = req.getParameter("cd");
        if (subjectCd == null) {
            subjectCd = req.getParameter("subject_cd"); // 念のためフォールバック
        }
        String subjectName = req.getParameter("name");
        if (subjectName == null) {
            subjectName = req.getParameter("subject_name");
        }
 
        Map<String, String> errors = new HashMap<>();
        SubjectDao dao = new SubjectDao();
 
        // ① 科目コード文字数チェック
        if (subjectCd == null || subjectCd.length() != 3) {
            errors.put("cd", "科目コードは3文字で入力してください");
        } 
        // ② 重複チェック（get を使う）
        else if (dao.get(subjectCd, teacher.getSchool()) != null) {
            errors.put("cd", "科目コードが重複しています");
        }
 
        // エラーがある場合は元の登録画面に戻す
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            // 入力中の値を保持して返す
            req.setAttribute("cd", subjectCd);
            req.setAttribute("name", subjectName);
            req.getRequestDispatcher("subject_create.jsp").forward(req, res);
            return;
        }
 
        // ③ 登録処理 (dao.save を使用)
        Subject subject = new Subject();
        subject.setSubjectCd(subjectCd);
        subject.setSubjectName(subjectName);
        subject.setSchool(teacher.getSchool());  
        boolean result = dao.save(subject);
 
        if (result) {
            // 成功時は完了画面へ
            req.getRequestDispatcher("subject_create_done.jsp").forward(req, res);
        } else {
            // 万が一の保存失敗時
            errors.put("cd", "登録に失敗しました");
            req.setAttribute("errors", errors);
            req.setAttribute("cd", subjectCd);
            req.setAttribute("name", subjectName);
            req.getRequestDispatcher("subject_create.jsp").forward(req, res);
        }
    }
}