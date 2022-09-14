// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.history.HistoricDecisionOutputInstance;
import org.zik.bpm.engine.history.HistoricDecisionInputInstance;
import java.util.List;
import java.util.Date;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.history.HistoricDecisionInstance;

public class HistoricDecisionInstanceEntity extends HistoryEvent implements HistoricDecisionInstance
{
    protected static final EnginePersistenceLogger LOG;
    private static final long serialVersionUID = 1L;
    protected String decisionDefinitionId;
    protected String decisionDefinitionKey;
    protected String decisionDefinitionName;
    protected String activityInstanceId;
    protected String activityId;
    protected Date evaluationTime;
    protected Double collectResultValue;
    protected String rootDecisionInstanceId;
    protected String decisionRequirementsDefinitionId;
    protected String decisionRequirementsDefinitionKey;
    protected String userId;
    protected String tenantId;
    protected List<HistoricDecisionInputInstance> inputs;
    protected List<HistoricDecisionOutputInstance> outputs;
    
    @Override
    public String getDecisionDefinitionId() {
        return this.decisionDefinitionId;
    }
    
    public void setDecisionDefinitionId(final String decisionDefinitionId) {
        this.decisionDefinitionId = decisionDefinitionId;
    }
    
    @Override
    public String getDecisionDefinitionKey() {
        return this.decisionDefinitionKey;
    }
    
    public void setDecisionDefinitionKey(final String decisionDefinitionKey) {
        this.decisionDefinitionKey = decisionDefinitionKey;
    }
    
    @Override
    public String getDecisionDefinitionName() {
        return this.decisionDefinitionName;
    }
    
    public void setDecisionDefinitionName(final String decisionDefinitionName) {
        this.decisionDefinitionName = decisionDefinitionName;
    }
    
    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    @Override
    public Date getEvaluationTime() {
        return this.evaluationTime;
    }
    
    public void setEvaluationTime(final Date evaluationTime) {
        this.evaluationTime = evaluationTime;
    }
    
    @Override
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public List<HistoricDecisionInputInstance> getInputs() {
        if (this.inputs != null) {
            return this.inputs;
        }
        throw HistoricDecisionInstanceEntity.LOG.historicDecisionInputInstancesNotFetchedException();
    }
    
    @Override
    public List<HistoricDecisionOutputInstance> getOutputs() {
        if (this.outputs != null) {
            return this.outputs;
        }
        throw HistoricDecisionInstanceEntity.LOG.historicDecisionOutputInstancesNotFetchedException();
    }
    
    public void setInputs(final List<HistoricDecisionInputInstance> inputs) {
        this.inputs = inputs;
    }
    
    public void setOutputs(final List<HistoricDecisionOutputInstance> outputs) {
        this.outputs = outputs;
    }
    
    public void delete() {
        Context.getCommandContext().getDbEntityManager().delete(this);
    }
    
    public void addInput(final HistoricDecisionInputInstance decisionInputInstance) {
        if (this.inputs == null) {
            this.inputs = new ArrayList<HistoricDecisionInputInstance>();
        }
        this.inputs.add(decisionInputInstance);
    }
    
    public void addOutput(final HistoricDecisionOutputInstance decisionOutputInstance) {
        if (this.outputs == null) {
            this.outputs = new ArrayList<HistoricDecisionOutputInstance>();
        }
        this.outputs.add(decisionOutputInstance);
    }
    
    @Override
    public Double getCollectResultValue() {
        return this.collectResultValue;
    }
    
    public void setCollectResultValue(final Double collectResultValue) {
        this.collectResultValue = collectResultValue;
    }
    
    @Override
    public String getRootDecisionInstanceId() {
        return this.rootDecisionInstanceId;
    }
    
    public void setRootDecisionInstanceId(final String rootDecisionInstanceId) {
        this.rootDecisionInstanceId = rootDecisionInstanceId;
    }
    
    @Override
    public String getDecisionRequirementsDefinitionId() {
        return this.decisionRequirementsDefinitionId;
    }
    
    public void setDecisionRequirementsDefinitionId(final String decisionRequirementsDefinitionId) {
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
    }
    
    @Override
    public String getDecisionRequirementsDefinitionKey() {
        return this.decisionRequirementsDefinitionKey;
    }
    
    public void setDecisionRequirementsDefinitionKey(final String decisionRequirementsDefinitionKey) {
        this.decisionRequirementsDefinitionKey = decisionRequirementsDefinitionKey;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
