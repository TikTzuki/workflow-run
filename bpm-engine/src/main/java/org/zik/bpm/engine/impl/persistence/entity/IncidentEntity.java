// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.incident.IncidentContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.zik.bpm.engine.impl.incident.IncidentLogger;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.runtime.Incident;

public class IncidentEntity implements Incident, DbEntity, HasDbRevision, HasDbReferences
{
    protected static final IncidentLogger LOG;
    protected int revision;
    protected String id;
    protected Date incidentTimestamp;
    protected String incidentType;
    protected String executionId;
    protected String activityId;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String causeIncidentId;
    protected String rootCauseIncidentId;
    protected String configuration;
    protected String incidentMessage;
    protected String tenantId;
    protected String jobDefinitionId;
    protected String historyConfiguration;
    protected String failedActivityId;
    protected String annotation;
    
    public List<IncidentEntity> createRecursiveIncidents() {
        final List<IncidentEntity> createdIncidents = new ArrayList<IncidentEntity>();
        this.createRecursiveIncidents(this.id, createdIncidents);
        return createdIncidents;
    }
    
    protected void createRecursiveIncidents(final String rootCauseIncidentId, final List<IncidentEntity> createdIncidents) {
        final ExecutionEntity execution = this.getExecution();
        if (execution != null) {
            final ExecutionEntity superExecution = execution.getProcessInstance().getSuperExecution();
            if (superExecution != null) {
                final IncidentEntity newIncident = create(this.incidentType);
                newIncident.setExecution(superExecution);
                newIncident.setActivityId(superExecution.getCurrentActivityId());
                newIncident.setFailedActivityId(superExecution.getCurrentActivityId());
                newIncident.setProcessDefinitionId(superExecution.getProcessDefinitionId());
                newIncident.setTenantId(superExecution.getTenantId());
                newIncident.setCauseIncidentId(this.id);
                newIncident.setRootCauseIncidentId(rootCauseIncidentId);
                insert(newIncident);
                createdIncidents.add(newIncident);
                newIncident.createRecursiveIncidents(rootCauseIncidentId, createdIncidents);
            }
        }
    }
    
    public static IncidentEntity createAndInsertIncident(final String incidentType, final IncidentContext context, final String message) {
        final IncidentEntity newIncident = create(incidentType);
        newIncident.setIncidentMessage(message);
        newIncident.setConfiguration(context.getConfiguration());
        newIncident.setActivityId(context.getActivityId());
        newIncident.setProcessDefinitionId(context.getProcessDefinitionId());
        newIncident.setTenantId(context.getTenantId());
        newIncident.setJobDefinitionId(context.getJobDefinitionId());
        newIncident.setHistoryConfiguration(context.getHistoryConfiguration());
        newIncident.setFailedActivityId(context.getFailedActivityId());
        if (context.getExecutionId() != null) {
            final ExecutionEntity execution = Context.getCommandContext().getExecutionManager().findExecutionById(context.getExecutionId());
            if (execution != null) {
                newIncident.setExecution(execution);
            }
            else {
                IncidentEntity.LOG.executionNotFound(context.getExecutionId());
            }
        }
        insert(newIncident);
        return newIncident;
    }
    
    protected static IncidentEntity create(final String incidentType) {
        final String incidentId = Context.getProcessEngineConfiguration().getDbSqlSessionFactory().getIdGenerator().getNextId();
        final IncidentEntity newIncident = new IncidentEntity();
        newIncident.setId(incidentId);
        newIncident.setIncidentTimestamp(ClockUtil.getCurrentTime());
        newIncident.setIncidentType(incidentType);
        newIncident.setCauseIncidentId(incidentId);
        newIncident.setRootCauseIncidentId(incidentId);
        return newIncident;
    }
    
    protected static void insert(final IncidentEntity incident) {
        Context.getCommandContext().getDbEntityManager().insert(incident);
        incident.fireHistoricIncidentEvent(HistoryEventTypes.INCIDENT_CREATE);
    }
    
    public void delete() {
        this.remove(false);
    }
    
    public void resolve() {
        this.remove(true);
    }
    
    protected void remove(final boolean resolved) {
        final ExecutionEntity execution = this.getExecution();
        if (execution != null) {
            ExecutionEntity superExecution = null;
            if (execution.getId().equals(execution.getProcessInstanceId())) {
                superExecution = execution.getSuperExecution();
            }
            else {
                superExecution = execution.getProcessInstance().getSuperExecution();
            }
            if (superExecution != null) {
                final IncidentEntity parentIncident = superExecution.getIncidentByCauseIncidentId(this.getId());
                if (parentIncident != null) {
                    parentIncident.remove(resolved);
                }
            }
            execution.removeIncident(this);
        }
        Context.getCommandContext().getDbEntityManager().delete(this);
        final HistoryEventType eventType = resolved ? HistoryEventTypes.INCIDENT_RESOLVE : HistoryEventTypes.INCIDENT_DELETE;
        this.fireHistoricIncidentEvent(eventType);
    }
    
