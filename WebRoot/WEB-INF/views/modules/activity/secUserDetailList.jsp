<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户实名信息表管理</title>
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
		<li class="active"><a href="${ctx}/activity/secUserDetail/">用户实名信息表列表</a></li>
		<shiro:hasPermission name="activity:secUserDetail:edit"><li><a href="${ctx}/activity/secUserDetail/form">用户实名信息表添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="secUserDetail" action="${ctx}/activity/secUserDetail/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>name：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>name</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="activity:secUserDetail:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="secUserDetail">
			<tr>
				<td><a href="${ctx}/activity/secUserDetail/form?id=${secUserDetail.id}">
					${secUserDetail.name}
				</a></td>
				<td>
					<fmt:formatDate value="${secUserDetail.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${secUserDetail.remarks}
				</td>
				<shiro:hasPermission name="activity:secUserDetail:edit"><td>
    				<a href="${ctx}/activity/secUserDetail/form?id=${secUserDetail.id}">修改</a>
					<a href="${ctx}/activity/secUserDetail/delete?id=${secUserDetail.id}" onclick="return confirmx('确认要删除该用户实名信息表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>