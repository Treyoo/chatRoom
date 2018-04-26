<!-- 将保存在集合类UserInfo中的在线人员显示到页面 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@ page import="com.cui.user.UserInfo" %>
<% request.setCharacterEncoding("UTF-8"); %>
<%
//获取用户列表
UserInfo list=UserInfo.getInstance();
Vector vector=list.getList();
int amount=0;
%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td height="32" align="center" class="word_orange">
     	 欢迎来到聊天室！
    </td>
  </tr>
  <tr>
    <td height="23" align="center">
      <a href="#" onclick="set('所有人')">所有人</a><!-- 作用？ -->
    </td>
  </tr>
  <%
    if(vector!=null&&vector.size()>0){
    	String username="";
    	amount=vector.size();
    	for(int i=0;i<amount;i++){
    		username=(String)vector.elementAt(i);
   %>
   <tr>
     <td height="23" align="center">
       <a href="#" onclick="set('<%=username%>')"><%=username%></a>
     </td>
   </tr>
 <%}}%>
  <tr>
    <td height="30" align="center">
    	当前在线[<font color="#FF6600"><%=amount%></font>]人
    </td>
  </tr>
</table>