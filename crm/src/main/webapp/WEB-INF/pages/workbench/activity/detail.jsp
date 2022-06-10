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

        //默认情况下取消和保存按钮是隐藏的
        var cancelAndSaveBtnDefault = true;

        $(function () {
            $("#remark").focus(function () {
                if (cancelAndSaveBtnDefault) {
                    //设置remarkDiv的高度为130px
                    $("#remarkDiv").css("height", "130px");
                    //显示
                    $("#cancelAndSaveBtn").show("2000");
                    cancelAndSaveBtnDefault = false;
                }
            });

            $("#cancelBtn").click(function () {
                //显示
                $("#cancelAndSaveBtn").hide();
                //设置remarkDiv的高度为130px
                $("#remarkDiv").css("height", "90px");
                cancelAndSaveBtnDefault = true;
            });

            $(".remarkDiv").mouseover(function () {
                $(this).children("div").children("div").show();
            });

            $(".remarkDiv").mouseout(function () {
                $(this).children("div").children("div").hide();
            });

            $(".myHref").mouseover(function () {
                $(this).children("span").css("color", "red");
            });

            $(".myHref").mouseout(function () {
                $(this).children("span").css("color", "#E6E6E6");
            });

            //为"保存"按钮添加单击事件
            $("#addRemarkBtn").click(function () {
                //收集参数
                let noteContent = $.trim($("#remark").val());
                let activityId = "${requestScope.activity.id}";

                //表单验证
                if (noteContent == "") {
                    alert("请输入内容！");
                    return;
                }

                //发送请求
                $.ajax({
                    url: "workbench/activity/saveCreateActivityRemark.do",
                    data: {
                        noteContent: noteContent,
                        activityId: activityId
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //添加成功
                            //清空输入框
                            $("#remark").val("");
                            //遍历data中的remarkList，局部刷新，渲染备注列表
                            refreshActivityRemarkList(data.returnData);
                        } else {
                            //添加失败
                            //提示信息
                            alert(data.message);
                            //输入框不清空，列表也不刷新
                        }
                    }
                });
            });

            //动态渲染生成的标签要用on绑定事件
            //笑了，老师为了这些事件添加，给remarkDiv列表外面又加了一个div，那之前渲染还不如用append呢
            $(document).on("mouseover", ".remarkDiv", function () {
                $(this).children("div").children("div").show();
            });

            $(document).on("mouseout", ".remarkDiv", function () {
                $(this).children("div").children("div").hide();
            });

            $(document).on("mouseover", ".myHref", function () {
                $(this).children("span").css("color", "red");
            });

            $(document).on("mouseout", ".myHref", function () {
                $(this).children("span").css("color", "#E6E6E6");
            });

            //为删除市场活动超链接添加单击事件
            $(document).on("click", ".deleteActivityRemarkBtn", function () {
                //收集参数
                let remarkId = $(this).attr("remarkId");
                let activityId = "${requestScope.activity.id}";
                // alert(remarkId);
                $.ajax({
                    url: "workbench/activity/deleteActivityRemarkById.do",
                    data: {
                        remarkId: remarkId,
                        activityId: activityId
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //添加成功
                            //刷新市场活动备注列表
                            refreshActivityRemarkList(data.returnData);
                        } else {
                            //添加失败
                            //提示信息
                            alert(data.message);
                            //列表不刷新
                        }
                    }
                });
            });

            //为修改市场活动超链接添加单击事件
            $(document).on("click", ".editActivityRemarkBtn", function () {
                //把修改按钮上的remarkId贴一份到modal窗口的div上
                let remarkId = $(this).attr("remarkId");
                $("#editRemarkModal").attr("remarkId", remarkId);
                //noteContent回显
                let noteContent = $(this).attr("noteContent");
                $("#noteContent").val(noteContent);
                //弹出modal窗口
                $("#editRemarkModal").modal("show");
            });

            //为修改市场活动modal窗口中"更新"按钮添加单击事件
            $("#updateRemarkBtn").click(function () {
                //收集信息
                let remarkId = $("#editRemarkModal").attr("remarkId");
                let noteContent = $("#noteContent").val();
                let activityId = "${requestScope.activity.id}";
                // alert(remarkId);

                if (noteContent == "") {
                    alert("修改备注内容不得为空！");
                    return;
                }

                //发送ajax请求
                $.ajax({
                    url: "workbench/activity/saveEditActivityRemark.do",
                    data: {
                        id: remarkId, //id是ActivityRemark实体类中的属性名
                        noteContent: noteContent,
                        activityId: activityId
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //修改成功
                            //刷新备注列表
                            refreshActivityRemarkList(data.returnData);
                            //关闭modal窗口，顺便把modal窗的属性和输入框内容清掉吧
                            $("#noteContent").val("");
                            $("#editRemarkModal").attr("remarkId", "");
                            $("#editRemarkModal").modal("hide");
                        } else {
                            //修改失败
                            //提示信息
                            alert(data.message);
                            //modal窗口不关闭
                            $("#editRemarkModal").modal("show");
                        }
                    }
                });
            });
        });

        function refreshActivityRemarkList(activityRemarkList) {
            //遍历data中的remarkList，局部刷新，渲染备注列表
            let htmlStr = "";
            $.each(activityRemarkList, function(index, obj) {
                htmlStr += "<div class=\"remarkDiv\" style=\"height: 60px;\">\n";
                htmlStr += "<img title=\"" + obj.createBy + "\" src=\"image/user-thumbnail.png\" style=\"width: 30px; height:30px;\">\n";
                htmlStr += "<div style=\"position: relative; top: -40px; left: 40px;\">\n";
                htmlStr += "<h5>" + obj.noteContent + "</h5>\n";
                htmlStr += "<font color=\"gray\">市场活动</font> <font color=\"gray\">-</font>\n";
                htmlStr += "<b>${requestScope.activity.name}</b>\n";
                htmlStr += "<small style=\"color: gray;\">\n";
                htmlStr += obj.editFlag == "1"? (obj.editTime + " 由" + obj.editBy + "修改") : (obj.createTime + " 由" + obj.createBy + "创建") + "\n";
                htmlStr += "</small>\n";
                htmlStr += "<div style=\"position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;\">\n";
                htmlStr += "<a class=\"myHref editActivityRemarkBtn\" href=\"javascript:void(0);\" remarkId=\"" + obj.id + "\" noteContent=\"" + obj.noteContent + "\" >\n";
                htmlStr += "<span class=\"glyphicon glyphicon-edit\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>\n";
                htmlStr += "&nbsp;&nbsp;&nbsp;&nbsp;\n";
                htmlStr += "<a class=\"myHref deleteActivityRemarkBtn\" href=\"javascript:void(0);\" remarkId=\"" + obj.id + "\">\n";
                htmlStr += "<span class=\" glyphicon glyphicon-remove\" style=\"font-size: 20px; color: #E6E6E6;\"></span></a>\n"
                htmlStr += "</div></div></div>\n";
            });

            $("#remarkListContainer").html(htmlStr);
        }
    </script>

