

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class PeticionHTTP
 */
public class PeticionHTTP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PeticionHTTP() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String user = request.getParameter("usuario");
		String password = request.getParameter("pass");
		JSONObject json = new JSONObject();
		try {
	        json.put("dni", user);
	        json.put("password", password);
		} catch(JSONException ejson) { out.println("Error "+ejson);}
		
		URL url = new URL("http://dew-entelbar-2021.dsic.cloud:9090/CentroEducativo/login");
				HttpURLConnection http = (HttpURLConnection)url.openConnection();
				http.setRequestMethod("POST");
				http.setRequestProperty("Content-Type", "application/json");
				http.setUseCaches(false);
		        http.setDoInput(true);
		        http.setDoOutput(true);
		        
		OutputStreamWriter stream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
		stream.write(json.toString());
		out.println(json.toString()); 
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
	              out.println("Res: "+res);
		} catch(Exception e) {
            response.sendError(500, "Hubo problemas al recuperar la información." + e);
            return;
		}
		out.close();
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
