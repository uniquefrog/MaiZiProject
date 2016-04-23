package com.dianping.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.dao.impl.CategoryDaoImpl;
import com.dianping.enity.Category;
import com.dianping.enity.ResponseObject;
import com.google.gson.GsonBuilder;
@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		CategoryDaoImpl daoCategory=new CategoryDaoImpl();
		long sum=daoCategory.getCategoryTotal();
		List<Category> list=daoCategory.getCategoryDao();
		Category first=list.get(0);
		first.setCategoryId(1+"");
		first.setCategeryNumber(sum);
		ResponseObject responseObject=null;
		if (list!=null && list.size()>0) {
			responseObject=new ResponseObject(1,list);
		}
		else{
			responseObject=new ResponseObject(0,"没有分类数据");
		}
		out.println(new GsonBuilder().create().toJson(responseObject));
		
		out.flush();
		out.close();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
