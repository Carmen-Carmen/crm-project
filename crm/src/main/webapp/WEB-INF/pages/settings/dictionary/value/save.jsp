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
            //为"保存"按钮添加单击事件
            $("#saveBtn").click(function () {
                //收集参数
                let typeCode = $("#create-dicTypeCode").val();
                let value = $("#create-dicValue").val();
                let text = $("#create-text").val();
                let orderNo = $("#create-orderNo").val();

                //表单验证
                if (typeCode == "" || value == "") {
                    alert("字典类型编码和字典值均不能为空！");
                    return;
                }
                var regExp = /^\d{n}$/;
                if (!regExp.test(orderNo)) {
                    alert("排序号只能为数字!");
                    return;
                }


                //发送请求
                $.ajax({
                    url: "settings/dictionary/value/saveCreateDictValue.do",
                    data: {
                        value: value,
                        text: text,
                        orderNo: orderNo,
                        typeCode: typeCode
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //创建成功
                            //跳转到字典值主页面
                            window.location.href = "settings/dictionary/value/valueIndex.do";
                        } else {
                            //创建失败
                            //提示信息
                            alert(data.message);
                            //页面不跳转
                        }
                    }
                });
            });
        });
    </script>
</head>
<body>

<div style="position:  relative; left: 30px;">
    <h3>新增字典值</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" id="saveBtn" class="btn btn-primary">保存</button>
        <button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form">

    <div class="form-group">
        <label for="create-dicTypeCode" class="col-sm-2 control-label">字典类型编码<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <select class="form-control" id="create-dicTypeCode" style="width: 200%;">
                <option></option>
<%--                <option>性别</option>--%>
<%--                <option>机构类型</option>--%>
                <c:forEach items="${requestScope.dictTypeList}" var="dictType">
                    <option value="${dictType.code}">${dictType.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label for="create-dicValue" class="col-sm-2 control-label">字典值<span
                style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-dicValue" style="width: 200%;">
        </div>
    </div>

    <div class="form-group">
        <label for="create-text" class="col-sm-2 control-label">文本</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-text" style="width: 200%;">
        </div>
    </div>

    <div class="form-group">
        <label for="create-orderNo" class="col-sm-2 control-label">排序号</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="create-orderNo" style="width: 200%;">
        </div>
    </div>
</form>

<div style="height: 200px;"></div>
</body>
</html>