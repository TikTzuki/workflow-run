// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.execution;

import java.util.HashMap;
import java.util.Map;

public interface CaseExecutionState
{
    public static final Map<Integer, CaseExecutionState> CASE_EXECUTION_STATES = new HashMap<Integer, CaseExecutionState>();
    public static final CaseExecutionState NEW = new CaseExecutionStateImpl(0, "new");
    public static final CaseExecutionState AVAILABLE = new CaseExecutionStateImpl(1, "available");
    public static final CaseExecutionState ENABLED = new CaseExecutionStateImpl(2, "enabled");
    public static final CaseExecutionState DISABLED = new CaseExecutionStateImpl(3, "disabled");
    public static final CaseExecutionState ACTIVE = new CaseExecutionStateImpl(4, "active");
    public static final CaseExecutionState SUSPENDED = new CaseExecutionStateImpl(5, "suspended");
    public static final CaseExecutionState TERMINATED = new CaseExecutionStateImpl(6, "terminated");
    public static final CaseExecutionState COMPLETED = new CaseExecutionStateImpl(7, "completed");
    public static final CaseExecutionState FAILED = new CaseExecutionStateImpl(8, "failed");
    public static final CaseExecutionState CLOSED = new CaseExecutionStateImpl(9, "closed");
    public static final CaseExecutionState TERMINATING_ON_TERMINATION = new CaseExecutionStateImpl(10, "terminatingOnTermination");
    public static final CaseExecutionState TERMINATING_ON_PARENT_TERMINATION = new CaseExecutionStateImpl(11, "terminatingOnParentTermination");
    public static final CaseExecutionState TERMINATING_ON_EXIT = new CaseExecutionStateImpl(12, "terminatingOnExit");
    public static final CaseExecutionState SUSPENDING_ON_SUSPENSION = new CaseExecutionStateImpl(13, "suspendingOnSuspension");
    public static final CaseExecutionState SUSPENDING_ON_PARENT_SUSPENSION = new CaseExecutionStateImpl(14, "suspendingOnParentSuspension");
    
    int getStateCode();
    
    public static class CaseExecutionStateImpl implements CaseExecutionState
    {
        public final int stateCode;
        protected final String name;
        
        public CaseExecutionStateImpl(final int stateCode, final String string) {
            this.stateCode = stateCode;
            this.name = string;
            CaseExecutionStateImpl.CASE_EXECUTION_STATES.put(stateCode, this);
        }
        
        public static CaseExecutionState getStateForCode(final Integer stateCode) {
            return CaseExecutionStateImpl.CASE_EXECUTION_STATES.get(stateCode);
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
            final CaseExecutionStateImpl other = (CaseExecutionStateImpl)obj;
            return this.stateCode == other.stateCode;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
