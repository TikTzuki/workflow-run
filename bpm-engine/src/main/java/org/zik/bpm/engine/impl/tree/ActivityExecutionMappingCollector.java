// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import java.util.HashMap;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class ActivityExecutionMappingCollector implements TreeVisitor<ActivityExecution>
{
    private final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping;
    private final ActivityExecution initialExecution;
    private boolean initialized;
    
    public ActivityExecutionMappingCollector(final ActivityExecution execution) {
        this.activityExecutionMapping = new HashMap<ScopeImpl, PvmExecutionImpl>();
        this.initialized = false;
        this.initialExecution = execution;
    }
    
    @Override
    public void visit(final ActivityExecution execution) {
        if (!this.initialized) {
            this.appendActivityExecutionMapping(this.initialExecution);
            this.initialized = true;
        }
        this.appendActivityExecutionMapping(execution);
    }
    
    private void appendActivityExecutionMapping(final ActivityExecution execution) {
        if (execution.getActivity() != null && !LegacyBehavior.hasInvalidIntermediaryActivityId((PvmExecutionImpl)execution)) {
            this.activityExecutionMapping.putAll(execution.createActivityExecutionMapping());
        }
    }
    
    public PvmExecutionImpl getExecutionForScope(final PvmScope scope) {
        return this.activityExecutionMapping.get(scope);
    }
}
