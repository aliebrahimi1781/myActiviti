<%@page import="org.activiti.engine.repository.ProcessDefinition"%>
<%@page import="java.util.List"%>
<%@page import="activiti.mgmt.util.ActivitiUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
#detail span.lbl{
vertical-align:top;
display:inline-block;
width:70px;
}
</style>
<%
String id = request.getParameter("id");
ProcessDefinition def = ActivitiUtil.getProcessDefinitionById(id);
%>
<div id="detail">
<ul>
  <li><span class="lbl">id:</span><%=def.getId() %></li>
  <li><span class="lbl">key:</span><%=def.getKey() %></li>
  <li><span class="lbl">name:</span><%=def.getName() %></li>
  <li><span class="lbl">version:</span><%=def.getVersion() %></li>
  <li><span class="lbl">内容:</span><img src="../mgmt?m=pdImg&id=<%=def.getId()%>"></li>
  <li><span class="lbl"></span><a href="../mgmt?m=pdXml&id=<%=def.getId()%>">下载流程定义</a></li>
</ul>
</div>