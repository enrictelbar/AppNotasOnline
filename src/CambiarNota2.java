

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CambiarNota2
 */
public class CambiarNota2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambiarNota2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();	
		HttpSession session = request.getSession(false);
		String key = (String) session.getAttribute("key");
		
		String acro = request.getParameter("acronimo");
		String alumno = request.getParameter("alumno");
		String nota = request.getParameter("nota");
		double checkNota;
        	checkNota = Double.parseDouble(nota);
        if (checkNota<0 || checkNota>10) {     
        		//JOptionPane.showMessageDialog(null, "Pon un número válido entre 0 y 10");
            	response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/ProyectoDEW/CambiarNota?acronimo="+acro+"&alumno="+alumno);
            	return;
        }
        
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		List<String> cookies = (List<String>) session.getAttribute("Cookie");	
		String urlreq = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/alumnos/"+alumno+"/asignaturas/"+acro+"?key="+key;
		URL urlpeticion = new URL(urlreq);
		HttpURLConnection httpreq = (HttpURLConnection)urlpeticion.openConnection();
		for (String cookie: cookies) {
			httpreq.addRequestProperty("Cookie", cookie.split(";", 2)[0]); 
			}
		
		httpreq.setRequestMethod("PUT");
		httpreq.setRequestProperty("Content-Type", "application/json");
		httpreq.setUseCaches(false);
		httpreq.setDoInput(true);
		httpreq.setDoOutput(true);
		OutputStreamWriter stream = new OutputStreamWriter(httpreq.getOutputStream(), "UTF-8");		
		stream.write(nota);
		stream.flush();
		stream.close();
		
		if(httpreq.getResponseCode()==HttpURLConnection.HTTP_OK) {
			//out.println("<input type=\"hidden\" name=\"acronimo\" value=\""+acro+"\"/>");
			//request.getRequestDispatcher("/ListaAlumnos").forward(request, response);
			response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/ProyectoDEW/ListaAlumnos?acronimo="+acro);
			
			//http://dew-entelbar-2021.dsic.cloud:8080/ProyectoDEW/
		}
		out.close();
        httpreq.disconnect();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
