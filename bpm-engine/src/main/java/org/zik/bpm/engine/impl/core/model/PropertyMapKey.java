// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.model;

public class PropertyMapKey<K, V>
{
    protected final String name;
    protected boolean allowOverwrite;
    
    public PropertyMapKey(final String name) {
        this(name, true);
    }
    
    public PropertyMapKey(final String name, final boolean allowOverwrite) {
        this.allowOverwrite = true;
        this.name = name;
        this.allowOverwrite = allowOverwrite;
    }
    
    public boolean allowsOverwrite() {
        return this.allowOverwrite;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return "PropertyMapKey [name=" + this.name + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final PropertyMapKey<?, ?> other = (PropertyMapKey<?, ?>)obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
