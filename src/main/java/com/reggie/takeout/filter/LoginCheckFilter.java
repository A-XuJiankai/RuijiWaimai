package com.reggie.takeout.filter;

import com.alibaba.fastjson.JSON;
import com.reggie.takeout.utils.BaseContext;
import com.reggie.takeout.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @Author gett
 * @Date 2022/4/13  9:54
 * @Description 检查登录是否已经完成
 *              登录添加一个过滤器或拦截器，判断用户是否已经完成登录，如果没有登录则返回提示信息，跳转到登录页面。
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static  final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //①. 获取本次请求的URI
        String requestURI = request.getRequestURI();

        log.info("拦截请求路径{}",requestURI);

        //不需要处理的请求路径
        String[] urls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/v2/api-docs",
                "/swagger-resources",
                "/v2/api-docs-ext"
        };

        //②. 判断本次请求, 是否需要登录, 才可以访问
        boolean check = check(urls, requestURI);

        //③. 如果不需要，则直接放行
        if (check){
            log.info("本次请求不需要处理{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        //④. 判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee")!=null){
            log.info("用户{}已登录",request.getSession().getAttribute("employee"));

            Long employeeId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(employeeId);

            filterChain.doFilter(request,response);
            return;
        }

        //④. 判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("user")!=null){
            log.info("用户{}已登录",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


        //⑤. 如果未登录, 则返回未登录结果
        //前端request.js中响应拦截器service.interceptors.response.use();
        //通过输出流方式向客户端响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    @Override
    public void destroy() {

    }

    /**
     * @Description   路径匹配，检查本次请求是否需要放行
     * @Author gett
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }


}
