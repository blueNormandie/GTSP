<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>手动执行</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript">
		function doMailSend(){
			form1.action="mailSend.action";
			form1.btn.disabled=true;
			form1.submit();
		}
		function doCoachMailSend(){
			form1.action="coachMailSend.action";
			form1.btn.disabled=true;
			form1.submit();
		}
	</script>
	
  </head>
  
  <body>
	<!-- <form name="form1" action="mailSend.action" method="POST">
		<input type="button" name="btn" onclick="doMailSend()" value="手动发工资单">
	</form>  -->
	
  </body>
</html>
