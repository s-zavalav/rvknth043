package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeManageHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hr.module.system.service.SecurityGroupQuery;
import com.agileai.hr.module.system.service.SecurityRoleTreeManage;
import com.agileai.hr.module.system.service.SecurityUserQuery;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.ListUtil;
import com.agileai.util.StringUtil;

public class SecurityRoleTreeManageHandler
        extends TreeManageHandler {
    public SecurityRoleTreeManageHandler() {
        super();
        this.serviceId = buildServiceId(SecurityRoleTreeManage.class);
        this.nodeIdField = "ROLE_ID";
        this.nodePIdField = "ROLE_PID";
        this.moveUpErrorMsg = "该节点是第一个节点，不能上移！";
        this.moveDownErrorMsg = "该节点是最后一个节点，不能下移！";
        this.deleteErrorMsg = "该节点还有子节点，不能删除！";
    }

    protected TreeBuilder provideTreeBuilder(DataParam param) {
        SecurityRoleTreeManage service = this.getService();
        List<DataRow> menuRecords = service.findTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(menuRecords, "ROLE_ID",
                                                  "ROLE_NAME", "ROLE_PID");

        return treeBuilder;
    }

	public ViewRenderer doDeleteAction(DataParam param){
		String nodeId = param.get(this.nodeIdField);
		List<DataRow> childRecords = getService().queryChildRecords(nodeId);
		if (ListUtil.isNullOrEmpty(childRecords)){
			DataParam dataParam = new DataParam("roleId",nodeId);
			SecurityUserQuery securityUserQuery = this.lookupService(SecurityUserQuery.class);
			List<DataRow> userRecords = securityUserQuery.findRecords(dataParam);
			if (!ListUtil.isNullOrEmpty(userRecords)){
				this.setErrorMsg("存在关联用户,不能删除!");
				return prepareDisplay(param);
			}
			SecurityGroupQuery groupQuery = this.lookupService(SecurityGroupQuery.class);
			List<DataRow> groupRecords = groupQuery.findRecords(dataParam);
			if (!ListUtil.isNullOrEmpty(groupRecords)){
				this.setErrorMsg("存在关联群组,不能删除!");
				return prepareDisplay(param);
			}
			
			getService().deleteCurrentRecord(nodeId);
			param.remove(this.nodeIdField);
			param.put(this.nodeIdField,param.get(this.nodePIdField));
			
		}else{
			this.setErrorMsg(deleteErrorMsg);
		}
		return prepareDisplay(param);
	}

    
    protected void processPageAttributes(DataParam param) {
        setAttribute("ROLE_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getAttributeValue("ROLE_STATE")));
        setAttribute("CHILD_ROLE_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getAttributeValue("CHILD_ROLE_STATE",
                                                                          "1")));
        this.setAttribute("currentTabId", param.get("currentTabId"));
        setAttribute("currentTabId",getAttributeValue("currentTabId", "0"));
    }

    protected String provideDefaultNodeId(DataParam param) {
        return "00000000-0000-0000-00000000000000000";
    }

    protected boolean isRootNode(DataParam param) {
        boolean result = true;
        String nodeId = param.get(this.nodeIdField,
                                  this.provideDefaultNodeId(param));
        DataParam queryParam = new DataParam(this.nodeIdField, nodeId);
        DataRow row = this.getService().queryCurrentRecord(queryParam);

        if (row == null) {
            result = false;
        } else {
            String parentId = row.stringValue("ROLE_PID");
            result = StringUtil.isNullOrEmpty(parentId);
        }

        return result;
    }

    protected SecurityRoleTreeManage getService() {
        return (SecurityRoleTreeManage) this.lookupService(this.getServiceId());
    }
}
