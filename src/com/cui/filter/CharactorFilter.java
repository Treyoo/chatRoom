package com.cui.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 字符编码过滤器，主要通过在doFilter()方法中指定request和response两个参数的字符集来进行编码处理，使目标资源的字符集支持中文
 */
//采用注解的方式配置过滤器
@WebFilter(
		description = "字符编码过滤器", 
		urlPatterns = { "/*" }, //过滤所有请求
		initParams = { 
				@WebInitParam(name = "encoding", value = "UTF-8", description = "字符编码设为UTF-8")
		})
public class CharactorFilter implements Filter {
	String encoding=null;//字符编码
    public CharactorFilter() {}
    
	/**
	 * 销毁方法
	 */
	public void destroy() {
		encoding=null;
	}

	/**
	 * 过滤处理方法，设置rquest和response的字符编码
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(encoding!=null) {
			request.setCharacterEncoding(encoding);
			response.setContentType("text/html;charset="+encoding);//这里同时设置了发送到客户短的响应的内容类型和字符编码
		}
		//通过FilterChain对象的doFilter()方法将请求传递到下一个过滤器或目标资源，这一步是必须的，不传递将出现错误
		chain.doFilter(request, response);
	}

	/**
	 * 初始化方法,获取初始化参数并保存到encoding成员变量中
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		encoding=fConfig.getInitParameter("encoding");
	}

}
