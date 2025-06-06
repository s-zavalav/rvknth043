package com.agileai.hr.module.information.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.core.MasterSubService;
import com.agileai.hotweb.controller.core.MasterSubEditMainHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.information.service.HrEmployeeManage;

public class HrEmployeeManageEditHandler extends MasterSubEditMainHandler {
	public HrEmployeeManageEditHandler() {
		super();
		this.listHandlerClass = HrEmployeeManageListHandler.class;
		this.serviceId = buildServiceId(HrEmployeeManage.class);
		this.baseTablePK = "EMP_ID";
		this.defaultTabId = "_base";
	}

	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		if ("insert".equals(operaType)) {
			setAttribute("doDetail", true);
			setAttribute("onlyRead", "");
		}
		if ("update".equals(operaType)) {
			if (isReqRecordOperaType(operaType)) {
				DataRow record = getService().getMasterRecord(param);
				if (record.get("EMP_STATE").equals("drafe")) {
					setAttribute("doDetail", true);
					setAttribute("doApprove", true);
					setAttribute("onlyRead", "readonly");
				}
				if(record.get("EMP_STATE").equals("approved")){
					setAttribute("doDetail", false);
					setAttribute("doApprove", false);
					setAttribute("doRevokeApprove", true);
					setAttribute("onlyRead", "");
				} 
				this.setAttributes(record);
			}
		}
		if ("approve".equals(operaType)) {
			setAttribute("isApprove", true);
			setAttribute("doApprove", true);
			if (!isReqRecordOperaType(operaType)) {
				DataRow record = getService().getMasterRecord(param);
				this.setAttributes(record);
			}
		}
		if ("detail".equals(operaType)) {
			setAttribute("onlyRead", "readonly");
			if (isReqRecordOperaType(operaType)) {
				DataRow record = getService().getMasterRecord(param);
				if (record.get("EMP_STATE").equals("drafe")) {
					setAttribute("doDetail", true);
					setAttribute("doApprove", true);
				}
				if(record.get("EMP_STATE").equals("approved")){
					setAttribute("doDetail", false);
					setAttribute("doApprove", false);
					setAttribute("doRevokeApprove", true);
				} 
				this.setAttributes(record);
			}
		}
		String currentSubTableId = param.get("currentSubTableId", defaultTabId);
		if (!currentSubTableId.equals(MasterSubService.BASE_TABLE_ID)) {
			String subRecordsKey = currentSubTableId + "Records";
			if (!this.getAttributesContainer().containsKey(subRecordsKey)) {
				List<DataRow> subRecords = getService().findSubRecords(
						currentSubTableId, param);
				this.setAttribute(currentSubTableId + "Records", subRecords);
			}
		}
		this.setAttribute("currentSubTableId", currentSubTableId);
		this.setAttribute("currentSubTableIndex",
				getTabIndex(currentSubTableId));
		String operateType = param.get(OperaType.KEY);
		this.setOperaType(operateType);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

	protected void processPageAttributes(DataParam param) {
		setAttribute("EMP_SEX", FormSelectFactory.create("USER_SEX")
				.addSelectedValue(getOperaAttributeValue("EMP_SEX", "M")));
		setAttribute("EMP_PARTY", FormSelectFactory.create("EMP_PARTY")
				.addSelectedValue(getOperaAttributeValue("EMP_PARTY", "")));
		setAttribute(
				"EMP_MARITAL_STATUS",
				FormSelectFactory
						.create("EMP_MARITAL_STATUS")
						.addSelectedValue(
								getOperaAttributeValue("EMP_MARITAL_STATUS", "")));
		setAttribute("EMP_EDUCATION", FormSelectFactory.create("EMP_EDUCATION")
				.addSelectedValue(getOperaAttributeValue("EMP_EDUCATION", "")));
		setAttribute("EMP_STATE", FormSelectFactory.create("EMP_STATE")
				.addSelectedValue(getOperaAttributeValue("EMP_STATE", "drafe")));
		setAttribute(
				"EMP_WORK_STATE",
				FormSelectFactory.create("EMP_WORK_STATE").addSelectedValue(
						getOperaAttributeValue("EMP_WORK_STATE", "")));
		setAttribute(
				"EMP_PARTICIPATE_SALARY",
				FormSelectFactory.create("BOOL_DEFINE").addSelectedValue(
						getOperaAttributeValue("EMP_PARTICIPATE_SALARY", "Y")));
		setAttribute(
				"EMP_PAY_INSURE",
				FormSelectFactory.create("BOOL_DEFINE").addSelectedValue(
						getOperaAttributeValue("EMP_PAY_INSURE", "N")));		
		BigDecimal empMoney = new BigDecimal("0.00");
		if (this.getAttribute("EMP_PROBATION") == null) {
			this.setAttribute("EMP_PROBATION", empMoney);
		}
		if (this.getAttribute("EMP_BASIC") == null) {
			this.setAttribute("EMP_BASIC", empMoney);
		}
		if (this.getAttribute("EMP_PERFORMANCE") == null) {
			this.setAttribute("EMP_PERFORMANCE", empMoney);
		}
		if (this.getAttribute("EMP_SUBSIDY") == null) {
			this.setAttribute("EMP_SUBSIDY", empMoney);
		}
		if (this.getAttribute("EMP_TAX") == null) {
			this.setAttribute("EMP_TAX", empMoney);
		}
		if (this.getAttribute("EMP_INSURE") == null) {
			this.setAttribute("EMP_INSURE", empMoney);
		}
		if (this.getAttribute("EMP_ALLOWANCE") == null) {
			this.setAttribute("EMP_ALLOWANCE", empMoney);
		}
		if (this.getAttribute("EMP_HOUSING_FUND") == null) {
			this.setAttribute("EMP_HOUSING_FUND", empMoney);
		}
		if (this.getAttribute("EMP_ANNUAL_LEAVE_DAYS") == null) {
			this.setAttribute("EMP_ANNUAL_LEAVE_DAYS", 0);
		}
		
	}

