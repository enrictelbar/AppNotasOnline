

import java.io.IOException; 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Resend
 */
public class Resend extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public int num = 0;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Resend() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		String [] direccion = {"https://www.abc.es/", "https://elpais.com/", "https://www.marca.com/", "http://personales.upv.es/rgarcia/dew3/quijotoc_tdc_listaNum_parpadeo.html" };
		if(num == 4) {num = 0;}
		response.sendRedirect(direccion[num++]);
		//response.sendRedirect("http://personales.upv.es/rgarcia/dew3/quijotoc_tdc_listaNum_parpadeo.html");
		//request.getRequestDispatcher("http://personales.upv.es/rgarcia/dew3/quijotoc_tdc_listaNum_parpadeo.html").forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
