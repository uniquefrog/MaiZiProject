package com.dianping.util;

public class CommonUtil {

	/***************************************************************************
	 * 计算2点直接的距离
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
//	public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
//		double radLat1 = lat1 * Math.PI / 180;
//		double radLat2 = lat2 * Math.PI / 180;
//		double a = radLat1 - radLat2;
//		double b = lon1 * Math.PI / 180 - lon2 * Math.PI / 180;
//		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
//		s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
//		s = Math.round(s * 10000) / 10000;
//		return s;
//	}

	/**
	 * 经度
	 * 纬度
	 * 搜索半径
	 * 
	 * 返回最大最小的经纬度
	 * 
	 * 地球的周长是24901英里  1英里等于1609米
	 */
	public static double[] getAround(double lat, double lon, int radius) {

		Double latitude = lat;
		Double longitude = lon;
		//计算地球一周中每一度占多少米
		Double degree = (24901 * 1609) / 360.0;
		//计算纬度上变化1米是多少度
		Double dpmLat = 1 / degree;
		//获取搜索半径在纬度上的度数变化
		Double radiusLat = dpmLat * radius;
		//获取最小的纬度和最大的纬度
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;
		
		//计算定位的地点上小圆上的距离变化   用于=获取经度
		Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
		Double dpmLng = 1 / mpdLng;//经度上变化一米是多少度
		Double radiusLng = dpmLng * radius;
		//获取的最小和最大的经度
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, minLng, maxLat, maxLng };
	}
	

}
