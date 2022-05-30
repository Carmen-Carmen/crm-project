<%--
  Created by IntelliJ IDEA.
  User: xulingrui
  Date: 2022/5/30
  Time: 13:51
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
<%--    引入jquery框架--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script>
        $(function () {
           $("#downloadBtn").click(function () {
               //向后台发送文件下载的请求
               window.location.href = "workbench/activity/fileDownload.do";
           });
        });
    </script>
</head>
<body>
<input type="button" value="download" id="downloadBtn">
</body>
</html>
