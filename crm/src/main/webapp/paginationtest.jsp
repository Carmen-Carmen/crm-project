<%--
  Created by IntelliJ IDEA.
  User: xulingrui
  Date: 2022/5/26
  Time: 16:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <base href="<%=basePath%>">
    <title>演示bs_pagination插件的使用</title>

<%--    jquery--%>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>

<%--    bootstrap--%>
    <link rel="stylesheet" href="jquery/bootstrap_3.3.0/css/bootstrap.min.css">
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<%--    pagination plugin--%>
    <link rel="stylesheet" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<%--    pagination插件的语言包，已经汉化过了--%>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">
        $(function () {
            $("#demo_page1").bs_pagination({
                currentPage: 1, //当前页

                //rowsPerPage, totalRows和totalPages这三个参数是必须符合关系的 totalRows / rowsPerPage = totalPages
                rowsPerPage: 20, //也就是pageSize
                totalRows: 985, //总条数
                totalPages: 50, //总页数，是必填参数

                visiblePageLinks: 5, //一组连续显示的页数

                showGoToPage: true, //是否显示跳转页面的输入框，默认为true
                showRowsPerPage: true, //是否显示设置每页条数的输入框，默认为true
                showRowsInfo: true, //是否显示每页数据信息，默认为true

                //这个函数会返回切换页码之后的pageNo和pageSize
                onChangePage: function (event, pageObj) {//当改变页码的时候，这个属性值为一个函数哦
                    // console.log("page changed!" + pageObj.currentPage)
                    //俩参数，event没啥用；pageObj封装了当前页和当前每页条数
                    console.log("current page: " + pageObj.currentPage + "\nrows per page: " + pageObj.rowsPerPage)
                }
            });
        });
    </script>
</head>
<body>
<div id="demo_page1"></div>

</body>
</html>
