// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

public interface JobState
{
    public static final JobState CREATED = new JobStateImpl(0, "created");
    public static final JobState FAILED = new JobStateImpl(1, "failed");
    public static final JobState SUCCESSFUL = new JobStateImpl(2, "successful");
    public static final JobState DELETED = new JobStateImpl(3, "deleted");
    
    int getStateCode();
    
    public static class JobStateImpl implements JobState
    {
        public final int stateCode;
        protected final String name;
        
        public JobStateImpl(final int stateCode, final String string) {
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
            final JobStateImpl other = (JobStateImpl)obj;
            return this.stateCode == other.stateCode;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
