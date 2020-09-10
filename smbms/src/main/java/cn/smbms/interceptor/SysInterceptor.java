package cn.smbms.interceptor;

import cn.smbms.pojo.User;
import cn.smbms.tools.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: smbms
 * @description:
 * @author: lrh
 * @create: 2020-09-07 09:04:21
 **/
public class SysInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User)request.getSession().getAttribute(Constants.USER_SESSION);
        if (user == null){
            response.sendRedirect(request.getContextPath()+"/error.jsp");
            return false;
        }
        return true;
    }
}
