// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public enum Resources implements Resource
{
    APPLICATION("Application", 0), 
    USER("User", 1), 
    GROUP("Group", 2), 
    GROUP_MEMBERSHIP("Group membership", 3), 
    AUTHORIZATION("Authorization", 4), 
    FILTER("Filter", 5), 
    PROCESS_DEFINITION("ProcessDefinition", 6), 
    TASK("Task", 7), 
    PROCESS_INSTANCE("ProcessInstance", 8), 
    DEPLOYMENT("Deployment", 9), 
    DECISION_DEFINITION("DecisionDefinition", 10), 
    TENANT("Tenant", 11), 
    TENANT_MEMBERSHIP("TenantMembership", 12), 
    BATCH("Batch", 13), 
    DECISION_REQUIREMENTS_DEFINITION("DecisionRequirementsDefinition", 14), 
    REPORT("Report", 15), 
    DASHBOARD("Dashboard", 16), 
    OPERATION_LOG_CATEGORY("OperationLogCatgeory", 17), 
    @Deprecated
    OPTIMIZE("Optimize", 18), 
    HISTORIC_TASK("HistoricTask", 19), 
    HISTORIC_PROCESS_INSTANCE("HistoricProcessInstance", 20), 
    SYSTEM("System", 21);
    
    String name;
    int id;
    
    private Resources(final String name, final int id) {
        this.name = name;
        this.id = id;
    }
    
    @Override
    public String resourceName() {
        return this.name;
    }
    
    @Override
    public int resourceType() {
        return this.id;
    }
}