	@PageAction
	public ViewRenderer approve(DataParam param) {
		doSaveMasterRecordAction(param);
		param.put("EMP_STATE", "approved");
		String guId = KeyGenerator.instance().genKey();
		param.put("GU_ID",guId);
		String responseText = getService().approveRecord(param);
		return new AjaxRenderer(responseText);
	}

	protected String[] getEntryEditFields(String currentSubTableId) {
		List<String> temp = new ArrayList<String>();

		return temp.toArray(new String[] {});
	}

	protected String getEntryEditTablePK(String currentSubTableId) {
		HashMap<String, String> primaryKeys = new HashMap<String, String>();
		primaryKeys.put("HrEducation", "EDU_ID");
		primaryKeys.put("HrExperience", "EXP_ID");
		primaryKeys.put("HrWorkPerformance", "PER_ID");

		return primaryKeys.get(currentSubTableId);
	}

	protected String getEntryEditForeignKey(String currentSubTableId) {
		HashMap<String, String> foreignKeys = new HashMap<String, String>();
		foreignKeys.put("HrEducation", "EMP_ID");
		foreignKeys.put("HrExperience", "EMP_ID");
		foreignKeys.put("HrWorkPerformance", "EMP_ID");

		return foreignKeys.get(currentSubTableId);
	}
	public ViewRenderer doSaveMasterRecordAction(DataParam param) {
		String operateType = param.get(OperaType.KEY);
		String responseText = "fail";
		if (OperaType.CREATE.equals(operateType)) {
			String empCode = param.get("EMP_CODE");
			DataParam repeatParam = new DataParam("EMP_CODE", empCode);
			DataRow record = getService().getMasterRecord(repeatParam);
			if (record != null && record.size() > 0) {
				responseText = "repeat";
			} else {
				param.put("EMP_CREATE_TIME", new Date());
				getService().createMasterRecord(param);
				responseText = param.get(baseTablePK);
			}
		} else if (OperaType.UPDATE.equals(operateType)) {
				if(param.get("EMP_BASIC")==""){
					param.put("EMP_BASIC", "0.00");
				} 
				if(param.get("EMP_PERFORMANCE")==""){
					param.put("EMP_PERFORMANCE", "0.00");
				} 
				if(param.get("EMP_SUBSIDY")==""){
					param.put("EMP_SUBSIDY", "0.00");
				}
				if(param.get("EMP_TAX")==""){
					param.put("EMP_TAX", "0.00");
				}
				if(param.get("EMP_INSURE")==""){
					param.put("EMP_INSURE", "0.00");
				} 
				if(param.get("EMP_ALLOWANCE")==""){
					param.put("EMP_ALLOWANCE", "0.00");
				}
				if(param.get("EMP_ANNUAL_LEAVE_DAYS")==""){
					param.put("EMP_ANNUAL_LEAVE_DAYS", "0");
				}
				getService().updateMasterRecord(param);
				saveSubRecords(param);
				responseText = param.get(baseTablePK);
		}else if (OperaType.DETAIL.equals(operateType)) {
			if(param.get("EMP_BASIC")==""){
				param.put("EMP_BASIC", "0.00");
			}
			if(param.get("EMP_PERFORMANCE")==""){
				param.put("EMP_PERFORMANCE", "0.00");
			}
			if(param.get("EMP_SUBSIDY")==""){
				param.put("EMP_SUBSIDY", "0.00");
			}
			if(param.get("EMP_TAX")==""){
				param.put("EMP_TAX", "0.00");
			}
			if(param.get("EMP_INSURE")==""){
				param.put("EMP_INSURE", "0.00");
			}
			if(param.get("EMP_ALLOWANCE")==""){
				param.put("EMP_ALLOWANCE", "0.00");
			}
			if(param.get("EMP_ANNUAL_LEAVE_DAYS")==""){
				param.put("EMP_ANNUAL_LEAVE_DAYS", "0");
			}
			getService().updateMasterRecord(param);
			saveSubRecords(param);
			responseText = param.get(baseTablePK);
		}
		return new AjaxRenderer(responseText);
	}
	protected HrEmployeeManage getService() {
		return (HrEmployeeManage) this.lookupService(this.getServiceId());
	}

	@PageAction
	public ViewRenderer revokeApproval(DataParam param) {
		String empId = param.get("EMP_ID");
		getService().revokeApprovalRecords(empId);
		return prepareDisplay(param);
	}
}
