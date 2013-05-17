<%@page import="org.activiti.engine.history.HistoricTaskInstance"%>
<%@page import="org.activiti.engine.repository.DiagramNode"%>
<%@page import="org.activiti.engine.repository.ProcessDefinition"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.activiti.engine.task.IdentityLink"%>
<%@page import="java.util.List"%>
<%@page import="activiti.mgmt.util.ActivitiUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String processInsId = request.getParameter("id");
ProcessDefinition def = ActivitiUtil.getProcessDefinitionByProcessInsId(processInsId);
List<HistoricTaskInstance> tasks = ActivitiUtil.queryTaskByProcessInsId(processInsId);
%>
<style>
.node,.node_finished{filter:alpha(opacity=40);}
.node{background:yellow;border:2px solid red;};
</style>
<img src="../mgmt?m=pdImg&id=<%=def.getId()%>">
<%for(HistoricTaskInstance task : tasks) {
  DiagramNode node = ActivitiUtil.getNodeInProcessDefinition(def.getId(), task.getTaskDefinitionKey());
  boolean finished = (task.getEndTime()!=null);
%>
<div class="node<%=finished?"_finished":"" %>" id="node_<%=task.getId() %>" taskId="<%=task.getId()%>" style="position:absolute;top:<%=node.getY()-1%>px;left:<%=node.getX()-1%>px;width:<%=node.getWidth()%>px;height:<%=node.getHeight()%>px;"></div>
<%} %>
