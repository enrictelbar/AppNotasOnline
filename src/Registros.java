

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class Registros
 */
public class Registros implements Filter {
	private static String nArch = "arch1.txt";
	private static String rArch = "/home/user/tomcat/temp/";  
    /**
     * Default constructor. 
     */
    public Registros() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * Se crea un fichero llamado arch1.txt en caso de no haberlo y se resgistran en él todas las interacciones con 
	 * la aplicación.
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		/*
		 * Obtención de los parámetros a registrar.
		*/
		HttpServletRequest request = (HttpServletRequest) req;
		String nom = request.getRemoteUser();
		LocalDateTime date = LocalDateTime.now();
		String iP = request.getRemoteAddr();
		String method = request.getMethod();
		String servlet = request.getServletPath().split("/")[2];
		//Eliminamos las llamadas a bootstrap y css.
		if (!(servlet.equals("bootstrap") || servlet.equals("css"))) {
			File f = new File(rArch + nArch);
			if(!f.exists()) {f.createNewFile();}
			FileWriter fW = new FileWriter(f, true);
			fW.write(date + " " + nom + " " + iP + " " + servlet + " "+ method + "\n");
			fW.close();
		}		
		// pass the request along the filter chain		
		
		chain.doFilter(req, res);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
