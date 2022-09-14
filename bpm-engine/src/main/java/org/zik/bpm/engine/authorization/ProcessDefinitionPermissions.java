// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public enum ProcessDefinitionPermissions implements Permission
{
    NONE("NONE", 0), 
    ALL("ALL", Integer.MAX_VALUE), 
    READ("READ", 2), 
    UPDATE("UPDATE", 4), 
    DELETE("DELETE", 16), 
    READ_TASK("READ_TASK", 64), 
    UPDATE_TASK("UPDATE_TASK", 128), 
    CREATE_INSTANCE("CREATE_INSTANCE", 256), 
    READ_INSTANCE("READ_INSTANCE", 512), 
    UPDATE_INSTANCE("UPDATE_INSTANCE", 1024), 
    DELETE_INSTANCE("DELETE_INSTANCE", 2048), 
    READ_HISTORY("READ_HISTORY", 4096), 
    DELETE_HISTORY("DELETE_HISTORY", 8192), 
    TASK_WORK("TASK_WORK", 16384), 
    TASK_ASSIGN("TASK_ASSIGN", 32768), 
    MIGRATE_INSTANCE("MIGRATE_INSTANCE", 65536), 
    RETRY_JOB("RETRY_JOB", 32), 
    SUSPEND("SUSPEND", 1048576), 
    SUSPEND_INSTANCE("SUSPEND_INSTANCE", 131072), 
    UPDATE_INSTANCE_VARIABLE("UPDATE_INSTANCE_VARIABLE", 262144), 
    UPDATE_TASK_VARIABLE("UPDATE_TASK_VARIABLE", 524288), 
    READ_INSTANCE_VARIABLE("READ_INSTANCE_VARIABLE", 2097152), 
    READ_HISTORY_VARIABLE("READ_HISTORY_VARIABLE", 4194304), 
    READ_TASK_VARIABLE("READ_TASK_VARIABLE", 8388608), 
    UPDATE_HISTORY("UPDATE_HISTORY", 16777216);
    
    private static final Resource[] RESOURCES;
    private String name;
    private int id;
    
    private ProcessDefinitionPermissions(final String name, final int id) {
        this.name = name;
        this.id = id;
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
        return ProcessDefinitionPermissions.RESOURCES;
    }
    
    static {
        RESOURCES = new Resource[] { Resources.PROCESS_DEFINITION };
    }
}
