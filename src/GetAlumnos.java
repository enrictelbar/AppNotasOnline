

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

/**
 * Servlet implementation class GetAlumnos
 */
public class GetAlumnos extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String acro;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAlumnos() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(request.isUserInRole("rolpro")) {
					
		String res = "";
		//Obtiene la sesión y con ella la clave del usuario.
		PrintWriter out = response.getWriter();	
		HttpSession session = request.getSession(false);
		String key = (String) session.getAttribute("key");
		
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		//Se procesa la asignatura recibida por el método POST para obtener su acrónimo.
		String [] aux = acro.split("=");
		acro = aux[1];
		
		//Se crea una conexión para recibir los alumnos.
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
	              reader.close();
        }
        JSONArray asigJSON = new JSONArray(res);
		out.print(asigJSON);
		out.close();	
		} else { response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/nol2021/contenido/Error.html"); }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Recibe la asignatura a la que pertenecen los alumnos que se piden.
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		
		String json = "";
		if(br != null){
			json = br.readLine();
		}
		br.close();
		acro = json;
		//Recibe "acronimo=IAP"
	}

}
