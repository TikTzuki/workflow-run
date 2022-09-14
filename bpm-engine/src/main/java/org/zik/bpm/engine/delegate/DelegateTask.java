// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.zik.bpm.engine.task.IdentityLink;
import java.util.Set;
import java.util.Collection;
import java.util.Date;

public interface DelegateTask extends VariableScope, BpmnModelExecutionContext, ProcessEngineServicesAware
{
    String getId();
    
    String getName();
    
    void setName(final String p0);
    
    String getDescription();
    
    void setDescription(final String p0);
    
    int getPriority();
    
    void setPriority(final int p0);
    
    String getProcessInstanceId();
    
    String getExecutionId();
    
    String getProcessDefinitionId();
    
    String getCaseInstanceId();
    
    String getCaseExecutionId();
    
    String getCaseDefinitionId();
    
    Date getCreateTime();
    
    String getTaskDefinitionKey();
    
    DelegateExecution getExecution();
    
    DelegateCaseExecution getCaseExecution();
    
    String getEventName();
    
    void addCandidateUser(final String p0);
    
    void addCandidateUsers(final Collection<String> p0);
    
    void addCandidateGroup(final String p0);
    
    void addCandidateGroups(final Collection<String> p0);
    
    String getOwner();
    
    void setOwner(final String p0);
    
    String getAssignee();
    
    void setAssignee(final String p0);
    
    Date getDueDate();
    
    void setDueDate(final Date p0);
    
    String getDeleteReason();
    
    void addUserIdentityLink(final String p0, final String p1);
    
    void addGroupIdentityLink(final String p0, final String p1);
    
    void deleteCandidateUser(final String p0);
    
    void deleteCandidateGroup(final String p0);
    
    void deleteUserIdentityLink(final String p0, final String p1);
    
    void deleteGroupIdentityLink(final String p0, final String p1);
    
    Set<IdentityLink> getCandidates();
    
    UserTask getBpmnModelElementInstance();
    
    String getTenantId();
    
    Date getFollowUpDate();
    
    void setFollowUpDate(final Date p0);
    
    void complete();
}
