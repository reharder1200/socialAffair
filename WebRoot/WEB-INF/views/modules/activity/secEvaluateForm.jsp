<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>活动管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/activity/secEvaluate/">活动列表</a></li>
		<li class="active"><a href="${ctx}/activity/secEvaluate/form?id=${secEvaluate.id}">活动<shiro:hasPermission name="activity:secEvaluate:edit">${not empty secEvaluate.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="activity:secEvaluate:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="secEvaluate" action="${ctx}/activity/secEvaluate/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">activity_id：</label>
			<div class="controls">
				<form:input path="activityId" htmlEscape="false" maxlength="100" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">user_id：</label>
			<div class="controls">
				<sys:treeselect id="user" name="user.id" value="${secEvaluate.user.id}" labelName="user.name" labelValue="${secEvaluate.user.name}"
					title="用户" url="/sys/office/treeData?type=3" cssClass="" allowClear="true" notAllowSelectParent="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">content：</label>
			<div class="controls">
				<form:textarea path="content" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">activity_score：</label>
			<div class="controls">
				<form:input path="activityScore" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">organizer_score：</label>
			<div class="controls">
				<form:input path="organizerScore" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">is_show：</label>
			<div class="controls">
				<form:input path="isShow" htmlEscape="false" maxlength="2" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">show_order：</label>
			<div class="controls">
				<form:input path="showOrder" htmlEscape="false" maxlength="10" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">remarks：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="500" class="input-xxlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="activity:secEvaluate:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>