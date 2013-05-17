<%@page import="org.activiti.engine.repository.ProcessDefinition"%>
<%@page import="java.util.List"%>
<%@page import="activiti.mgmt.util.ActivitiUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="../include/jquery/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="../include/jquery/ux/jquery.form.js"></script>
</head>
<body>

<a href="process_definition_deploy.jsp">发布新流程</a>

<%
List<ProcessDefinition> list = ActivitiUtil.queryProcessDefinition(true);
%>

<table border="1">
<tr>
  <th>流程分类</th>
  <th>流程名称</th>
  <th>流程版本</th>
  <th>操作</th>
</tr>
<%
for(ProcessDefinition def : list) {
%>

<tr>
  <td><%=def.getCategory() %></td>
  <td><a href="process_definition_detail.jsp?id=<%=def.getId()%>"><%=def.getName() %></a></td>
  <td><%=def.getVersion() %></td>
  <td><a href="process_instance_list.jsp?id=<%=def.getId()%>">查看实例</a></td>
</tr>
<%
}
%>
</table>

</body>
</html>