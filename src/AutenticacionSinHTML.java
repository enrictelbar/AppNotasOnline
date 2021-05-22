

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
/**
 * Servlet implementation class AutenticacionSinHTML
 */
public class AutenticacionSinHTML extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private List<String> cookiesAdmin = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AutenticacionSinHTML() {
        super();
        // TODO Auto-generated constructor stub
    }
    public class Usuario {
		private String dni;
		private String password;
		public Usuario(String dni, String password) {
			this.dni = dni;
			this.password = password;
		}
		public void setDni(String dni) {
			this.dni = dni;
		}
		public void setPwd(String password) {
			this.password = password;
		}
		public String getPwd() {
			return this.password;
		}
		public String getDni() {
			return this.dni;
		}
	}
    
    
    private  HashMap <String, Usuario> buildDB() throws IOException{//HashMap <String, Usuario> buildDB() throws IOException{
    	HashMap <String, Usuario> users = new HashMap<String, Usuario>();
    	String adminKey = getKey("111111111","654321");
    	
    	JSONObject[] usuariosDB = getUsuarios(adminKey);
    	
    	for(int i = 0; i < usuariosDB.length;i++) {
    		users.put(usuariosDB[i].getString("nombre"), new Usuario(usuariosDB[i].getString("dni"), "123456"));
    	}
    	return users;
    }
    
    private String getKey(String dni, String pass) throws IOException {
    	JSONObject json = new JSONObject();
		try {
		json.put("dni", dni);
	    json.put("password", pass);
		} catch(JSONException ejson) { System.err.print(ejson);}
		
		URL url = new URL("http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/login");
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.setRequestMethod("POST");
		http.setRequestProperty("Content-Type", "application/json");
		http.setUseCaches(false);
        http.setDoInput(true);
        http.setDoOutput(true);
        
        OutputStreamWriter stream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
		stream.write(json.toString()); 
		stream.flush();
		stream.close();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
	              String res = "";
	              String responseLine = null;
	              while ((responseLine = reader.readLine()) != null) {
	                res += responseLine.trim();
	              }
	    cookiesAdmin =  http.getHeaderFields().get("Set-Cookie");
    	return res;
    }
    
    private JSONObject [] getUsuarios(String key) throws IOException {
    	String res="";
    	String res1="";
    	String urlreq = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/alumnos?key="+key;
		URL urlpeticion = new URL(urlreq);
		HttpURLConnection httpreq = (HttpURLConnection)urlpeticion.openConnection();
    	
		for (String cookie: cookiesAdmin) {
			httpreq.addRequestProperty("Cookie", cookie.split(";", 2)[0]); 
			}
		
		httpreq.setRequestMethod("GET");
		httpreq.setRequestProperty("Content-Type", "application/json");
		httpreq.setUseCaches(false);
		httpreq.setDoInput(true);
		httpreq.setDoOutput(true);
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpreq.getInputStream(), "utf-8"));
		String responseLine = null;
		      while ((responseLine = reader.readLine()) != null) {
		          res += responseLine.trim();
		      }     
        String urlreq1 = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/profesores?key="+key;
		URL urlpeticion1 = new URL(urlreq1);
		HttpURLConnection httpreq1 = (HttpURLConnection)urlpeticion1.openConnection();
		
		httpreq1.setRequestMethod("GET");
		httpreq1.setRequestProperty("Content-Type", "application/json");
		httpreq1.setUseCaches(false);
		httpreq1.setDoInput(true);
		httpreq1.setDoOutput(true);
		
		for (String cookie: cookiesAdmin) {
			httpreq1.addRequestProperty("Cookie", cookie.split(";", 2)[0]); 
			}
		
		BufferedReader reader1 = new BufferedReader(new InputStreamReader(httpreq1.getInputStream(), "utf-8"));
		String responseLine1 = null;
		      while ((responseLine1 = reader1.readLine()) != null) {
		          res1 += responseLine1.trim();
		      }             
		
	    //JSONObject usuariosDB = new JSONObject(res);
		res = res.replace("[", "").replace("]", "");
		String[] aux = res.split("\"}");
		for(int i = 0; i < aux.length;i++) {
			aux[i] = aux[i] + "\"}";
			if(i!=0) {aux[i]=aux[i].substring(1, aux[i].length());}
		}
		res1 = res1.replace("[", "").replace("]", "");
		String[] aux1 = res1.split("\"}");
		for(int i = 0; i < aux1.length;i++) {
			aux1[i] = aux1[i] + "\"}";
			if(i!=0) {aux1[i]=aux1[i].substring(1, aux1[i].length());}
		}
		JSONObject [] json = new JSONObject[aux.length+aux1.length];
		int i;
		int cont = 0;
		for(i = 0; i < aux.length; i++) {
			json[i] = new JSONObject(aux[i]);			
		}
		for(int j = i; j < aux.length+aux1.length; j++) {
			json[j] = new JSONObject(aux1[cont]);
			cont++;
		}			
    	return json ;
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HashMap <String, Usuario> users = buildDB();
		PrintWriter out = response.getWriter();
        HttpSession misesion = request.getSession(true);
        URL url = new URL("http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/login");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
		String login = null;
		
		List<String> cookies = null;
		if(misesion.getAttribute("key") == null) {
			try {
			login = request.getRemoteUser();
			
			} catch (Exception e){ response.sendError(500,"El usuario o la contraseña son incorrectos: WEB.AUTH" + e);} 
			if(!(login == null)) {
				
				Usuario user = users.get(login);
				//dew-entelbar-2021.dsic.cloud:8080/ProyectoDEW/VistaPrincipal.html
				misesion.setAttribute("dni", user.getDni());
				misesion.setAttribute("pass", user.getPwd());
				
				//Envío al servidor para que nos devuelva la key
				JSONObject json = new JSONObject();
				try {
				json.put("dni", user.getDni());
			    json.put("password", user.getPwd());
				} catch(JSONException ejson) { out.println("Error "+ejson);}
				//Guardas las cookies en un array para asociar la lista a la sesión
                
                
				http.setRequestMethod("POST");
				http.setRequestProperty("Content-Type", "application/json");
				http.setUseCaches(false);
		        http.setDoInput(true);
		        http.setDoOutput(true);
		        
		        
				OutputStreamWriter stream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
				stream.write(json.toString());
				//out.println(json.toString()); 
				stream.flush();
				stream.close();
				//leemos la respuesta del servidor y nos guardamos la key
				try(BufferedReader reader = new BufferedReader(
			            new InputStreamReader(http.getInputStream(), "utf-8"))) {
			              String res = "";
			              String responseLine = null;
			              while ((responseLine = reader.readLine()) != null) {
			                res += responseLine.trim();
			              }   
			              misesion.setAttribute("key", res);        
			              cookies =  http.getHeaderFields().get("Set-Cookie");
			              misesion.setAttribute("Cookie", cookies); //ID = 6731741734, PATH=CENTRO.. X 
				} catch(Exception e1) {
                    response.sendError(500, "Hubo problemas al recuperar la información. DATA.AUTH" + e1);
                    return;
                }

                    if(request.isUserInRole("rolalu")) {
                        response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/ProyectoDEW/Asignaturas");

                    }
                    if(request.isUserInRole("rolpro")) {
                        response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/ProyectoDEW/AsignaturasProfesor");
                    } 
                    out.close();


            }
        }  else { 

            if(request.isUserInRole("rolalu")) {
                response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/ProyectoDEW/Asignaturas");

                }
            if(request.isUserInRole("rolpro")) {
                response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/ProyectoDEW/AsignaturasProfesor");

                }
        }
        http.disconnect();
    }

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
