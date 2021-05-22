

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
 * Servlet implementation class AsignaturasProfesor
 */
public class AsignaturasProfesor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AsignaturasProfesor() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String res = "";
		PrintWriter out = response.getWriter();	
		HttpSession session = request.getSession(false);
		String dni = (String) session.getAttribute("dni");
		String key = (String) session.getAttribute("key");
		
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		List<String> cookies = (List<String>) session.getAttribute("Cookie");	
		String urlreq = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/profesores/"+dni+"/asignaturas?key="+key;
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
        /*
		[
		  {
		    "acronimo": "DEW",
		    "nombre": "Desarrollo Web",
		    "curso": 3,
		    "cuatrimestre": "B",
		    "creditos": 4.5
		  }
		]
         */
        JSONArray asigJSON = new JSONArray(res);   
        String nombre = request.getRemoteUser();
        getServletContext().getRequestDispatcher("/AsigProf1.html").include(request, response);
        out.println("<h1>Notas Online. Asignaturas impartidas por el Profesor@ "+nombre+"</h1>");
        getServletContext().getRequestDispatcher("/AsigProf2.html").include(request, response);
        
        for (int i = 0; i < asigJSON.length(); i++) {
       	
            JSONObject object =(JSONObject) asigJSON.get(i);         
            out.println("<div class=\"col-sm-4 div-inf\">");
            out.println("<form action='ListaAlumnos'><h4>Acronimo: "+object.getString("acronimo")+"</h4><ul>");
            out.println("<li>Nombre: "+object.getString("nombre")+"</li>");
            out.println("<li>Curso: "+object.getInt("curso")+"</li>");
            out.println("<li>Cuatrimestre: "+object.getString("cuatrimestre")+"</li>");
            out.println("<li>Creditos: "+object.getFloat("creditos")+"</li></ul>");
            out.println("<input type=\"hidden\" name=\"acronimo\" value=\""+object.getString("acronimo")+"\"/>");
            out.println("<button type =\"submit\" class=\"btn btn-primary\">Ver Alumnos</button></form></div>"); 
        }
        getServletContext().getRequestDispatcher("/AsigProf3.html").include(request, response);  
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
