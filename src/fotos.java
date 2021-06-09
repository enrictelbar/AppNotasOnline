

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class fotos
 */
public class fotos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public fotos() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
		String dni = (String) session.getAttribute("dni");	
		String carpeta = getServletContext().getInitParameter("directorio_imagenes");
		response.setContentType("application/json");		
		
		BufferedReader origen = new BufferedReader(new FileReader(carpeta+"/"+dni+".pngb64"));		
		PrintWriter out = response.getWriter();
		out.print("{\"img\": \"");
		String linea = origen.readLine(); out.print(linea);
		while ((linea = origen.readLine()) != null) {out.print("\n"+linea);}
		out.print("\"}");
		out.close(); 
		origen.close();		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		
		String dni = "";
		if(br != null){
			dni = br.readLine();
		}
		br.close();
		//dni es dni=12345678W
		String [] aux = dni.split("=");
		dni = aux[1];
		
		String carpeta = getServletContext().getInitParameter("directorio_imagenes");
		response.setContentType("application/json");		
		BufferedReader origen = new BufferedReader(new FileReader(carpeta+"/"+dni+".pngb64"));
		
		PrintWriter out = response.getWriter();
		out.print("{\"img\": \"");
		String linea = origen.readLine(); out.print(linea);
		while ((linea = origen.readLine()) != null) {out.print("\n"+linea);}
		out.print("\"}");
		out.close(); origen.close();
	}

}
