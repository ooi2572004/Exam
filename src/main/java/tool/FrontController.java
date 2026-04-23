package tool;
 
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@WebServlet(urlPatterns = { "*.action" })
public class FrontController extends HttpServlet {
 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			// パスを取得し、クラス名に変換する（元の完璧なロジック）
			String path = req.getServletPath().substring(1);
			String name = path.replace(".a", "A").replace('/', '.');
			
			System.out.println("★FrontControllerが探しているクラス: " + name);
 
			Action action = (Action) Class.forName(name).getDeclaredConstructor().newInstance();
			action.execute(req, res);
 
		} catch (Exception e) {
			e.printStackTrace(); // コンソールに本当のエラーを出す
			// error.jspがないと404になるので、一旦コメントアウトします
			// req.getRequestDispatcher("/error.jsp").forward(req, res);
		}
	}
 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
}
 