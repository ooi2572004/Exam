package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		// ローカル変数の指定 1
		HttpSession session = req.getSession(); // セッション
		Teacher teacher = (Teacher) session.getAttribute("user");
		int ent_year = 0; // 選択された入学年度
		String student_no = ""; // 学生番号（変更不可）
		String student_name = ""; // 入力された氏名
		String class_num = ""; // 選択されたクラス番号
		boolean is_attend = false; // 在学フラグ
		Student student = new Student();
		StudentDao studentDao = new StudentDao();
		ClassNumDao classNumDao = new ClassNumDao(); // クラス番号Dao
		Map<String, String> errors = new HashMap<>(); // エラーメッセージ

		// リクエストパラメーターの取得 2
		ent_year = Integer.parseInt(req.getParameter("ent_year"));
		student_no = req.getParameter("no");
		student_name = req.getParameter("name");
		class_num = req.getParameter("class_num");
		// チェックボックスはチェックされていない場合nullになるため、nullチェックで判定
		is_attend = req.getParameter("is_attend") != null;

		// DBからデータ取得 3
		// なし

		// ビジネスロジック 4
		if (ent_year == 0) { // 入学年度が未選択だった場合
			errors.put("1", "入学年度を選択してください");
			// リクエストにエラーメッセージをセット
			req.setAttribute("errors", errors);
		} else {
			// studentに変更後の学生情報をセット
			student.setStudentNo(student_no);
			student.setStudentName(student_name);
			student.setEntYear(ent_year);
			student.setClassNum(class_num);
			student.setAttend(is_attend);
			student.setSchool(teacher.getSchool());
			// saveメソッドで情報を更新（学生番号が存在するのでUPDATE文が実行される）
			studentDao.save(student);
		}

		// レスポンス値をセット 6
		// リクエストに入学年度をセット
		req.setAttribute("ent_year", ent_year);
		// リクエストに学生番号をセット
		req.setAttribute("no", student_no);
		// リクエストに氏名をセット
		req.setAttribute("name", student_name);
		// リクエストにクラス番号をセット
		req.setAttribute("class_num", class_num);
		// リクエストに在学フラグをセット
		req.setAttribute("is_attend", is_attend);

		// JSPへフォワード 7
		if (errors.isEmpty()) { // エラーメッセージがない場合
			// 変更完了画面にフォワード
			req.getRequestDispatcher("student_update_done.jsp").forward(req, res);
		} else { // エラーメッセージがある場合
			// エラー時は変更画面の再表示に必要なデータをセット
			LocalDate todaysDate = LocalDate.now();
			int year = todaysDate.getYear();
			List<Integer> entYearSet = new ArrayList<>();
			for (int i = year - 10; i < year + 11; i++) {
				entYearSet.add(i);
			}
			List<String> list = classNumDao.filter(teacher.getSchool());
			req.setAttribute("ent_year_set", entYearSet);
			req.setAttribute("class_num_set", list);
			// 変更画面にフォワード
			req.getRequestDispatcher("student_update.jsp").forward(req, res);
		}
	}

}