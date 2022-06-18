<%@page contentType="text/html; charset=UTF-8" language="java" %>
<%--引入jstl的标签--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    //使用request的各种信息，拼凑出根目录
                    //协议 http                    地址 127.0.0.1                     端口 8080                  项目名 /crm
    String basePath=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

    //用jsp作为servlet的特性，实现从cookie中获取记住的账户密码；在标签中用jsp表达式传入value
//    String loginAct = null;
//    String loginPwd = null;
//    for (Cookie cookie : request.getCookies()) {
//        if ("loginAct".equals(cookie.getName())) {
//            loginAct = cookie.getValue();
//        }
//        if ("loginPwd".equals(cookie.getName())) {
//            loginPwd = cookie.getValue();
//        }
//    }
//
//    System.out.println("loginAct: " + loginAct + "\nloginPwd: " + loginPwd);
//
//    if (loginAct == null || loginPwd == null) {
//        loginAct = "";
//        loginPwd = "";
//    }

    //当然，最好还是通过EL表达式，获取cookie作用域中的数据value="${cookie.loginAct.value}"填入对应标签的value
%>
<html>
<head>
    <meta charset="UTF-8">
    <%--	静态资源在根目录webapp/下，如果都相对于页面的位置来找很麻烦，因此用base标签规定根目录--%>
<%--    <base href="http://127.0.0.1:8080/crm/">--%>
    <base href="<%=basePath%>">
    <link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
    <script type="text/javascript">
        //入口函数，页面加载完后会加载
        $(function () {
            //给"登陆"按钮添加单击事件
            $("#loginButton").click(function () {
                //1、收集页面中的参数
                let loginAct = $("#loginAct").val();
                loginAct = $.trim(loginAct);//去除首尾空格
                let loginPwd = $.trim($("#loginPwd").val());
                let isRemPwd = $("#idRemPwd").prop("checked");

                // alert(isRemPwd);

                //2、对收集到的参数进行验证，不合法的参数不发给后台，减少服务器压力
                if (loginAct == "" || loginPwd == "") {
                    //用户名和密码不能为空
                    alert("用户名或密码不能为空");
                    return;//不要继续往下执行
                }

                //点击完登陆之后，在响应返回值之前，显示"正在加载数据"
                //这个功能还能在ajax请求参数的beforeSend属性中实现！
                // $("#msg").text("正在加载数据...");

                //3、发送异步请求 {}中实际上传了一个对象进去作为参数，
                // 这个对象包含了url、携带的数据、方法类型、接收返回值的类型、成功后的处理等
                $.ajax({
                    url:'settings/qx/user/login.do',
                    data:{
                        loginAct: loginAct,
                        loginPwd: loginPwd,
                        isRemPwd: isRemPwd
                    },
                    type:'post',
                    dataType:'json',
                //4、处理后台返回来的json字符串
                    success:function (data) {//这个data就是controller返回的ObjectForReturn对象，包含code、message和returnData
                        if (data.code == "1") {
                            //处理成功，发送同步请求，跳转到主页面，这个请求要发给controller哦
                            window.location.href = "workbench/index.do";
                        } else {
                            //登陆失败
                            //将json对象中的信息填入
                            $("#msg").html(data.message);
                            // $("#msg").text(data.message);//也可以用text()方法对span的文本进行填充
                        }
                    },
                    //当ajax向后台发送请求之前自动执行本函数
                    beforeSend:function () {
                        //意义：该函数的返回值能够决定ajax是否真的要想后台发送请求
                        //可以把表单验证的步骤放到beforeSend的函数中！当然也可以放在前面，更符合逻辑
                        $("#msg").text("正在加载数据...");
                        return true;//如果作为beforeSend属性值的函数返回值为false，则ajax会放弃发送请求！
                    }
                });
            });

            //给整个浏览器窗口添加键盘按下事件
            $(window).keydown(function (event) {
                //判断按下的是否为enter键
                // console.log(event.keyCode);
                if (event.keyCode == 13) {
                    //模拟"登陆"按钮的单击事件，这个比较帅！
                    $("#loginButton").click();//jquery选择器.click()，传递function(){}参数，表示绑定事件；不传参数，表示模拟这个事件
                }
            });

            //读取cookie，为loginAct和loginPwd填入数据

        });
    </script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
    <img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
    <div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">
        CRM &nbsp;<span style="font-size: 12px;">&copy;2022&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
    <div style="position: absolute; top: 0px; right: 60px;">
        <div class="page-header">
            <h1>登录</h1>
        </div>
        <form action="workbench/index.html" class="form-horizontal" role="form">
            <div class="form-group form-group-lg">
                <div style="width: 350px;">
                    <input class="form-control"
                           id="loginAct"
<%--                           value="<%=loginAct%>"--%>
                           value="${cookie.loginAct.value}"
                           type="text" placeholder="用户名">
                </div>
                <div style="width: 350px; position: relative;top: 20px;">
                    <input class="form-control"
                           id="loginPwd"
<%--                           value="<%=loginPwd%>"--%>
                           value="${cookie.loginPwd.value}"
                           type="password" placeholder="密码">
                </div>
                <div class="checkbox" style="position: relative;top: 30px; left: 10px;">
                    <label>
<%--                        使用jstl标签对cookie是否为空判断，决定是否要勾上--%>
                        <c:if test="${not empty cookie.loginAct and not empty cookie.loginPwd}">
                            <input type="checkbox" id="idRemPwd" checked="checked">
                        </c:if>
                        <c:if test="${empty cookie.loginAct or empty cookie.loginPwd}">
                            <input type="checkbox" id="idRemPwd">
                        </c:if>
                        十天内免登陆
                    </label>
                    &nbsp;&nbsp;
                    <span id="msg" style="color: red"></span>
                </div>
                <button type="button" class="btn btn-primary btn-lg btn-block"
<%--                        为了方便jquery通过id选择器获取这个按钮对象--%>
                        id="loginButton"
                        style="width: 350px; position: relative;top: 45px;">登录
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>