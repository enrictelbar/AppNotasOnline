

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class ListaAlumnos
 */
public class ListaAlumnos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListaAlumnos() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String acro = request.getParameter("acronimo");	
		
		String res = "";		
		PrintWriter out = response.getWriter();	
		HttpSession session = request.getSession(false);
		String key = (String) session.getAttribute("key");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		List<String> cookies = (List<String>) session.getAttribute("Cookie");	
		String urlreq = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/asignaturas/"+acro+"/alumnos?key="+key;
		URL urlpeticion = new URL(urlreq);
		HttpURLConnection httpreq = (HttpURLConnection)urlpeticion.openConnection();
		for (String cookie: cookies) {
			httpreq.addRequestProperty("Cookie", cookie.split(";", 2)[0]); 
			}
		httpreq.setRequestMethod("GET");
		httpreq.setRequestProperty("Content-Type", "application/json");
		httpreq.setUseCaches(false);
		httpreq.setDoInput(true);
		httpreq.setDoOutput(true);
       
        try(BufferedReader reader = new BufferedReader(
	            new InputStreamReader(httpreq.getInputStream(), "utf-8"))) {
	              
	              String responseLine = null;
	              while ((responseLine = reader.readLine()) != null) {
	                res += responseLine.trim();
	              } 	          
        }
        JSONArray asigJSON = new JSONArray(res);    
        getServletContext().getRequestDispatcher("/ListaAlumnosPro1.html").include(request, response);
        out.println("<h1>Notas Online. Notas de la asignatura "+acro+"</h1>");
        getServletContext().getRequestDispatcher("/ListaAlumnosPro2.html").include(request, response);
        for (int i = 0; i < asigJSON.length(); i++) {
	       	
            JSONObject object =(JSONObject) asigJSON.get(i);  
            out.println("<div class=\"col-sm-2 div-inf\"><form action ='CambiarNota'><ul>");
            out.println("<li>DNI: <span class=\"DNI-alumno\">"+object.getString("alumno")+"</span></li>");
            out.println("<li>Calificación: <span class=\"Nota-Alumno\" id=\"notaAlumno-"+object.getString("alumno")+"\">"+object.getString("nota")+"</span></li></ul>");
            out.println("<input type=\"hidden\" name=\"acronimo\" value=\""+acro+"\"/>");
            out.println("<input type=\"hidden\" name=\"alumno\" value=\""+object.getString("alumno")+"\"/>");
            out.println("<button type =\"submit\" class=\"btn btn-primary\">Modificar Nota</button></form></div>");
        }
        getServletContext().getRequestDispatcher("/ListaAlumnosPro3.html").include(request, response);      
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