</head>
<body>

<!-- 修改市场活动备注的模态窗口 -->
<div class="modal fade" id="editRemarkModal" role="dialog">
    <%-- 备注的id --%>
    <input type="hidden" id="remarkId">
    <div class="modal-dialog" role="document" style="width: 40%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">修改备注</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="noteContent" class="col-sm-2 control-label">内容</label>
                        <div class="col-sm-10" style="width: 81%;" id="edit-describe">
                            <textarea class="form-control" rows="3" id="noteContent"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
            </div>
        </div>
    </div>
</div>


<!-- 返回按钮 -->
<div style="position: relative; top: 35px; left: 10px;">
    <a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
</div>

<!-- 大标题 -->
<div style="position: relative; left: 40px; top: -30px;">
    <div class="page-header">
        <h3>市场活动-${requestScope.activity.name}<small>${requestScope.activity.startDate}
            ~ ${requestScope.activity.endDate}</small></h3>
    </div>

</div>

<br/>
<br/>
<br/>

<!-- 详细信息 -->
<div style="position: relative; top: -70px;">
    <div style="position: relative; left: 40px; height: 30px;">
        <div style="width: 300px; color: gray;">所有者</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.owner}</b>
        </div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.activity.name}</b>
        </div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>

    <div style="position: relative; left: 40px; height: 30px; top: 10px;">
        <div style="width: 300px; color: gray;">开始日期</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.startDate}</b>
        </div>
        <div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
        <div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.activity.endDate}</b>
        </div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 20px;">
        <div style="width: 300px; color: gray;">成本</div>
        <div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.cost}</b>
        </div>
        <div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 30px;">
        <div style="width: 300px; color: gray;">创建者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.activity.createBy}&nbsp;&nbsp;</b><small
                style="font-size: 10px; color: gray;">${requestScope.activity.createTime}</small></div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 40px;">
        <div style="width: 300px; color: gray;">修改者</div>
        <div style="width: 500px;position: relative; left: 200px; top: -20px;">
            <c:if test="${not empty requestScope.activity.editBy}">
                <b>${requestScope.activity.editBy}&nbsp;&nbsp;</b>
            </c:if>
            <c:if test="${empty requestScope.activity.editBy}">
                <b>无&nbsp;&nbsp;</b>
            </c:if>
            <c:if test="${not empty requestScope.activity.editTime}">
                <small style="font-size: 10px; color: gray;">${requestScope.activity.editTime}</small>
            </c:if>
        </div>
        <div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
    <div style="position: relative; left: 40px; height: 30px; top: 50px;">
        <div style="width: 300px; color: gray;">描述</div>
        <div style="width: 630px;position: relative; left: 200px; top: -20px;">
            <b>
                ${requestScope.activity.description}
            </b>
        </div>
        <div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
    </div>
