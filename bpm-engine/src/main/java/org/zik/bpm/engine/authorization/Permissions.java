// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

import java.util.EnumSet;

public enum Permissions implements Permission
{
    NONE("NONE", 0, EnumSet.allOf(Resources.class)), 
    ALL("ALL", Integer.MAX_VALUE, EnumSet.allOf(Resources.class)), 
    READ("READ", 2, EnumSet.of(Resources.AUTHORIZATION, Resources.BATCH, Resources.DASHBOARD, Resources.DECISION_DEFINITION, Resources.DECISION_REQUIREMENTS_DEFINITION, Resources.DEPLOYMENT, Resources.FILTER, Resources.GROUP, Resources.PROCESS_DEFINITION, Resources.PROCESS_INSTANCE, Resources.REPORT, Resources.TASK, Resources.TENANT, Resources.USER)), 
    UPDATE("UPDATE", 4, EnumSet.of(Resources.AUTHORIZATION, Resources.BATCH, Resources.DASHBOARD, Resources.DECISION_DEFINITION, Resources.FILTER, Resources.GROUP, Resources.PROCESS_DEFINITION, Resources.PROCESS_INSTANCE, Resources.REPORT, Resources.TASK, Resources.TENANT, Resources.USER)), 
    CREATE("CREATE", 8, EnumSet.of(Resources.AUTHORIZATION, Resources.BATCH, Resources.DASHBOARD, Resources.DEPLOYMENT, Resources.FILTER, Resources.GROUP, Resources.GROUP_MEMBERSHIP, Resources.PROCESS_INSTANCE, Resources.REPORT, Resources.TASK, Resources.TENANT, Resources.TENANT_MEMBERSHIP, Resources.USER)), 
    DELETE("DELETE", 16, EnumSet.of(Resources.AUTHORIZATION, Resources.BATCH, Resources.DASHBOARD, Resources.DEPLOYMENT, Resources.FILTER, Resources.GROUP, Resources.GROUP_MEMBERSHIP, Resources.PROCESS_DEFINITION, Resources.PROCESS_INSTANCE, Resources.REPORT, Resources.TASK, Resources.TENANT, Resources.TENANT_MEMBERSHIP, Resources.USER)), 
    ACCESS("ACCESS", 32, EnumSet.of(Resources.APPLICATION)), 
    READ_TASK("READ_TASK", 64, EnumSet.of(Resources.PROCESS_DEFINITION)), 
    UPDATE_TASK("UPDATE_TASK", 128, EnumSet.of(Resources.PROCESS_DEFINITION)), 
    CREATE_INSTANCE("CREATE_INSTANCE", 256, EnumSet.of(Resources.DECISION_DEFINITION, Resources.PROCESS_DEFINITION)), 
    READ_INSTANCE("READ_INSTANCE", 512, EnumSet.of(Resources.PROCESS_DEFINITION)), 
    UPDATE_INSTANCE("UPDATE_INSTANCE", 1024, EnumSet.of(Resources.PROCESS_DEFINITION)), 
    DELETE_INSTANCE("DELETE_INSTANCE", 2048, EnumSet.of(Resources.PROCESS_DEFINITION)), 
    READ_HISTORY("READ_HISTORY", 4096, EnumSet.of(Resources.BATCH, Resources.DECISION_DEFINITION, Resources.PROCESS_DEFINITION, Resources.TASK)), 
    DELETE_HISTORY("DELETE_HISTORY", 8192, EnumSet.of(Resources.BATCH, Resources.DECISION_DEFINITION, Resources.PROCESS_DEFINITION)), 
    TASK_WORK("TASK_WORK", 16384, EnumSet.of(Resources.PROCESS_DEFINITION, Resources.TASK)), 
    TASK_ASSIGN("TASK_ASSIGN", 32768, EnumSet.of(Resources.PROCESS_DEFINITION, Resources.TASK)), 
    MIGRATE_INSTANCE("MIGRATE_INSTANCE", 65536, EnumSet.of(Resources.PROCESS_DEFINITION));
    
    private String name;
    private int id;
    private Resource[] resourceTypes;
    
    private Permissions(final String name, final int id) {
        this.name = name;
        this.id = id;
    }
    
    private Permissions(final String name, final int id, final EnumSet<Resources> resourceTypes) {
        this.name = name;
        this.id = id;
        this.resourceTypes = resourceTypes.toArray(new Resource[resourceTypes.size()]);
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public int getValue() {
        return this.id;
    }
    
    @Override
    public Resource[] getTypes() {
        return this.resourceTypes;
    }
    
    public static Permission forName(final String name) {
        final Permission permission = valueOf(name);
        return permission;
    }
}
