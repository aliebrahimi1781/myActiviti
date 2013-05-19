package activiti.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActivitUrlMappingFilter implements Filter {


	/**
	 * 
	 */
	private static String SERVLET_PACKAGE_BASE = null;
	private static String ENCODING = "utf-8";

	private final static HashMap<String, Object> actions = new HashMap<String, Object>();
	private final static HashMap<String, Method> methods = new HashMap<String, Method>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String param = null;
		SERVLET_PACKAGE_BASE = filterConfig.getInitParameter("servlet_package_base");
		
		param = filterConfig.getInitParameter("encoding");
		if(param != null) {
			ENCODING = filterConfig.getInitParameter("encoding");
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		

		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		request.setCharacterEncoding(ENCODING);
		response.setContentType("text/html;charset=utf-8");
		
		String uri = request.getRequestURI();
		Object action = null;
		Method method = null;
		

		action = _loadAction(uri);
		if (null == action) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} else {
			method = _loadMethod(action, uri);
			if(null == method) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} else {
				try {
					method.invoke(action, request, response);
				} catch (Exception e) {
					response.getWriter().write("<pre>");
					e.printStackTrace(response.getWriter());
					response.getWriter().write("</pre>");
				}
			}
		}
	}
	
	/**
	 * /activiti/mgmt/AAA/BBB
	 */
	private Method _loadMethod(Object action, String uri) {
		Method method = null;
		String s_method = null;
		
		int i = uri.indexOf("/", 1);
		String full_path = SERVLET_PACKAGE_BASE;
		full_path += uri.substring(i + 1).replaceAll("/", ".");
		
		i = uri.lastIndexOf("/");
		s_method = uri.substring(i + 1);
		
		method = methods.get(full_path.toUpperCase());
		if(method == null) {
			for(Method m : action.getClass().getMethods()) {
                if(m.getModifiers()==Modifier.PUBLIC && m.getName().equals(s_method)){
                	synchronized(methods){
                		methods.put(full_path.toUpperCase(), m);
                	}
                	method = m;
                    break;
                }
            }
			
		}
		return method;
	}
	
	
	/**
	 * /action/AAA/BBB/CCC/Method
	 */
	private Object _loadAction(String uri) {
		Object action = null;
		String cls = SERVLET_PACKAGE_BASE;
		String method = null;
		
		int i = uri.lastIndexOf("/");
		method = uri.substring(i + 1);
		i = uri.indexOf("/", 1);
		cls += uri.substring(i + 1, uri.length() - method.length() - 1);
		cls = cls.replaceAll("/", ".");
		cls += "Servlet";
		
		action = actions.get(cls.toUpperCase());
		if (action == null) {
			try {
				action = Class.forName(cls).newInstance();
				synchronized (actions) {
					actions.put(cls.toUpperCase(), action);
				}
			} catch (Exception e) {
			}
		}
		return action;
	}
}
