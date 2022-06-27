<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <meta charset="UTF-8">
    <base href="<%=basePath%>">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //为"删除"按钮绑定单击事件
            $("#deleteBtn").click(function () {
                //从选中的checkbox中收集要删除的code
                let checked = $("#dictType-tbody input[type='checkbox']:checked");

                if (checked.size() == 0) {
                    alert("请选择要删除的字典类型!");
                    return;
                }

                //拼接字符串
                let codes = "";
                let alertStr = "";
                $.each(checked, function () {
                    codes += "code=" + this.value + "&";
                    alertStr += this.value + ", ";
                })
                codes = codes.substring(0, codes.length - 1);
                alertStr = alertStr.substring(0, alertStr.length - 2);

                // alert(codes);
                if (!window.confirm("是否确认删除字典类型: "  + alertStr + "?")) return;

                $.ajax({
                    url: "settings/dictionary/type/deleteDictType.do",
                    data: codes,
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            // 删除成功
                            // 刷新字典类型列表，懒得遍历渲染了，就用跳转吧
                            window.location.href = "settings/dictionary/type/typeIndex.do"
                        } else {
                            //删除失败
                            //提示信息
                            alert(data.message);
                            //列表不刷新
                        }
                    }
                });
            });

            //为 全选 checkbox添加单击事件
            $("#selectAll").click(function () {
                $("#dictType-tbody input[type='checkbox']").prop("checked", this.checked)
            });

            //为每个checkbox添加单击事件
            $("#dictType-tbody").on("click", "input[type=checkbox]", function () {
                if ($("#dictType-tbody input[type='checkbox']:checked").size() == $("#dictType-tbody input[type='checkbox']").size()) {
                    $("#selectAll").prop("checked", true);
                } else {
                    $("#selectAll").prop("checked", false);
                }
            });

            //为"编辑"按钮添加单击事件
            $("#editBtn").click(function () {
                //收集参数
                let checked = $("#dictType-tbody input[type='checkbox']:checked");
                if (checked.size() != 1) {
                    alert("请选中1条要编辑的字典类型!");
                    return;
                }
                //此时肯定只有一条被选中
                let checkedCode = checked.val();

                //页面跳转
                window.location.href = "settings/dictionary/type/toEditDictType.do?code=" + checkedCode;
            });
        });
    </script>
</head>
<body>

<div>
    <div style="position: relative; left: 30px; top: -10px;">
        <div class="page-header">
            <h3>字典类型列表</h3>
        </div>
    </div>
</div>
<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
    <div class="btn-group" style="position: relative; top: 18%;">
        <button type="button" class="btn btn-primary" onclick="window.location.href='settings/dictionary/type/toCreateDictType.do'"><span
                class="glyphicon glyphicon-plus"></span> 创建
        </button>
        <button type="button" id="editBtn" class="btn btn-default" ><span
                class="glyphicon glyphicon-edit"></span> 编辑
        </button>
        <button type="button" id="deleteBtn" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
    </div>
</div>
<div style="position: relative; left: 30px; top: 20px;">
    <table class="table table-hover">
        <thead>
        <tr style="color: #B3B3B3;">
            <td><input type="checkbox" id="selectAll"/></td>
            <td>序号</td>
            <td>编码</td>
            <td>名称</td>
            <td>描述</td>
        </tr>
        </thead>
        <tbody id="dictType-tbody">
        <c:set var="index" value="1"></c:set>
        <c:forEach items="${requestScope.dictTypeList}" var="dictType">
            <tr class="active">
                <td><input type="checkbox" value="${dictType.code}"/></td>
                <td>${index}</td>
                <td>${dictType.code}</td>
                <td>${dictType.name}</td>
                <td>${dictType.description}</td>
            </tr>
            <c:set var="index" value="${index + 1}"></c:set>
        </c:forEach>
<%--        <tr class="active">--%>
<%--            <td><input type="checkbox"/></td>--%>
<%--            <td>1</td>--%>
<%--            <td>sex</td>--%>
<%--            <td>性别</td>--%>
<%--            <td>性别包括男和女</td>--%>
<%--        </tr>--%>
        </tbody>
    </table>
</div>

</body>
</html>