// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;
import org.zik.bpm.engine.impl.pvm.PvmException;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.exception.cmmn.CaseIllegalStateTransitionException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class CmmnBehaviorLogger extends ProcessEngineLogger
{
    protected final String caseStateTransitionMessage = "Could not perform transition '{} on case execution with id '{}'.";
    
    public ProcessEngineException ruleExpressionNotBooleanException(final Object result) {
        return new ProcessEngineException(this.exceptionMessage("001", "Rule expression returns a non-boolean value. Value: '{}', Class: '{}'", new Object[] { result, result.getClass().getName() }));
    }
    
    public CaseIllegalStateTransitionException forbiddenManualCompletitionException(final String transition, final String id, final String type) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("002", "Could not perform transition '{} on case execution with id '{}'.Reason: It is not possible to manually complete the case execution which is associated with an element of type {}.", new Object[] { transition, id, type }));
    }
    
    public CaseIllegalStateTransitionException criteriaNotAllowedException(final String criteria, final String id, final String additionalMessage) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("003", "Cannot trigger case execution with id '{}' because {} criteria is not allowed for {}.", new Object[] { id, criteria, additionalMessage }));
    }
    
    public CaseIllegalStateTransitionException criteriaNotAllowedForEventListenerOrMilestonesException(final String criteria, final String id) {
        return this.criteriaNotAllowedException(criteria, id, "event listener or milestones");
    }
    
    public CaseIllegalStateTransitionException criteriaNotAllowedForEventListenerException(final String criteria, final String id) {
        return this.criteriaNotAllowedException(criteria, id, "event listener");
    }
    
    public CaseIllegalStateTransitionException criteriaNotAllowedForCaseInstanceException(final String criteria, final String id) {
        return this.criteriaNotAllowedException(criteria, id, "case instances");
    }
    
    CaseIllegalStateTransitionException executionAlreadyCompletedException(final String transition, final String id) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("004", "Could not perform transition '{} on case execution with id '{}'.Reason: Case execution must be available or suspended, but was completed.", new Object[] { transition, id }));
    }
    
    public CaseIllegalStateTransitionException resumeInactiveCaseException(final String transition, final String id) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("005", "Could not perform transition '{} on case execution with id '{}'.Reason: It is not possible to resume the case execution which parent is not active.", new Object[] { transition, id }));
    }
    
    public CaseIllegalStateTransitionException illegalStateTransitionException(final String transition, final String id, final String typeName) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("006", "Could not perform transition '{} on case execution with id '{}'.Reason: It is not possible to {} the case execution which is associated with a {}", new Object[] { transition, id, transition, typeName }));
    }
    
    public CaseIllegalStateTransitionException alreadyStateCaseException(final String transition, final String id, final String state) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("007", "Could not perform transition '{} on case execution with id '{}'.Reason: The case instance is already {}.", new Object[] { transition, id, state }));
    }
    
    public CaseIllegalStateTransitionException alreadyClosedCaseException(final String transition, final String id) {
        return this.alreadyStateCaseException(transition, id, "closed");
    }
    
    public CaseIllegalStateTransitionException alreadyActiveException(final String transition, final String id) {
        return this.alreadyStateCaseException(transition, id, "active");
    }
    
    public CaseIllegalStateTransitionException alreadyTerminatedException(final String transition, final String id) {
        return this.alreadyStateCaseException(transition, id, "terminated");
    }
    
    public CaseIllegalStateTransitionException alreadySuspendedException(final String transition, final String id) {
        return this.alreadyStateCaseException(transition, id, "suspended");
    }
    
    public CaseIllegalStateTransitionException wrongCaseStateException(final String transition, final String id, final String acceptedState, final String currentState) {
        return this.wrongCaseStateException(transition, id, transition, acceptedState, currentState);
    }
    
    public CaseIllegalStateTransitionException wrongCaseStateException(final String transition, final String id, final String altTransition, final String acceptedState, final String currentState) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("008", "Could not perform transition '{} on case execution with id '{}'.Reason: The case instance must be in state '{}' to {} it, but the state is '{}'.", new Object[] { transition, id, acceptedState, transition, currentState }));
    }
    
    public CaseIllegalStateTransitionException notACaseInstanceException(final String transition, final String id) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("009", "Could not perform transition '{} on case execution with id '{}'.Reason: It is not possible to close a case execution which is not a case instance.", new Object[] { transition, id }));
    }
    
    public CaseIllegalStateTransitionException isAlreadyInStateException(final String transition, final String id, final CaseExecutionState state) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("010", "Could not perform transition '{} on case execution with id '{}'.Reason: The case execution is already in state '{}'.", new Object[] { transition, id, state }));
    }
    
    public CaseIllegalStateTransitionException unexpectedStateException(final String transition, final String id, final CaseExecutionState expectedState, final CaseExecutionState currentState) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("011", "Could not perform transition '{} on case execution with id '{}'.Reason: The case execution must be in state '{}' to {}, but it was in state '{}'", new Object[] { transition, id, expectedState, transition, currentState }));
    }
    
    public CaseIllegalStateTransitionException impossibleTransitionException(final String transition, final String id) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("012", "Could not perform transition '{} on case execution with id '{}'.Reason: The transition is not possible for this case instance.", new Object[] { transition, id }));
    }
    
    public CaseIllegalStateTransitionException remainingChildException(final String transition, final String id, final String childId, final CaseExecutionState childState) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("013", "Could not perform transition '{} on case execution with id '{}'.Reason: There is a child case execution with id '{}' in state '{}'", new Object[] { transition, id, childId, childState }));
    }
    
    public CaseIllegalStateTransitionException wrongChildStateException(final String transition, final String id, final String childId, final String stateList) {
        return new CaseIllegalStateTransitionException(this.exceptionMessage("014", "Could not perform transition '{} on case execution with id '{}'.Reason: There is a child case execution with id '{}' which is in one of the following states: {}", new Object[] { transition, id, childId, stateList }));
    }
    
    public PvmException transitCaseException(final String transition, final String id, final CaseExecutionState currentState) {
        return new PvmException(this.exceptionMessage("015", "Could not perform transition '{} on case execution with id '{}'.Reason: Expected case execution state to be {terminatingOnTermination|terminatingOnExit} but it was '{}'.", new Object[] { transition, id, currentState }));
    }
    
    public PvmException suspendCaseException(final String id, final CaseExecutionState currentState) {
        return this.transitCaseException("suspend", id, currentState);
    }
    
    public PvmException terminateCaseException(final String id, final CaseExecutionState currentState) {
        return this.transitCaseException("terminate", id, currentState);
    }
    
    public ProcessEngineException missingDelegateParentClassException(final String className, final String parentClass) {
        return new ProcessEngineException(this.exceptionMessage("016", "Class '{}' doesn't implement '{}'.", new Object[] { className, parentClass }));
    }
    
    public UnsupportedOperationException unsupportedTransientOperationException(final String className) {
        return new UnsupportedOperationException(this.exceptionMessage("017", "Class '{}' is not supported in transient CaseExecutionImpl", new Object[] { className }));
    }
    
    public ProcessEngineException invokeVariableListenerException(final Throwable cause) {
        return new ProcessEngineException(this.exceptionMessage("018", "Variable listener invocation failed. Reason: {}", new Object[] { cause.getMessage() }), cause);
    }
    
    public ProcessEngineException decisionDefinitionEvaluationFailed(final CmmnActivityExecution execution, final Exception cause) {
        return new ProcessEngineException(this.exceptionMessage("019", "Could not evaluate decision in case execution '" + execution.getId() + "'. Reason: {}", new Object[] { cause.getMessage() }), cause);
    }
}
