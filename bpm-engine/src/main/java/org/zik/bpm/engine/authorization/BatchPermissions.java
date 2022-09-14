// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public enum BatchPermissions implements Permission
{
    NONE("NONE", 0), 
    ALL("ALL", Integer.MAX_VALUE), 
    READ("READ", 2), 
    UPDATE("UPDATE", 4), 
    CREATE("CREATE", 8), 
    DELETE("DELETE", 16), 
    READ_HISTORY("READ_HISTORY", 4096), 
    DELETE_HISTORY("DELETE_HISTORY", 8192), 
    CREATE_BATCH_MIGRATE_PROCESS_INSTANCES("CREATE_BATCH_MIGRATE_PROCESS_INSTANCES", 32), 
    CREATE_BATCH_MODIFY_PROCESS_INSTANCES("CREATE_BATCH_MODIFY_PROCESS_INSTANCES", 64), 
    CREATE_BATCH_RESTART_PROCESS_INSTANCES("CREATE_BATCH_RESTART_PROCESS_INSTANCES", 128), 
    CREATE_BATCH_DELETE_RUNNING_PROCESS_INSTANCES("CREATE_BATCH_DELETE_RUNNING_PROCESS_INSTANCES", 256), 
    CREATE_BATCH_DELETE_FINISHED_PROCESS_INSTANCES("CREATE_BATCH_DELETE_FINISHED_PROCESS_INSTANCES", 512), 
    CREATE_BATCH_DELETE_DECISION_INSTANCES("CREATE_BATCH_DELETE_DECISION_INSTANCES", 1024), 
    CREATE_BATCH_SET_JOB_RETRIES("CREATE_BATCH_SET_JOB_RETRIES", 2048), 
    CREATE_BATCH_SET_EXTERNAL_TASK_RETRIES("CREATE_BATCH_SET_EXTERNAL_TASK_RETRIES", 16384), 
    CREATE_BATCH_UPDATE_PROCESS_INSTANCES_SUSPEND("CREATE_BATCH_UPDATE_PROCESS_INSTANCES_SUSPEND", 32768), 
    CREATE_BATCH_SET_REMOVAL_TIME("CREATE_BATCH_SET_REMOVAL_TIME", 65536), 
    CREATE_BATCH_SET_VARIABLES("CREATE_BATCH_SET_VARIABLES", 131072), 
    CREATE_BATCH_CORRELATE_MESSAGE("CREATE_BATCH_CORRELATE_MESSAGE", 262144);
    
    protected static final Resource[] RESOURCES;
    protected String name;
    protected int id;
    
    private BatchPermissions(final String name, final int id) {
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
        return BatchPermissions.RESOURCES;
    }
    
    public static Permission forName(final String name) {
        final Permission permission = valueOf(name);
        return permission;
    }
    
    static {
        RESOURCES = new Resource[] { Resources.BATCH };
    }
}
