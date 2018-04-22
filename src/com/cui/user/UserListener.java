package com.cui.user;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
/**
 * 用于监听上线和下线的用户。
 */
/*原理：如果一个对象实现了HttpSessionBindingListener接口，当这个对象被绑定到Session中或者从session中被删除时，
  Servlet容器会通知这个对象，而这个对象在接收到通知后，可以做一些初始化或清除状态的操作。*/
public class UserListener implements HttpSessionBindingListener {
	/**当前上线或者下线的用户*/
	private String user;
	private UserInfo container=UserInfo.getInstance();
	public UserListener() {
		user="";
	}
	/**获取当前上线或者下线的用户*/
	public String getUser() {
		return user;
	}
	/**设置当前上线或者下线的用户*/
	public void setUser(String user) {
		this.user = user;
	}
	@Override
	//当Session有对象加入（即有人登录）时执行的方法
	public void valueBound(HttpSessionBindingEvent arg0) {
		System.out.println("上线用户："+this.user);
	}

	@Override
	//当Session有对象移除（即有人退出）时执行的方法
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		System.out.println("下线用户："+this.user);

	}

}
