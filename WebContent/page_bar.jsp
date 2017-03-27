<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分页</title>
</head>
<body>
	<form action="StudentAction" method="post" id="navigatorForm">
		<a href="${pb.url}&pageNumber=1">首页</a>
		<c:if test="${pb.pageNumber>1}">
			<a href="${pb.url }&pageNumber=${pb.pageNumber-1}">上一页</a>
		</c:if>

		<c:if test="${pb.pageNumber<pb.totalPages}">
			<a href="${pb.url }&pageNumber=${pb.pageNumber+1}">下一页</a>
		</c:if>
		<a href="${pb.url }&pageNumber=${pb.totalPages}">末页</a>
	</form>
</body>
</html>