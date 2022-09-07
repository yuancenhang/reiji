package com.hang.reiji.settings;

import com.alibaba.fastjson.JSON;
import com.hang.reiji.domain.R;
import com.hang.reiji.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全拦截器,用了过滤器，这个暂时没用
 */
@Slf4j
public class SafeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("非法访问，访问资源是：" + request.getRequestURI());
        Long id = (Long) request.getSession().getAttribute("employee");
        if (id == null){
            //response.sendRedirect("/backend/page/login/login.html");
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }
        return true;
    }
}
