package com.lingrui.crm.settings.web.controller;

import com.lingrui.crm.common.constants.Constants;
import com.lingrui.crm.common.domain.ObjectForReturn;
import com.lingrui.crm.common.utils.DateUtils;
import com.lingrui.crm.settings.domain.User;
import com.lingrui.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-19 16:53
 * @ Version 1.0
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * @param :
     * @return String
     * @author xulingrui
     * @description TODO
     * RequestMapping的url要和controller方法处理完请求之后，响应信息返回的页面的资源目录保持一致
     * @date 2022/5/19 17:05
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(HttpServletRequest request) {
        //服务器内部转发请求给WEB-INF/pages/ settings/qx/user/login. jsp
        return "settings/qx/user/login";//前面那个"/"在视图解析器的配置中已经加上了
    }

    /**
     * @param loginAct:
     * @param loginPwd:
     * @param isRemPwd: 是否10天以内记住密码
     * @return Object
     * @author xulingrui
     * @description TODO
     * 处理用户登录的请求
     * @date 2022/5/20 20:36
     */
    @RequestMapping("/settings/qx/user/login.do")//响应信息往/settings/qx/user/login.jsp返回，所以路径也要写在它的资源目录
    @ResponseBody//通过Jackson，把对象以json字符串返回给页面
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        //封装参数，便于往下传递
        HashMap<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        //这个map最终会被传到mapper层，映射文件中的sql语句的#{}会直接从mapper中取出key并填入value，所以这个封装参数的map中key的值不能乱写哦
        User user = userService.queryUserByLoginActAndPwd(map);

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //根据查询结果，生成响应信息
        ObjectForReturn objectForReturn = new ObjectForReturn();
        if (user != null) {
            //至少用户名和密码匹配到数据库中的记录了
            //进一步判断账号是否合法

            //1、判断expireDate是否到期
            //方法一：使用SimpleDateFormat，parse一下user中的expireDate，和new Date()的getTime()进行比较
            //方法二：利用字符串比大小是逐位比较的特性，但是需要保证格式一致
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String currentTime = sdf.format(new Date());
            String currentTime = DateUtils.formatDateTime(new Date());//使用通用的工具类，将格式化Date对象的操作封装起来
            if (currentTime.compareTo(user.getExpireTime()) > 0) {
                //登陆失败，账号已过期
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);//使用常量，增加可读性和后期可维护性
                objectForReturn.setMessage("账户已过期");
            } else if ("0".equals(user.getLockState())) {
            //2、判断账户是否已锁定
                //登陆失败，账户已锁定状态
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage("账户已被锁定");
            } else if (user.getAllowIps() != null && !user.getAllowIps().contains(request.getRemoteAddr())){
            //3、判断请求的ip是否为用户的常用ip
                //登陆失败，当前请求的ip不在用户常用ip中
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
                objectForReturn.setMessage("当前ip访问受限");
            } else {
                //登陆成功！
                objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_SUCCESS);
                //把user存入session中，供页面显示和其他controller获取使用
                session.setAttribute(Constants.SESSION_USER, user);

                //如果需要记住密码，则往浏览器写cookie
//                System.out.println(isRemPwd);
                if ("true".equals(isRemPwd)) {
                    //1、新建一个cookie
                    Cookie loginActCookie = new Cookie("loginAct", user.getLoginAct());
                    //2、把它的过期时间设定为10天
                    loginActCookie.setMaxAge(10 * 24 * 60 * 60);
                    //3、通过HttpServletResponse写出
                    response.addCookie(loginActCookie);

                    Cookie loginPwdCookie = new Cookie("loginPwd", user.getLoginPwd());
                    loginPwdCookie.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(loginPwdCookie);
                } else {
                //没有选择记住密码，表示用户今后不希望记住密码了！
                    //但是不可能在后端的server删除客户端的文件
                    //方法：往外写出同名cookie，并且把maxAge设为0
                    Cookie loginActCookie = new Cookie("loginAct", "1");
                    Cookie loginPwdCookie = new Cookie("loginPwd", "1");

                    loginActCookie.setMaxAge(0);
                    loginPwdCookie.setMaxAge(0);

                    response.addCookie(loginActCookie);
                    response.addCookie(loginPwdCookie);
                }
            }
        } else {//登陆失败，用户名或密码错误
            objectForReturn.setCode(Constants.OBJECT_FOR_RETURN_FAIL);
            objectForReturn.setMessage("用户名或密码错误");
        }

        return objectForReturn;
    }

    /**
     * @param response:
     * @param session:
     * @return String
     * @author xulingrui
     * @description TODO
     * 处理用户安全退出的请求
     * @date 2022/5/23 12:46
     */
    @RequestMapping("settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session) {
        //1、清空cookie
        Cookie loginActCookie = new Cookie("loginAct", "1");
        Cookie loginPwdCookie = new Cookie("loginPwd", "1");
        loginActCookie.setMaxAge(0);
        loginPwdCookie.setMaxAge(0);
        response.addCookie(loginActCookie);
        response.addCookie(loginPwdCookie);

        //2、销毁会话，清除session所占内存，并且清除session中保存的所有数据
        session.invalidate();

        //3、跳转到首页
        return "redirect:/";//重定向到地址crm/，这个请求会被发给web.controller.IndexController处理，返回/index.jsp，然后index.jsp中再转发给/settings/qx/user/toLogin.do。。。
    }
}
