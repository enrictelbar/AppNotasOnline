

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * Servlet Filter implementation class Autenticacion
 */
public class Autenticacion implements Filter {
	 private List<String> cookiesAdmin = null;
    /**
     * Default constructor. 
     */
    public Autenticacion() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}
	/*
	 * Clase Usuario con los atributos de tipo String dni y password.
	*/
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
	
	/*
	 * Crea un HashMap en el que se introducen los usuarios con sus respectivos dni y password indexados por login. 
	 * Siendo el login el nombre de los usuarios.
	*/
	private  HashMap <String, Usuario> buildDB() throws IOException{
    	HashMap <String, Usuario> users = new HashMap<String, Usuario>();
    	String adminKey = getKey("23456733H","123456");
    	
    	JSONObject[] usuariosDB = getUsuarios(adminKey);
    	
    	for(int i = 0; i < usuariosDB.length;i++) {
    		users.put(usuariosDB[i].getString("nombre"), new Usuario(usuariosDB[i].getString("dni"), "123456"));
    	}
    	users.remove("Ramón");
    	Usuario u = new Usuario("23456733H","123456");
    	users.put("Ramon", u);
    	return users;
    }
    
	/*
	 * Devuelve la clave del usuario con dni y contraseña pasados como parámetros
	*/
    private String getKey(String dni, String pass) throws IOException {
    	JSONObject json = new JSONObject();
    	
    	//Realiza un petición a CentroEducativo/login
		try {
		json.put("dni", dni);
	    json.put("password", pass);
		} catch(JSONException ejson) { System.err.print(ejson);}
		
		URL url = new URL("http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/login");
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.setRequestMethod("POST");
		http.setRequestProperty("Content-Type", "application/json;  charset=utf-8");
		http.setUseCaches(false);
        http.setDoInput(true);
        http.setDoOutput(true);
        
        OutputStreamWriter stream = new OutputStreamWriter(http.getOutputStream(), "utf-8");
		stream.write(json.toString()); 
		stream.flush();
		stream.close();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(), "utf-8"));
	              String res = "";
	              String responseLine = null;
	              while ((responseLine = reader.readLine()) != null) {
	                res += responseLine.trim();
	              }
	              reader.close();
	     
	    //Guardamos las cookies del administrador para pedir usuarios posteriormente.
	              
	    cookiesAdmin =  http.getHeaderFields().get("Set-Cookie"); 
    	return res;
    }
    
    /* 
     * Devuelve un array de objetos JSON con los alumnos y profesores del CentroEductivo pasándo como 
     * parámetro la clave de un usuario autorizado.
    */
    private JSONObject [] getUsuarios(String key) throws IOException {
    	String res="";
    	String res1="";
    	/*
    	 * Petición de usuarios alumno y almacenamiento de estos en la variable res.
    	*/
    	String urlreq = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/alumnos?key="+key;
		URL urlpeticion = new URL(urlreq);
		HttpURLConnection httpreq = (HttpURLConnection)urlpeticion.openConnection();
    	
		for (String cookie: cookiesAdmin) {
			httpreq.addRequestProperty("Cookie", cookie.split(";", 2)[0]); 
			}
		
		httpreq.setRequestMethod("GET");
		httpreq.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		httpreq.setUseCaches(false);
		httpreq.setDoInput(true);
		httpreq.setDoOutput(true);
		
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpreq.getInputStream(), "utf-8"));
		String responseLine = null;
		      while ((responseLine = reader.readLine()) != null) {
		          res += responseLine.trim();
		      } 
		      reader.close();
		      
		/*
		 * Petición de usuarios profesor y almacenamiento de estos en la variable res1.
		*/      
        String urlreq1 = "http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/profesores?key="+key;
		URL urlpeticion1 = new URL(urlreq1);
		HttpURLConnection httpreq1 = (HttpURLConnection)urlpeticion1.openConnection();
		
		httpreq1.setRequestMethod("GET");
		httpreq1.setRequestProperty("Content-Type", "application/json; charset=utf-8");
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
		      reader1.close();
		
		
		/*
		 * Procesamiento de res y res1 para poder crear objetos JSON.
		 * Queda así:
		 * 0: {"dni":"12345678W","nombre":"Pepe","apellidos":"Garcia Sanchez}
		 * 1: {"dni":"23456387R","nombre":"Maria","apellidos":"Fernandez Gómez}
		 * 2: {"dni":"34567891F","nombre":"Miguel","apellidos":"Hernandez Llopis}
		 * 3: {"dni":"93847525G","nombre":"Laura","apellidos":"Benitez Torres}
		 * 4: {"dni":"37264096W","nombre":"Minerva","apellidos":"Alonso Pérez}
		*/
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
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;			
		//Crea un HashMap de usuarios indexado por login
			HashMap <String, Usuario> users = buildDB(); 
			PrintWriter out = response.getWriter();
	        HttpSession misesion = request.getSession(true);
	        
	        //Creamos una conexión con el servidor de CentroEducativo.
	        URL url = new URL("http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/login");
	        HttpURLConnection http = (HttpURLConnection)url.openConnection();
			String login = null;
			
			List<String> cookies = null;
			
			/*
			 * Si aun no se ha creado una sesión, creamos una y añadimos la clave, las cookies, el dni 
			 * y la contraseña como atributos
			*/
			
			if(misesion.getAttribute("key") == null) {
				try {
				login = request.getRemoteUser();
				
				} catch (Exception e){ response.sendError(500,"El usuario o la contraseña son incorrectos: WEB.AUTH" + e);} 
				if(!(login == null)) {					
					Usuario user = users.get(login);
					misesion.setAttribute("dni", user.getDni());
					misesion.setAttribute("pass", user.getPwd());
					
					JSONObject json = new JSONObject();
					
					/*
					 * Enviamos las credenciales del usuario para obtener la clave.
					*/
					try {
					json.put("dni", user.getDni());
				    json.put("password", user.getPwd());
					} catch(JSONException ejson) { out.println("Error "+ejson);}
	                
					http.setRequestMethod("POST");
					http.setRequestProperty("Content-Type", "application/json; charset=utf-8");
					http.setUseCaches(false);
			        http.setDoInput(true);
			        http.setDoOutput(true);
			        
			        
					OutputStreamWriter stream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
					stream.write(json.toString());
					stream.flush();
					stream.close();

					try(BufferedReader reader = new BufferedReader(
				            new InputStreamReader(http.getInputStream(), "utf-8"))) {
				              String res = "";
				              String responseLine = null;
				              while ((responseLine = reader.readLine()) != null) {
				                res += responseLine.trim();
				              }   
				              reader.close();
				              
				              misesion.setAttribute("key", res);        
				              cookies =  http.getHeaderFields().get("Set-Cookie");
				              misesion.setAttribute("Cookie", cookies); //ID = 6731741734, PATH=CENTRO.. X 
					} catch(Exception e1) {
	                    response.sendError(500, "Hubo problemas al recuperar la información. DATA.AUTH" + e1);
	                    return;
	                }

	                    if(request.isUserInRole("rolalu")) {
	                    	//Si el usuario es un alumno, se le redirige al servlet Asignaturas.
	                        response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/nol2021/contenido/Asignaturas");
	                        return;
	                    }
	                    if(request.isUserInRole("rolpro")) {
	                    	//Si el usuario es un profesor, se le redirige al servlet AsignaturasProfesor
	                        response.sendRedirect("http://dew-entelbar-2021.dsic.cloud:8080/nol2021/contenido/AsignaturasProfesor");
	                        return;
	                    } 
	                    out.close();


	            }
			}
	        http.disconnect();	    
	        
		chain.doFilter(req, resp);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
