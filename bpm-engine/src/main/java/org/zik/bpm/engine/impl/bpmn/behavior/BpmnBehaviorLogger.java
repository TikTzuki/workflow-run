// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.delegate.JavaDelegate;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class BpmnBehaviorLogger extends ProcessEngineLogger
{
    public void missingBoundaryCatchEvent(final String executionId, final String errorCode) {
        this.logInfo("001", "Execution with id '{}' throws an error event with errorCode '{}', but no catching boundary event was defined. Execution is ended (none end event semantics).", new Object[] { executionId, errorCode });
    }
    
    public void leavingActivity(final String activityId) {
        this.logDebug("002", "Leaving activity '{}'.", new Object[] { activityId });
    }
    
    public void missingOutgoingSequenceFlow(final String activityId) {
        this.logDebug("003", "No outgoing sequence flow found for activity '{}'. Ending execution.", new Object[] { activityId });
    }
    
    public ProcessEngineException stuckExecutionException(final String activityId) {
        return new ProcessEngineException(this.exceptionMessage("004", "No outgoing sequence flow for the element with id '{}' could be selected for continuing the process.", new Object[] { activityId }));
    }
    
    public ProcessEngineException missingDefaultFlowException(final String activityId, final String defaultSequenceFlow) {
        return new ProcessEngineException(this.exceptionMessage("005", "Default sequence flow '{}' for element with id '{}' could not be not found.", new Object[] { defaultSequenceFlow, activityId }));
    }
    
    public ProcessEngineException missingConditionalFlowException(final String activityId) {
        return new ProcessEngineException(this.exceptionMessage("006", "No conditional sequence flow leaving the Flow Node '{}' could be selected for continuing the process.", new Object[] { activityId }));
    }
    
    public ProcessEngineException incorrectlyUsedSignalException(final String className) {
        return new ProcessEngineException(this.exceptionMessage("007", "signal() can only be called on a '{}' instance.", new Object[] { className }));
    }
    
    public ProcessEngineException missingDelegateParentClassException(final String className, final String javaDelegate, final String activityBehavior) {
        return new ProcessEngineException(this.exceptionMessage("008", "Class '{}' doesn't implement '{}' nor '{}'.", new Object[] { className, javaDelegate, activityBehavior }));
    }
    
    public void outgoingSequenceFlowSelected(final String sequenceFlowId) {
        this.logDebug("009", "Sequence flow with id '{}' was selected as outgoing sequence flow.", new Object[] { sequenceFlowId });
    }
    
    public ProcessEngineException unsupportedSignalException(final String activityId) {
        return new ProcessEngineException(this.exceptionMessage("010", "The activity with id '{}' doesn't accept signals.", new Object[] { activityId }));
    }
    
    public void activityActivation(final String activityId) {
        this.logDebug("011", "Element with id '{}' activates.", new Object[] { activityId });
    }
    
    public void noActivityActivation(final String activityId) {
        this.logDebug("012", "Element with id '{}' does not activate.", new Object[] { activityId });
    }
    
    public void ignoringEventSubscription(final EventSubscriptionEntity eventSubscription, final String processDefinitionId) {
        this.logDebug("014", "Found event subscription '{}' but process definition with id '{}' could not be found.", new Object[] { eventSubscription.toString(), processDefinitionId });
    }
    
    public ProcessEngineException sendingEmailException(final String recipient, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("015", "Unable to send email to recipient '{}'.", new Object[] { recipient }), cause);
    }
    
    public ProcessEngineException emailFormatException() {
        return new ProcessEngineException(this.exceptionMessage("016", "'html' or 'text' is required to be defined as mail format when using the mail activity.", new Object[0]));
    }
    
    public ProcessEngineException emailCreationException(final String format, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("017", "Unable to create a mail with format '{}'.", new Object[] { format }), cause);
    }
    
    public ProcessEngineException addRecipientException(final String recipient, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("018", "Unable to add '{}' as recipient.", new Object[] { recipient }), cause);
    }
    
    public ProcessEngineException missingRecipientsException() {
        return new ProcessEngineException(this.exceptionMessage("019", "No recipient could be found for sending email.", new Object[0]));
    }
    
    public ProcessEngineException addSenderException(final String sender, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("020", "Could not set '{}' as from address in email.", new Object[] { sender }), cause);
    }
    
    public ProcessEngineException addCcException(final String cc, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("021", "Could not add '{}' as cc recipient.", new Object[] { cc }), cause);
    }
    
    public ProcessEngineException addBccException(final String bcc, final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("022", "Could not add '{}' as bcc recipient.", new Object[] { bcc }), cause);
    }
    
    public ProcessEngineException invalidAmountException(final String type, final int amount) {
        return new ProcessEngineException(this.exceptionMessage("023", "Invalid number of '{}': must be positive integer value or zero, but was '{}'.", new Object[] { type, amount }));
    }
    
    public ProcessEngineException unresolvableExpressionException(final String expression, final String type) {
        return new ProcessEngineException(this.exceptionMessage("024", "Expression '{}' didn't resolve to type '{}'.", new Object[] { expression, type }));
    }
    
    public ProcessEngineException invalidVariableTypeException(final String variable, final String type) {
        return new ProcessEngineException(this.exceptionMessage("025", "Variable '{}' is not of the expected type '{}'.", new Object[] { variable, type }));
    }
    
    public ProcessEngineException resolveCollectionExpressionOrVariableReferenceException() {
        return new ProcessEngineException(this.exceptionMessage("026", "Couldn't resolve collection expression nor variable reference", new Object[0]));
    }
    
    public ProcessEngineException expressionNotANumberException(final String type, final String expression) {
        return new ProcessEngineException(this.exceptionMessage("027", "Could not resolve expression of type '{}'. Expression '{}' needs to be a number or number String.", new Object[] { type, expression }));
    }
    
    public ProcessEngineException expressionNotBooleanException(final String type, final String expression) {
        return new ProcessEngineException(this.exceptionMessage("028", "Could not resolve expression of type '{}'. Expression '{}' needs to evaluate to a boolean value.", new Object[] { type, expression }));
    }
    
    public void multiInstanceCompletionConditionState(final Boolean state) {
        this.logDebug("029", "Completion condition of multi-instance satisfied: '{}'", new Object[] { state });
    }
    
    public void activityActivation(final String activityId, final int joinedExecutions, final int availableExecution) {
        this.logDebug("030", "Element with id '{}' activates. Joined '{}' of '{}' available executions.", new Object[] { activityId, joinedExecutions, availableExecution });
    }
    
    public void noActivityActivation(final String activityId, final int joinedExecutions, final int availableExecution) {
        this.logDebug("031", "Element with id '{}' does not activate. Joined '{}' of '{}' available executions.", new Object[] { activityId, joinedExecutions, availableExecution });
    }
    
    public ProcessEngineException unsupportedConcurrencyException(final String scopeExecutionId, final String className) {
        return new ProcessEngineException(this.exceptionMessage("032", "Execution '{}' with execution behavior of class '{}' cannot have concurrency.", new Object[] { scopeExecutionId, className }));
    }
    
    public ProcessEngineException resolveDelegateExpressionException(final Expression expression, final Class<?> parentClass, final Class<JavaDelegate> javaDelegateClass) {
        return new ProcessEngineException(this.exceptionMessage("033", "Delegate Expression '{}' did neither resolve to an implementation of '{}' nor '{}'.", new Object[] { expression, parentClass, javaDelegateClass }));
    }
    
    public ProcessEngineException shellExecutionException(final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("034", "Could not execute shell command.", new Object[0]), cause);
    }
    
    public void errorPropagationException(final String activityId, final Throwable cause) {
        this.logError("035", "caught an exception while propagate error in activity with id '{}'", new Object[] { activityId, cause });
    }
    
    public void debugConcurrentScopeIsPruned(final PvmExecutionImpl execution) {
        this.logDebug("036", "Concurrent scope is pruned {}", new Object[] { execution });
    }
    
    public void debugCancelConcurrentScopeExecution(final PvmExecutionImpl execution) {
        this.logDebug("037", "Cancel concurrent scope execution {}", new Object[] { execution });
    }
    
    public void destroyConcurrentScopeExecution(final PvmExecutionImpl execution) {
        this.logDebug("038", "Destroy concurrent scope execution", new Object[] { execution });
    }
    
    public void completeNonScopeEventSubprocess() {
        this.logDebug("039", "Destroy non-socpe event subprocess", new Object[0]);
    }
    
    public void endConcurrentExecutionInEventSubprocess() {
        this.logDebug("040", "End concurrent execution in event subprocess", new Object[0]);
    }
    
    public ProcessEngineException missingDelegateVariableMappingParentClassException(final String className, final String delegateVarMapping) {
        return new ProcessEngineException(this.exceptionMessage("041", "Class '{}' doesn't implement '{}'.", new Object[] { className, delegateVarMapping }));
    }
    
    public ProcessEngineException missingBoundaryCatchEventError(final String executionId, final String errorCode) {
        return new ProcessEngineException(this.exceptionMessage("042", "Execution with id '{}' throws an error event with errorCode '{}', but no error handler was defined. ", new Object[] { executionId, errorCode }));
    }
    
    public ProcessEngineException missingBoundaryCatchEventEscalation(final String executionId, final String escalationCode) {
        return new ProcessEngineException(this.exceptionMessage("043", "Execution with id '{}' throws an escalation event with escalationCode '{}', but no escalation handler was defined. ", new Object[] { executionId, escalationCode }));
    }
}
