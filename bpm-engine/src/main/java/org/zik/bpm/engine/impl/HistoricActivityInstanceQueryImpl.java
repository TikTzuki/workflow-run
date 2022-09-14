// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.pvm.runtime.ActivityInstanceState;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricActivityInstance;
import org.zik.bpm.engine.history.HistoricActivityInstanceQuery;

public class HistoricActivityInstanceQueryImpl extends AbstractQuery<HistoricActivityInstanceQuery, HistoricActivityInstance> implements HistoricActivityInstanceQuery
{
    private static final long serialVersionUID = 1L;
    protected String activityInstanceId;
    protected String processInstanceId;
    protected String executionId;
    protected String processDefinitionId;
    protected String activityId;
    protected String activityName;
    protected String activityNameLike;
    protected String activityType;
    protected String assignee;
    protected boolean finished;
    protected boolean unfinished;
    protected Date startedBefore;
    protected Date startedAfter;
    protected Date finishedBefore;
    protected Date finishedAfter;
    protected ActivityInstanceState activityInstanceState;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    
    public HistoricActivityInstanceQueryImpl() {
    }
    
    public HistoricActivityInstanceQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricActivityInstanceManager().findHistoricActivityInstanceCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricActivityInstance> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricActivityInstanceManager().findHistoricActivityInstancesByQueryCriteria(this, page);
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl processInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl executionId(final String executionId) {
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl processDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl activityId(final String activityId) {
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl activityName(final String activityName) {
        this.activityName = activityName;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl activityNameLike(final String activityNameLike) {
        this.activityNameLike = activityNameLike;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl activityType(final String activityType) {
        this.activityType = activityType;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl taskAssignee(final String assignee) {
        this.assignee = assignee;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl finished() {
        this.finished = true;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl unfinished() {
        this.unfinished = true;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl completeScope() {
        if (this.activityInstanceState != null) {
            throw new ProcessEngineException("Already querying for activity instance state <" + this.activityInstanceState + ">");
        }
        this.activityInstanceState = ActivityInstanceState.SCOPE_COMPLETE;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl canceled() {
        if (this.activityInstanceState != null) {
            throw new ProcessEngineException("Already querying for activity instance state <" + this.activityInstanceState + ">");
        }
        this.activityInstanceState = ActivityInstanceState.CANCELED;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl startedAfter(final Date date) {
        this.startedAfter = date;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl startedBefore(final Date date) {
        this.startedBefore = date;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl finishedAfter(final Date date) {
        this.finishedAfter = date;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl finishedBefore(final Date date) {
        this.finishedBefore = date;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.areNotInAscendingOrder(this.startedAfter, this.startedBefore) || CompareUtil.areNotInAscendingOrder(this.finishedAfter, this.finishedBefore);
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByHistoricActivityInstanceDuration() {
        this.orderBy(HistoricActivityInstanceQueryProperty.DURATION);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByHistoricActivityInstanceEndTime() {
        this.orderBy(HistoricActivityInstanceQueryProperty.END);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByExecutionId() {
        this.orderBy(HistoricActivityInstanceQueryProperty.EXECUTION_ID);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByHistoricActivityInstanceId() {
        this.orderBy(HistoricActivityInstanceQueryProperty.HISTORIC_ACTIVITY_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByProcessDefinitionId() {
        this.orderBy(HistoricActivityInstanceQueryProperty.PROCESS_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByProcessInstanceId() {
        this.orderBy(HistoricActivityInstanceQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByHistoricActivityInstanceStartTime() {
        this.orderBy(HistoricActivityInstanceQueryProperty.START);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQuery orderByActivityId() {
        this.orderBy(HistoricActivityInstanceQueryProperty.ACTIVITY_ID);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByActivityName() {
        this.orderBy(HistoricActivityInstanceQueryProperty.ACTIVITY_NAME);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl orderByActivityType() {
        this.orderBy(HistoricActivityInstanceQueryProperty.ACTIVITY_TYPE);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQuery orderPartiallyByOccurrence() {
        this.orderBy(HistoricActivityInstanceQueryProperty.SEQUENCE_COUNTER);
        return this;
    }
    
    @Override
    public HistoricActivityInstanceQuery orderByTenantId() {
        return ((AbstractQuery<HistoricActivityInstanceQuery, U>)this).orderBy(HistoricActivityInstanceQueryProperty.TENANT_ID);
    }
    
    @Override
    public HistoricActivityInstanceQueryImpl activityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
        return this;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public String getActivityName() {
        return this.activityName;
    }
    
    public String getActivityType() {
        return this.activityType;
    }
    
    public String getAssignee() {
        return this.assignee;
    }
    
    public boolean isFinished() {
        return this.finished;
    }
    
    public boolean isUnfinished() {
        return this.unfinished;
    }
    
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    public Date getStartedAfter() {
        return this.startedAfter;
    }
    
    public Date getStartedBefore() {
        return this.startedBefore;
    }
    
    public Date getFinishedAfter() {
        return this.finishedAfter;
    }
    
    public Date getFinishedBefore() {
        return this.finishedBefore;
    }
    
    public ActivityInstanceState getActivityInstanceState() {
        return this.activityInstanceState;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
}
