package activiti.servlet.mgmt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import activiti.util.ActivitiUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ActTestServlet {
	private static final long serialVersionUID = 1L;


//	public void completeTask(HttpServletRequest request,
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
	
	public static void main(String[] args) {
		String var = "{employeeName:\"haodw\", numberOfDays:100,vacationMotivation:\"I'm really tired!\"}";
		if(StringUtils.isNotEmpty(var)) {
			JSONObject json = JSON.parseObject(var);
			Iterator<String> ite = json.keySet().iterator();
			while(ite.hasNext()) {
				String key = ite.next();
				String value = json.getString(key);
				System.out.println(key);
			}
		}
		
	}
	

	public void startProcessIns(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String procDefId = request.getParameter("id");
		String var = request.getParameter("var");
		Map<String, Object> variables = new HashMap<String, Object>();
		
		
		
		if(StringUtils.isNotEmpty(var)) {
			JSONObject json = JSON.parseObject(var);
			Iterator<String> ite = json.keySet().iterator();
			while(ite.hasNext()) {
				String key = ite.next();
				String value = json.getString(key);
				variables.put(key, value);
			}
		}
		
		ActivitiUtil.runtimeService.startProcessInstanceById(procDefId, variables);
		response.sendRedirect(request.getContextPath() + "/pages/process_instance_list.jsp?id="+procDefId);
	}

}
