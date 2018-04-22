package com.cui.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
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
//			this.getMessages(request, response);
		}else if(action.equals("sendMessage")) {//发送聊天信息
//			this.sendMessage();
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
		boolean flag=true;//标记是否登录
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
}
