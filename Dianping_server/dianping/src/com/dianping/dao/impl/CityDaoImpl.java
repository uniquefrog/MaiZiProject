package com.dianping.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dianping.dao.CItyDao;
import com.dianping.enity.City;

public class CityDaoImpl extends BaseDao implements CItyDao {

	public List<City> getCity() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		List<City> cities = null;
		try {
			connection = getConn();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("select * from city order by city_sortkey");
			cities = new ArrayList<City>();
			//将结果集里的数据循环放列表里
			while (resultSet.next()) {
				City city = new City();
				city.setId(resultSet.getString("city_id"));
				city.setName(resultSet.getString("city_name"));
				city.setSortKey(resultSet.getString("city_sortkey"));
				cities.add(city);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(resultSet, statement, connection);
		}
		return cities;
	}
}
