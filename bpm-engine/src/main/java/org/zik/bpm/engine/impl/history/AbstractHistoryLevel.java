// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

public abstract class AbstractHistoryLevel implements HistoryLevel
{
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.getId();
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
        final AbstractHistoryLevel other = (AbstractHistoryLevel)obj;
        return this.getId() == other.getId();
    }
    
    @Override
    public String toString() {
        return String.format("%s(name=%s, id=%d)", this.getClass().getSimpleName(), this.getName(), this.getId());
    }
}
