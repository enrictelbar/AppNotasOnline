

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
 * Servlet implementation class CambiarNotas
 */
public class CambiarNotas extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String dni;
    private String acro;
    private String nota;
    private String key;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CambiarNotas() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Método para debuggear.
		PrintWriter out = response.getWriter();
		out.print(key);
		out.print(nota);
		out.print(dni);
		out.print(acro);
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.isUserInRole("rolpro")) {
BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		
		String json = "";
		if(br != null){
			json = br.readLine();
		}
		String [] aux = json.split("&"); //nota=9 | acronimo=DCU | dni=23456387R
		String [] valores = new String[3];
		for(int i = 0; i < aux.length; i++) {
			valores[i] = aux[i].split("=")[1]; //9 | DCU | 23456387R
		}
		//Lee los valores que le envían y los almacena.
		nota = valores[0];
		acro = valores[1];
		dni  = valores[2];
		
		PrintWriter out = response.getWriter();	
		
		//Comprueba que la nota esté entre 0 y 10.
		double checkNota;
    	checkNota = Double.parseDouble(nota);
    	if (checkNota<0 || checkNota>10) {  
    		out.print("0");
    		out.close();
    	}else {
    		//Si lo está y todo va bien, crea una conexión con CentroEducativo y cambia la nota.
    		HttpSession session = request.getSession(false);
    		String key = (String) session.getAttribute("key");
    		response.setContentType("text/html; charset=UTF-8");
    		response.setCharacterEncoding("UTF-8");
    		List<String> cookies = (List<String>) session.getAttribute("Cookie");	
    		String urlreq = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/alumnos/"+dni+"/asignaturas/"+acro+"?key="+key;
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
				httpreq.disconnect();
				out.print("1");
				out.close();
			}else {
		        httpreq.disconnect();
		    	out.close();
			}
    	}
		} else { response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/nol2021/contenido/Error.html"); }
	}
	
}

