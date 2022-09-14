// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public interface ProcessEngineServices
{
    RuntimeService getRuntimeService();
    
    RepositoryService getRepositoryService();
    
    FormService getFormService();
    
    TaskService getTaskService();
    
    HistoryService getHistoryService();
    
    IdentityService getIdentityService();
    
    ManagementService getManagementService();
    
    AuthorizationService getAuthorizationService();
    
    CaseService getCaseService();
    
    FilterService getFilterService();
    
    ExternalTaskService getExternalTaskService();
    
    DecisionService getDecisionService();
}
