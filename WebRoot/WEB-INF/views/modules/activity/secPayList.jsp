<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>活动支付管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/activity/secPay/">活动支付列表</a></li>
		<shiro:hasPermission name="activity:secPay:edit"><li><a href="${ctx}/activity/secPay/form">活动支付添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="secPay" action="${ctx}/activity/secPay/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="activity:secPay:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="secPay">
			<tr>
				<td><a href="${ctx}/activity/secPay/form?id=${secPay.id}">
					<fmt:formatDate value="${secPay.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<td>
					${secPay.remarks}
				</td>
				<shiro:hasPermission name="activity:secPay:edit"><td>
    				<a href="${ctx}/activity/secPay/form?id=${secPay.id}">修改</a>
					<a href="${ctx}/activity/secPay/delete?id=${secPay.id}" onclick="return confirmx('确认要删除该活动支付吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>