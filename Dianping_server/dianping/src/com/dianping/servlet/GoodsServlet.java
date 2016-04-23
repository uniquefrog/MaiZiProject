package com.dianping.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianping.dao.GoodsDao;
import com.dianping.dao.impl.GoodsDaoImpl;
import com.dianping.enity.Goods;
import com.dianping.enity.ResponseObject;
import com.google.gson.GsonBuilder;

public class GoodsServlet extends HttpServlet {

	private static final long serialVersionUID = -3834906902702728798L;
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		GoodsDao dao = new GoodsDaoImpl();
		String cityId = request.getParameter("cityId");
		String catId = request.getParameter("catId");
		int page = Integer.parseInt(request.getParameter("page"));
		int size = Integer.parseInt(request.getParameter("size"));
		List<Goods> list = dao.getList(cityId, catId, page, size);
		double count = dao.getCount(cityId, catId);
		//将对象转换为json字符串
		ResponseObject result = null;
		if(list != null && list.size()>0){
			result = new ResponseObject(1, list);
			result.setPage(page);
			result.setCount((int)Math.ceil(count/size));
			result.setSize(size);
		}else{
			result = new ResponseObject(0, "没有商品数据！");
		}
		out.println(new GsonBuilder().create().toJson(result));
		out.flush();
		out.close();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}
}
