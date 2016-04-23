package com.dianping.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.dianping.dao.CategoryDao;
import com.dianping.enity.Category;
import  java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CategoryDaoImpl extends BaseDao implements CategoryDao {

	public List<Category> getCategoryDao() {
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		List<Category> result=null;
		try{
			connection=getConn();
			statement=connection.createStatement();
			String sql="select category_id,count(category_id) number from prodouct "
					+ "group by category_id order by category_id ";
			resultSet=statement.executeQuery(sql);
			result=new ArrayList<Category>();
			while (resultSet.next()) {
				Category category=new Category();
				category.setCategoryId(resultSet.getString("category_id"));
				category.setCategeryNumber(resultSet.getLong("number"));
				result.add(category);
				
				
			}
		}
		catch(Exception e){
			return null;
		}
		finally {
			close(resultSet, statement, connection);
		}
		return result;
	}

	public long getCategoryTotal() {
		Connection connection=null;
		Statement statement=null;
		ResultSet resultSet=null;
		long sum=0;
		try{
			connection=getConn();
			statement=connection.createStatement();
			String sql="select count(category_id) sum from prodouct";
			resultSet=statement.executeQuery(sql);
			while (resultSet.next()) {
				sum=resultSet.getLong("sum");
			}
		}
		catch(Exception e){
			return -1;
		}
		finally {
			close(resultSet, statement, connection);
		}
		return sum;
	}

}
