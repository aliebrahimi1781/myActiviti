<%@page import="org.activiti.engine.runtime.ProcessInstance"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.activiti.engine.history.HistoricProcessInstance"%>
<%@page import="org.activiti.engine.repository.ProcessDefinition"%>
<%@page import="java.util.List"%>
<%@page import="activiti.util.ActivitiUtil"%>
<%
String procDefId = request.getParameter("id");
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>process_instance_list</title>
<link href="../include/jquery/ux/poshytip/1.1/tip-twitter/tip-twitter.css" rel="stylesheet" type="text/css">
<link href="../skins/default/styles/common.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="../include/jquery/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="../include/jquery/ux/jquery.form.js"></script>
<script type="text/javascript" src="../include/jquery/ux/poshytip/1.1/jquery.poshytip.min.js"></script>
<script>
$(function() {
	$('input[name="var"]').poshytip({
		className: 'tip-twitter',
		content : function() {
			return '实例启动参数(仅支持json格式)';
		}
	});
});
</script>
</head>
<body>
<h1>流程定义</h1>
<jsp:include page="inc/process_definition_detail.jsp">
	<jsp:param name="id" value="<%=procDefId %>" />
</jsp:include>
<hr>
<%


List<ProcessInstance> list = ActivitiUtil.runtimeService.createProcessInstanceQuery().list();
DateFormat fmt = SimpleDateFormat.getDateTimeInstance();
%>
<form action="../action/mgmt/ActMgmt/startProcessIns?id=<%=procDefId %>" method="post">
<input type="submit" value="开始一个新实例">&nbsp;&nbsp;<input type="text" name="var">
<table border="1">
  <tr>
    <th>Id</th>
    <th>BusinessKey</th>
    <th>isEnded</th>
    <th>isSuspended</th>
    <th></th>
  </tr>
<%
for(int i=0;i<list.size();i++) {
	ProcessInstance ins = list.get(i);
%>
  <tr>
    <td><%=ins.getId() %></td>
    <td><%=ins.getBusinessKey() %></td>
    <td><%=ins.isEnded()?"√":"" %></td>
    <td><%=ins.isSuspended()?"√":"" %></td>
    <td><a href="process_instance_detail.jsp?id=<%=ins.getId()%>">详细</a></td>
  </tr>
<%} %>
<tr>
<tr>
</table>
</form>
</body>
</html>