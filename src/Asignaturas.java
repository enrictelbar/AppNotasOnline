

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Servlet implementation class Asignaturas
 */
public class Asignaturas extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Asignaturas() {
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
		
		List<String> cookies = (List<String>) session.getAttribute("Cookie");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String urlreq = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/alumnos/"+dni+"/asignaturas?key="+key;
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
        //[{"asignatura":"DCU","nota":""},{"asignatura":"DEW","nota":""},{"asignatura":"IAP","nota":""}]
        JSONArray asigJSON = new JSONArray(res);
        String nombre = request.getRemoteUser();
        getServletContext().getRequestDispatcher("/Intermedio1.html").include(request, response);
        out.println("<h1>Notas Online. Asignaturas <br /> del/la alumn@ "+ nombre +"</h1>");
        getServletContext().getRequestDispatcher("/Intermedio2.html").include(request, response);

        for (int i = 0; i < asigJSON.length(); i++) {

            JSONObject object =(JSONObject) asigJSON.get(i);
            String asig = object.getString("asignatura");
            out.println("<li class=\"nav-item\" role=\"presentation\">");
            out.println("<button class=\"nav-link\" id=\""+asig+"-tab\" data-bs-toggle=\"tab\" data-bs-target=\"#"+asig+"\" type=\"button\" role=\"tab\" aria-controls=\""+asig+"\" aria-selected=\"true\">"+asig+"</button>");
            out.println("</li>");
             }
        out.println("</ul>");      
        out.println("<div class=\"tab-content\" id=\"ex1-content\">");
        for (int i = 0; i < asigJSON.length(); i++) {

            JSONObject object =(JSONObject) asigJSON.get(i);
            String asig = object.getString("asignatura");
            String nota = object.getString("nota");
            out.println("<div class=\"tab-pane fade\" id=\""+asig+"\" role=\"tabpanel\" aria-labelledby=\""+asig+"-tab\">");
            out.println("<h4>"+asig+"</h4>");
            out.println("<p>Nota obtenida: "+nota+"</p>");
            out.println("</div>");
             }
        out.println("</div>");
        getServletContext().getRequestDispatcher("/Intermedio3.html").include(request, response);
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
