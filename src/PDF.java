

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Servlet implementation class PDF
 */
public class PDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap <String, List<String>> datos = new HashMap<String, List<String>>();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PDF() {
        super();
        // TODO Auto-generated constructor stub
    } 
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
    	if(request.isUserInRole("rolalu")) {
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
	             reader.close();
        }
        //para nombre asignatura
        String res1 = "";
        String urlreq1 = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/asignaturas?key="+key;
		URL urlpeticion1 = new URL(urlreq1);
		HttpURLConnection httpreq1 = (HttpURLConnection)urlpeticion1.openConnection();
		for (String cookie: cookies) {
			httpreq1.addRequestProperty("Cookie", cookie.split(";", 2)[0]); 
			}
		httpreq1.setRequestMethod("GET");
		httpreq1.setRequestProperty("Content-Type", "application/json");
		httpreq1.setUseCaches(false);
		httpreq1.setDoInput(true);
		httpreq1.setDoOutput(true);
        
        try(BufferedReader reader1 = new BufferedReader(
	            new InputStreamReader(httpreq1.getInputStream(), "utf-8"))) {
	              
	              String responseLine1 = null;
	              while ((responseLine1 = reader1.readLine()) != null) {
	                res1 += responseLine1.trim();
	              }       
	             reader1.close();
        }
       
        JSONArray asigJSON = new JSONArray(res);
        JSONArray asigJSON1 = new JSONArray(res1);
        
        
        String asignatura = null;  
    	
       
    	int j;
    	JSONObject object1 = null;
    	for(int i=0;i<asigJSON.length(); i++) {
    		JSONObject object =(JSONObject) asigJSON.get(i);
    		asignatura = object.getString("asignatura");
    		
    		j = -1;
    		do {    			
    			j++;     			
    			object1 =(JSONObject) asigJSON1.get(j);
    			
    			
    		} while(!(asignatura.equals(object1.getString("acronimo"))));    
    		    List<String> a = new ArrayList<String>();
    		  
    	        a.add(object1.getString("nombre"));	
    	      
    	        a.add(String.valueOf(object1.getInt("curso")));		
    	      
    	        a.add(object1.getString("cuatrimestre"));	
    	       
				datos.put(asignatura, a);							
    		}              
    	
        String nombre = request.getRemoteUser();
        getServletContext().getRequestDispatcher("/contenido/PaginaPDF1.html").include(request, response);
        out.println("<span class=\"negrita\">"+nombre+"</span>, con DNI "+dni+", matriculado/a en el curso 2020/21, ha obtenido las calificaciones que se muestran en la siguiente tabla.</p>");       
        getServletContext().getRequestDispatcher("/contenido/PaginaPDF2.html").include(request, response);

        List<String> data = new ArrayList<String>();
        for (int i = 0; i < asigJSON.length(); i++) {

            JSONObject object =(JSONObject) asigJSON.get(i);
            String acro = object.getString("asignatura");
         
            data = datos.get(acro);           
          
            String nom = data.get(0);
           
            String curso = data.get(1);
            String cuatri = data.get(2);
            String nota = object.getString("nota");
            
            out.println("<tr>");
            out.println("<td class=\"text-center columns\">"+curso+cuatri+"</td>");
            out.println("<td class=\"text-center columns\">"+acro+"</td>");
            out.println("<td class=\"table-row\">"+nom+"</td>");
            out.println("<td class=\"text-center table-row\">"+nota+"</td>");
            out.println("</tr>");
        }
        getServletContext().getRequestDispatcher("/contenido/PaginaPDF3.html").include(request, response);
        out.close();
        httpreq.disconnect();
    	} else { response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/nol2021/contenido/Error.html"); }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
