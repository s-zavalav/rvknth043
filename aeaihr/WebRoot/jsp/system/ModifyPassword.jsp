<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>密码修改</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function modifyPassword(){
	if (validate()){
		if ($("#newUserPwd").val() != $("#confirmUserPwd").val()){
			writeErrorMsg('两次密码输入不一致，请确认！');
			return;
		}
		showSplash();
		postRequest('form1',{actionType:'confirmOldPwd',onComplete:function(responseText){
			if (responseText != 'success'){
				writeErrorMsg('原密码输入错误，请确认！');
				$("#oldUserPwd").val('');
				$("#newUserPwd").val('');
				$("#confirmUserPwd").val('');
				hideSplash();
			}else{
				postRequest('form1',{actionType:'modifyPassword',onComplete:function(responseText){
					if (responseText == 'success'){
						hideSplash();
						jAlert('修改密码成功,请重新登录系统！');
						parent.logout();
						//resetForm();
					}else{
						writeErrorMsg('修改密码失败，请联系管理员！');
						hideSplash();
					}
				}});
			}
		}});
	}
}
function resetForm(){
	doSubmit({doValidate:'false',actionType:'prepareDisplay'});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="modifyPassword()"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="resetForm()"><input value="&nbsp;" type="button" class="closeImgBtn" title="重置" />重置</td>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>用户名</th>
	<td><input id="userCode" label="用户名" name="userCode" type="text" readonly="readonly" value="<%=pageBean.inputValue("userCode")%>" size="24" class="text" /></td>
</tr>
<tr>
	<th width="100" nowrap>旧密码</th>
	<td><input id="oldUserPwd" label="旧密码" name="oldUserPwd" type="password" value="<%=pageBean.inputValue("oldUserPwd")%>" size="24" class="text" /></td>
</tr>
<tr>
	<th width="100" nowrap>新密码</th>
	<td><input id="newUserPwd" label="新密码" name="newUserPwd" type="password" value="<%=pageBean.inputValue("newUserPwd")%>" size="24" class="text" /></td>
</tr>
<tr>
	<th width="100" nowrap>重复新密码</th>
	<td><input id="confirmUserPwd" label="重复新密码" name="confirmUserPwd" type="password" value="<%=pageBean.inputValue("confirmUserPwd")%>" size="24" class="text" /></td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
</form>
<script language="javascript">
requiredValidator.add("oldUserPwd");
requiredValidator.add("newUserPwd");
requiredValidator.add("confirmUserPwd");
$(function(){
	document.getElementById('oldUserPwd').value=''; 
});
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
