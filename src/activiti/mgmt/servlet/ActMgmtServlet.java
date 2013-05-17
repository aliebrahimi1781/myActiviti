package activiti.mgmt.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;

import com.alibaba.fastjson.JSONObject;
import com.oreilly.servlet.MultipartRequest;

import activiti.mgmt.util.ActivitiUtil;

import common.AppConfig;

/**
 * Servlet implementation class ActMgmtServlet
 */
@WebServlet("/mgmt")
public class ActMgmtServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public ActMgmtServlet() {
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String m = request.getParameter("m");
		if("pdImg".equals(m)) {
			pdImg(request, response);
		} else if("pdXml".equals(m)) {
			pdXml(request, response);
		} else if("deploy".equals(m)) {
			deploy(request, response);
		}
	}

	private void deploy(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		JSONObject json = new JSONObject();
		try {
			MultipartRequest multi = new MultipartRequest(request, AppConfig.upload, "utf-8");
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
	
	private void pdXml(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		ProcessDefinition def = ActivitiUtil.getProcessDefinitionById(id);
		InputStream in = ActivitiUtil.getProcessDefinitionXmlById(def);
		OutputStream out = null;
		try {
			out = response.getOutputStream();
			response.reset();
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(def.getResourceName(), "UTF-8"));
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

	private void pdImg(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		InputStream in = ActivitiUtil.getProcessDefinitionImgById(id);
		
		response.reset();
		response.setContentType("image/jpeg");
		byte[] b = new byte[1024];
		try {
			while(in.read(b)>0) {
				response.getOutputStream().write(b, 0, b.length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
