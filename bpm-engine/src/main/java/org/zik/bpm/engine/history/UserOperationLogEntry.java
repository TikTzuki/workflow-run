// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface UserOperationLogEntry
{
    @Deprecated
    public static final String ENTITY_TYPE_TASK = "Task";
    @Deprecated
    public static final String ENTITY_TYPE_IDENTITY_LINK = "IdentityLink";
    @Deprecated
    public static final String ENTITY_TYPE_ATTACHMENT = "Attachment";
    public static final String OPERATION_TYPE_ASSIGN = "Assign";
    public static final String OPERATION_TYPE_CLAIM = "Claim";
    public static final String OPERATION_TYPE_COMPLETE = "Complete";
    public static final String OPERATION_TYPE_CREATE = "Create";
    public static final String OPERATION_TYPE_DELEGATE = "Delegate";
    public static final String OPERATION_TYPE_DELETE = "Delete";
    public static final String OPERATION_TYPE_RESOLVE = "Resolve";
    public static final String OPERATION_TYPE_SET_OWNER = "SetOwner";
    public static final String OPERATION_TYPE_SET_PRIORITY = "SetPriority";
    public static final String OPERATION_TYPE_UPDATE = "Update";
    public static final String OPERATION_TYPE_ACTIVATE = "Activate";
    public static final String OPERATION_TYPE_SUSPEND = "Suspend";
    public static final String OPERATION_TYPE_MIGRATE = "Migrate";
    public static final String OPERATION_TYPE_ADD_USER_LINK = "AddUserLink";
    public static final String OPERATION_TYPE_DELETE_USER_LINK = "DeleteUserLink";
    public static final String OPERATION_TYPE_ADD_GROUP_LINK = "AddGroupLink";
    public static final String OPERATION_TYPE_DELETE_GROUP_LINK = "DeleteGroupLink";
    public static final String OPERATION_TYPE_SET_DUEDATE = "SetDueDate";
    public static final String OPERATION_TYPE_RECALC_DUEDATE = "RecalculateDueDate";
    public static final String OPERATION_TYPE_UNLOCK = "Unlock";
    public static final String OPERATION_TYPE_EXECUTE = "Execute";
    public static final String OPERATION_TYPE_EVALUATE = "Evaluate";
    public static final String OPERATION_TYPE_ADD_ATTACHMENT = "AddAttachment";
    public static final String OPERATION_TYPE_DELETE_ATTACHMENT = "DeleteAttachment";
    public static final String OPERATION_TYPE_SUSPEND_JOB_DEFINITION = "SuspendJobDefinition";
    public static final String OPERATION_TYPE_ACTIVATE_JOB_DEFINITION = "ActivateJobDefinition";
    public static final String OPERATION_TYPE_SUSPEND_PROCESS_DEFINITION = "SuspendProcessDefinition";
    public static final String OPERATION_TYPE_ACTIVATE_PROCESS_DEFINITION = "ActivateProcessDefinition";
    public static final String OPERATION_TYPE_CREATE_HISTORY_CLEANUP_JOB = "CreateHistoryCleanupJobs";
    public static final String OPERATION_TYPE_UPDATE_HISTORY_TIME_TO_LIVE = "UpdateHistoryTimeToLive";
    public static final String OPERATION_TYPE_DELETE_HISTORY = "DeleteHistory";
    public static final String OPERATION_TYPE_MODIFY_PROCESS_INSTANCE = "ModifyProcessInstance";
    public static final String OPERATION_TYPE_RESTART_PROCESS_INSTANCE = "RestartProcessInstance";
    public static final String OPERATION_TYPE_SUSPEND_JOB = "SuspendJob";
    public static final String OPERATION_TYPE_ACTIVATE_JOB = "ActivateJob";
    public static final String OPERATION_TYPE_SET_JOB_RETRIES = "SetJobRetries";
    public static final String OPERATION_TYPE_SET_EXTERNAL_TASK_RETRIES = "SetExternalTaskRetries";
    public static final String OPERATION_TYPE_SET_VARIABLE = "SetVariable";
    public static final String OPERATION_TYPE_SET_VARIABLES = "SetVariables";
    public static final String OPERATION_TYPE_REMOVE_VARIABLE = "RemoveVariable";
    public static final String OPERATION_TYPE_MODIFY_VARIABLE = "ModifyVariable";
    public static final String OPERATION_TYPE_SUSPEND_BATCH = "SuspendBatch";
    public static final String OPERATION_TYPE_ACTIVATE_BATCH = "ActivateBatch";
    public static final String OPERATION_TYPE_CREATE_INCIDENT = "CreateIncident";
    public static final String OPERATION_TYPE_SET_REMOVAL_TIME = "SetRemovalTime";
    public static final String OPERATION_TYPE_SET_ANNOTATION = "SetAnnotation";
    public static final String OPERATION_TYPE_CLEAR_ANNOTATION = "ClearAnnotation";
    public static final String OPERATION_TYPE_CORRELATE_MESSAGE = "CorrelateMessage";
    public static final String CATEGORY_ADMIN = "Admin";
    public static final String CATEGORY_OPERATOR = "Operator";
    public static final String CATEGORY_TASK_WORKER = "TaskWorker";
    
    String getId();
    
    String getDeploymentId();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getExecutionId();
    
    String getCaseDefinitionId();
    
    String getCaseInstanceId();
    
    String getCaseExecutionId();
    
    String getTaskId();
    
    String getJobId();
    
    String getJobDefinitionId();
    
    String getBatchId();
    
    String getUserId();
    
    Date getTimestamp();
    
    String getOperationId();
    
    String getExternalTaskId();
    
    String getOperationType();
    
    String getEntityType();
    
    String getProperty();
    
    String getOrgValue();
    
    String getNewValue();
    
    Date getRemovalTime();
    
    String getCategory();
    
    String getAnnotation();
}
