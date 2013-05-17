package activiti.mgmt.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * Servlet implementation class ActTestServlet
 */
@WebServlet("/test")
public class ActTestServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public ActTestServlet() {
//        super();
//    }
//
//	/**
//	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		String m = request.getParameter("m");
//		if("startProcessIns".equals(m)) {
//			startProcessIns(request, response);
//		} else if("completeTask".equals(m)) {
//			completeTask(request, response);
//		}
//	
//	}
//
//	private void completeTask(HttpServletRequest request,
//			HttpServletResponse response) throws IOException {
//		String id = request.getParameter("id");
//		String var = request.getParameter("var");
//		Map<String, Object> variables = new HashMap<String, Object>();
//		
//		if(StringUtils.isNotEmpty(var)) {
//			JSONObject json = JSONObject.fromObject(var);
//			Iterator<String> ite = json.keys();
//			while(ite.hasNext()) {
//				String key = ite.next();
//				String value = json.getString(key);
//				variables.put(key, value);
//			}
//		}
//		
//		JSONObject json = new JSONObject();
//		try {
//			ActivitiUtil.completeTask(id, variables);
//			json.put("success", true);
//		} catch(Exception e) {
//			json.put("success", false);
//			json.put("msg", e.getMessage());
//		} finally {
//			response.getWriter().write(json.toString());
//		}
//	}
//
//	private void startProcessIns(HttpServletRequest request,
//			HttpServletResponse response) throws IOException, ServletException {
//		String pdId = request.getParameter("id");
//		String var = request.getParameter("var");
//		Map<String, Object> variables = new HashMap<String, Object>();
//		
//		if(StringUtils.isNotEmpty(var)) {
//			JSONObject json = JSONObject.fromObject(var);
//			Iterator<String> ite = json.keys();
//			while(ite.hasNext()) {
//				String key = ite.next();
//				String value = json.getString(key);
//				variables.put(key, value);
//			}
//		}
//		
//		ActivitiUtil.runtimeService.startProcessInstanceById(pdId, variables);
//		response.sendRedirect(request.getContextPath() + "/pages/process_instance_list.jsp?id="+pdId);
//	}

}
