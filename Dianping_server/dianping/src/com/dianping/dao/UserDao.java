package com.dianping.dao;

import com.dianping.enity.User;

public interface UserDao {
	
	User register(String name,String pwd);
	User login(String name,String pwd);
	User social(String name,String pwd);
}
