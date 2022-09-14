// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricIdentityLinkLog;
import org.zik.bpm.engine.history.HistoricIdentityLinkLogQuery;

public class HistoricIdentityLinkLogQueryImpl extends AbstractVariableQueryImpl<HistoricIdentityLinkLogQuery, HistoricIdentityLinkLog> implements HistoricIdentityLinkLogQuery
{
    private static final long serialVersionUID = 1L;
    protected Date dateBefore;
    protected Date dateAfter;
    protected String type;
    protected String userId;
    protected String groupId;
    protected String taskId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String operationType;
    protected String assignerId;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    
    public HistoricIdentityLinkLogQueryImpl() {
    }
    
    public HistoricIdentityLinkLogQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public String getGroupId() {
        return this.groupId;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String getOperationType() {
        return this.operationType;
    }
    
    public String getAssignerId() {
        return this.assignerId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    public Date getDateBefore() {
        return this.dateBefore;
    }
    
    public Date getDateAfter() {
        return this.dateAfter;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery type(final String type) {
        EnsureUtil.ensureNotNull("type", (Object)type);
        this.type = type;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery dateBefore(final Date dateBefore) {
        EnsureUtil.ensureNotNull("dateBefore", dateBefore);
        this.dateBefore = dateBefore;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery dateAfter(final Date dateAfter) {
        EnsureUtil.ensureNotNull("dateAfter", dateAfter);
        this.dateAfter = dateAfter;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery userId(final String userId) {
        EnsureUtil.ensureNotNull("userId", (Object)userId);
        this.userId = userId;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery groupId(final String groupId) {
        EnsureUtil.ensureNotNull("groupId", (Object)groupId);
        this.groupId = groupId;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery taskId(final String taskId) {
        EnsureUtil.ensureNotNull("taskId", (Object)taskId);
        this.taskId = taskId;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery operationType(final String operationType) {
        EnsureUtil.ensureNotNull("operationType", (Object)operationType);
        this.operationType = operationType;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery assignerId(final String assignerId) {
        EnsureUtil.ensureNotNull("assignerId", (Object)assignerId);
        this.assignerId = assignerId;
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByTime() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.TIME);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByType() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.TYPE);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByUserId() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.USER_ID);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByGroupId() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.GROUP_ID);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByTaskId() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.TASK_ID);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByProcessDefinitionId() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.PROC_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByProcessDefinitionKey() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.PROC_DEFINITION_KEY);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByOperationType() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.OPERATION_TYPE);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByAssignerId() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.ASSIGNER_ID);
        return this;
    }
    
    @Override
    public HistoricIdentityLinkLogQuery orderByTenantId() {
        this.orderBy(HistoricIdentityLinkLogQueryProperty.TENANT_ID);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricIdentityLinkManager().findHistoricIdentityLinkLogCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricIdentityLinkLog> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricIdentityLinkManager().findHistoricIdentityLinkLogByQueryCriteria(this, page);
    }
}
