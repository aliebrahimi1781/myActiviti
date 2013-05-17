<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="../include/jquery/jquery-1.8.1.min.js"></script>
<script type="text/javascript" src="../include/jquery/ux/jquery.form.js"></script>
<script type="text/javascript">
$(function() {
	$('#deployForm').ajaxForm({
		dataType:'json',
		success:function(data) {
			if(data.success) {
				location.href = "process_definition_list.jsp";
			} else {
				alert(data.msg);
			}
		}
	});
});


</script>
</head>
<body>
<form id="deployForm" name="deployForm" action="../mgmt?m=deploy" method="POST" enctype="multipart/form-data">
  <input type="file" name="attach"><br>
  <input type="submit" value="发布">
</form>
</body>
</html>