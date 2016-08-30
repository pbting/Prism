<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page isELIgnored="false" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>日志信息</title>
</head>
<body>
	<h2><strong class="color:green;">查询Key</strong> ${requestScope.key}</h2>
	<table width="1120" border="1" cellpadding="1" cellspacing="1">
		<c:forEach var="logInfo" items="${logList}">
			<tr style="border: 1px;">
				<td><font color="red"><b>时间：</b></font><font color="#124567">${logInfo.occurDate}</font></td>
			</tr>
			<tr style="border: 1px;">
				<td><font color="green"><b>日志信息：</b></font> <small>${logInfo.logInfo}</small></td>
			</tr>
		</c:forEach>
	</table>
	
</body>
</html>