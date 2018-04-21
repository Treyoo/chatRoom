package com.cui.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理页面请求
 */
@WebServlet("/MessagesAction")
public class MessagesAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);//调用doPost()方法
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//根据request中的action参数执行相应的方法
		String action=request.getParameter("action");
		if(action.equals("getMessages")) {//读取聊天信息
//			this.getMessages(request, response);
		}else if(action.equals("sendMessage")) {//发送聊天信息
//			this.sendMessage();
		}else if(action.equals("loginRoom")) {//登录
//			this.loginRoom();
		}else if(action.equals("exitRoom")) {//退出
//			this.exitRoom();
		}
	}

}
