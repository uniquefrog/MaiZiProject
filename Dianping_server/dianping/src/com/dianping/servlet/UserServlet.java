package com.dianping.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.dianping.dao.UserDao;
import com.dianping.dao.impl.UserDaoImpl;
import com.dianping.enity.ResponseObject;
import com.dianping.enity.User;
import com.google.gson.GsonBuilder;

public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 6159285953322414434L;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		 response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String name = request.getParameter("username");
		String pwd = request.getParameter("password");
		String flag = request.getParameter("flag");
		UserDao uDao = new UserDaoImpl();
		ResponseObject result = null;
		//http://127.0.0.1:8080/ls-server/api/user?flag=register&username=test&password=123456
		if("register".equals(flag)){
			if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(pwd)){
				User user = uDao.register(name, pwd);
				if(user != null){
					result = new ResponseObject(1,"注册成功！",user);
				}else{
					result = new ResponseObject(0,"用户已存在！");
				}
			}else{
				result = new ResponseObject(0,"用户名和密码不能为空！");
			}
		}else if("login".equals(flag)){
			if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(pwd)){
				User user = uDao.login(name, pwd);
				if(user != null){
					result = new ResponseObject(1,"登录成功！",user);
				}else{
					result = new ResponseObject(0,"登录失败，用户名或密码不正确！");
				}
			}else{
				result = new ResponseObject(0,"用户名和密码不能为空！");
			}
		}else if("social".equals(flag)){
			if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(pwd)){
				User user = uDao.social(name, pwd);
				if(user != null){
					result = new ResponseObject(1,"授权登录成功！",user);
				}else{
					result = new ResponseObject(0,"授权登录失败，请重试！");
				}
			}else{
				result = new ResponseObject(0,"用户名和密码不能为空！");
			}
		}
		out.print(new GsonBuilder().create().toJson(result));
		out.flush();
		out.close();
	}
}
