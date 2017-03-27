<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>left</title>
    <base target="body"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/zTree/zTreeStyle/zTreeStyle.css">
</head>
<body> 
	 <ul id="categoryTree" class="ztree"></ul> 
	<script type="text/javascript" src="${pageContext.request.contextPath}/zTree/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/zTree/jquery.ztree.core.min.js"></script>
	<script type="text/javascript">
		var zTreeObj;
		var setting = {
			data : {
				simpleData : {
					enable : true,
					idKey:"cid",
					pIdKey:"pid",
					rootPid:null
				},
				key:{
					name:"cname"
				}
			},
			async : {
				enable : true,
				url : "${pageContext.request.contextPath}/listCats",
				autoParam : ["cid"],
			},
			view: {
				fontCss : {color:" #c99979"}
			}
		};
		var zNodes = [];
		$(function() {
			zTreeObj = $.fn.zTree.init($("#categoryTree"), setting, zNodes);
		});
	</script>
</body>
</html>
