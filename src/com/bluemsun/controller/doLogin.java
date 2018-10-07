package com.bluemsun.controller;

import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by mafx on 2018/8/1.
 */
public class doLogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String flag = request.getParameter("flag");
        switch (flag) {
            case "login":
                login(request, response);
                break;
            case "getUsername":
                getUsername(request, response);
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    private void login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String message;//反馈信息
        int stateCode;//状态码，1：成功 0：失败
        String jsonMessage;//返回该前台的json数据
        if (username != null && password != null && username.equals("534") && password.equals("zizhu")) {
            message = "登录成功";
            stateCode = 1;
            request.getSession().setAttribute("username", username);
        } else {
            message = "用户名或密码错误！！！";
            stateCode = 0;
        }
        jsonMessage = "{\"stateCode\":\"" + stateCode + "\",\"message\":\"" + message + "\"}";
        try {
            response.getWriter().write(JSONObject.fromObject(jsonMessage).toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void getUsername(HttpServletRequest request, HttpServletResponse response){
        String username=request.getSession(true).getAttribute("username").toString();
        String message="{\"username\":\""+username+"\"}";
        try {
            response.getWriter().write(JSONObject.fromObject(message).toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

