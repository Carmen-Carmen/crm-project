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
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css"
          rel="stylesheet"/>

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        $(function () {
            //为"更新"按钮添加单击事件
            $("#updateBtn").click(function () {
                //收集参数
                let code = $("#edit-code").val();
                let name = $("#edit-name").val();
                let describe = $("#edit-describe").val();

                //表单验证
                if (code == "") {
                    alert("字典类型编码不能为空！");
                    return;
                }

                //发送请求
                $.ajax({
                    url: "settings/dictionary/type/saveEditDictType.do",
                    data: {
                        code: code,
                        name: name,
                        description: describe
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //修改成功
                            //页面跳转
                            window.location.href="settings/dictionary/type/typeIndex.do";
                        } else {
                            //修改失败
                            //提示信息
                            alert(data.message);
                            //不跳转
                        }
                    }
                });
            });
        })
    </script>
</head>
<body>

<div style="position:  relative; left: 30px;">
    <h3>修改字典类型</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" id="updateBtn" class="btn btn-primary">更新</button>
        <button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form">

    <div class="form-group">
        <label for="edit-code" class="col-sm-2 control-label">编码<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">                                                                               <!--数据字典类型的编码不能够修改-->
            <input type="text" id="edit-code" class="form-control" id="code" style="width: 200%;" value="${requestScope.dictType.code}" readonly>
        </div>
    </div>

    <div class="form-group">
        <label for="edit-name" class="col-sm-2 control-label">名称</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" id="edit-name" class="form-control" id="name" style="width: 200%;" value="${requestScope.dictType.name}">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 300px;">
            <textarea id="edit-describe" class="form-control" rows="3" id="describe" style="width: 200%;">${requestScope.dictType.description}</textarea>
        </div>
    </div>
</form>

<div style="height: 200px;"></div>
</body>
</html>