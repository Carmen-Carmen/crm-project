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
    <link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">

    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <%--    引入bootstrap_datetimepicker插件，之前已经引入它依赖的jquery和bootstrap了，只有js存在引入顺序哦--%>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript"
            src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

    <%--    引入bs_pagination插件--%>

    <script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
    <script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>

    <script type="text/javascript">
        //入口函数
        $(function () {
            //给"创建"按钮绑定单击事件
            $("#createActivityBtn").click(function () {
                //使用js实现按钮点击弹出modal框的好处：在弹出之前还能写逻辑
                //比如在弹出窗口之前，清空表单中的所有数据
                //方法：重置form表单中所有的input
                //获取jquery对象     jquery对象的第一个，才是表单
                $("#createActivityForm").get(0).reset();

                //弹出创建市场活动的modal窗口
                $("#createActivityModal").modal("show");
            });

            //给"保存"按钮绑定单击事件，提交表单，发异步请求
            $("#saveCreateActivityBtn").click(function () {
                //1、收集表单中填入的参数
                let owner = $("#create-marketActivityOwner").val();
                let name = $.trim($("#create-marketActivityName").val());
                let startDate = $("#create-startDate").val();
                let endDate = $("#create-endDate").val();
                let cost = $.trim($("#create-cost").val());
                let description = $.trim($("#create-description").val());

                //2、表单验证
                if (owner == null) {
                    alert("所有者不能为空！");
                    return;
                }
                if (name == null) {
                    alert("活动名称不能为空！");
                    return;
                }
                //验证开始日期和结束日期
                if (startDate != null && endDate != null) {
                    //在js中，可以把字符串类型的日期转化为Date类型：new Date()之后，分别设置这个Date对象的年、月、日
                    //也可以通过字符串比较大小，和Java中的规则一样，逐位比较
                    if (endDate < startDate) {
                        alert("结束日期不能比开始日期早！")
                        return;
                    }
                }
                //验证成本是否为非负整数，使用正则表达式
                let reg = /^(([1-9]\d*)|0)$/;//0或正整数
                if (!reg.test(cost)) {
                    alert("成本只能为非负整数！")
                    return;
                }

                //3、发送异步请求
                $.ajax({
                    url:"workbench/activity/saveCreateActivity.do",
                    data:{
                        owner:owner,
                        name:name,
                        startDate:startDate,
                        endDate:endDate,
                        cost:cost,
                        description:description
                    },
                    type:"post",
                    dataType:"json",
                    success:function (data) {
                        if (data.code == "1") {
                            //添加成功
                            //关闭modal窗口
                            $("#createActivityModal").modal("hide");
                            //刷新市场活动列表，显示列表第一页，并保留每页显示条数
                            queryActivityByConditionForPage(1, $("#paginationContainer").bs_pagination('getOption', 'rowsPerPage'));

                        } else {//data.code == "0"
                            //添加失败
                            alert(data.message);
                            //modal窗口不关闭
                            $("#createActivityModal").modal("show");//这条代码也可以不写的
                        }
                    }
                });

            });

            //当容器加载完成后，对容器调用工具函数，但是现在有俩容器
            //所以考虑使用类选择器等
            $(".show-calendar").datetimepicker({
                language: 'zh-CN', //语言
                format: 'yyyy-mm-dd', //format of date
                minView: 'month', //可以选择到的最小视图，这里是选择一个月中的哪一天
                initialDate: new Date(), //打开后的默认显示的日期，一般逻辑就是今天
                autoclose: 'true', //选择完最后一层后是否关闭，默认为false
                todayBtn: 'true', //是否显示"今天"按钮，默认为false
                clearBtn: 'true' //是否显示"清空"按钮，默认为false
            });

            //当市场活动主页面加载加载完成，查询所有数据的第一页以及所有数据的总条数，默认每页显示10条数据
            queryActivityByConditionForPage(1, 10);

            //为"查询"按钮绑定单击事件
            $("#queryActivityBtn").click(function () {
                //查询符合条件的所有数据的第一页数据
                //但是希望保留查询前每页显示条数
                //调用工具函数中返回pagination各种参数的函数
                let currentPageSize = $("#paginationContainer").bs_pagination('getOption', 'rowsPerPage');
                queryActivityByConditionForPage(1, currentPageSize)
            });

            //给"全选"按钮添加单击事件
            $("#selectAll").click(function () {
                let selectAllChecked = this.checked;//事件函数中的内置对象this就代表当前事件发生对象的dom对象
                //jquery选择器和css选择器一模一样
                $("#activity-tbody input[type='checkbox']").prop("checked", this.checked);//将"全选"选框的选中状态同步给列表中所有checkbox
            });

            //给列表中所有checkbox添加单击事件
            //有一个问题，列表中的checkbox是ajax请求成功后动态创建的，jquery选择器.xxx.()的方法加不了
            //使用on函数添加事件！
            $("#activity-tbody").on("click", "input[type='checkbox']", function () {
                //jquery选择器获取当前列表中所有被选中的checkbox，成为一个数组；这个数组有length属性，也可以用size()
                let checkedNum = $("#activity-tbody input[type='checkbox']:checked").size();

                //与当前页面所有checkbox的jquery对象数组长度比较
                if ($("#activity-tbody input[type='checkbox']").size() == checkedNum) {
                    $("#selectAll").prop("checked", true);
                } else {
                    $("#selectAll").prop("checked", false);
                }
            });

            //为"删除"按钮添加单击事件
            $("#deleteActivityBtn").click(function () {
                //收集参数
                //获取列表中所有被选中的checkbox，生成一个数组
                let checkedIds = $("#activity-tbody input[type='checkbox']:checked");

                //如果没有选中任何记录，则提示，且不发送请求
                if (checkedIds.size() == 0) {
                    alert("请选择要删除的市场活动！")
                    return;
                }

                //遍历id列表，拼成id=xxx&id=xxx&...&id=xxx的字符串
                let ids = "";
                $.each(checkedIds, function () {
                    //用dom对象即可，没必要传入index和obj的jquery参数
                    ids += "id=" + this.value + "&";
                })
                // ids = ids.substr(0, ids.length - 1);//substr是从startIndex开始，往后取n个
                // ids += "id=15&";//故意让前台和后台id不符，确实能被controller catch到这个错误
                ids = ids.substring(0, ids.length - 1);//去除多余的&

                if (window.confirm("是否确定删除" + checkedIds.size() + "条数据?")) {
                    $.ajax({
                        url: "workbench/activity/deleteActivityByIds.do",
                        data: ids,
                        type: "post",
                        dataType: "json",
                        success: function (data) {
                            if (data.code == "1") {
                                //删除成功
                                //刷新市场活动列表，显示第一页数据，                             并且保持每页显示条数不变
                                queryActivityByConditionForPage(1, $("#paginationContainer").bs_pagination("getOption", "rowsPerPage"));
                            } else {
                                //提示信息
                                alert(data.message);
                            }
                        }
                    });
                }
            });

            //为"修改"按钮添加单击事件
            $("#editActivityBtn").click(function () {
                //收集参数
                let checked = $("#activity-tbody input[type='checkbox']:checked");
                //判断是否只选择了一条记录
                if (checked.size() != 1) {
                    alert("请选中一条市场活动！");
                    return;
                }
                //此时checked中有且只有一个input对象
                // let id = checked.get(0).value;//通过dom对象.value
                // let id = checked[0].value;//通过dom对象.value
                let id = checked.val();//通过jquery对象获取value

                $.ajax({
                    url: "workbench/activity/queryActivityById.do",
                    data: {
                        id: id
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        //选中市场活动的信息回显在modal框
                        $("#edit-id").val(data.id);//edit-id是为表单添加的隐藏域的id
                        //显示owner
                        <%--<option value="${u.id}">${u.name}</option>--%>
                        $("#edit-marketActivityOwner").val(data.owner);//option的value就是user的id，因此只需要将data.owner赋值给select就行了
                        $("#edit-marketActivityName").val(data.name);
                        $("#edit-startDate").val(data.startDate);
                        $("#edit-endDate").val(data.endDate);
                        $("#edit-cost").val(data.cost);
                        $("#edit-description").val(data.description);
                        //弹出modal窗口
                        $("#editActivityModal").modal("show");
                    }
                });
            });

            //给"更新"按钮添加单击事件
            $("#saveEditActivityBtn").click(function () {
                //1、收集参数
                var id = $("#edit-id").val();
                var owner = $("#edit-marketActivityOwner").val();
                var name = $.trim($("#edit-marketActivityName").val());
                var startDate = $("#edit-startDate").val();
                var endDate = $("#edit-endDate").val();
                var cost = $.trim($("#edit-cost").val());
                var description = $.trim($("#edit-description").val());

                //2、表单验证
                if (owner == null) {
                    alert("所有者不能为空！");
                    return;
                }
                if (name == null) {
                    alert("活动名称不能为空！");
                    return;
                }
                //验证开始日期和结束日期
                if (startDate != null && endDate != null) {
                    //在js中，可以把字符串类型的日期转化为Date类型：new Date()之后，分别设置这个Date对象的年、月、日
                    //也可以通过字符串比较大小，和Java中的规则一样，逐位比较
                    if (endDate < startDate) {
                        alert("结束日期不能比开始日期早！")
                        return;
                    }
                }
                //验证成本是否为非负整数，使用正则表达式
                let reg = /^(([1-9]\d*)|0)$/;//0或正整数
                if (!reg.test(cost)) {
                    alert("成本只能为非负整数！")
                    return;
                }

                //3、发送请求
                $.ajax({
                    url: "workbench/activity/saveEditActivity.do",
                    data: {
                        id: id,
                        owner: owner,
                        name: name,
                        startDate: startDate,
                        endDate: endDate,
                        cost: cost,
                        description: description
                    },
                    type: "post",
                    dataType: "json",
                    success: function (data) {
                        if (data.code == "1") {
                            //成功
                            //关闭modal窗口
                            $("#editActivityModal").modal("hide");
                            //刷新市场活动列表，保持页号和每页显示条数都不变
                            let currentPage = $("#paginationContainer").bs_pagination("getOption", "currentPage");
                            let rowsPerPage = $("#paginationContainer").bs_pagination("getOption", "rowsPerPage");
                            queryActivityByConditionForPage(currentPage, rowsPerPage)
                        } else {
                            //失败
                            alert(data.message);
                            //保持modal窗口不关闭，列表不刷新
                            $("#editActivityModal").modal("show");//理论上不写也不会关闭
                        }
                    }
                });

            });

            //给"批量导出"按钮添加单击事件
            $("#exportAllActivityBtn").click(function () {
                //向后台发送文件下载的请求
                window.location.href = "workbench/activity/exportAllActivity.do";
            });

            //给"选择导出"按钮添加单击事件
            $("#exportSelectedActivityBtn").click(function () {
                //收集数据
                let checkedIds = $("#activity-tbody input[type='checkbox']:checked");

                if (checkedIds.size() == 0) {
                    alert("请选择要导出的市场活动！");
                    return;
                }

                //拼接请求参数字符串
                let ids = "";
                $.each(checkedIds, function () {
                    //用dom对象即可，没必要传入index和obj的jquery参数
                    ids += "id=" + this.value + "&";
                })
                ids = ids.substring(0, ids.length - 1);

                //发送请求
                window.location.href = "workbench/activity/exportSelectedActivity.do?" + ids;
            });

            //给"导入"按钮添加单击事件
            $("#importActivityBtn").click(function () {
                let filePath = $("#activityFile").val();
                if (!filePath.endsWith(".xls")) {
                    alert("仅支持.xls文件，请重新上传！");
                    $("#importActivityForm").get(0).reset();
                    return;
                }

                $("#importActivityForm").get(0).submit();
            });

        });//入口函数的屁股

        //在!入口函数外面!封装函数
        function queryActivityByConditionForPage(pageNo, pageSize) {
            //收集参数：对于查询参数，不会涉及修改数据库，因此不需要trim()前后空格
            let name = $("#query-name").val();
            let owner = $("#query-owner").val();
            let startDate = $("#query-startDate").val();
            let endDate = $("#query-endDate").val();
            //分页信息用改为用参数传进来，更符合需求
            // let pageNo = 1;
            // let pageSize = 10;

            //发送异步请求
            $.ajax({
                url: "workbench/activity/queryActivityByConditionForPage.do",
                data: {
                    name: name,
                    owner: owner,
                    startDate: startDate,
                    endDate: endDate,
                    pageNo: pageNo,
                    pageSize: pageSize
                },
                type: 'post',
                dataType: 'json',
                success: function(data) {
                    //data的json内容，包含List activityList和int totalRows
                    //1、显示总条数
                    let totalRows = data.totalRows;
                    $("#totalRowsB").text(totalRows);//给这个b标签的text赋值

                    //把计算总页数的代码扔到后端去吧，太累了
                    //2、为分页插件的容器调用工具函数（这个时候容器一定加载完了！
                    $("#paginationContainer").bs_pagination({
                        currentPage: pageNo,

                        totalRows: data.totalRows,
                        rowsPerPage: pageSize,
                        totalPages: data.totalPages,

                        visiblePageLinks: 5,

                        showGoToPage: false,
                        showRowsPerPage: true,
                        showRowsInfo: true,

                        onChangePage: function (event, pageObj) {
                            //当翻页或者每页行数变化时，再执行一次发送ajax请求和渲染分页、列表的操作
                            //传入的两个参数分别是翻到的页码和更新后的每页行数
                            queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage)
                            //并不是没有递归终点的死循环哦，只有变化的时候才会执行
                        }
                    });

                    //3、遍历activityList，*拼接*所有行的数据，使用jquery的遍历
                    let htmlStr = "";//存放每一行拼接的html代码
                    //俩参数，第一个参数是要遍历的list，第二个是要做啥，即一个function
                    $.each(data.activityList, function (index, obj) {//回掉函数的俩参数，第一个是下标，第二个是拿到的list中的具体对象
                        //在方法体中this和参数obj作用一致：一般来说，简单的用this，复杂对象用obj
                        htmlStr += "<tr class=\"active\">";
                        htmlStr += "<td><input type=\"checkbox\" value=\"" + obj.id +"\" /></td>"//为方便checkbox后续的修改、删除操作，先把这行数据的id绑定给它
                        htmlStr += "<td><a style=\"text-decoration: none; cursor: pointer;"
                        htmlStr += "onclick=\"window.location.href='detail.html';\">" + obj.name + "</a></td>"
                        htmlStr += "<td>" + obj.owner + "</td>"
                        htmlStr += "<td>" + obj.startDate + "</td>"
                        htmlStr += "<td>" + obj.endDate + "</td>"
                        htmlStr += "</tr>"
                    })
                    // 然后找到table标签的jquery对象，把htmlStr填入它的html，注意是html不是text！
                    $("#activity-tbody").html(htmlStr);//使用html的覆盖显示，而不是append的追加显示，不然点一次查询就会多几行，前面数据不会清掉

                    //4、刷新后的列表肯定是没有被选中的，应当取消全选按钮的checked属性
                    $("#selectAll").prop("checked", false);
                }
            });
        }
    </script>
