// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public class TaskAlreadyClaimedException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    private String taskId;
    private String taskAssignee;
    
    public TaskAlreadyClaimedException(final String taskId, final String taskAssignee) {
        super("Task '" + taskId + "' is already claimed by someone else.");
        this.taskId = taskId;
        this.taskAssignee = taskAssignee;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public String getTaskAssignee() {
        return this.taskAssignee;
    }
}
