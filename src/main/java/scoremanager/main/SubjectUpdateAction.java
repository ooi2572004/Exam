package scoremanager.main;

import bean.School;
import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // セッションからログインユーザー（先生）の情報を取得
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        School school = teacher.getSchool();

        // リクエストパラメータから編集したい「科目コード」を取得
        String cd = request.getParameter("cd");

        // DAOを使って、データベースからその科目の詳細情報を取得
        SubjectDao sDao = new SubjectDao();
        Subject subject = sDao.get(cd, school);

        if (subject != null) {
            // 取得した科目情報をリクエスト属性にセット（JSPで表示するため）
            request.setAttribute("cd", subject.getSubjectCd());
            request.setAttribute("name", subject.getSubjectName());
        }

        // 科目情報変更画面（JSP）へフォワード
        request.getRequestDispatcher("subject_update.jsp").forward(request, response);
    }
}