</head>
<body>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
            </div>
            <div class="modal-body">

                <form id="createActivityForm" class="form-horizontal" role="form">

                    <div class="form-group">
                        <label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="create-marketActivityOwner">
                                <c:forEach items="${requestScope.userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-marketActivityName">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control show-calendar" id="create-startDate">
                        </div>
                        <label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control show-calendar" id="create-endDate">
                        </div>
                    </div>
                    <div class="form-group">

                        <label for="create-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="create-cost">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="create-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="create-description"></textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" role="form">
                    <input type="hidden" id="edit-id">
                    <div class="form-group">
                        <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <select class="form-control" id="edit-marketActivityOwner">
<%--                                使用jstl标签，对request作用域中传入的list进行遍历--%>
                                <c:forEach items="${requestScope.userList}" var="u">
                                    <option value="${u.id}">${u.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span
                                style="font-size: 15px; color: red;">*</span></label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control show-calendar" id="edit-startDate" value="2020-10-10">
                        </div>
                        <label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control show-calendar" id="edit-endDate" value="2020-10-20">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                        <div class="col-sm-10" style="width: 300px;">
                            <input type="text" class="form-control" id="edit-cost" value="5,000">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="edit-description" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10" style="width: 81%;">
                            <textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
                        </div>
                    </div>

                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="saveEditActivityBtn" >更新</button>
            </div>
        </div>
    </div>
</div>

<!-- 导入市场活动的模态窗口 -->
<div class="modal fade" id="importActivityModal" role="dialog">
    <div class="modal-dialog" role="document" style="width: 85%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
            </div>
            <form id="importActivityForm" action="workbench/activity/importActivity.do" method="post" enctype="multipart/form-data">
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile" name="importActivityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;">
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </form>
        </div>
    </div>
</div>


<div>
    <div style="position: relative; left: 10px; top: -10px;">
        <div class="page-header">
            <h3>市场活动列表</h3>
        </div>
    </div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
    <div style="width: 100%; position: absolute;top: 5px; left: 10px;">

        <div class="btn-toolbar" role="toolbar" style="height: 80px;">
            <form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">名称</div>
                        <input class="form-control" type="text" id="query-name">
                    </div>
                </div>

                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">所有者</div>
                        <input class="form-control" type="text" id="query-owner">
                    </div>
                </div>


                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">开始日期</div>
                        <input class="form-control show-calendar" type="text" id="query-startDate" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="input-group">
                        <div class="input-group-addon">结束日期</div>
                        <input class="form-control  show-calendar" type="text" id="query-endDate" />
                    </div>
                </div>

                <button type="button" class="btn btn-default" id="queryActivityBtn">查询</button>

            </form>
        </div>
        <div class="btn-toolbar" role="toolbar"
             style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-primary"
<%--                        不通过data-toggle和data-target弹出modal框，使用jquery的id选择器绑定单击事件--%>
<%--                        data-toggle="modal" data-target="#createActivityModal"--%>
                        id="createActivityBtn">
                    <span class="glyphicon glyphicon-plus"></span> 创建
                </button>
                <button type="button" class="btn btn-default"
<%--                        同上，不通过data-toggle和data-target来弹出modal窗--%>
                        id="editActivityBtn">
                    <span class="glyphicon glyphicon-pencil"></span> 修改
                </button>
                <button type="button" class="btn btn-danger" id="deleteActivityBtn">
                    <span class="glyphicon glyphicon-minus" ></span> 删除
                </button>
            </div>
            <div class="btn-group" style="position: relative; top: 18%;">
                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal">
                    <span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）
                </button>
                <button id="exportAllActivityBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）
                </button>
                <button id="exportSelectedActivityBtn" type="button" class="btn btn-default"><span
                        class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）
                </button>
            </div>
        </div>
        <div style="position: relative;top: 10px;">
            <table class="table table-hover">
                <thead>
                <tr style="color: #B3B3B3;">
                    <td><input type="checkbox" id="selectAll"/></td>
                    <td>名称</td>
                    <td>所有者</td>
                    <td>开始日期</td>
                    <td>结束日期</td>
                </tr>
                </thead>
                <tbody id="activity-tbody">
<%--                写死的市场活动列表--%>
<%--                <tr class="active">--%>
<%--                    <td><input type="checkbox"/></td>--%>
<%--                    <td><a style="text-decoration: none; cursor: pointer;"--%>
<%--                           onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                    <td>zhangsan</td>--%>
<%--                    <td>2020-10-10</td>--%>
<%--                    <td>2020-10-20</td>--%>
<%--                </tr>--%>
<%--                <tr class="active">--%>
<%--                    <td><input type="checkbox"/></td>--%>
<%--                    <td><a style="text-decoration: none; cursor: pointer;"--%>
<%--                           onclick="window.location.href='detail.html';">发传单</a></td>--%>
<%--                    <td>zhangsan</td>--%>
<%--                    <td>2020-10-10</td>--%>
<%--                    <td>2020-10-20</td>--%>
<%--                </tr>--%>
                </tbody>
            </table>
        </div>

        <div id="paginationContainer"></div>
<%--        分页条--%>
<%--        <div style="height: 50px; position: relative;top: 30px;">--%>
<%--            <div>--%>
<%--&lt;%&ndash;                                                                    b标签是加粗用的哦&ndash;%&gt;--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>--%>
<%--            </div>--%>
<%--            <div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
<%--                <div class="btn-group">--%>
<%--                    <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--                        10--%>
<%--                        <span class="caret"></span>--%>
<%--                    </button>--%>
<%--                    <ul class="dropdown-menu" role="menu">--%>
<%--                        <li><a href="#">20</a></li>--%>
<%--                        <li><a href="#">30</a></li>--%>
<%--                    </ul>--%>
<%--                </div>--%>
<%--                <button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
<%--            </div>--%>
<%--            <div style="position: relative;top: -88px; left: 285px;">--%>
<%--                <nav>--%>
<%--                    <ul class="pagination">--%>
<%--                        <li class="disabled"><a href="#">首页</a></li>--%>
<%--                        <li class="disabled"><a href="#">上一页</a></li>--%>
<%--                        <li class="active"><a href="#">1</a></li>--%>
<%--                        <li><a href="#">2</a></li>--%>
<%--                        <li><a href="#">3</a></li>--%>
<%--                        <li><a href="#">4</a></li>--%>
<%--                        <li><a href="#">5</a></li>--%>
<%--                        <li><a href="#">下一页</a></li>--%>
<%--                        <li class="disabled"><a href="#">末页</a></li>--%>
<%--                    </ul>--%>
<%--                </nav>--%>
<%--            </div>--%>
<%--        </div>--%>

    </div>

</div>
</body>
</html>