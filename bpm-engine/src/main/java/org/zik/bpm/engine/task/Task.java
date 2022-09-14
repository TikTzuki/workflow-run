// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.task;

import org.zik.bpm.engine.form.CamundaFormRef;
import java.util.Date;

public interface Task
{
    public static final int PRIORITY_MINIUM = 0;
    public static final int PRIORITY_NORMAL = 50;
    public static final int PRIORITY_MAXIMUM = 100;
    
    String getId();
    
    String getName();
    
    void setName(final String p0);
    
    String getDescription();
    
    void setDescription(final String p0);
    
    int getPriority();
    
    void setPriority(final int p0);
    
    String getOwner();
    
    void setOwner(final String p0);
    
    String getAssignee();
    
    void setAssignee(final String p0);
    
    DelegationState getDelegationState();
    
    void setDelegationState(final DelegationState p0);
    
    String getProcessInstanceId();
    
    String getExecutionId();
    
    String getProcessDefinitionId();
    
    String getCaseInstanceId();
    
    void setCaseInstanceId(final String p0);
    
    String getCaseExecutionId();
    
    String getCaseDefinitionId();
    
    Date getCreateTime();
    
    String getTaskDefinitionKey();
    
    Date getDueDate();
    
    void setDueDate(final Date p0);
    
    Date getFollowUpDate();
    
    void setFollowUpDate(final Date p0);
    
    void delegate(final String p0);
    
    void setParentTaskId(final String p0);
    
    String getParentTaskId();
    
    boolean isSuspended();
    
    String getFormKey();
    
    CamundaFormRef getCamundaFormRef();
    
    String getTenantId();
    
    void setTenantId(final String p0);
}
