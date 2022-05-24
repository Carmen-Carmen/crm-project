<%--
  Created by IntelliJ IDEA.
  User: xulingrui
  Date: 2022/5/24
  Time: 23:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>演示bootstrap_datatimepicker插件</title>
<%--    框架多依赖于jquery，因此jquery要最先引入--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<%--    bootstrap_datetimepicker插件是基于bootstrap框架的，所以在之前还要引入bootstrap框架--%>
    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<%--    引入bootstrap_datetimepicker插件--%>
    <link rel="stylesheet" href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript">
        //调用插件的工具函数
        $(function () {
            //在入口函数调用工具函数的原因：入口函数是在页面加载完成后才加载的，保证了容器（这里是input标签）已被加载
            //当容器加载完成，对容器（jquery对象）调用工具函数；
            //工具函数会自动给容器绑定单击事件，显示日历
            $("#myDate").datetimepicker({
                //工具函数的参数是一个js对象（js对象写在大括号里哦
                language: 'zh-CN', //语言
                format: 'yyyy-mm-dd', //format of date
                minView: 'month', //可以选择到的最小视图，这里是选择一个月中的哪一天
                initialDate: new Date(), //打开后的默认显示的日期，一般逻辑就是今天
                autoclose: 'true', //选择完最后一层后是否关闭，默认为false
                todayBtn: 'true', //是否显示"今天"按钮，默认为false
                clearBtn: 'true' //是否显示"清空"按钮，默认为false
            });

        });
    </script>
</head>
<body>
<input type="text" id="myDate" readonly>

</body>
</html>
