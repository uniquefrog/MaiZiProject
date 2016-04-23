package com.dianping.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.dao.CItyDao;
import com.dianping.dao.impl.CityDaoImpl;
import com.dianping.enity.City;
import com.dianping.enity.ResponseObject;
import com.google.gson.GsonBuilder;

public class CityServlet extends HttpServlet {

	private static final long serialVersionUID = -7116871746214279602L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		CItyDao dao = new CityDaoImpl();
		List<City> list = dao.getCity();
		ResponseObject result = null;
		if(list != null && list.size()>0){
			result = new ResponseObject(1, list);
		}else{
			result = new ResponseObject(0, "没有城市数据！");
		}
		out.println(new GsonBuilder().create().toJson(result));
		out.flush();
		out.close();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
