<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>活动应用管理</title>
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
		<li class="active"><a href="${ctx}/activity/secActivityApply/">活动应用列表</a></li>
		<shiro:hasPermission name="activity:secActivityApply:edit"><li><a href="${ctx}/activity/secActivityApply/form">活动应用添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="secActivityApply" action="${ctx}/activity/secActivityApply/" method="post" class="breadcrumb form-search">
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
				<shiro:hasPermission name="activity:secActivityApply:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="secActivityApply">
			<tr>
				<td><a href="${ctx}/activity/secActivityApply/form?id=${secActivityApply.id}">
					<fmt:formatDate value="${secActivityApply.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</a></td>
				<td>
					${secActivityApply.remarks}
				</td>
				<shiro:hasPermission name="activity:secActivityApply:edit"><td>
    				<a href="${ctx}/activity/secActivityApply/form?id=${secActivityApply.id}">修改</a>
					<a href="${ctx}/activity/secActivityApply/delete?id=${secActivityApply.id}" onclick="return confirmx('确认要删除该活动应用吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>