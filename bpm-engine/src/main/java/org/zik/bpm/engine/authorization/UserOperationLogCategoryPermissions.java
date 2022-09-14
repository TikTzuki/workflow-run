// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public enum UserOperationLogCategoryPermissions implements Permission
{
    NONE("NONE", 0), 
    ALL("ALL", Integer.MAX_VALUE), 
    READ("READ", 2), 
    UPDATE("UPDATE", 4), 
    DELETE("DELETE", 16);
    
    private static final Resource[] RESOURCES;
    private String name;
    private int id;
    
    private UserOperationLogCategoryPermissions(final String name, final int id) {
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
        return UserOperationLogCategoryPermissions.RESOURCES;
    }
    
    static {
        RESOURCES = new Resource[] { Resources.OPERATION_LOG_CATEGORY };
    }
}
