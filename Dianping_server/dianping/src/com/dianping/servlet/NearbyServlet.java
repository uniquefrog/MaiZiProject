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
import com.dianping.util.CommonUtil;
import com.google.gson.GsonBuilder;

public class NearbyServlet extends HttpServlet {

	private static final long serialVersionUID = -1765316287476383500L;

	// http://localhost:8080/ls-server/api/nearby?lat=39.983456&lon=116.3154950&radius=1000&category=2856
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String lat = request.getParameter("lat");//纬度
		String lon = request.getParameter("lon");//精度
		String radius = request.getParameter("radius");//搜索半径
		int page = 0;
		int size =20;
		////按照给定的坐标和半径计算获取最小经度 最大经度  最小纬度 最大纬度
		double[] around = CommonUtil.getAround(Double.parseDouble(lat), Double.parseDouble(lon), Integer.parseInt(radius));
		GoodsDao dao = new GoodsDaoImpl();
		//System.out.print("经度："+lon+"纬度："+lat);
		List<Goods> list = dao.getGoodsbyLBS(page,size,null,Double.parseDouble(lat),Double.parseDouble(lon) ,around[0], around[1], around[2], around[3]);
		ResponseObject result = null;
		if(list != null && list.size()>0){
			result = new ResponseObject(1, list);
		}else{
			result = new ResponseObject(0, "你的附近还没有商品");
		}
		
		//提供出去json串
		out.println(new GsonBuilder().create().toJson(result));
		out.flush();
		out.close();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}
