

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logv1
 */
public class Logv1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String nArch = "arch1.txt";
	private static String rArch = "/home/user/tomcat/temp/";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logv1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LocalDateTime date = LocalDateTime.now();
		String iP = request.getRemoteAddr();
		String method = request.getMethod();
		String servlet = request.getServletPath().split("/")[1];
		String nom = request.getParameter("usuario");
		
		File f = new File(rArch + nArch);
		if(!f.exists()) {f.createNewFile();}
		FileWriter fW = new FileWriter(f, true);
		fW.write(date + " " + nom + " " + iP + " " + servlet + " "+ method + "\n");
		fW.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
