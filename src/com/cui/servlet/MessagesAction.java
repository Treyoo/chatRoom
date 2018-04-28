package com.cui.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.cui.user.UserInfo;
import com.cui.user.UserListener;

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
			this.getMessages(request, response);
		}else if(action.equals("sendMessage")) {//发送聊天信息
			this.sendMessage(request,response);
		}else if(action.equals("loginRoom")) {//登录
			this.loginRoom(request,response);
		}else if(action.equals("exitRoom")) {//退出
//			this.exitRoom();
		}
	}

	/**
	 * 登录聊天室
	 */
	public void loginRoom(HttpServletRequest request, HttpServletResponse response) {
		//response.setContentType("text/html;charset=UTF-8");
		HttpSession session=request.getSession();
		String username=request.getParameter("username");
		UserInfo user=UserInfo.getInstance();
		session.setMaxInactiveInterval(600);//设置Session的过期时间为10分钟
		Vector<String> vector=user.getList();//获取用户列表
		boolean flag=true;//标记是否登录成功
		//判断用户是否登录
		if(vector!=null&&vector.size()>0) {
			if(vector.contains(username)) {
				try {
					PrintWriter out=response.getWriter();
					out.println("<script language='javascript'>alert('该用户已经登录');"
							+ "window.location.href='index.jsp';</script>");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				flag=false;
			}
		}
		//若用户成功登录，保存用户信息
		if(flag) {
			UserListener ul=new UserListener();
			ul.setUser(username);//将登录的用户名告诉监听对象
			user.addUser(ul.getUser());//将当前登录的用户名添加到已登录用户中
			session.setAttribute("user", ul);//将ul对象绑定到Session中
			session.setAttribute("username", username);//保存当前登录的用户名到Session中
			String loginTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			session.setAttribute("loginTime", loginTime);//保存登录时间到Session中
			/*****************系统公告登录用户（xml文件实现）**************/
			String newTime=new SimpleDateFormat("yyyyMMdd").format(new Date());
			String fileURL=request.getSession().getServletContext()
					.getRealPath("xml/"+newTime+".xml");//一天创建一个xml文件
			System.out.println("XMLFileURL="+fileURL);
			createFile(fileURL);//判断XML文件是否存在，如果不存在则创建
			//获取当前用户
			SAXReader reader=new SAXReader();
//			reader.setEncoding("GB2312");//设置文件编码
			try {
				//获取XML文件对应的XML文档对象
				Document feedDoc=reader.read(new File(fileURL));
				Element root=feedDoc.getRootElement();
				Element messages=root.element("messages");
				//创建子结点message
				Element message=messages.addElement("message");
				//添加message的子结点
				message.addElement("from").setText("[系统公告]");
				message.addElement("face").setText("");
				message.addElement("to").setText("");
				message.addElement("content").addCDATA("<font color='gray'>"
				+username+"进入了聊天室。</font>");
				String sendTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				message.addElement("sendTime").setText(sendTime);
				message.addElement("isPrivate").setText("false");
				//跳转到登录成功页面
				request.getRequestDispatcher("login_ok.jsp").forward(request, response);
				OutputFormat format=OutputFormat.createPrettyPrint();//输出格式美化
				//获取XML文件输出流
				XMLWriter writer=new XMLWriter(new FileOutputStream(fileURL),format);
				writer.write(feedDoc);//向流写入数据
				writer.flush();
				writer.close();
			} catch (DocumentException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	/**自定义方法，判断fileURL对应的文件是否存在，不存在则创建，存在则无操作*/
	public void createFile(String fileURL){
		File file=new File(fileURL);//创建一个引用文件的对象
		if(!file.exists()) {//若文件不存在，创建之
			try {
				file.createNewFile();
				//定义写入xml文件的内容
				String dataStr="<?xml version=\"1.0\""
						+ " encoding=\"UTF-8\"?>\r\n";
				dataStr+="<chat>\r\n";
				dataStr+="<messages></messages>\r\n";
				dataStr+="</chat>\r\n";
				byte[]content=dataStr.getBytes();
				FileOutputStream fout=new FileOutputStream(file);//获取文件的输出流
				fout.write(content);//将数据写入输出流
				fout.flush();//刷新输出流缓冲区
				fout.close();//关闭输出流
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将用户发送的聊天消息保存到xml文件
	 */
	public void sendMessage(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		Random random=new Random();
		/**获取发送的内容***/
		String from=request.getParameter("from");
		String face=request.getParameter("face");
		String to=request.getParameter("to");
		String color=request.getParameter("color");
		String content=request.getParameter("content");
		String isPrivate=request.getParameter("isPrivate");
		String sendTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		/************************开始添加聊天信息到xml文件*********************/
		String newTime=new SimpleDateFormat("yyyyMMdd").format(new Date());//保存文件的年月日
		String fileURL=request.getSession().getServletContext().getRealPath("xml/"+newTime+".xml");
		createFile(fileURL);
		try {
			SAXReader reader=new SAXReader();
			Document feedDoc=reader.read(new File(fileURL));
			Element root=feedDoc.getRootElement();
			Element messages=root.element("messages");
			//添加一个<message>子结点
			Element message=messages.addElement("message");
			//将聊天信息分别保存到<message>结点下对应的子结点里
			message.addElement("from").setText(from);
			message.addElement("face").setText(face);
			message.addElement("to").setText(to);
			message.addElement("content").addCDATA("<font color='"+color+"'>"+content+"</font>");
			message.addElement("isPrivate").setText(isPrivate);
			message.addElement("sendTime").setText(sendTime);
			//创建OutputFormat对象
			OutputFormat format=OutputFormat.createPrettyPrint();//输出格式美化
			//将请求转发到本类的getMessages()方法
			request.getRequestDispatcher("MessagesAction?action=getMessages&nocache="
			+random.nextInt(10000)).forward(request, response);
			//获取XML文件输出流
			XMLWriter writer=new XMLWriter(new FileOutputStream(fileURL),format);
			writer.write(feedDoc);//向流写入数据
			writer.flush();
			writer.close();
			/**********************添加结束******************************/
		} catch (DocumentException | ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 从xml文件读取聊天信息
	 * @param request
	 * @param response
	 */
	public void getMessages(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=UTF-8");
		String newTime=new SimpleDateFormat("yyyyMMdd").format(new Date());//当前年月日
		String fileURL=request.getSession().getServletContext().getRealPath("xml/"+newTime+".xml");
		createFile(fileURL);
		/***************开始解析保存聊天内容的xml文件********************/
		try {
			SAXReader reader=new SAXReader();
			Document feedDoc = reader.read(new File(fileURL));
			Element root=feedDoc.getRootElement();
			Element messagesNode=root.element("messages");
			//获取所有<message>子结点
			Iterator<Element> items=messagesNode.elementIterator("message");
			String messages="";
			//获取当前用户
			HttpSession session=request.getSession();
			String userName="";
			if(session.getAttribute("username")==null) {
				//保存标记信息，表示用户账户已经过期
				request.setAttribute("messages", "error");
			}else {
				userName=session.getAttribute("username").toString();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				while(items.hasNext()) {
					Element item=items.next();
					String sendTime=item.elementText("sendTime");//获取发言时间					
					if(sdf.parse(sendTime).after(sdf.parse(session.getAttribute("loginTime").toString()))
							||sendTime.equals(session.getAttribute("loginTime").toString())) {//只获取登录后的聊天信息
						String from=item.elementText("from");
						String face=item.elementText("face");
						String to=item.elementText("to");
						String content=item.elementText("content");
						boolean isPrivate=Boolean.valueOf(item.elementText("isPrivate"));
						/***构造聊天内容字符串***/
						if(isPrivate) {//私聊的内容
							if(userName.equals(to)||userName.equals(from)) {
								messages+="<font color='red'>[私人对话]</font><font color='blue'><b>"
										+from+"</b></font><font color='#CC0000'>"+face+"</font>对"
										+"<font color='green'>["+to+"]</font>说："+content+"&nbsp;"
												+ "<font color='gray'>["+sendTime+"]</font><br>";
							}
						}else if((from.equals("[系统公告]"))) {//系统公告信息
							messages+="[系统公告]："+content+"&nbsp;<font color='gray'>["+sendTime+"]</font><br>";
						}else {//群聊的内容
							messages+="<font color='blue'><b>"+from+"</b></font><font color='#CC0000'>"
									+face+"</font>对<font color='green'>["+to+"]</font>说："+content
									+"&nbsp;<font color='gray'>["+sendTime+"]</font><br>";
						}
					}
				}
				request.setAttribute("messages", messages);//保存从xml获取的聊天信息到request中
			}
			//将request转发到content.jsp
			request.getRequestDispatcher("content.jsp").forward(request, response);
		} catch (DocumentException | ServletException | IOException | ParseException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
