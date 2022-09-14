// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

public interface ActivityInstanceState
{
    public static final ActivityInstanceState DEFAULT = new ActivityInstanceStateImpl(0, "default");
    public static final ActivityInstanceState SCOPE_COMPLETE = new ActivityInstanceStateImpl(1, "scopeComplete");
    public static final ActivityInstanceState CANCELED = new ActivityInstanceStateImpl(2, "canceled");
    public static final ActivityInstanceState STARTING = new ActivityInstanceStateImpl(3, "starting");
    public static final ActivityInstanceState ENDING = new ActivityInstanceStateImpl(4, "ending");
    
    int getStateCode();
    
    public static class ActivityInstanceStateImpl implements ActivityInstanceState
    {
        public final int stateCode;
        protected final String name;
        
        public ActivityInstanceStateImpl(final int suspensionCode, final String string) {
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
            final ActivityInstanceStateImpl other = (ActivityInstanceStateImpl)obj;
            return this.stateCode == other.stateCode;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
