package com.lingrui.crm.settings.web.interceptor;

import com.lingrui.crm.common.constants.Constants;
import com.lingrui.crm.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ Description
 * @ Author Carmen
 * @ Date 2022-05-23 12:14
 * @ Version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * @param httpServletRequest:
     * @param httpServletResponse:
     * @param o:
     * @return boolean
     *      返回true表示放行
     *      返回false表示拦截
     * @author xulingrui
     * @description TODO
     *      到达目标资源之前执行的代码
     * @date 2022/5/23 12:16
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //验证用户是否登陆

        HttpSession session = httpServletRequest.getSession();//控制器里拿session可以注入；但是现在是拦截器！
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        if (user == null) {
            //如果用户没有登陆成功，则跳转到登陆页面
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath());//自己手动sendRedirect，要加入项目的名字；请求转发不加项目名字！
            //controller中"redirect:/"没有写项目名字，是因为springMVC底层自动加上了项目名！
            return false;//终止往下访问
        }
        //session中sessionUser存在，表明登陆过，返回true放行对目标资源的访问
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
