<%@page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<script type="text/javascript">
		//浏览器显示的内容的location跳转到登陆界面，等于还是用url访问WEB-INF/下的资源，不能直接访问
		// document.location.href = "settings/qx/user/login.html";
		//window和document都是将浏览器封装成对象
		window.location.href = "settings/qx/user/toLogin.do";
	</script>
</body>
</html>