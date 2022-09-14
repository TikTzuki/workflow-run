// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public enum ProcessInstancePermissions implements Permission
{
    NONE("NONE", 0), 
    ALL("ALL", Integer.MAX_VALUE), 
    READ("READ", 2), 
    UPDATE("UPDATE", 4), 
    CREATE("CREATE", 8), 
    DELETE("DELETE", 16), 
    RETRY_JOB("RETRY_JOB", 32), 
    SUSPEND("SUSPEND", 64), 
    UPDATE_VARIABLE("UPDATE_VARIABLE", 128);
    
    private static final Resource[] RESOURCES;
    private String name;
    private int id;
    
    private ProcessInstancePermissions(final String name, final int id) {
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
        return ProcessInstancePermissions.RESOURCES;
    }
    
    static {
        RESOURCES = new Resource[] { Resources.PROCESS_INSTANCE };
    }
}
