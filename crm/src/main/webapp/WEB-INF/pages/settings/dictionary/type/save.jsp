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
        $(function () {//入口函数
            //为"保存"按钮添加单击事件
            $("#saveBtn").click(function () {
                //1、收集参数
                let code = $("#create-code").val();//编码
                let name = $("#create-name").val();//名称
                let description = $("#create-describe").val();//描述

                //表单验证
                if (code == "") {
                    alert("编码不能为空!");
                    return;
                }
                if (isExistDictType(code)) {
                    alert("字典编码已存在!");
                    return;
                }

                //2、发送ajax请求
                $.ajax({
                    url: "settings/dictionary/type/saveCreateDictType.do",
                    data: {
                        code: code,
                        name: name,
                        description: description
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //添加成功
                            //跳转到字典类型主页面
                            window.location.href = "settings/dictionary/type/typeIndex.do";
                        } else {
                            //添加失败
                            //提示信息
                            alert(data.message);
                            //页面不跳转
                        }
                    }
                });
            });
        });

        //根据用户输入的字典类型编码，查询是否已经存在
        function isExistDictType(code) {
            let isExist = false;
            $.ajax({
                url: "settings/dictionary/type/isExistDictType.do",
                data: {
                    code: code
                },
                async: false,//不把异步取消的话，success的回调函数中无法影响全局变量的值
                type: "post",
                dataType: "json",
                success: function (data){
                    if (data.code == "0") {
                        isExist = true;
                    }
                }
            });

            return isExist;
        }
    </script>
</head>
<body>

<div style="position:  relative; left: 30px;">
    <h3>新增字典类型</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" id="saveBtn" class="btn btn-primary">保存</button>
        <button type="button" id="cancelBtn" class="btn btn-default" onclick="window.history.back();">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form">

    <div class="form-group">
        <label for="create-code" class="col-sm-2 control-label">编码<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" id="create-code" class="form-control" id="code" style="width: 200%;">
        </div>
    </div>

    <div class="form-group">
        <label for="create-name" class="col-sm-2 control-label">名称</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" id="create-name" class="form-control" id="name" style="width: 200%;">
        </div>
    </div>

    <div class="form-group">
        <label for="create-describe" class="col-sm-2 control-label">描述</label>
        <div class="col-sm-10" style="width: 300px;">
            <textarea class="form-control" id="create-describe" rows="3" id="describe" style="width: 200%;"></textarea>
        </div>
    </div>
</form>

<div style="height: 200px;"></div>
</body>
</html>