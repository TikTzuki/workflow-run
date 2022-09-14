// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface ExternalTaskState
{
    public static final ExternalTaskState CREATED = new ExternalTaskStateImpl(0, "created");
    public static final ExternalTaskState FAILED = new ExternalTaskStateImpl(1, "failed");
    public static final ExternalTaskState SUCCESSFUL = new ExternalTaskStateImpl(2, "successful");
    public static final ExternalTaskState DELETED = new ExternalTaskStateImpl(3, "deleted");
    
    int getStateCode();
    
    public static class ExternalTaskStateImpl implements ExternalTaskState
    {
        public final int stateCode;
        protected final String name;
        
        public ExternalTaskStateImpl(final int stateCode, final String string) {
            this.stateCode = stateCode;
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
            final ExternalTaskStateImpl other = (ExternalTaskStateImpl)obj;
            return this.stateCode == other.stateCode;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
