package com.agileai.hr.module.attendance.handler;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.SimpleHandler;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.cxmodule.HrAttendanceManage;
import com.agileai.util.DateUtil;

public class MobileAttendanceStatHandler extends SimpleHandler{
	public MobileAttendanceStatHandler(){
		super();
	}
	
	@PageAction
	public ViewRenderer getAttendanceStatInfo(DataParam param){
		String responseText = FAIL;
		try {
			HrAttendanceManage hrAttendanceManage = this.lookupService(HrAttendanceManage.class);
			List<DataRow> records = hrAttendanceManage.getAttendanceStatInfo();
			JSONArray jsonArray1 = new JSONArray();
			JSONArray jsonArray2 = new JSONArray();
			JSONArray lastNums = new JSONArray();
			JSONArray leaveNums = new JSONArray();
			JSONArray overWorkNums = new JSONArray();
			jsonArray2.put(0, "迟到天数");
			jsonArray2.put(1, "请假天数");
			jsonArray2.put(2, "加班天数");
			for(int i =0;i<records.size();i++){
				DataRow row = records.get(i);
				jsonArray1.put(i, row.getString("USER_NAME"));
				lastNums.put(i, row.get("lastNums"));
				leaveNums.put(i, row.get("leaveNums"));
				overWorkNums.put(i, row.get("overWorkNums"));
			}
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(0, lastNums);
			jsonArray.put(1, leaveNums);
			jsonArray.put(2, overWorkNums);
			jsonObject.put("labels", jsonArray1);
			jsonObject.put("series", jsonArray2);
			jsonObject.put("data", jsonArray);
			responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return new AjaxRenderer(responseText);
	}
	
	
	@PageAction
	public ViewRenderer getAttendanceInfoSheet(DataParam param){
		String responseText = FAIL;
		try {
			HrAttendanceManage hrAttendanceManage = this.lookupService(HrAttendanceManage.class);
			List<DataRow> records = hrAttendanceManage.getAttendanceStatInfo();
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<records.size();i++){
				DataRow row = records.get(i);
				JSONObject jsonoObject = new JSONObject();
				jsonoObject.put("name",row.getString("USER_NAME"));
				jsonoObject.put("code",row.getString("USER_CODE"));
				jsonoObject.put("lateDays",row.get("lastNums"));
				jsonoObject.put("leaveDays",row.get("leaveNums"));
				jsonoObject.put("overworkDays",row.get("overWorkNums"));
				jsonArray.put(jsonoObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("attendanceInfoList", jsonArray);
			responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return new AjaxRenderer(responseText);
	}
	
	@PageAction
	public ViewRenderer getContentData(DataParam param){
		String responseText = FAIL;
		try {
			String date = param.getString("month");
			String code = param.getString("code");
			String mark = param.getString("mark");
			date = date+"-01";
			if("Last".equals(mark)){
				if(date.endsWith("01")){
					String tempDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(date),DateUtil.YEAR,-1));
					date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(tempDate),DateUtil.MONTH,11));
				}else{
					date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(date),DateUtil.MONTH,-1));
				}
			}else if("Next".equals(mark)){
				if(date.endsWith("12")){
					String tempDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(date),DateUtil.YEAR,1));
					date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(tempDate),DateUtil.MONTH,-11));
				}else{
					date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(date),DateUtil.MONTH,1));
				}
			}else{
				date = DateUtil.getYear()+"-"+DateUtil.getMonth();
			}
			date = date.substring(0,7);
			HrAttendanceManage hrAttendanceManage = this.lookupService(HrAttendanceManage.class);
			List<DataRow> attendRecords = hrAttendanceManage.getAttendContentData(code,date);
			List<DataRow> leaveRecords = hrAttendanceManage.getLeaveContentData(code,date);
			List<DataRow> overworkRecords = hrAttendanceManage.getOverworkContentData(code,date);
			JSONArray jsonArray1 = new JSONArray();
			for(int i=0;i<attendRecords.size();i++){
				DataRow dataRow = attendRecords.get(i);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", dataRow.get("USER_ID_NAME"));
				jsonObject.put("inTime", dataRow.get("ATD_IN_TIME"));
				jsonObject.put("inPlace", dataRow.get("ATD_IN_PLACE"));
				jsonObject.put("outTime", dataRow.get("ATD_OUT_TIME"));
				jsonObject.put("outPlace", dataRow.get("ATD_OUT_PLACE"));
				jsonArray1.put(jsonObject);
			}
			JSONArray jsonArray2 = new JSONArray();
			for(int i=0;i<leaveRecords.size();i++){
				DataRow dataRow = leaveRecords.get(i);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("date", dataRow.get("LEA_DATE"));
				jsonObject.put("name", dataRow.get("USER_ID_NAME"));
				jsonObject.put("type", dataRow.get("LEA_TYPE_TEXT"));
				jsonObject.put("days", dataRow.get("LEA_DAYS"));
				jsonObject.put("cause", dataRow.get("LEA_CAUSE"));
				jsonObject.put("appName", dataRow.get("LEA_APPOVER_NAME"));
				jsonObject.put("appTime", dataRow.get("LEA_APP_TIME"));
				jsonArray2.put(jsonObject);
			}
			JSONArray jsonArray3 = new JSONArray();
			for(int i=0;i<overworkRecords.size();i++){
				DataRow dataRow = overworkRecords.get(i);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("name", dataRow.get("USER_ID_NAME"));
				jsonObject.put("date", dataRow.get("WOT_OVERTIME_DATE"));
				jsonObject.put("participant", dataRow.get("WOT_PARTICIPANT"));
				jsonObject.put("place", dataRow.get("WOT_PLACE"));
				jsonObject.put("num", dataRow.get("WOT_TIME_TEXT"));
				jsonObject.put("unit", dataRow.get("WOT_TIME_COMPANY_TEXT"));
				jsonObject.put("cause", dataRow.get("WOT_DESC"));
				jsonObject.put("appName", dataRow.get("WOT_APPROVER_NAME"));
				jsonObject.put("appTime", dataRow.get("WOT_APP_TIME"));
				jsonArray3.put(jsonObject);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("lateInfoList", jsonArray1);
			jsonObject.put("leaveInfoList", jsonArray2);
			jsonObject.put("overworkInfoList", jsonArray3);
			jsonObject.put("month", date);
			responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return new AjaxRenderer(responseText);
	}
}
