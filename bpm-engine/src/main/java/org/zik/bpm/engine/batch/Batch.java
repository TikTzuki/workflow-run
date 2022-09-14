// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.batch;

public interface Batch
{
    public static final String TYPE_PROCESS_INSTANCE_MIGRATION = "instance-migration";
    public static final String TYPE_PROCESS_INSTANCE_MODIFICATION = "instance-modification";
    public static final String TYPE_PROCESS_INSTANCE_RESTART = "instance-restart";
    public static final String TYPE_PROCESS_INSTANCE_DELETION = "instance-deletion";
    public static final String TYPE_PROCESS_INSTANCE_UPDATE_SUSPENSION_STATE = "instance-update-suspension-state";
    public static final String TYPE_HISTORIC_PROCESS_INSTANCE_DELETION = "historic-instance-deletion";
    public static final String TYPE_HISTORIC_DECISION_INSTANCE_DELETION = "historic-decision-instance-deletion";
    public static final String TYPE_SET_JOB_RETRIES = "set-job-retries";
    public static final String TYPE_SET_EXTERNAL_TASK_RETRIES = "set-external-task-retries";
    public static final String TYPE_PROCESS_SET_REMOVAL_TIME = "process-set-removal-time";
    public static final String TYPE_DECISION_SET_REMOVAL_TIME = "decision-set-removal-time";
    public static final String TYPE_BATCH_SET_REMOVAL_TIME = "batch-set-removal-time";
    public static final String TYPE_SET_VARIABLES = "set-variables";
    public static final String TYPE_CORRELATE_MESSAGE = "correlate-message";
    
    String getId();
    
    String getType();
    
    int getTotalJobs();
    
    int getJobsCreated();
    
    int getBatchJobsPerSeed();
    
    int getInvocationsPerBatchJob();
    
    String getSeedJobDefinitionId();
    
    String getMonitorJobDefinitionId();
    
    String getBatchJobDefinitionId();
    
    String getTenantId();
    
    String getCreateUserId();
    
    boolean isSuspended();
}
