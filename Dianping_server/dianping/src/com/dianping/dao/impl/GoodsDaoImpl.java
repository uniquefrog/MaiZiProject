package com.dianping.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dianping.dao.GoodsDao;
import com.dianping.enity.Goods;
import com.dianping.enity.Shop;

public class GoodsDaoImpl extends BaseDao implements GoodsDao {

	private String str = "prodouct.prodouct_id, prodouct.category_id,prodouct.shop_id,prodouct.city_id,prodouct.prodouct_title,"
			+ "prodouct.prodouct_sort_title, prodouct.prodouct_image, prodouct.prodouct_start_time,prodouct.prodouct_value,prodouct.prodouct_price,"
			+ "prodouct.prodouct_ribat, prodouct.prodouct_bought, prodouct.prodouct_minquota, prodouct.prodouct_maxquota,prodouct.prodouct_post,"
			+ " prodouct.prodouct_soldout,prodouct.prodouct_tip, prodouct.prodouct_end_time,prodouct.prodouct_detail,prodouct.prodouct_is_refund, prodouct.prodouct_is_over_time,shop.shop_id,"
			+ "shop.shop_name, shop.shop_tel,shop.shop_address,shop.shop_area,shop.shop_open_time, shop.shop_lon, shop.shop_lat,shop.shop_traffic_info";

	public List<Goods> getList(String cityId, String categoryId, int page, int size) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = "select " + str + " from prodouct,shop where prodouct.shop_id=shop.shop_id " + (StringUtils.isNotBlank(cityId) ? (" and city_id = " + cityId) : "")
				+ (StringUtils.isNotBlank(categoryId) ? (" and category_id = " + categoryId) : "") + " limit " + (page * size) + "," + size;
		List<Goods> goods = null;
		try {
			connection = getConn();
			statement = connection.createStatement();
			//System.out.println("产品查询的sql语句:" + sql);
			resultSet = statement.executeQuery(sql);
			goods = new ArrayList<Goods>();

			while (resultSet.next()) {
				Goods product = new Goods();
				product.setId(resultSet.getString("prodouct_id"));
				product.setCategoryId(resultSet.getString("category_id"));
				product.setShopId(resultSet.getString("shop_id"));
				product.setCityId(resultSet.getString("city_id"));
				product.setTitle(resultSet.getString("prodouct_title"));
				product.setSortTitle(resultSet.getString("prodouct_sort_title"));
				product.setImgUrl(resultSet.getString("prodouct_image"));
				product.setStartTime(resultSet.getString("prodouct_start_time"));
				product.setValue(resultSet.getString("prodouct_value"));
				product.setPrice(resultSet.getString("prodouct_price"));
				product.setRibat(resultSet.getString("prodouct_ribat"));
				product.setBought(resultSet.getString("prodouct_bought"));
				product.setMaxQuota(resultSet.getString("prodouct_maxquota"));
				product.setPost(resultSet.getString("prodouct_post"));
				product.setSoldOut(resultSet.getString("prodouct_soldout"));
				product.setTip(resultSet.getString("prodouct_tip"));
				product.setEndTime(resultSet.getString("prodouct_end_time"));
				product.setDetail(resultSet.getString("prodouct_detail"));
				product.setMinquota(resultSet.getString("prodouct_minquota"));

				Shop shop = new Shop();
				shop.setId(resultSet.getString("shop.shop_id"));
				shop.setName(resultSet.getString("shop_name"));
				shop.setAddress(resultSet.getString("shop_address"));
				shop.setArea(resultSet.getString("shop_area"));
				shop.setOpentime(resultSet.getString("shop_open_time"));
				shop.setTel(resultSet.getString("shop_tel"));
				shop.setLat(resultSet.getString("shop_lat"));
				shop.setLon(resultSet.getString("shop_lon"));
				product.setShop(shop);
				int refund = resultSet.getInt("prodouct_is_refund");
				int overTime = resultSet.getInt("prodouct_is_over_time");
				if (refund == 1) {
					product.setRefund(true);
				} else if (refund == 0) {
					product.setRefund(false);
				}
				if (overTime == 1) {
					product.setOverTime(true);
				} else if (overTime == 0) {
					product.setOverTime(false);
				}
				goods.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(resultSet, statement, connection);
		}
		return goods;
	}

	public double getCount(String cityId, String catId) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = getConn();
			statement = connection.createStatement();
			String sql = "select  count(*) from prodouct where 1=1";
			if (StringUtils.isNotBlank(cityId)) {
				sql += " and city_id = " + cityId;
			}
			if (StringUtils.isNotBlank(catId)) {
				sql += " and category_id = " + catId;
			}
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				int count = Integer.parseInt(resultSet.getString("count(*)"));
				return count;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			close(resultSet, statement, connection);
		}
		return 0;
	}
	
	
	
	//搜索附近的商品
	public List<Goods> getGoodsbyLBS(int page,int size,String category,double lat,double lon, double minlat, double minlon, double maxlat, double maxlon) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
