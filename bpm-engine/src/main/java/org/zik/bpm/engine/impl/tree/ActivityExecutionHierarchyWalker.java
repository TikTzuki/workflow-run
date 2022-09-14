// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Map;

public class ActivityExecutionHierarchyWalker extends SingleReferenceWalker<ActivityExecutionTuple>
{
    private Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping;
    
    public ActivityExecutionHierarchyWalker(final ActivityExecution execution) {
        super(createTuple(execution));
        this.activityExecutionMapping = execution.createActivityExecutionMapping();
    }
    
    @Override
    protected ActivityExecutionTuple nextElement() {
        final ActivityExecutionTuple currentElement = this.getCurrentElement();
        final PvmScope currentScope = currentElement.getScope();
        PvmExecutionImpl currentExecution = (PvmExecutionImpl)currentElement.getExecution();
        final PvmScope flowScope = currentScope.getFlowScope();
        if (!currentExecution.isScope()) {
            currentExecution = this.activityExecutionMapping.get(currentScope);
            return new ActivityExecutionTuple(currentScope, currentExecution);
        }
        if (flowScope != null) {
            final PvmExecutionImpl execution = this.activityExecutionMapping.get(flowScope);
            return new ActivityExecutionTuple(flowScope, execution);
        }
        currentExecution = this.activityExecutionMapping.get(currentScope);
        final PvmExecutionImpl superExecution = currentExecution.getSuperExecution();
        if (superExecution != null) {
            this.activityExecutionMapping = superExecution.createActivityExecutionMapping();
            return createTuple(superExecution);
        }
        return null;
    }
    
    protected static ActivityExecutionTuple createTuple(final ActivityExecution execution) {
        final PvmScope flowScope = getCurrentFlowScope(execution);
        return new ActivityExecutionTuple(flowScope, execution);
    }
    
    protected static PvmScope getCurrentFlowScope(final ActivityExecution execution) {
        ScopeImpl scope = null;
        if (execution.getTransition() != null) {
            scope = execution.getTransition().getDestination().getFlowScope();
        }
        else {
            scope = (ScopeImpl)execution.getActivity();
        }
        if (scope.isScope()) {
            return scope;
        }
        return scope.getFlowScope();
    }
    
    public ReferenceWalker<ActivityExecutionTuple> addScopePreVisitor(final TreeVisitor<PvmScope> visitor) {
        return this.addPreVisitor(new ScopeVisitorWrapper(visitor));
    }
    
    public ReferenceWalker<ActivityExecutionTuple> addScopePostVisitor(final TreeVisitor<PvmScope> visitor) {
        return this.addPostVisitor(new ScopeVisitorWrapper(visitor));
    }
    
    public ReferenceWalker<ActivityExecutionTuple> addExecutionPreVisitor(final TreeVisitor<ActivityExecution> visitor) {
        return this.addPreVisitor(new ExecutionVisitorWrapper(visitor));
    }
    
    public ReferenceWalker<ActivityExecutionTuple> addExecutionPostVisitor(final TreeVisitor<ActivityExecution> visitor) {
        return this.addPostVisitor(new ExecutionVisitorWrapper(visitor));
    }
    
    private class ExecutionVisitorWrapper implements TreeVisitor<ActivityExecutionTuple>
    {
        private final TreeVisitor<ActivityExecution> collector;
        
        public ExecutionVisitorWrapper(final TreeVisitor<ActivityExecution> collector) {
            this.collector = collector;
        }
        
        @Override
        public void visit(final ActivityExecutionTuple tuple) {
            this.collector.visit(tuple.getExecution());
        }
    }
    
    private class ScopeVisitorWrapper implements TreeVisitor<ActivityExecutionTuple>
    {
        private final TreeVisitor<PvmScope> collector;
        
        public ScopeVisitorWrapper(final TreeVisitor<PvmScope> collector) {
            this.collector = collector;
        }
        
        @Override
        public void visit(final ActivityExecutionTuple tuple) {
            this.collector.visit(tuple.getScope());
        }
    }
}
