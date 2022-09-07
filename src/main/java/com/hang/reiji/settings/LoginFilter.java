package com.hang.reiji.settings;

import com.alibaba.fastjson2.JSON;
import com.hang.reiji.domain.R;
import com.hang.reiji.utils.UtilOne;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器，用来验证是否登陆的，防止非法访问
 */
//注册名字，拦截的路径。要被扫描到创建对象才能生效，所以还需要在启动类上扫描
@WebFilter(filterName = "loginFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {
    private static final AntPathMatcher bj = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI();
        String[] excludePath = {"/backend/**",
                "/employee/login",
                "/employee/logout",
                "/front/**",
                "/user/login",
                "/user/loginout",
                "/user/getCode"};
        boolean ok = compare(path,excludePath);
        //ok为true，说明访问的是登陆相关，放行
        if (ok){
            filterChain.doFilter(request,servletResponse);
            return;
        }
        //执行到此，说明不是访问登陆相关，判断是否登陆
        Long id = (Long) request.getSession().getAttribute("employee");
        if (id != null){
            //把登陆者的ID保存到线程中的变量，公共字段赋值会用到
            UtilOne.setIdInThread(id);
            filterChain.doFilter(request,servletResponse);
            return;
        }
        Long id1 = (Long) request.getSession().getAttribute("user");
        if (id1 != null){
            //把登陆者的ID保存到线程中的变量，公共字段赋值会用到
            UtilOne.setIdInThread(id1);
            filterChain.doFilter(request,servletResponse);
            return;
        }
        //执行到此，说明是非法访问
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    //封装的一个比较路径的方法，如果返回true，表示无条件放行
    boolean compare(String path,String[] excludePath){
        for (String s : excludePath){
            if (bj.match(s,path)){
                return true;
            }
        }
        return false;
    }
}
