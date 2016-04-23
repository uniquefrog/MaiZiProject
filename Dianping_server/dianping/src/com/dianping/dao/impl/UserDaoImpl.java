package com.dianping.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.dianping.dao.UserDao;
import com.dianping.enity.User;

public class UserDaoImpl extends BaseDao implements UserDao {

    public User register(String name, String pwd) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
	        connection = getConn();
	        statement = connection.createStatement();
	        String sqlForCheck = "select * from user where user_name = '"+name+"'";
	        resultSet = statement.executeQuery(sqlForCheck);
	        if(resultSet.next()){
	        	return null;
	        }
	        String sqlForInsert = "insert into user (user_name,user_login_pwd) values('"+name+"','"+pwd+"')";
	        statement.execute(sqlForInsert);
	        resultSet = statement.executeQuery(sqlForCheck);
	        if(resultSet.next()){
	        	User user = new User();
	        	user.setId(resultSet.getString("user_id"));
	        	user.setName(resultSet.getString("user_name"));
				user.setLoginPwd(resultSet.getString("user_login_pwd"));
				user.setPayPwd(resultSet.getString("user_pay_pwd"));
				user.setTel(resultSet.getString("user_tel"));
				return user;
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	close(resultSet, statement, connection);
        }
	    return null;
    }

    public User login(String name, String pwd) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
	        connection = getConn();
	        statement = connection.createStatement();
	        String sqlForCheck = "select * from user where user_name = '"+name+"' and user_login_pwd = '"+pwd+"'";
	        resultSet = statement.executeQuery(sqlForCheck);
	        if(resultSet.next()){
	        	User user = new User();
	        	user.setId(resultSet.getString("user_id"));
	        	user.setName(resultSet.getString("user_name"));
				user.setLoginPwd(resultSet.getString("user_login_pwd"));
				user.setPayPwd(resultSet.getString("user_pay_pwd"));
				user.setTel(resultSet.getString("user_tel"));
				return user;
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	close(resultSet, statement, connection);
        }
	    return null;
    }

    public User social(String name, String pwd) {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
	        connection = getConn();
	        statement = connection.createStatement();
	        String sqlForCheck = "select * from user where user_name = '"+name+"' and user_login_pwd = '"+pwd+"'";
	        resultSet = statement.executeQuery(sqlForCheck);
	        if(resultSet.next()){
	        	User user = new User();
	        	user.setId(resultSet.getString("user_id"));
	        	user.setName(resultSet.getString("user_name"));
				user.setLoginPwd(resultSet.getString("user_login_pwd"));
				user.setPayPwd(resultSet.getString("user_pay_pwd"));
				user.setTel(resultSet.getString("user_tel"));
				return user;
	        }
	        String sqlForInsert = "insert into user (user_name,user_login_pwd) values('"+name+"','"+pwd+"')";
	        statement.execute(sqlForInsert);
	        resultSet = statement.executeQuery(sqlForCheck);
	        if(resultSet.next()){
	        	User user = new User();
	        	user.setId(resultSet.getString("user_id"));
	        	user.setName(resultSet.getString("user_name"));
				user.setLoginPwd(resultSet.getString("user_login_pwd"));
				user.setPayPwd(resultSet.getString("user_pay_pwd"));
				user.setTel(resultSet.getString("user_tel"));
				return user;
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }finally{
        	close(resultSet, statement, connection);
        }
	    return null;
    }}
