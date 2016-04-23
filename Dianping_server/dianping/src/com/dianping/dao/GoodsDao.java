package com.dianping.dao;

import java.util.List;

import com.dianping.enity.Goods;

public interface GoodsDao {
	public List<Goods> getList(String cityId,String catId,int page,int size);
	
	public double getCount(String cityId,String catId);
	
	/**
	 * @param lat 精度
	 * @param lon 纬度
	 * @return 范围内指定商家
	 */
	List<Goods> getGoodsbyLBS(int page,int size,String category,double lat,double lon,double minlat,double minlon,double maxlat,double maxlon);
	int getCountByLBS(String category,double lat,double lon,double minlat,double minlon,double maxlat,double maxlon);
}
