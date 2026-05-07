package scoremanager.main;

import java.util.List;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class SubjectListAction extends Action {
	@Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 学校コード取得
        String schoolCd = teacher.getSchool().getSchoolCd();

        // 科目一覧取得（filter ではなく findAll を使う）
        SubjectDao dao = new SubjectDao();
        List<Subject> list = dao.findAll(schoolCd);

        // JSP に渡す
        req.setAttribute("subjects", list);

        // レイアウトに渡すパラメータ
        req.setAttribute("page", "/scoremanager/main/subject/subject_list.jsp");
        req.setAttribute("title", "科目管理");

        // レイアウト（base.jsp）へフォワード
        req.getRequestDispatcher("/common/base.jsp").forward(req, res);
    }
}