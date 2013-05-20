<%@page import="org.activiti.engine.task.Task"%>
<%@page import="org.activiti.engine.history.HistoricTaskInstance"%>
<%@page import="org.activiti.engine.repository.DiagramNode"%>
<%@page import="org.activiti.engine.repository.ProcessDefinition"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.activiti.engine.task.IdentityLink"%>
<%@page import="java.util.List"%>
<%@page import="activiti.util.ActivitiUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String processInsId = request.getParameter("id");
ProcessDefinition def = ActivitiUtil.getProcDefByInst(processInsId);
List<Task> tasks = ActivitiUtil.queryTaskByProcInst(processInsId);
%>
<style>
.node,.node_finished{filter:alpha(opacity=40);}
.node{background:yellow;border:2px solid red;};
</style>
<img src="../action/mgmt/ActMgmt/procdefImg?id=<%=def.getId()%>">
<%
for(Task task : tasks) {
  DiagramNode node = ActivitiUtil.getNodeInProcDef(def.getId(), task.getTaskDefinitionKey());
  boolean finished = (task.getDueDate()!=null);
%>
<div class="node<%=finished?"_finished":"" %>" id="node_<%=task.getId() %>" taskId="<%=task.getId()%>" style="position:absolute;top:<%=node.getY()-1%>px;left:<%=node.getX()-1%>px;width:<%=node.getWidth()%>px;height:<%=node.getHeight()%>px;"></div>
<%} %>
