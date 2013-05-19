package activiti.servlet.mgmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;

import activiti.util.ActivitiUtil;

import com.alibaba.fastjson.JSONObject;
import com.oreilly.servlet.MultipartRequest;
import common.AppConfig;

public class ActMgmtServlet {
	private static final long serialVersionUID = 1L;

	public ActMgmtServlet() {
	}
	

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

	

}
