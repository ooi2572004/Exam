package scoremanager.main;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // セッションからログインユーザー情報を取得
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        // 画面（JSP）から入力された値を取得
        String cd = request.getParameter("cd");     // 科目コード
        String name = request.getParameter("name"); // 新しい科目名

        // データベース更新用のSubjectインスタンスを作成
        Subject subject = new Subject();
        subject.setSubjectCd(cd);
        subject.setSubjectName(name);
        subject.setSchool(school);

        // DAOを使ってデータベースを更新
        SubjectDao sDao = new SubjectDao();
        sDao.save(subject);

        // 更新が終わったら、科目一覧画面にリダイレクト（またはフォワード）
        // ※パスはプロジェクトの構成に合わせて調整してください
        request.getRequestDispatcher("subject_list.jsp").forward(request, response);
    }
}