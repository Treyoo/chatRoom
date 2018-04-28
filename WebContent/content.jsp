<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*" errorPage=""%>
<!-- 此页面用于显示聊天内容 -->
<%
  request.setCharacterEncoding("UTF-8");
  out.println(request.getAttribute("messages").toString());
%>