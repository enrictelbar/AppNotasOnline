
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CambiarNota
 */
public class CambiarNota extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambiarNota() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();	
		String acro = request.getParameter("acronimo");
		String alumno = request.getParameter("alumno");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
        getServletContext().getRequestDispatcher("/Nota1.html").include(request, response);
        out.println("<h2>Alumn@: <span id=\"DNIAlumno\">"+alumno+"</span></h2>");       
        getServletContext().getRequestDispatcher("/Nota2.html").include(request, response); 
        out.println("<input type=\"hidden\" name=\"acronimo\" value=\""+acro+"\"/>");
        out.println("<input type=\"hidden\" name=\"alumno\" value=\""+alumno+"\"/>");
        getServletContext().getRequestDispatcher("/Nota3.html").include(request, response);
        out.close();	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
