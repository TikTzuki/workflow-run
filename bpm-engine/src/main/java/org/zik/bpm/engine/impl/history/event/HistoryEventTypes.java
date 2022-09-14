// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

public enum HistoryEventTypes implements HistoryEventType
{
    PROCESS_INSTANCE_START("process-instance", "start"), 
    PROCESS_INSTANCE_UPDATE("process-instance-update", "update"), 
    PROCESS_INSTANCE_MIGRATE("process-instance", "migrate"), 
    PROCESS_INSTANCE_END("process-instance", "end"), 
    ACTIVITY_INSTANCE_START("activity-instance", "start"), 
    ACTIVITY_INSTANCE_UPDATE("activity-instance", "update"), 
    ACTIVITY_INSTANCE_MIGRATE("activity-instance", "migrate"), 
    ACTIVITY_INSTANCE_END("activity-instance", "end"), 
    TASK_INSTANCE_CREATE("task-instance", "create"), 
    TASK_INSTANCE_UPDATE("task-instance", "update"), 
    TASK_INSTANCE_MIGRATE("task-instance", "migrate"), 
    TASK_INSTANCE_COMPLETE("task-instance", "complete"), 
    TASK_INSTANCE_DELETE("task-instance", "delete"), 
    VARIABLE_INSTANCE_CREATE("variable-instance", "create"), 
    VARIABLE_INSTANCE_UPDATE("variable-instance", "update"), 
    VARIABLE_INSTANCE_MIGRATE("variable-instance", "migrate"), 
    VARIABLE_INSTANCE_UPDATE_DETAIL("variable-instance", "update-detail"), 
    VARIABLE_INSTANCE_DELETE("variable-instance", "delete"), 
    FORM_PROPERTY_UPDATE("form-property", "form-property-update"), 
    INCIDENT_CREATE("incident", "create"), 
    INCIDENT_MIGRATE("incident", "migrate"), 
    INCIDENT_DELETE("incident", "delete"), 
    INCIDENT_RESOLVE("incident", "resolve"), 
    INCIDENT_UPDATE("incident", "update"), 
    CASE_INSTANCE_CREATE("case-instance", "create"), 
    CASE_INSTANCE_UPDATE("case-instance", "update"), 
    CASE_INSTANCE_CLOSE("case-instance", "close"), 
    CASE_ACTIVITY_INSTANCE_CREATE("case-activity-instance", "create"), 
    CASE_ACTIVITY_INSTANCE_UPDATE("case-activity-instance", "update"), 
    CASE_ACTIVITY_INSTANCE_END("case-activity_instance", "end"), 
    JOB_CREATE("job", "create"), 
    JOB_FAIL("job", "fail"), 
    JOB_SUCCESS("job", "success"), 
    JOB_DELETE("job", "delete"), 
    DMN_DECISION_EVALUATE("decision", "evaluate"), 
    BATCH_START("batch", "start"), 
    BATCH_END("batch", "end"), 
    IDENTITY_LINK_ADD("identity-link-add", "add-identity-link"), 
    IDENTITY_LINK_DELETE("identity-link-delete", "delete-identity-link"), 
    EXTERNAL_TASK_CREATE("external-task", "create"), 
    EXTERNAL_TASK_FAIL("external-task", "fail"), 
    EXTERNAL_TASK_SUCCESS("external-task", "success"), 
    EXTERNAL_TASK_DELETE("external-task", "delete"), 
    USER_OPERATION_LOG("user-operation-log", "create");
    
    protected String entityType;
    protected String eventName;
    
    private HistoryEventTypes(final String entityType, final String eventName) {
        this.entityType = entityType;
        this.eventName = eventName;
    }
    
    @Override
    public String getEntityType() {
        return this.entityType;
    }
    
    @Override
    public String getEventName() {
        return this.eventName;
    }
}
