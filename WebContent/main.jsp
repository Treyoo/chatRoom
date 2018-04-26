<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.sql.*"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>聊天室主界面</title>
<!-- 导入用于进行Ajax重构的js文件 -->
<script type="text/javascript" src="JS/AjaxRequest.js"></script>
<script type="text/javascript">
/**实例化Ajax对象，实时获取并显示在线人员列表*/
function showOnline(){
	var loader=new net.AjaxRequest("online.jsp?nocache="
			+new Date().getTime(),deal_online,onerror,"GET");
}
/**处理一步请求结果函数。将在线人员显示到online单元格*/
function deal_online() {
	online.innerHTML=this.req.responseText;
}
/**异步请求出错处理函数*/
function onerror() {
	alert("很抱歉，服务器出现错误，当前窗口将关闭！");
	//opener属性是一个可读可写的属性，可返回对创建该窗口的 Window 对象的引用
	window.opener=null;
	window.close();
}

//当页面载入后执行的操作
window.onload=function(){
	showOnline();//显示在线用户
}
</script>
</head>
<body>
你好！${username}。
  这是聊天室主界面（临时）。<br>
<!-- 顶部 -->
<table width="778" height="152" border="0" align="center"
 cellpadding="0" cellspacing="0" background="images/top.jpg">
   <tr>
     <td>&nbsp;</td>
   </tr>
 </table>
 
 <!-- 聊天区域-->
 <table width="778" height="276" border="0" align="center"
 cellpadding="0" cellspacing="0">
   <tr>
     <td width="165" valign="top" bgcolor="#F7FDED" id="online"
     style="padding:5px;">在线人员列表</td>
     <td width="613" height="200px" valign="top" bgcolor="#FFFFFF"
     style="padding:5px;">
       <div style="height:290px;overflow:hidden" id="content">
      	   聊天内容
       </div>
     </td>
   </tr>
 </table>
 
 <!-- 发言区域 -->
 <form action="" name="form1" method="post">
   <table width="778" border="0" align="center"  cellpadding="0"
    cellspacing="0" bordercolor="#D6D3CE" bgcolor="#D1E9AA">
     <tr>
       <td height="30" align="left">&nbsp;</td>
       <td height="37" align="left">
         <input name="from" type="hidden" value=
         "<%=session.getAttribute("username") %>">
         [<%session.getAttribute("username");%>]对<input name="to" type="text"
         value="" size="35" readonly="readonly">
 			表情
 		<select name="face" class="wenbenkuang">
		  <option  value="无表情的">无表情的</option>
		  <option value="微笑着" selected>微笑着</option>
		  <option value="笑呵呵地">笑呵呵地</option>
		  <option value="热情的">热情的</option>
		  <option value="温柔的">温柔的</option>
		  <option value="红着脸">红着脸</option>
		  <option value="幸福的">幸福的</option>
		  <option value="嘟着嘴">嘟着嘴</option>
		  <option value="热泪盈眶的">热泪盈眶的</option>
		  <option value="依依不舍的">依依不舍的</option>
		  <option value="得意的">得意的</option>
		  <option value="神秘兮兮的">神秘兮兮的</option>
		  <option value="恶狠狠的">恶狠狠的</option>
		  <option value="大声的">大声的</option>
		  <option value="生气的">生气的</option>
		  <option value="幸灾乐祸的">幸灾乐祸的</option>
		  <option value="同情的">同情的</option>
		  <option value="遗憾的">遗憾的</option>
		  <option value="正义凛然的">正义凛然的</option>
		  <option value="严肃的">严肃的</option>
		  <option value="慢条斯理的">慢条斯理的</option>
		  <option value="无精打采的">无精打采的</option>
		</select>
		说：悄悄话<input name="isPrivate" type="checkbox" class="noborder"
		id="isPrivate" value="true" onClick="checkIsPrivate()">
		滚屏<input name="scrollScreen" type="checkbox" class="noborder"
		id="scrollScreen" onClick="checkScrollScreen()" value="1" checked>
       </td>
       <td width="163" align="left">&nbsp;&nbsp;字体颜色：
         <select name="color" size="1" class="wenbenkuang" id="select">
	        <option selected>默认颜色</option>
	        <option style="color:#FF0000" value="FF0000">红色热情</option>
	        <option style="color:#0000FF" value="0000ff">蓝色开朗</option>
	        <option style="color:#ff00ff" value="ff00ff">桃色浪漫</option>
	        <option style="color:#009900" value="009900">绿色青春</option>
	        <option style="color:#009999" value="009999">青色清爽</option>
	        <option style="color:#990099" value="990099">紫色拘谨</option>
	        <option style="color:#990000" value="990000">暗夜兴奋</option>
	        <option style="color:#000099" value="000099">深蓝忧郁</option>
	        <option style="color:#999900" value="999900">卡其制服</option>
	        <option style="color:#ff9900" value="ff9900">镏金岁月</option>
	        <option style="color:#0099ff" value="0099ff">湖波荡漾</option>
	        <option style="color:#9900ff" value="9900ff">发亮蓝紫</option>
	        <option style="color:#ff0099" value="ff0099">爱的暗示</option>
	        <option style="color:#006600" value="006600">墨绿深沉</option>
	        <option style="color:#999999" value="999999">烟雨蒙蒙</option>
         </select>
       </td>
       <td width="19" align="left">&nbsp;</td>
     </tr>
     <tr>
       <td width="21" height="30" align="left">&nbsp;</td>
       <td width="575" align="left">
         <input name="content1" type="text" size="70" onKeyDown=
         "if(event.keyCode==13&&event.ctrlKey){send();}">
         <input name="Submit2" type="button" class="btn_blank" value="发送" 
         onClick="send()">
       </td>
       <td align="right"><input type="button" name="button_exit"
       class="btn_orange" value="退出聊天室" onClick="Exit()"> 
       </td>
     </tr>
     <tr>
       <td height="30" align="left">&nbsp;</td>
       <td colspan="2" align="center" class="word_dark">
       All CopyRights&copy;reserved Cui
       </td>
     </tr>
     
   </table>
 </form>
</body>
</html>