    protected void fireHistoricIncidentEvent(final HistoryEventType eventType) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = processEngineConfiguration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(eventType, this)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    HistoryEvent event = null;
                    if (HistoryEvent.INCIDENT_CREATE.equals(eventType.getEventName())) {
                        event = producer.createHistoricIncidentCreateEvt(IncidentEntity.this);
                    }
                    else if (HistoryEvent.INCIDENT_RESOLVE.equals(eventType.getEventName())) {
                        event = producer.createHistoricIncidentResolveEvt(IncidentEntity.this);
                    }
                    else if (HistoryEvent.INCIDENT_DELETE.equals(eventType.getEventName())) {
                        event = producer.createHistoricIncidentDeleteEvt(IncidentEntity.this);
                    }
                    return event;
                }
            });
        }
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referenceIds = new HashSet<String>();
        if (this.causeIncidentId != null) {
            referenceIds.add(this.causeIncidentId);
        }
        return referenceIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        if (this.causeIncidentId != null) {
            referenceIdAndClass.put(this.causeIncidentId, IncidentEntity.class);
        }
        if (this.processDefinitionId != null) {
            referenceIdAndClass.put(this.processDefinitionId, ProcessDefinitionEntity.class);
        }
        if (this.processInstanceId != null) {
            referenceIdAndClass.put(this.processInstanceId, ExecutionEntity.class);
        }
        if (this.jobDefinitionId != null) {
            referenceIdAndClass.put(this.jobDefinitionId, JobDefinitionEntity.class);
        }
        if (this.executionId != null) {
            referenceIdAndClass.put(this.executionId, ExecutionEntity.class);
        }
        if (this.rootCauseIncidentId != null) {
            referenceIdAndClass.put(this.rootCauseIncidentId, IncidentEntity.class);
        }
        return referenceIdAndClass;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public Date getIncidentTimestamp() {
        return this.incidentTimestamp;
    }
    
    public void setIncidentTimestamp(final Date incidentTimestamp) {
        this.incidentTimestamp = incidentTimestamp;
    }
    
    @Override
    public String getIncidentType() {
        return this.incidentType;
    }
    
    public void setIncidentType(final String incidentType) {
        this.incidentType = incidentType;
    }
    
    @Override
    public String getIncidentMessage() {
        return this.incidentMessage;
    }
    
    public void setIncidentMessage(final String incidentMessage) {
        this.incidentMessage = incidentMessage;
    }
    
    @Override
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public ProcessDefinitionEntity getProcessDefinition() {
        if (this.processDefinitionId != null) {
            return Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(this.processDefinitionId);
        }
        return null;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String getCauseIncidentId() {
        return this.causeIncidentId;
    }
    
    public void setCauseIncidentId(final String causeIncidentId) {
        this.causeIncidentId = causeIncidentId;
    }
    
    @Override
    public String getRootCauseIncidentId() {
        return this.rootCauseIncidentId;
    }
    
    public void setRootCauseIncidentId(final String rootCauseIncidentId) {
        this.rootCauseIncidentId = rootCauseIncidentId;
    }
    
    @Override
    public String getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(final String configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public void setJobDefinitionId(final String jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }
    
    @Override
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public void setExecution(final ExecutionEntity execution) {
        final ExecutionEntity oldExecution = this.getExecution();
        if (oldExecution != null) {
            oldExecution.removeIncident(this);
        }
        if (execution != null) {
            this.executionId = execution.getId();
            this.processInstanceId = execution.getProcessInstanceId();
            execution.addIncident(this);
        }
        else {
            this.executionId = null;
            this.processInstanceId = null;
        }
    }
    
    public ExecutionEntity getExecution() {
        if (this.executionId != null) {
            final ExecutionEntity execution = Context.getCommandContext().getExecutionManager().findExecutionById(this.executionId);
            if (execution == null) {
                IncidentEntity.LOG.executionNotFound(this.executionId);
            }
            return execution;
        }
        return null;
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("executionId", this.executionId);
        persistentState.put("processDefinitionId", this.processDefinitionId);
        persistentState.put("activityId", this.activityId);
        persistentState.put("jobDefinitionId", this.jobDefinitionId);
        persistentState.put("annotation", this.annotation);
        return persistentState;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public String getHistoryConfiguration() {
        return this.historyConfiguration;
    }
    
    public void setHistoryConfiguration(final String historyConfiguration) {
        this.historyConfiguration = historyConfiguration;
    }
    
    @Override
    public String getFailedActivityId() {
        return this.failedActivityId;
    }
    
    public void setFailedActivityId(final String failedActivityId) {
        this.failedActivityId = failedActivityId;
    }
    
    @Override
    public String getAnnotation() {
        return this.annotation;
    }
    
    public void setAnnotation(final String annotation) {
        this.annotation = annotation;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", incidentTimestamp=" + this.incidentTimestamp + ", incidentType=" + this.incidentType + ", executionId=" + this.executionId + ", activityId=" + this.activityId + ", processInstanceId=" + this.processInstanceId + ", processDefinitionId=" + this.processDefinitionId + ", causeIncidentId=" + this.causeIncidentId + ", rootCauseIncidentId=" + this.rootCauseIncidentId + ", configuration=" + this.configuration + ", tenantId=" + this.tenantId + ", incidentMessage=" + this.incidentMessage + ", jobDefinitionId=" + this.jobDefinitionId + ", failedActivityId=" + this.failedActivityId + ", annotation=" + this.annotation + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final IncidentEntity other = (IncidentEntity)obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    static {
        LOG = ProcessEngineLogger.INCIDENT_LOGGER;
    }
}
