package com.nuist.filter;

import com.alibaba.fastjson.JSON;
import com.nuist.common.BaseContext;
import com.nuist.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
@Slf4j
public class LoginFilter implements Filter {

    public static AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        //log.info("拦截路径为" + requestURI);

        //公共路径，所有人可以访问
        String[] commonUrls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/user/loginout"
        };

        //employee登录后可访问的路径
        String[] employeeUrls = new String[]{
                "/dish/**",
                "/employee/**",
                "/category/**",
                "/setmeal/**",
                "/order/**"
        };

        //user登陆后可访问的路径
        String[] userUrls = new String[]{
                "/user/**",
                "/addressBook/**",
                "/order/**",
                "/shoppingCart/**",
                "/setmeal/dish/**",
                "/setmeal/list",
                "/dish/list",
                "/category/list",
        };


        if(checkFilter(commonUrls, requestURI)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if(null != request.getSession().getAttribute("employee") && checkFilter(employeeUrls, requestURI)) {
            BaseContext.setValue((Long)request.getSession().getAttribute("employee"));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if(null != request.getSession().getAttribute("user") && checkFilter(userUrls, requestURI)) {
            BaseContext.setValue((Long)request.getSession().getAttribute("user"));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        response.getWriter().write(JSON.toJSONString(Result.fail("NOTLOGIN")));
        return;
    }


    private boolean checkFilter(String[] urls, String URI) {
        for(String url : urls) {
            if(antPathMatcher.match(url, URI)) return true;
        }
        return false;
    }
}
