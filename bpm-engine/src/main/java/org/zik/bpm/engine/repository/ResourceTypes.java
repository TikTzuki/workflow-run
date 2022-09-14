// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public enum ResourceTypes implements ResourceType
{
    REPOSITORY("REPOSITORY", Integer.valueOf(1)), 
    RUNTIME("RUNTIME", Integer.valueOf(2)), 
    HISTORY("HISTORY", Integer.valueOf(3));
    
    private String name;
    private Integer id;
    
    private ResourceTypes(final String name, final Integer id) {
        this.name = name;
        this.id = id;
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
    public Integer getValue() {
        return this.id;
    }
    
    public static ResourceType forName(final String name) {
        final ResourceType type = valueOf(name);
        return type;
    }
}
