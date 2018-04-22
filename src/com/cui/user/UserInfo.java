package com.cui.user;

import java.util.Vector;

/**
 * 已登录用户信息，实际是使用Vector保存了所有已登录用户的用户名
 */
public class UserInfo {
	private static UserInfo user=new UserInfo();
	private Vector<String> vector=null;
	//利用private调用构造函数，防止外界产生新的instance对象
	public UserInfo() {
		this.vector=new Vector<String>();
	}
	//外界使用的instance对象
	public static UserInfo getInstance() {
		return user;
	}
	//增加用户
	public boolean addUser(String user) {
		if(user!=null) {
			this.vector.add(user);
			return true;
		}else {
			return false;
		}
	}
	//获取用户列表
	public Vector<String> getList() {
		return vector;
	}
	//移除用户
	public void removeUser(String user) {
		if(user!=null) {
			vector.remove(user);
		}
	}
}
