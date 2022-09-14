// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface IncidentState
{
    public static final IncidentState DEFAULT = new IncidentStateImpl(0, "open");
    public static final IncidentState RESOLVED = new IncidentStateImpl(1, "resolved");
    public static final IncidentState DELETED = new IncidentStateImpl(2, "deleted");
    
    int getStateCode();
    
    public static class IncidentStateImpl implements IncidentState
    {
        public final int stateCode;
        protected final String name;
        
        public IncidentStateImpl(final int suspensionCode, final String string) {
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
            final IncidentStateImpl other = (IncidentStateImpl)obj;
            return this.stateCode == other.stateCode;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