//		"3", "2894", "8", "2892", "2856", "2924", "4", "6", ""
//		"美食", "电影", "酒店", "KTV", "火锅", "美容美发", "休闲娱乐", "生活服务", "全部"
		
		String sql = "select " +
				"GLength(LineString(PointFromWKB( POINT(  "+lat+" , "+lon+")),PointFromWKB( POINT( shop.shop_lat, shop.shop_lon)))) * 69*1609.344 AS distance,"+
				" shop.*, prodouct.* from prodouct,shop where prodouct.shop_id=shop.shop_id " ;
				if(category == null || "".equals(category)){
					
				}else if(category.length()<4){
					sql +="and category_id = "+category;
				}else{
					sql +="and sub_category_id = "+category;
				}
				sql +=" and shop.shop_lon BETWEEN " + minlon +" and " + maxlon +
				" and shop.shop_lat BETWEEN " +minlat+"  and "+maxlat + " ORDER BY distance";
				if(page == 0){
					page = 1;
				}
				sql +=" limit "+((page-1)*size)+" ,"+size;
		List<Goods> products = null;//用于保存查询出来的数据
		try {
			connection = getConn();
			statement = connection.createStatement();
			//System.out.println("小分类产品查询的sql语句：" + sql);
			products = new ArrayList<Goods>();
			resultSet = statement.executeQuery(sql);
			//遍历结果集
			while (resultSet.next()) {
				Goods product = new Goods();
				product.setId(resultSet.getString("prodouct_id"));
				product.setCategoryId(resultSet.getString("category_id"));
				product.setShopId(resultSet.getString("shop_id"));
				product.setCityId(resultSet.getString("city_id"));
				product.setTitle(resultSet.getString("prodouct_title"));
				product.setSortTitle(resultSet.getString("prodouct_sort_title"));
				product.setImgUrl(resultSet.getString("prodouct_image"));
				product.setStartTime(resultSet.getString("prodouct_start_time"));
				product.setValue(resultSet.getString("prodouct_value"));
				product.setPrice(resultSet.getString("prodouct_price"));
				product.setRibat(resultSet.getString("prodouct_ribat"));
				product.setBought(resultSet.getString("prodouct_bought"));
				product.setMaxQuota(resultSet.getString("prodouct_maxquota"));
				product.setPost(resultSet.getString("prodouct_post"));
				product.setSoldOut(resultSet.getString("prodouct_soldout"));
				product.setTip(resultSet.getString("prodouct_tip"));
				product.setEndTime(resultSet.getString("prodouct_end_time"));
				product.setDetail(resultSet.getString("prodouct_detail"));
				product.setMinquota(resultSet.getString("prodouct_minquota"));

				Shop shop = new Shop();
				shop.setId(resultSet.getString("shop_id"));
				shop.setName(resultSet.getString("shop_name"));
				shop.setAddress(resultSet.getString("shop_address"));
				shop.setArea(resultSet.getString("shop_area"));
				shop.setOpentime(resultSet.getString("shop_open_time"));
				shop.setTel(resultSet.getString("shop_tel"));
				shop.setLat(resultSet.getString("shop_lat"));
				shop.setLon(resultSet.getString("shop_lon"));
				product.setShop(shop);
				int refund = resultSet.getInt("prodouct_is_refund");
				int overTime = resultSet.getInt("prodouct_is_over_time");

				if (refund == 1) {
					product.setRefund(true);
				} else if (refund == 0) {
					product.setRefund(false);
				}

				if (overTime == 1) {
					product.setOverTime(true);
				} else if (overTime == 0) {
					product.setOverTime(false);
				}
				products.add(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(resultSet, statement, connection);
		}
		return products;
	}

	public int getCountByLBS(String category, double lat, double lon, double minlat, double minlon, double maxlat, double maxlon) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String sql = "select " + " count(*) as count from prodouct,shop where prodouct.shop_id=shop.shop_id ";
		if (category == null || "".equals(category)) {} else if (category.length() < 4) {
			sql += "and category_id = " + category;
		} else {
			sql += "and sub_category_id = " + category;
		}
		sql += " and shop.shop_lon BETWEEN " + minlon + " and " + maxlon + " and shop.shop_lat BETWEEN " + minlat + "  and " + maxlat;
		int count = 0;
		try {
			connection = getConn();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				count = resultSet.getInt("count");
			}
		} catch (Exception e) {} finally {
			close(resultSet, statement, connection);
		}
		return count;
	}
}
