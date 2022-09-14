// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;

public class AtomicOperationCaseExecutionDeleteCascade implements CmmnAtomicOperation
{
    @Override
    public String getCanonicalName() {
        return "delete-cascade";
    }
    
    protected CmmnExecution findFirstLeaf(final CmmnExecution execution) {
        final List<? extends CmmnExecution> executions = execution.getCaseExecutions();
        if (executions.size() > 0) {
            return this.findFirstLeaf((CmmnExecution)executions.get(0));
        }
        return execution;
    }
    
    @Override
    public void execute(final CmmnExecution execution) {
        final CmmnExecution firstLeaf = this.findFirstLeaf(execution);
        firstLeaf.remove();
        final CmmnExecution parent = firstLeaf.getParent();
        if (parent != null) {
            parent.deleteCascade();
        }
    }
    
    @Override
    public boolean isAsync(final CmmnExecution execution) {
        return false;
    }
}
