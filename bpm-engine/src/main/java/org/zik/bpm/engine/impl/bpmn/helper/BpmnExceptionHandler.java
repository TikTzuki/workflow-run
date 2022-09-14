// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.helper;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.bpmn.parser.ErrorEventDefinition;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.tree.ActivityExecutionTuple;
import org.zik.bpm.engine.impl.tree.ReferenceWalker;
import org.zik.bpm.engine.impl.tree.OutputVariablesPropagator;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.tree.TreeVisitor;
import org.zik.bpm.engine.impl.tree.ActivityExecutionMappingCollector;
import org.zik.bpm.engine.impl.tree.ActivityExecutionHierarchyWalker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BpmnError;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.behavior.BpmnBehaviorLogger;

public class BpmnExceptionHandler
{
    private static final BpmnBehaviorLogger LOG;
    
    public static void propagateException(final ActivityExecution execution, final Exception ex) throws Exception {
        final BpmnError bpmnError = checkIfCauseOfExceptionIsBpmnError(ex);
        if (bpmnError != null) {
            propagateBpmnError(bpmnError, execution);
        }
        else {
            propagateExceptionAsError(ex, execution);
        }
    }
    
    protected static void propagateExceptionAsError(final Exception exception, final ActivityExecution execution) throws Exception {
        if (isProcessEngineExceptionWithoutCause(exception) || isTransactionNotActive()) {
            throw exception;
        }
        propagateError(null, exception.getMessage(), exception, execution);
    }
    
    protected static boolean isTransactionNotActive() {
        return !Context.getCommandContext().getTransactionContext().isTransactionActive();
    }
    
    protected static boolean isProcessEngineExceptionWithoutCause(final Exception exception) {
        return exception instanceof ProcessEngineException && exception.getCause() == null;
    }
    
    protected static BpmnError checkIfCauseOfExceptionIsBpmnError(final Throwable e) {
        if (e instanceof BpmnError) {
            return (BpmnError)e;
        }
        if (e.getCause() == null) {
            return null;
        }
        return checkIfCauseOfExceptionIsBpmnError(e.getCause());
    }
    
    public static void propagateBpmnError(final BpmnError error, final ActivityExecution execution) throws Exception {
        propagateError(error.getErrorCode(), error.getMessage(), null, execution);
    }
    
    public static void propagateError(final String errorCode, final String errorMessage, final Exception origException, final ActivityExecution execution) throws Exception {
        final ActivityExecutionHierarchyWalker walker = new ActivityExecutionHierarchyWalker(execution);
        final ErrorDeclarationForProcessInstanceFinder errorDeclarationFinder = new ErrorDeclarationForProcessInstanceFinder(origException, errorCode, execution.getActivity());
        final ActivityExecutionMappingCollector activityExecutionMappingCollector = new ActivityExecutionMappingCollector(execution);
        walker.addScopePreVisitor(errorDeclarationFinder);
        walker.addExecutionPreVisitor(activityExecutionMappingCollector);
        walker.addExecutionPreVisitor(new OutputVariablesPropagator());
        try {
            walker.walkUntil(new ReferenceWalker.WalkCondition<ActivityExecutionTuple>() {
                @Override
                public boolean isFulfilled(final ActivityExecutionTuple element) {
                    return errorDeclarationFinder.getErrorEventDefinition() != null || element == null;
                }
            });
        }
        catch (Exception e) {
            BpmnExceptionHandler.LOG.errorPropagationException(execution.getActivityInstanceId(), e);
            throw new ErrorPropagationException(e.getCause());
        }
        final PvmActivity errorHandlingActivity = errorDeclarationFinder.getErrorHandlerActivity();
        if (errorHandlingActivity == null) {
            if (origException != null) {
                throw origException;
            }
            if (Context.getCommandContext().getProcessEngineConfiguration().isEnableExceptionsAfterUnhandledBpmnError()) {
                throw BpmnExceptionHandler.LOG.missingBoundaryCatchEventError(execution.getActivity().getId(), errorCode);
            }
            BpmnExceptionHandler.LOG.missingBoundaryCatchEvent(execution.getActivity().getId(), errorCode);
            execution.end(true);
        }
        else {
            final ErrorEventDefinition errorDefinition = errorDeclarationFinder.getErrorEventDefinition();
            final PvmExecutionImpl errorHandlingExecution = activityExecutionMappingCollector.getExecutionForScope(errorHandlingActivity.getEventScope());
            if (errorDefinition.getErrorCodeVariable() != null) {
                errorHandlingExecution.setVariable(errorDefinition.getErrorCodeVariable(), errorCode);
            }
            if (errorDefinition.getErrorMessageVariable() != null) {
                errorHandlingExecution.setVariable(errorDefinition.getErrorMessageVariable(), errorMessage);
            }
            errorHandlingExecution.executeActivity(errorHandlingActivity);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
