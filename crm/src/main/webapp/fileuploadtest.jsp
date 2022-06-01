<%--
  Created by IntelliJ IDEA.
  User: xulingrui
  Date: 2022/6/1
  Time: 16:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>演示文件下载</title>
</head>
<body>
<%--
1、表单组件标签只能用input type=“file”
2、请求方式只能用post
    get：参数写在url里；对参数有长度限制；数据不安全；能用缓存，效率高！
		应用：向后台提交文本数据
		所以开发的时候如果用get，常常因为缓存问题显示不出更改内容
	post：参数通过请求体提交至后台；理论上对参数长度无限制；相对安全；不使用缓存，效率相度低
		应用：既能向后台提交文本数据，还可以向后台提交二进制数据（音视频等）
3、表单的编码格式（encoding type）只能使用：enctype=“multipart/form-data”
	根据HTTP协议的规定，浏览器每次向后台提交参数，都会对参数进行统一编码；
	默认采用urlencoded，只能对文本数据进行编码，浏览器每次向后台提交参数，都会首先把所有的参数转换成字符串，然后对这些数据统一进行urlencoded编码
	文件上传的表单编码格式只能使用multipart/form-data，其功能是阻止默认编码，原封不动的提交给后台！
--%>
<form action="workbench/activity/fileUpload.do" method="post" enctype="multipart/form-data">
    <input type="text" name="userName"><br>
    <input type="file" name="myFile"><br>
    <input type="submit" value="提交">
</form>
</body>
</html>
