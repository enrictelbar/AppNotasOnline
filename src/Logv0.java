

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Logv0
 */
public class Logv0 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logv0() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String preHTML = "<!DOCTYPE html> <html lang=\'es-es\'> <head> <meta charset=\'utf-8\' />";
		preHTML += "<meta name='description' content='Laboratorio de DEW, curso 2020/21' /> <meta name='author' content='EquipoDEW' />";
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		LocalDateTime date = LocalDateTime.now();
		String iP = request.getRemoteAddr();
		String method = request.getMethod();
		String servlet = request.getServletPath().split("/")[1];
		String nom = request.getParameter("usuario");

		out.println(preHTML + "<title>S0</title></head><body><h1>" + date + " " + nom + " " + iP + " " + servlet + " "+ method + "</h1></body></html>");
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
