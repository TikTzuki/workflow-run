// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.auth;

import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.identity.Tenant;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.zik.bpm.engine.identity.User;

public interface ResourceAuthorizationProvider
{
    AuthorizationEntity[] newUser(final User p0);
    
    AuthorizationEntity[] newGroup(final Group p0);
    
    AuthorizationEntity[] newTenant(final Tenant p0);
    
    AuthorizationEntity[] groupMembershipCreated(final String p0, final String p1);
    
    AuthorizationEntity[] tenantMembershipCreated(final Tenant p0, final User p1);
    
    AuthorizationEntity[] tenantMembershipCreated(final Tenant p0, final Group p1);
    
    AuthorizationEntity[] newFilter(final Filter p0);
    
    AuthorizationEntity[] newDeployment(final Deployment p0);
    
    AuthorizationEntity[] newProcessDefinition(final ProcessDefinition p0);
    
    AuthorizationEntity[] newProcessInstance(final ProcessInstance p0);
    
    AuthorizationEntity[] newTask(final Task p0);
    
    AuthorizationEntity[] newTaskAssignee(final Task p0, final String p1, final String p2);
    
    AuthorizationEntity[] newTaskOwner(final Task p0, final String p1, final String p2);
    
    AuthorizationEntity[] newTaskUserIdentityLink(final Task p0, final String p1, final String p2);
    
    AuthorizationEntity[] newTaskGroupIdentityLink(final Task p0, final String p1, final String p2);
    
    AuthorizationEntity[] deleteTaskUserIdentityLink(final Task p0, final String p1, final String p2);
    
    AuthorizationEntity[] deleteTaskGroupIdentityLink(final Task p0, final String p1, final String p2);
    
    AuthorizationEntity[] newDecisionDefinition(final DecisionDefinition p0);
    
    AuthorizationEntity[] newDecisionRequirementsDefinition(final DecisionRequirementsDefinition p0);
}
