<%@page import="org.activiti.engine.history.HistoricProcessInstance"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.SimpleDateFormat"%>
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

HistoricProcessInstance processIns = ActivitiUtil.getProcessInsById(processInsId);

ProcessDefinition def = ActivitiUtil.getProcessDefinitionByProcessInsId(processInsId);
List<HistoricTaskInstance> tasks = ActivitiUtil.queryTaskByProcessInsId(processInsId);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>process_instance_detail</title>
<link href="../skins/default/styles/common.css" rel="stylesheet" type="text/css">
<link href="../include/jquery/ux/poshytip/1.1/tip-twitter/tip-twitter.css" rel="stylesheet" type="text/css">
<style>

#tasks{
padding:5px 5px;
}

#tasks .task {
border:1px solid #ccc;
margin:5px 0px;
}

#tasks.detail .task .lbl{
vertical-align:top;
display:inline-block;
width:80px;
}
.tip-inner a{color:#ffffff;font-size:12px;}
</style>
<script type="text/javascript" src="../include/jquery/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="../include/jquery/ux/jquery.form.js"></script>
<script type="text/javascript" src="../include/jquery/ux/poshytip/1.1/jquery.poshytip.min.js"></script>
<script>
function completeTask(taskId) {
	$.post('../test?m=completeTask', {id:taskId,"var":$("#var").val()}, function(data) {
		if(data.success) {
			
			location.reload();
			
			
		} else {
			alert(data.msg);
		}
	},"json");
}

$(function() {
	$('#processDefinition').load('inc/process_instance_img.jsp?id=<%=processInsId%>',null,function() {
		<%if(processIns.getEndTime()==null) {%>
		$('#processDefinition .node').poshytip({
			className: 'tip-twitter',
			content : function() {
				var taskId = $(this).attr('taskId');
				return '<a href="#" onclick="completeTask('+taskId+')">完成</a>';
			}
		});
		<%}%>
	});
	
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
<input type="text" id="var" name="var">
<div id="tasks" class="detail">
<%for(HistoricTaskInstance task : tasks) {
  List<IdentityLink> links = null;
  boolean finished = (task.getEndTime()!=null);
  if(finished) {
	  links = new ArrayList();
  } else {
      links = ActivitiUtil.taskService.getIdentityLinksForTask(task.getId());
  }
%>
<div class="task">
<ul>
  <li><span class="lbl">id:</span><%=task.getId() %></li>
  <li><span class="lbl">name:</span><%=task.getName() %></li>
  <li><span class="lbl">starttime:</span><%=SimpleDateFormat.getDateTimeInstance().format(task.getStartTime()) %></li>
  <li><span class="lbl">endtime:</span><%=task.getEndTime()==null?"":SimpleDateFormat.getDateTimeInstance().format(task.getEndTime()) %></li>
  <li><span class="lbl">description:</span><%=task.getDescription() %></li>
  
  <%if(!finished){ %>
  <li>
    <span class="lbl">分配给:</span>
    <%for(IdentityLink link : links) {
      if(link.getUserId()!=null) {
    %>
        <a href="#"><%=link.getUserId() %></a>(user:<%=link.getType() %>)
    <%
      }
      if(link.getGroupId()!=null) {
    %>
        <a href="#"><%=link.getGroupId() %></a>(group:<%=link.getType() %>)
    <%
      }
    %>
    <%} %>
  </li>
  <%} %>
</ul>
</div>
<%}%>
</div>

<div id="processDefinition" style="position: relative;">
<jsp:include page="inc/process_instance_img.jsp"></jsp:include>
</div>

</body>
</html>