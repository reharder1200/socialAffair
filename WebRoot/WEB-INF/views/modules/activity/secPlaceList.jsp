<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>创建地铁线路管理</title>
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
		<li class="active"><a href="${ctx}/activity/secPlace/">创建地铁线路列表</a></li>
		<shiro:hasPermission name="activity:secPlace:edit"><li><a href="${ctx}/activity/secPlace/form">创建地铁线路添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="secPlace" action="${ctx}/activity/secPlace/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>id：</label>
				<form:input path="id" htmlEscape="false" maxlength="32" class="input-medium"/>
			</li>
			<li><label>地点名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>parent_id：</label>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>id</th>
				<th>地点名称</th>
				<th>update_date</th>
				<th>remarks</th>
				<shiro:hasPermission name="activity:secPlace:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="secPlace">
			<tr>
				<td><a href="${ctx}/activity/secPlace/form?id=${secPlace.id}">
					${secPlace.id}
				</a></td>
				<td>
					${secPlace.name}
				</td>
				<td>
					<fmt:formatDate value="${secPlace.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${secPlace.remarks}
				</td>
				<shiro:hasPermission name="activity:secPlace:edit"><td>
    				<a href="${ctx}/activity/secPlace/form?id=${secPlace.id}">修改</a>
					<a href="${ctx}/activity/secPlace/delete?id=${secPlace.id}" onclick="return confirmx('确认要删除该创建地铁线路吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>