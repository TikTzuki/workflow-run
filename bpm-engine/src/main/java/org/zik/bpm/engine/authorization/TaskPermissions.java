// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public enum TaskPermissions implements Permission
{
    NONE("NONE", 0), 
    ALL("ALL", Integer.MAX_VALUE), 
    READ("READ", 2), 
    UPDATE("UPDATE", 4), 
    CREATE("CREATE", 8), 
    DELETE("DELETE", 16), 
    @Deprecated
    READ_HISTORY("READ_HISTORY", 4096), 
    TASK_WORK("TASK_WORK", 16384), 
    TASK_ASSIGN("TASK_ASSIGN", 32768), 
    UPDATE_VARIABLE("UPDATE_VARIABLE", 32), 
    READ_VARIABLE("READ_VARIABLE", 64);
    
    private static final Resource[] RESOURCES;
    private String name;
    private int id;
    
    private TaskPermissions(final String name, final int id) {
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
        return TaskPermissions.RESOURCES;
    }
    
    static {
        RESOURCES = new Resource[] { Resources.TASK };
    }
}