</div>

<!-- 备注 -->
<div style="position: relative; top: 30px; left: 40px;">
    <div class="page-header">
        <h4>备注</h4>
    </div>

    <div id="remarkListContainer">
    <%--    使用jstl遍历request作用域中的remarkList          循环变量名--%>
        <c:forEach items="${requestScope.remarkList}" var="remark">
            <div class="remarkDiv" style="height: 60px;">
                <img title="${remark.createBy}" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
                <div style="position: relative; top: -40px; left: 40px;">
                    <h5>${remark.noteContent}</h5>
                    <font color="gray">市场活动</font> <font color="gray">-</font>
                    <b>${requestScope.activity.name}</b>
                    <small style="color: gray;">
                            <%--                    用el表达式的三元运算符代替jstl的if标签！--%>
                            ${remark.editFlag == '1'?remark.editTime:remark.createTime}
                        由${remark.editFlag == '1'?remark.editBy:remark.createBy}${remark.editFlag == '1'?'修改':'创建'}
                    </small>
                    <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
                        <a class="myHref editActivityRemarkBtn" href="javascript:void(0);" remarkId="${remark.id}" noteContent="${remark.noteContent}" >
                            <span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a class="myHref deleteActivityRemarkBtn" href="javascript:void(0);" remarkId="${remark.id}">
                            <span class=" glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <%--    <!-- 备注1 -->--%>
    <%--    <div class="remarkDiv" style="height: 60px;">--%>
    <%--        <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">--%>
    <%--        <div style="position: relative; top: -40px; left: 40px;">--%>
    <%--            <h5>哎呦！</h5>--%>
    <%--            <font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;">--%>
    <%--            2017-01-22 10:10:10 由zhangsan</small>--%>
    <%--            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">--%>
    <%--                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"--%>
    <%--                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
    <%--                &nbsp;&nbsp;&nbsp;&nbsp;--%>
    <%--                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove"--%>
    <%--                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
    <%--            </div>--%>
    <%--        </div>--%>
    <%--    </div>--%>

    <%--    <!-- 备注2 -->--%>
    <%--    <div class="remarkDiv" style="height: 60px;">--%>
    <%--        <img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">--%>
    <%--        <div style="position: relative; top: -40px; left: 40px;">--%>
    <%--            <h5>呵呵！</h5>--%>
    <%--            <font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;">--%>
    <%--            2017-01-22 10:20:10 由zhangsan</small>--%>
    <%--            <div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">--%>
    <%--                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit"--%>
    <%--                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
    <%--                &nbsp;&nbsp;&nbsp;&nbsp;--%>
    <%--                <a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove"--%>
    <%--                                                                   style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
    <%--            </div>--%>
    <%--        </div>--%>
    <%--    </div>--%>

<%--    我的方案是给所有的class="remarkDiv"外面加了一个container的div；但实际上也可以在下面这个id="remarkDiv"的jquery选择器.before(htmlStr)来渲染新数据--%>

<%--
    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;" activityId="${requestScope.activity.id}">
                                                                    在js代码中也可以通过el表达式获取作用域里的数据，因此这里不用把所属activity的id绑定在标签上了
--%>
    <div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;" activityId="${requestScope.activity.id}">
        <form role="form" style="position: relative;top: 10px; left: 10px;">
            <textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"
                      placeholder="添加备注..."></textarea>
            <p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
                <button id="cancelBtn" type="button" class="btn btn-default">取消</button>
                <button type="button" class="btn btn-primary" id="addRemarkBtn">保存</button>
            </p>
        </form>
    </div>
</div>
<div style="height: 200px;"></div>
</body>
</html>