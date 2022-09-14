// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import java.util.Iterator;
import java.util.Collections;
import org.zik.bpm.engine.impl.pvm.runtime.OutgoingExecution;
import java.util.ArrayList;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmLogger;

public class PvmAtomicOperationTransitionDestroyScope implements PvmAtomicOperation
{
    private static final PvmLogger LOG;
    
    @Override
    public boolean isAsync(final PvmExecutionImpl instance) {
        return false;
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
    
    @Override
    public void execute(final PvmExecutionImpl execution) {
        PvmExecutionImpl propagatingExecution = execution;
        final PvmActivity activity = execution.getActivity();
        final List<PvmTransition> transitionsToTake = execution.getTransitionsToTake();
        execution.setTransitionsToTake(null);
        if (execution.isScope() && activity.isScope()) {
            if (!LegacyBehavior.destroySecondNonScope(execution)) {
                if (execution.isConcurrent()) {
                    LegacyBehavior.destroyConcurrentScope(execution);
                }
                else {
                    propagatingExecution = execution.getParent();
                    PvmAtomicOperationTransitionDestroyScope.LOG.debugDestroyScope(execution, propagatingExecution);
                    execution.destroy();
                    propagatingExecution.setActivity(execution.getActivity());
                    propagatingExecution.setTransition(execution.getTransition());
                    propagatingExecution.setActive(true);
                    execution.remove();
                }
            }
        }
        else {
            propagatingExecution = execution;
        }
        if (transitionsToTake.isEmpty()) {
            throw new ProcessEngineException(execution.toString() + ": No outgoing transitions from activity " + activity);
        }
        if (transitionsToTake.size() == 1) {
            propagatingExecution.setTransition(transitionsToTake.get(0));
            propagatingExecution.take();
        }
        else {
            propagatingExecution.inactivate();
            final List<OutgoingExecution> outgoingExecutions = new ArrayList<OutgoingExecution>();
            for (int i = 0; i < transitionsToTake.size(); ++i) {
                final PvmTransition transition = transitionsToTake.get(i);
                final PvmExecutionImpl scopeExecution = propagatingExecution.isScope() ? propagatingExecution : propagatingExecution.getParent();
                PvmExecutionImpl concurrentExecution = null;
                if (i == 0) {
                    concurrentExecution = propagatingExecution;
                }
                else {
                    concurrentExecution = scopeExecution.createConcurrentExecution();
                    if (i == 1 && !propagatingExecution.isConcurrent()) {
                        outgoingExecutions.remove(0);
                        PvmExecutionImpl replacingExecution = null;
                        for (final PvmExecutionImpl concurrentChild : scopeExecution.getNonEventScopeExecutions()) {
                            if (concurrentChild != propagatingExecution) {
                                replacingExecution = concurrentChild;
                                break;
                            }
                        }
                        outgoingExecutions.add(new OutgoingExecution(replacingExecution, transitionsToTake.get(0)));
                    }
                }
                outgoingExecutions.add(new OutgoingExecution(concurrentExecution, transition));
            }
            Collections.reverse(outgoingExecutions);
            for (final OutgoingExecution outgoingExecution : outgoingExecutions) {
                outgoingExecution.take();
            }
        }
    }
    
    @Override
    public String getCanonicalName() {
        return "transition-destroy-scope";
    }
    
    static {
        LOG = ProcessEngineLogger.PVM_LOGGER;
    }
}
