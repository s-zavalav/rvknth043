<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>基本信息</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script type="text/javascript">
function controlUpdateBtn(stateResult){
	if(stateResult =='drafe'){
		<%if (!pageBean.getBoolValue("hasRight")){%>
		enableButton("editImgBtn");
		enableButton("approveImgBtn");
		disableButton("revokeApproval");
	<%}%>
		enableButton("detailImgBtn");
		enableButton("deleteImgBtn");
	}
	if(stateResult =='approved'){
		<%if (!pageBean.getBoolValue("hasRight")){%>
		enableButton("editImgBtn");
		disableButton("approveImgBtn");
		enableButton("revokeApproval");
		<%}%>
		enableButton("detailImgBtn");
		disableButton("deleteImgBtn");
		
	}
}
function revokeApproval(){
	if (actionType != insertRequestActionValue && !isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	doSubmit({actionType:'revokeApproval'});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolBar" border="0" cellpadding="0" cellspacing="1">
<tr>
   <aeai:previlege code="create"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="doRequest('insertRequest')"><input id="createImgBtn" value="&nbsp;" title="新增" type="button" class="createImgBtn" />新增</td></aeai:previlege>
   <aeai:previlege code="edit"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="doRequest('updateRequest')"><input id="editImgBtn" value="&nbsp;" title="编辑" type="button" class="editImgBtn" />编辑</td></aeai:previlege>
   <aeai:previlege code="approve"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="R" align="center" onclick="doRequest('approveRequest')"><input id="approveImgBtn" value="&nbsp;" title="核准" type="button" class="approveImgBtn" />核准</td></aeai:previlege>
   <aeai:previlege code="revokeApproval"><td  onmouseover="onMover(this);" onmouseout="onMout(this);"   align="center" class="bartdx" onclick="revokeApproval()" ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td></aeai:previlege>
   <aeai:previlege code="detail"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="doRequest('viewDetail')"><input id="detailImgBtn" value="&nbsp;" title="查看" type="button" class="detailImgBtn" />查看</td></aeai:previlege>
   <aeai:previlege code="delete"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doDelete($('#'+rsIdTagId).val());"><input id="deleteImgBtn" value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td></aeai:previlege>
   <aeai:previlege code="amountSet"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="U" align="center" onclick="openSalarySetBox()"><input id="salaryImgBtn" value="&nbsp;" title="工资额度设定" type="button" class="salaryImgBtn"/>额度设定</td></aeai:previlege>
</tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable"><tr><td>
&nbsp;状态<select id="EMP_STATE" label="状态" name="EMP_STATE" class="select" onchange="doQuery()"><%=pageBean.selectValue("EMP_STATE")%></select>
&nbsp;性别<select id="empSex" label="性别" name="empSex" class="select" onchange="doQuery()"><%=pageBean.selectValue("empSex")%></select>

&nbsp;姓名<input id="empName" label="姓名" name="empName" type="text" value="<%=pageBean.inputValue("empName")%>" size="24" class="text" ondblclick="emptyText('empName')" /><img id="userIdSelectImage" src="images/sta.gif" width="16" height="16" onclick="openUserIdBox()" />

&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
</td></tr></table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="基本信息.csv"
retrieveRowsCallback="process" xlsFileName="基本信息.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="${ec_rd == null ?15:ec_rd}"
listWidth="100%" 
height="390px"
>
<ec:row styleClass="odd" ondblclick="clearSelection();doRequest('viewDetail')" oncontextmenu="selectRow(this,{EMP_ID:'${row.EMP_ID}'});controlUpdateBtn('${row.EMP_STATE}');refreshConextmenu()" onclick="selectRow(this,{EMP_ID:'${row.EMP_ID}'});controlUpdateBtn('${row.EMP_STATE}');">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="EMP_CODE" title="编号"   />
	<ec:column width="100" property="EMP_NAME" title="姓名"   />
	<ec:column width="100" property="EMP_SEX" title="性别"   mappingItem="EMP_SEX"/>
	<ec:column width="100" property="EMP_BIRTHDAY" title="出生日期" cell="date" format="yyyy-MM-dd" />
	<ec:column width="100" property="EMP_TEL" title="电话"   />
	<ec:column width="100" property="EMP_EMAIL" title="邮箱"   />
	<ec:column width="100" property="EMP_NOW_DEPT_NAME" title="部门"   />
	<ec:column width="100" property="EMP_NOW_JOB" title="岗位"   />
	<ec:column width="100" property="EMP_EDUCATION" title="学历"   mappingItem="EMP_EDUCATION"/>
	<ec:column width="100" property="EMP_STATE" title="状态" mappingItem="EMP_STATE"/>
</ec:row>
</ec:table>
<input type="hidden" name="EMP_ID" id="EMP_ID" value="" />
<input type="hidden" name="actionType" id="actionType" />
<script language="JavaScript">
setRsIdTag('EMP_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
var userIdBox;
function openUserIdBox(){
	var handlerId = "UserListSelectList"; 
	if (!userIdBox){
		userIdBox = new PopupBox('userIdBox','请选择人员      ',{size:'normal',width:'300',top:'2px'});
	}
	var url = 'index?'+handlerId+'&targetId=empName&targetName=empName';
	userIdBox.sendRequest(url);
}
var salarySetBox;
function openSalarySetBox(){
	var handlerId = "SalarySet"; 
	if (!userIdBox){
		salarySetBox = new PopupBox('salarySetBox','额度设定 ',{size:'normal',height:'200',width:'400',top:'2px'});
	}
	var url = 'index?'+handlerId;
	salarySetBox.sendRequest(url);
}
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
