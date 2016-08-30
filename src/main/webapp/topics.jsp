<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page isELIgnored="false" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/jscript/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/jscript/sortable-table-1.js"></script>
<title>日志监控[主题列表]</title>
	<style type="text/css">
		#logTable tr{
			cursor: pointer;
			font-weight: bold;
		}
		table.hovertable {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #999999;
			border-collapse: collapse;
		}
		table.hovertable th {
			background-color:#c3dde0;
			border-width: 1px;
			padding: 8px;
			border-style: solid;
			border-color: #a9c6c9;
		}
		table.hovertable tr {
			background-color:#d4e3e5;
		}
		table.hovertable td {
			border-width: 1px;
			padding: 8px;
			border-style: solid;
			border-color: #a9c6c9;
		}
	</style>
</head>
<body>
	<h2>日志监控</h2>
	<table class="hovertable sortableTable" id="logTable" width="1100">
		<thead>
			<tr><th class="sortableCol" valuetype="date">主题列表</th></tr>
		</thead>
		<tbody></tbody>
	</table>
</body>
<script type="text/javascript">
	var contextPath = '${pageContext.request.contextPath}';
	$(document).ready(function(){
		$.ajax({
			url:contextPath+"/showTopics?time="+new Date(),
			type:"post",
			dataType:"json"
		}).done(function(data,statusText){
			var logList = data ;
			console.info("key size :"+logList.length);
			var oFragment = document.createDocumentFragment();
			for(var i=0;i<data.length;i++){
				$(oFragment).append("<tr><td onclick='getLogInfo(this)' key='"+data[i]+"'><a javascript:void(0);>"+data[i]+"</a></td></tr>");
			}
			$("#logTable>tbody").empty().append(oFragment);
			//
			$("#logTable>tbody>tr").each(function(){
				$(this).mouseover(function(){
					this.style.backgroundColor='#ffff66';
				});
				$(this).mouseout(function(){
					this.style.backgroundColor='#d4e3e5';
				});
			});
		});
	});
	
	function getLogInfo(tdObject){
		var key = $(tdObject).attr("key");
		window.open(contextPath+"/forwardLogList?topic="+key);
	}
	
</script>
</html>