// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

@Deprecated
public enum OptimizePermissions implements Permission
{
    NONE("NONE", 0), 
    ALL("ALL", Integer.MAX_VALUE), 
    EDIT("EDIT", 2), 
    SHARE("SHARE", 4);
    
    private static final Resource[] RESOURCES;
    private String name;
    private int id;
    
    private OptimizePermissions(final String name, final int id) {
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
        return OptimizePermissions.RESOURCES;
    }
    
    static {
        RESOURCES = new Resource[] { Resources.OPTIMIZE };
    }
}
