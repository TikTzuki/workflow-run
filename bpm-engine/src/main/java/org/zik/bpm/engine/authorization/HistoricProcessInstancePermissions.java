// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

public enum HistoricProcessInstancePermissions implements Permission
{
    NONE("NONE", 0), 
    ALL("ALL", Integer.MAX_VALUE), 
    READ("READ", 2);
    
    protected static final Resource[] RESOURCES;
    protected String name;
    protected int id;
    
    private HistoricProcessInstancePermissions(final String name, final int id) {
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
        return HistoricProcessInstancePermissions.RESOURCES;
    }
    
    static {
        RESOURCES = new Resource[] { Resources.HISTORIC_PROCESS_INSTANCE };
    }
}
