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
            //为"字典类型编码"选框设置已选中的选项
            let options = $("#edit-dicTypeCode>option");
            $.each(options, function (index, obj){
                if ("${requestScope.dictValue.typeCode}" == obj.value) {
                    //这里需要用jquery对象才能调用prop()函数
                    $(obj).prop("selected", true);
                }
            });

            //为"更新"按钮绑定单击事件
            $("#updateBtn").click(function () {
                //收集参数
                let id = $("#edit-id").val();
                let value = $("#edit-dicValue").val();
                let text = $("#edit-text").val();
                let orderNo = $("#edit-orderNo").val();
                let typeCode = $("#edit-dicTypeCode").val();

                //表单验证
                if (value == "") {
                    alert("字典值不能为空!");
                    return;
                }
                var regExp = /^\d{n}$/;
                if (!regExp.test(orderNo)) {
                    alert("排序号只能为数字!");
                    return;
                }

                //发送请求
                $.ajax({
                    url: "settings/dictionary/value/saveEditDictValue.do",
                    data: {
                        id: id,
                        value: value,
                        text: text,
                        orderNo: orderNo,
                        typeCode: typeCode
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == 1) {
                            //编辑成功
                            //跳转到数据字典值主页面
                            window.location.href = "settings/dictionary/value/valueIndex.do";
                        } else {
                            //编辑失败
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
    <h3>修改字典值</h3>
    <div style="position: relative; top: -40px; left: 70%;">
        <button type="button" id="updateBtn" class="btn btn-primary">更新</button>
        <button type="button" class="btn btn-default" onclick="window.history.back();">取消</button>
    </div>
    <hr style="position: relative; top: -40px;">
</div>
<form class="form-horizontal" role="form">

    <input type="hidden" id="edit-id" value="${requestScope.dictValue.id}">

    <div class="form-group">
        <label for="edit-dicTypeCode" class="col-sm-2 control-label">字典类型编码</label>
        <div class="col-sm-10" style="width: 300px;">
<%--            <input type="text" class="form-control" id="edit-dicTypeCode" style="width: 200%;" value="性别" readonly>--%>
            <select class="form-control" id="edit-dicTypeCode" style="width: 200%;" disabled>
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
        <label for="edit-dicValue" class="col-sm-2 control-label">字典值<span style="font-size: 15px; color: red;">*</span></label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-dicValue" style="width: 200%;" value="${requestScope.dictValue.value}">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-text" class="col-sm-2 control-label">文本</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-text" style="width: 200%;" value="${requestScope.dictValue.text}">
        </div>
    </div>

    <div class="form-group">
        <label for="edit-orderNo" class="col-sm-2 control-label">排序号</label>
        <div class="col-sm-10" style="width: 300px;">
            <input type="text" class="form-control" id="edit-orderNo" style="width: 200%;" value="${requestScope.dictValue.orderNo}">
        </div>
    </div>
</form>

<div style="height: 200px;"></div>
</body>
</html>