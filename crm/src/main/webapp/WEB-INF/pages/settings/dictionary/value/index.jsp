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
            //为"创建"按钮添加单击事件
            $("#createBtn").click(function () {
                window.location.href = "settings/dictionary/value/toCreateDictValue.do";
            });
            //为"编辑"按钮添加单击事件
            $("#editBtn").click(function () {
                //收集参数
                let checked = $("#dictValue-tbody input[type='checkbox']:checked");
                if (checked.size() != 1) {
                    alert("请选中1条要编辑的字典值!");
                    return;
                }

                //此时肯定只有一条被选中了
                let checkedId = checked.val();

                window.location.href = "settings/dictionary/value/toEditDictValue.do?id=" + checkedId;
            });

            //为"删除"按钮添加单击事件
            $("#deleteBtn").click(function () {
                //收集参数
                let checked = $("#dictValue-tbody input[type='checkbox']:checked");

                if (checked.size() == 0) {
                    alert("请选择要删除的字典值!");
                    return;
                }

                //拼接参数字符串(ids)和用于弹窗的字符串(alertStr)
                let ids = "";
                $.each(checked, function (index, obj) {
                    let id = this.value;
                    ids += "id=" + id +"&";
                });
                ids = ids.substring(0, ids.length - 1);

                if (!window.confirm("是否确认删除" + checked.size() + "条字典值?")) return;

                // alert(ids);

                //发送请求
                $.ajax({
                    url: "settings/dictionary/value/deleteDictValue.do",
                    data: ids,
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //删除成功
                            //跳转到数据字典值主页
                            window.location.href = "settings/dictionary/value/valueIndex.do"
                        } else {
                            //删除失败
                            //提示信息
                            alert(data.message);
                            //页面不跳转
                        }
                    }
                });
            });

            //为"全选"checkbox添加单击事件
            $("#selectAll").click(function () {
                $("#dictValue-tbody input[type='checkbox']").prop("checked", this.checked);
            });
            //为每个checkbox添加单击事件
            $("dictValue-tbody").on("click", "input[type='checkbox']", function () {
                if ($("#dictValue-tbody input[type='checkbox']:checked").size() == $("#dictValue-tbody input[type='checkbox']").size()) {
                    $("#selectAll").prop("checked", true);
                } else {
                    $("#selectAll").prop("checked", false);
                }
            });
        });
    </script>
</head>
<body>

<div>
    <div style="position: relative; left: 30px; top: -10px;">
        <div class="page-header">
            <h3>字典值列表</h3>
        </div>
    </div>
</div>
<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;left: 30px;">
    <div class="btn-group" style="position: relative; top: 18%;">
        <button type="button" id="createBtn" class="btn btn-primary" ><span
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
            <td>字典值</td>
            <td>文本</td>
            <td>排序号</td>
            <td>字典类型编码</td>
        </tr>
        </thead>
        <tbody id="dictValue-tbody">
        <c:set var="index" value="1"></c:set>
        <c:forEach items="${requestScope.dictValueList}" var="dictValue">
            <tr class="active">
                <td><input type="checkbox" value="${dictValue.id}"/></td>
                <td>${index}</td>
                <td>${dictValue.value}</td>
                <td>${dictValue.text}</td>
                <td>${dictValue.orderNo}</td>
                <td>${dictValue.typeCode}</td>
            </tr>
            <c:set var="index" value="${index + 1}"></c:set>
        </c:forEach>
<%--        <tr class="active">--%>
<%--            <td><input type="checkbox"/></td>--%>
<%--            <td>1</td>--%>
<%--            <td>m</td>--%>
<%--            <td>男</td>--%>
<%--            <td>1</td>--%>
<%--            <td>sex</td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td><input type="checkbox"/></td>--%>
<%--            <td>2</td>--%>
<%--            <td>f</td>--%>
<%--            <td>女</td>--%>
<%--            <td>2</td>--%>
<%--            <td>sex</td>--%>
<%--        </tr>--%>
<%--        <tr class="active">--%>
<%--            <td><input type="checkbox"/></td>--%>
<%--            <td>3</td>--%>
<%--            <td>1</td>--%>
<%--            <td>一级部门</td>--%>
<%--            <td>1</td>--%>
<%--            <td>orgType</td>--%>
<%--        </tr>--%>
<%--        <tr>--%>
<%--            <td><input type="checkbox"/></td>--%>
<%--            <td>4</td>--%>
<%--            <td>2</td>--%>
<%--            <td>二级部门</td>--%>
<%--            <td>2</td>--%>
<%--            <td>orgType</td>--%>
<%--        </tr>--%>
<%--        <tr class="active">--%>
<%--            <td><input type="checkbox"/></td>--%>
<%--            <td>5</td>--%>
<%--            <td>3</td>--%>
<%--            <td>三级部门</td>--%>
<%--            <td>3</td>--%>
<%--            <td>orgType</td>--%>
<%--        </tr>--%>
        </tbody>
    </table>
</div>

</body>
</html>