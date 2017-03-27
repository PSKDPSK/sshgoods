<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
function go() {
	var pageNumber = $("#pageCode").val();//获取文本框中的当前页码
	if(!/^[1-9]\d*$/.test(pageNumber)) {//对当前页码进行整数校验
		alert('请输入数字！');
		return;
	}
	if(pageNumber > '${pb.totalPages}') {//判断当前页码是否大于最大页
		alert('请输入正确的页码！');
		return;
	}
	location = "${pb.url}&pageNumber=" + pageNumber;
}
</script>
<div class="divBody">
  <div class="divContent">
    <%--上一页 --%>
      <c:choose>
			<c:when test="${pb.pageNumber eq 1 }">
				<span class="spanBtnDisabled">上一页</span>
			</c:when>
			<c:otherwise>
				<a href="${pb.url }&pageNumber=${pb.pageNumber-1}" class="aBtn bold">上一页</a>
			</c:otherwise>
		</c:choose>
		<%-- 计算begin和end --%>
		<%-- 如果总页数<=6，那么显示所有页码，即begin=1 end=${pb.tp} --%>
		<%-- 设置begin=当前页码-2，end=当前页码+3 --%>
		<%-- 如果begin<1，那么让begin=1 end=6 --%>
		<%-- 如果end>最大页，那么begin=最大页-5 end=最大页 --%>
		<%-- 显示页码列表 --%>
		<c:choose>
			<c:when test="${pb.totalPages <= 6 }">
				<c:set var="begin" value="1" />
				<c:set var="end" value="${pb.totalPages }" />
			</c:when>
			<c:otherwise>
				<c:set var="begin" value="${pb.pageNumber-2 }" />
				<c:set var="end" value="${pb.pageNumber + 3}" />
				<c:if test="${begin < 1 }">
					<c:set var="begin" value="1" />
					<c:set var="end" value="6" />
				</c:if>
				<c:if test="${end > pb.totalPages }">
					<c:set var="begin" value="${pb.totalPages-5 }" />
					<c:set var="end" value="${pb.totalPages }" />
				</c:if>
			</c:otherwise>
		</c:choose>
		<c:forEach begin="${begin }" end="${end }" var="i">
			<c:choose>
				<c:when test="${i eq pb.pageNumber }">
					<span class="spanBtnSelect">${i }</span>
				</c:when>
				<c:otherwise>
					<a href="${pb.url }&pageNumber=${i}" class="aBtn">${i}</a>
				</c:otherwise>
			</c:choose>
		</c:forEach>
    <%-- 显示点点点 --%>
     <c:if test="${end < pb.totalPages }">
			<span class="spanApostrophe">...</span>
		</c:if>
		<c:choose>
			<c:when test="${pb.pageNumber eq pb.totalPages }">
				<span class="spanBtnDisabled">下一页</span>
			</c:when>
			<c:otherwise>
				<a href="${pb.url }&pageNumber=${pb.pageNumber+1}" class="aBtn bold">下一页</a>
			</c:otherwise>
		</c:choose>
    <%-- 共N页 到M页 --%>
    <span>共${pb.totalPages }页</span> <span>到</span> <input type="text"
			class="inputPageCode" id="pageCode" value="${pb.pageNumber}" /> <span>页</span>
		<a href="javascript:go();" class="aSubmit">确定</a>
  </div>
</div>