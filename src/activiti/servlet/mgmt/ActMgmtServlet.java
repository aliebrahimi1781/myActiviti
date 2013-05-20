package activiti.servlet.mgmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.StringUtils;

import activiti.util.ActivitiUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.oreilly.servlet.MultipartRequest;
import common.AppConfig;

public class ActMgmtServlet {
	private static final long serialVersionUID = 1L;

	public ActMgmtServlet() {
	}
	

	/**
	 * 发布流程定义
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void deploy(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		JSONObject json = new JSONObject();
		try {
			MultipartRequest multi = new MultipartRequest(request,
					AppConfig.upload, "utf-8");
			File attach = multi.getFile("attach");
			FileInputStream fin = new FileInputStream(attach);
			ActivitiUtil.deploy(attach.getName(), fin);

			json.put("success", true);

		} catch (IOException e) {
			json.put("success", false);
			json.put("msg", e.getMessage());
			e.printStackTrace();
		} finally {
			response.getWriter().write(json.toString());
		}
	}
	
	/**
	 * 得到流程定义图片
	 * @param request
	 * @param response
	 */
	public void procdefImg(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		InputStream in = ActivitiUtil.getProcDefImg(id);

		response.reset();
		response.setContentType("image/jpeg");
		byte[] b = new byte[1024];
		try {
			while (in.read(b) > 0) {
				response.getOutputStream().write(b, 0, b.length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 得到流程定义配置xml
	 * @param request
	 * @param response
	 */
	public void procdefXml(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		ProcessDefinition def = ActivitiUtil.getProcDef(id);
		InputStream in = ActivitiUtil.getProcDefXml(id);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.reset();
			response.setHeader("Content-Disposition", "attachment;filename="
					+ URLEncoder.encode(def.getResourceName(), "UTF-8"));
			response.setContentType("application/octet-stream; CHARSET=utf-8");
			byte[] b = new byte[1024];
			while (in.read(b) > 0) {
				out.write(b, 0, b.length);
			}
			out.flush();
			out.close();
			out = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发起流程实例
	 * @param request
	 * @param response
	 * @throws IOException
	 */
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
	
	/**
	 * 完成任务
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void completeTask(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
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
		
		JSONObject json = new JSONObject();
		try {
			ActivitiUtil.completeTask(id, variables);
			json.put("success", true);
		} catch(Exception e) {
			json.put("success", false);
			json.put("msg", e.getMessage());
		} finally {
			response.getWriter().write(json.toString());
		}
	}

	

}
