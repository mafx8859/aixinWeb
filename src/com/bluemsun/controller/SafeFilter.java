package com.bluemsun.controller;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mafx on 2018/8/5.
 */
public class SafeFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletResponse response=(HttpServletResponse)resp;
        HttpServletRequest request=(HttpServletRequest)req;
        if(request.getSession().getAttribute("username")==null){
            if("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
                //告诉ajax我是重定向
                response.setHeader("REDIRECT", "REDIRECT");
                //告诉ajax我重定向的路径
                response.setHeader("CONTENTPATH", "index.html");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }else {
                response.sendRedirect("http://wxy.nenu.edu.cn/aixinWeb/supermarket/index.html");
                //request.getRequestDispatcher("/supermarket/index.html").forward(request,response);
                return;
            }
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
