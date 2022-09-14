// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

public interface SuspensionState
{
    public static final SuspensionState ACTIVE = new SuspensionStateImpl(1, "active");
    public static final SuspensionState SUSPENDED = new SuspensionStateImpl(2, "suspended");
    
    int getStateCode();
    
    String getName();
    
    public static class SuspensionStateImpl implements SuspensionState
    {
        public final int stateCode;
        protected final String name;
        
        public SuspensionStateImpl(final int suspensionCode, final String string) {
            this.stateCode = suspensionCode;
            this.name = string;
        }
        
        @Override
        public int getStateCode() {
            return this.stateCode;
        }
        
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = 31 * result + this.stateCode;
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
            final SuspensionStateImpl other = (SuspensionStateImpl)obj;
            return this.stateCode == other.stateCode;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
