// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.execution;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.delegate.CaseVariableListener;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.VariableListener;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnBehaviorLogger;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseSentryPartEntity;
import org.zik.bpm.engine.impl.cmmn.model.*;
import org.zik.bpm.engine.impl.cmmn.operation.CmmnAtomicOperation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.pvm.PvmException;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.task.TaskDecorator;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.variable.listener.CaseVariableListenerInvocation;
import org.zik.bpm.engine.impl.variable.listener.DelegateCaseVariableInstanceImpl;
import org.zik.bpm.engine.task.Task;

import java.util.*;

public abstract class CmmnExecution extends CoreExecution implements CmmnCaseInstance {
    protected static final CmmnBehaviorLogger LOG;
    private static final long serialVersionUID = 1L;
    protected transient CmmnCaseDefinition caseDefinition;
    protected transient CmmnActivity activity;
    protected boolean required;
    protected int previousState;
    protected int currentState;
    protected Queue<VariableEvent> variableEventsQueue;
    protected transient TaskEntity task;
    protected boolean entryCriterionSatisfied;

    public CmmnExecution() {
        this.required = false;
        this.currentState = CaseExecutionState.NEW.getStateCode();
        this.entryCriterionSatisfied = false;
    }

    @Override
    public abstract List<? extends CmmnExecution> getCaseExecutions();

    protected abstract List<? extends CmmnExecution> getCaseExecutionsInternal();

    @Override
    public CmmnExecution findCaseExecution(final String activityId) {
        if (this.getActivity() != null && this.getActivity().getId().equals(activityId)) {
            return this;
        }
        for (final CmmnExecution nestedExecution : this.getCaseExecutions()) {
            final CmmnExecution result = nestedExecution.findCaseExecution(activityId);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public TaskEntity getTask() {
        return this.task;
    }

    public void setTask(final Task task) {
        this.task = (TaskEntity) task;
    }

    @Override
    public TaskEntity createTask(final TaskDecorator taskDecorator) {
        final TaskEntity task = new TaskEntity((CaseExecutionEntity) this);
        task.insert();
        this.setTask(task);
        taskDecorator.decorate(task, this);
        task.transitionTo(TaskEntity.TaskState.STATE_CREATED);
        return task;
    }

    public abstract PvmExecutionImpl getSuperExecution();

    public abstract void setSuperExecution(final PvmExecutionImpl p0);

    public abstract PvmExecutionImpl getSubProcessInstance();

    public abstract void setSubProcessInstance(final PvmExecutionImpl p0);

    @Override
    public abstract PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition p0);

    @Override
    public abstract PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition p0, final String p1);

    @Override
    public abstract PvmExecutionImpl createSubProcessInstance(final PvmProcessDefinition p0, final String p1, final String p2);

    public abstract CmmnExecution getSubCaseInstance();

    public abstract void setSubCaseInstance(final CmmnExecution p0);

    @Override
    public abstract CmmnExecution createSubCaseInstance(final CmmnCaseDefinition p0);

    @Override
    public abstract CmmnExecution createSubCaseInstance(final CmmnCaseDefinition p0, final String p1);

    public abstract CmmnExecution getSuperCaseExecution();

    public abstract void setSuperCaseExecution(final CmmnExecution p0);

    protected abstract CmmnSentryPart newSentryPart();

    protected abstract void addSentryPart(final CmmnSentryPart p0);

    @Override
    public void createSentryParts() {
        final CmmnActivity activity = this.getActivity();
        EnsureUtil.ensureNotNull("Case execution '" + this.id + "': has no current activity", "activity", activity);
        final List<CmmnSentryDeclaration> sentries = activity.getSentries();
        if (sentries != null && !sentries.isEmpty()) {
            for (final CmmnSentryDeclaration sentryDeclaration : sentries) {
                final CmmnIfPartDeclaration ifPartDeclaration = sentryDeclaration.getIfPart();
                if (ifPartDeclaration != null) {
                    final CmmnSentryPart ifPart = this.createIfPart(sentryDeclaration, ifPartDeclaration);
                    this.addSentryPart(ifPart);
                }
                final List<CmmnOnPartDeclaration> onPartDeclarations = sentryDeclaration.getOnParts();
                for (final CmmnOnPartDeclaration onPartDeclaration : onPartDeclarations) {
                    final CmmnSentryPart onPart = this.createOnPart(sentryDeclaration, onPartDeclaration);
                    this.addSentryPart(onPart);
                }
                final List<CmmnVariableOnPartDeclaration> variableOnPartDeclarations = sentryDeclaration.getVariableOnParts();
                for (final CmmnVariableOnPartDeclaration variableOnPartDeclaration : variableOnPartDeclarations) {
                    final CmmnSentryPart variableOnPart = this.createVariableOnPart(sentryDeclaration, variableOnPartDeclaration);
                    this.addSentryPart(variableOnPart);
                }
            }
        }
    }

    protected CmmnSentryPart createOnPart(final CmmnSentryDeclaration sentryDeclaration, final CmmnOnPartDeclaration onPartDeclaration) {
        final CmmnSentryPart sentryPart = this.createSentryPart(sentryDeclaration, "planItemOnPart");
        final String standardEvent = onPartDeclaration.getStandardEvent();
        sentryPart.setStandardEvent(standardEvent);
        final CmmnActivity source = onPartDeclaration.getSource();
        EnsureUtil.ensureNotNull("The source of sentry '" + sentryDeclaration.getId() + "' is null.", "source", source);
        final String sourceActivityId = source.getId();
        sentryPart.setSource(sourceActivityId);
        return sentryPart;
    }

    protected CmmnSentryPart createIfPart(final CmmnSentryDeclaration sentryDeclaration, final CmmnIfPartDeclaration ifPartDeclaration) {
        return this.createSentryPart(sentryDeclaration, "ifPart");
    }

    protected CmmnSentryPart createVariableOnPart(final CmmnSentryDeclaration sentryDeclaration, final CmmnVariableOnPartDeclaration variableOnPartDeclaration) {
        final CmmnSentryPart sentryPart = this.createSentryPart(sentryDeclaration, "variableOnPart");
        final String variableEvent = variableOnPartDeclaration.getVariableEvent();
        sentryPart.setVariableEvent(variableEvent);
        final String variableName = variableOnPartDeclaration.getVariableName();
        sentryPart.setVariableName(variableName);
        return sentryPart;
    }

    protected CmmnSentryPart createSentryPart(final CmmnSentryDeclaration sentryDeclaration, final String type) {
        final CmmnSentryPart newSentryPart = this.newSentryPart();
        newSentryPart.setType(type);
        newSentryPart.setCaseInstance(this.getCaseInstance());
        newSentryPart.setCaseExecution(this);
        final String sentryId = sentryDeclaration.getId();
        newSentryPart.setSentryId(sentryId);
        return newSentryPart;
    }

    public void handleChildTransition(final CmmnExecution child, final String transition) {
        final List<String> affectedSentries = this.collectAffectedSentries(child, transition);
        this.forceUpdateOnSentries(affectedSentries);
        final List<String> satisfiedSentries = this.getSatisfiedSentries(affectedSentries);
        this.resetSentries(satisfiedSentries);
        this.fireSentries(satisfiedSentries);
    }

    @Override
    public void fireIfOnlySentryParts() {
        final Set<String> affectedSentries = new HashSet<String>();
        final List<CmmnSentryPart> sentryParts = this.collectSentryParts(this.getSentries());
        for (final CmmnSentryPart sentryPart : sentryParts) {
            if (this.isNotSatisfiedIfPartOnly(sentryPart)) {
                affectedSentries.add(sentryPart.getSentryId());
            }
        }
        final List<String> satisfiedSentries = this.getSatisfiedSentries(new ArrayList<String>(affectedSentries));
        this.resetSentries(satisfiedSentries);
        this.fireSentries(satisfiedSentries);
    }

    public void handleVariableTransition(final String variableName, final String transition) {
        final Map<String, List<CmmnSentryPart>> sentries = this.collectAllSentries();
        final List<CmmnSentryPart> sentryParts = this.collectSentryParts(sentries);
        final List<String> affectedSentries = this.collectAffectedSentriesWithVariableOnParts(variableName, transition, sentryParts);
        final List<CmmnSentryPart> affectedSentryParts = this.getAffectedSentryParts(sentries, affectedSentries);
        this.forceUpdateOnCaseSentryParts(affectedSentryParts);
        final List<String> allSentries = new ArrayList<String>(sentries.keySet());
        final List<String> satisfiedSentries = this.getSatisfiedSentriesInExecutionTree(allSentries, sentries);
        final List<CmmnSentryPart> satisfiedSentryParts = this.getAffectedSentryParts(sentries, satisfiedSentries);
        this.resetSentryParts(satisfiedSentryParts);
        this.fireSentries(satisfiedSentries);
    }

    protected List<String> collectAffectedSentries(final CmmnExecution child, final String transition) {
        final List<? extends CmmnSentryPart> sentryParts = this.getCaseSentryParts();
        final List<String> affectedSentries = new ArrayList<String>();
        for (final CmmnSentryPart sentryPart : sentryParts) {
            final String sourceCaseExecutionId = sentryPart.getSourceCaseExecutionId();
            final String sourceRef = sentryPart.getSource();
            if (child.getActivityId().equals(sourceRef) || child.getId().equals(sourceCaseExecutionId)) {
                final String standardEvent = sentryPart.getStandardEvent();
                if (!transition.equals(standardEvent)) {
                    continue;
                }
                this.addIdIfNotSatisfied(affectedSentries, sentryPart);
            }
        }
        return affectedSentries;
    }

    protected boolean isNotSatisfiedIfPartOnly(final CmmnSentryPart sentryPart) {
        return "ifPart".equals(sentryPart.getType()) && this.getSentries().get(sentryPart.getSentryId()).size() == 1 && !sentryPart.isSatisfied();
    }

    protected void addIdIfNotSatisfied(final List<String> affectedSentries, final CmmnSentryPart sentryPart) {
        if (!sentryPart.isSatisfied()) {
            final String sentryId = sentryPart.getSentryId();
            sentryPart.setSatisfied(true);
            if (!affectedSentries.contains(sentryId)) {
                affectedSentries.add(sentryId);
            }
        }
    }

    protected List<String> collectAffectedSentriesWithVariableOnParts(final String variableName, final String variableEvent, final List<CmmnSentryPart> sentryParts) {
        final List<String> affectedSentries = new ArrayList<String>();
        for (final CmmnSentryPart sentryPart : sentryParts) {
            final String sentryVariableName = sentryPart.getVariableName();
            final String sentryVariableEvent = sentryPart.getVariableEvent();
            final CmmnExecution execution = sentryPart.getCaseExecution();
            if ("variableOnPart".equals(sentryPart.getType()) && sentryVariableName.equals(variableName) && sentryVariableEvent.equals(variableEvent) && !this.hasVariableWithSameNameInParent(execution, sentryVariableName)) {
                this.addIdIfNotSatisfied(affectedSentries, sentryPart);
            }
        }
        return affectedSentries;
    }

    protected boolean hasVariableWithSameNameInParent(CmmnExecution execution, final String variableName) {
        while (execution != null) {
            if (execution.getId().equals(this.getId())) {
                return false;
            }
            final TypedValue variableTypedValue = execution.getVariableLocalTyped(variableName);
            if (variableTypedValue != null) {
                return true;
            }
            execution = execution.getParent();
        }
        return false;
    }

    protected Map<String, List<CmmnSentryPart>> collectAllSentries() {
        final Map<String, List<CmmnSentryPart>> sentries = new HashMap<String, List<CmmnSentryPart>>();
        final List<? extends CmmnExecution> caseExecutions = this.getCaseExecutions();
        for (final CmmnExecution caseExecution : caseExecutions) {
            sentries.putAll(caseExecution.collectAllSentries());
        }
        sentries.putAll(this.getSentries());
        return sentries;
    }

    protected List<CmmnSentryPart> getAffectedSentryParts(final Map<String, List<CmmnSentryPart>> allSentries, final List<String> affectedSentries) {
        final List<CmmnSentryPart> affectedSentryParts = new ArrayList<CmmnSentryPart>();
        for (final String affectedSentryId : affectedSentries) {
            affectedSentryParts.addAll(allSentries.get(affectedSentryId));
        }
        return affectedSentryParts;
    }

    protected List<CmmnSentryPart> collectSentryParts(final Map<String, List<CmmnSentryPart>> sentries) {
        final List<CmmnSentryPart> sentryParts = new ArrayList<CmmnSentryPart>();
        for (final String sentryId : sentries.keySet()) {
            sentryParts.addAll(sentries.get(sentryId));
        }
        return sentryParts;
    }

    protected void forceUpdateOnCaseSentryParts(final List<CmmnSentryPart> sentryParts) {
        for (final CmmnSentryPart sentryPart : sentryParts) {
            if (sentryPart instanceof CaseSentryPartEntity) {
                final CaseSentryPartEntity sentryPartEntity = (CaseSentryPartEntity) sentryPart;
                sentryPartEntity.forceUpdate();
            }
        }
    }

    protected List<String> getSatisfiedSentries(final List<String> sentryIds) {
        final List<String> result = new ArrayList<String>();
        if (sentryIds != null) {
            for (final String sentryId : sentryIds) {
                if (this.isSentrySatisfied(sentryId)) {
                    result.add(sentryId);
                }
            }
        }
        return result;
    }

    protected List<String> getSatisfiedSentriesInExecutionTree(final List<String> sentryIds, final Map<String, List<CmmnSentryPart>> allSentries) {
        final List<String> result = new ArrayList<String>();
        if (sentryIds != null) {
            for (final String sentryId : sentryIds) {
                final List<CmmnSentryPart> sentryParts = allSentries.get(sentryId);
                if (this.isSentryPartsSatisfied(sentryId, sentryParts)) {
                    result.add(sentryId);
                }
            }
        }
        return result;
    }

    protected void forceUpdateOnSentries(final List<String> sentryIds) {
        for (final String sentryId : sentryIds) {
            final List<? extends CmmnSentryPart> sentryParts = this.findSentry(sentryId);
            for (final CmmnSentryPart sentryPart : sentryParts) {
                if (sentryPart instanceof CaseSentryPartEntity) {
                    final CaseSentryPartEntity sentryPartEntity = (CaseSentryPartEntity) sentryPart;
                    sentryPartEntity.forceUpdate();
                }
            }
        }
    }

    protected void resetSentries(final List<String> sentries) {
        for (final String sentry : sentries) {
            final List<CmmnSentryPart> parts = this.getSentries().get(sentry);
            for (final CmmnSentryPart part : parts) {
                part.setSatisfied(false);
            }
        }
    }

    protected void resetSentryParts(final List<CmmnSentryPart> parts) {
        for (final CmmnSentryPart part : parts) {
            part.setSatisfied(false);
        }
    }

    protected void fireSentries(final List<String> satisfiedSentries) {
        if (satisfiedSentries != null && !satisfiedSentries.isEmpty()) {
            final ArrayList<CmmnExecution> children = new ArrayList<CmmnExecution>();
            this.collectCaseExecutionsInExecutionTree(children);
            for (final CmmnExecution currentChild : children) {
                currentChild.checkAndFireExitCriteria(satisfiedSentries);
                currentChild.checkAndFireEntryCriteria(satisfiedSentries);
            }
            if (this.isCaseInstanceExecution() && this.isActive()) {
                this.checkAndFireExitCriteria(satisfiedSentries);
            }
        }
    }

    protected void collectCaseExecutionsInExecutionTree(final List<CmmnExecution> children) {
        for (final CmmnExecution child : this.getCaseExecutions()) {
            child.collectCaseExecutionsInExecutionTree(children);
        }
        children.addAll(this.getCaseExecutions());
    }

    protected void checkAndFireExitCriteria(final List<String> satisfiedSentries) {
        if (this.isActive()) {
            final CmmnActivity activity = this.getActivity();
            EnsureUtil.ensureNotNull(PvmException.class, "Case execution '" + this.getId() + "': has no current activity.", "activity", activity);
            final List<CmmnSentryDeclaration> exitCriteria = activity.getExitCriteria();
            for (final CmmnSentryDeclaration sentryDeclaration : exitCriteria) {
                if (sentryDeclaration != null && satisfiedSentries.contains(sentryDeclaration.getId())) {
                    this.fireExitCriteria();
                    break;
                }
            }
        }
    }

    protected void checkAndFireEntryCriteria(final List<String> satisfiedSentries) {
        if (this.isAvailable() || this.isNew()) {
            final CmmnActivity activity = this.getActivity();
            EnsureUtil.ensureNotNull(PvmException.class, "Case execution '" + this.getId() + "': has no current activity.", "activity", activity);
            final List<CmmnSentryDeclaration> criteria = activity.getEntryCriteria();
            for (final CmmnSentryDeclaration sentryDeclaration : criteria) {
                if (sentryDeclaration != null && satisfiedSentries.contains(sentryDeclaration.getId())) {
                    if (this.isAvailable()) {
                        this.fireEntryCriteria();
                        break;
                    }
                    this.entryCriterionSatisfied = true;
                    break;
                }
            }
        }
    }

    public void fireExitCriteria() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_FIRE_EXIT_CRITERIA);
    }

    public void fireEntryCriteria() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_FIRE_ENTRY_CRITERIA);
    }

    public abstract List<? extends CmmnSentryPart> getCaseSentryParts();

    protected abstract List<? extends CmmnSentryPart> findSentry(final String p0);

    protected abstract Map<String, List<CmmnSentryPart>> getSentries();

    @Override
    public boolean isSentrySatisfied(final String sentryId) {
        final List<? extends CmmnSentryPart> sentryParts = this.findSentry(sentryId);
        return this.isSentryPartsSatisfied(sentryId, sentryParts);
    }

    protected boolean isSentryPartsSatisfied(final String sentryId, final List<? extends CmmnSentryPart> sentryParts) {
        CmmnSentryPart ifPart = null;
        if (sentryParts != null && !sentryParts.isEmpty()) {
            for (final CmmnSentryPart sentryPart : sentryParts) {
                if ("planItemOnPart".equals(sentryPart.getType())) {
                    if (!sentryPart.isSatisfied()) {
                        return false;
                    }
                    continue;
                } else if ("variableOnPart".equals(sentryPart.getType())) {
                    if (!sentryPart.isSatisfied()) {
                        return false;
                    }
                    continue;
                } else {
                    ifPart = sentryPart;
                    if (ifPart.isSatisfied()) {
                        return true;
                    }
                    continue;
                }
            }
        }
        if (ifPart != null) {
            final CmmnExecution execution = ifPart.getCaseExecution();
            EnsureUtil.ensureNotNull("Case execution of sentry '" + ifPart.getSentryId() + "': is null", execution);
            final CmmnActivity activity = ifPart.getCaseExecution().getActivity();
            EnsureUtil.ensureNotNull("Case execution '" + this.id + "': has no current activity", "activity", activity);
            final CmmnSentryDeclaration sentryDeclaration = activity.getSentry(sentryId);
            EnsureUtil.ensureNotNull("Case execution '" + this.id + "': has no declaration for sentry '" + sentryId + "'", "sentryDeclaration", sentryDeclaration);
            final CmmnIfPartDeclaration ifPartDeclaration = sentryDeclaration.getIfPart();
            EnsureUtil.ensureNotNull("Sentry declaration '" + sentryId + "' has no definied ifPart, but there should be one defined for case execution '" + this.id + "'.", "ifPartDeclaration", ifPartDeclaration);
            final Expression condition = ifPartDeclaration.getCondition();
            EnsureUtil.ensureNotNull("A condition was expected for ifPart of Sentry declaration '" + sentryId + "' for case execution '" + this.id + "'.", "condition", condition);
            final Object result = condition.getValue(this);
            EnsureUtil.ensureInstanceOf("condition expression returns non-Boolean", "result", result, Boolean.class);
            final Boolean booleanResult = (Boolean) result;
            ifPart.setSatisfied(booleanResult);
            return booleanResult;
        }
        return true;
    }

    protected boolean containsIfPartAndExecutionActive(final String sentryId, final Map<String, List<CmmnSentryPart>> sentries) {
        final List<? extends CmmnSentryPart> sentryParts = sentries.get(sentryId);
        for (final CmmnSentryPart part : sentryParts) {
            final CmmnExecution caseExecution = part.getCaseExecution();
            if ("ifPart".equals(part.getType()) && caseExecution != null && caseExecution.isActive()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEntryCriterionSatisfied() {
        return this.entryCriterionSatisfied;
    }

    @Override
    public String getCaseBusinessKey() {
        return this.getCaseInstance().getBusinessKey();
    }

    @Override
    public String getBusinessKey() {
        if (this.isCaseInstanceExecution()) {
            return this.businessKey;
        }
        return this.getCaseBusinessKey();
    }

    public CmmnCaseDefinition getCaseDefinition() {
        return this.caseDefinition;
    }

    public void setCaseDefinition(final CmmnCaseDefinition caseDefinition) {
        this.caseDefinition = caseDefinition;
    }

    public abstract CmmnExecution getCaseInstance();

    public abstract void setCaseInstance(final CmmnExecution p0);

    @Override
    public boolean isCaseInstanceExecution() {
        return this.getParent() == null;
    }

    @Override
    public String getCaseInstanceId() {
        return this.getCaseInstance().getId();
    }

    @Override
    public abstract CmmnExecution getParent();

    public abstract void setParent(final CmmnExecution p0);

    @Override
    public CmmnActivity getActivity() {
        return this.activity;
    }

    public void setActivity(final CmmnActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getVariableScopeKey() {
        return "caseExecution";
    }

    @Override
    public AbstractVariableScope getParentVariableScope() {
        return this.getParent();
    }

    public void deleteCascade() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_DELETE_CASCADE);
    }

    @Override
    public void remove() {
        final CmmnExecution parent = this.getParent();
        if (parent != null) {
            parent.getCaseExecutionsInternal().remove(this);
        }
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    @Override
    public void setRequired(final boolean required) {
        this.required = required;
    }

    @Override
    public CaseExecutionState getCurrentState() {
        return CaseExecutionState.CASE_EXECUTION_STATES.get(this.getState());
    }

    @Override
    public void setCurrentState(final CaseExecutionState currentState) {
        if (!this.isSuspending() && !this.isTerminating()) {
            this.previousState = this.currentState;
        }
        this.currentState = currentState.getStateCode();
    }

    public int getState() {
        return this.currentState;
    }

    public void setState(final int state) {
        this.currentState = state;
    }

    @Override
    public boolean isNew() {
        return this.currentState == CaseExecutionState.NEW.getStateCode();
    }

    @Override
    public boolean isAvailable() {
        return this.currentState == CaseExecutionState.AVAILABLE.getStateCode();
    }

    @Override
    public boolean isEnabled() {
        return this.currentState == CaseExecutionState.ENABLED.getStateCode();
    }

    @Override
    public boolean isDisabled() {
        return this.currentState == CaseExecutionState.DISABLED.getStateCode();
    }

    @Override
    public boolean isActive() {
        return this.currentState == CaseExecutionState.ACTIVE.getStateCode();
    }

    @Override
    public boolean isCompleted() {
        return this.currentState == CaseExecutionState.COMPLETED.getStateCode();
    }

    @Override
    public boolean isSuspended() {
        return this.currentState == CaseExecutionState.SUSPENDED.getStateCode();
    }

    @Override
    public boolean isSuspending() {
        return this.currentState == CaseExecutionState.SUSPENDING_ON_SUSPENSION.getStateCode() || this.currentState == CaseExecutionState.SUSPENDING_ON_PARENT_SUSPENSION.getStateCode();
    }

    @Override
    public boolean isTerminated() {
        return this.currentState == CaseExecutionState.TERMINATED.getStateCode();
    }

    @Override
    public boolean isTerminating() {
        return this.currentState == CaseExecutionState.TERMINATING_ON_TERMINATION.getStateCode() || this.currentState == CaseExecutionState.TERMINATING_ON_PARENT_TERMINATION.getStateCode() || this.currentState == CaseExecutionState.TERMINATING_ON_EXIT.getStateCode();
    }

    @Override
    public boolean isFailed() {
        return this.currentState == CaseExecutionState.FAILED.getStateCode();
    }

    @Override
    public boolean isClosed() {
        return this.currentState == CaseExecutionState.CLOSED.getStateCode();
    }

    @Override
    public CaseExecutionState getPreviousState() {
        return CaseExecutionState.CASE_EXECUTION_STATES.get(this.getPrevious());
    }

    public int getPrevious() {
        return this.previousState;
    }

    public void setPrevious(final int previous) {
        this.previousState = previous;
    }

    @Override
    public void create() {
        this.create(null);
    }

    @Override
    public void create(final Map<String, Object> variables) {
        if (variables != null) {
            this.setVariables(variables);
        }
        this.performOperation(CmmnAtomicOperation.CASE_INSTANCE_CREATE);
    }

    @Override
    public List<CmmnExecution> createChildExecutions(final List<CmmnActivity> activities) {
        final List<CmmnExecution> children = new ArrayList<CmmnExecution>();
        for (final CmmnActivity currentActivity : activities) {
            final CmmnExecution child = this.createCaseExecution(currentActivity);
            children.add(child);
        }
        return children;
    }

    @Override
    public void triggerChildExecutionsLifecycle(final List<CmmnExecution> children) {
        for (final CmmnExecution child : children) {
            if (!this.isActive()) {
                break;
            }
            if (!child.isNew()) {
                continue;
            }
            child.performOperation(CmmnAtomicOperation.CASE_EXECUTION_CREATE);
        }
    }

    protected abstract CmmnExecution createCaseExecution(final CmmnActivity p0);

    protected abstract CmmnExecution newCaseExecution();

    @Override
    public void enable() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_ENABLE);
    }

    @Override
    public void disable() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_DISABLE);
    }

    @Override
    public void reenable() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_RE_ENABLE);
    }

    @Override
    public void manualStart() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_MANUAL_START);
    }

    @Override
    public void start() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_START);
    }

    @Override
    public void complete() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_COMPLETE);
    }

    @Override
    public void manualComplete() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_MANUAL_COMPLETE);
    }

    @Override
    public void occur() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_OCCUR);
    }

    @Override
    public void terminate() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_TERMINATING_ON_TERMINATION);
    }

    @Override
    public void performTerminate() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_TERMINATE);
    }

    @Override
    public void parentTerminate() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_TERMINATING_ON_PARENT_TERMINATION);
    }

    @Override
    public void performParentTerminate() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_PARENT_TERMINATE);
    }

    @Override
    public void exit() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_TERMINATING_ON_EXIT);
    }

    @Override
    public void parentComplete() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_PARENT_COMPLETE);
    }

    @Override
    public void performExit() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_EXIT);
    }

    @Override
    public void suspend() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_SUSPENDING_ON_SUSPENSION);
    }

    @Override
    public void performSuspension() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_SUSPEND);
    }

    @Override
    public void parentSuspend() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_SUSPENDING_ON_PARENT_SUSPENSION);
    }

    @Override
    public void performParentSuspension() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_PARENT_SUSPEND);
    }

    @Override
    public void resume() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_RESUME);
    }

    @Override
    public void parentResume() {
        this.performOperation(CmmnAtomicOperation.CASE_EXECUTION_PARENT_RESUME);
    }

    @Override
    public void reactivate() {
        this.performOperation( CmmnAtomicOperation.CASE_EXECUTION_RE_ACTIVATE);
    }

    @Override
    public void close() {
        this.performOperation(CmmnAtomicOperation.CASE_INSTANCE_CLOSE);
    }

    @Override
    public void dispatchEvent(final VariableEvent variableEvent) {
        final boolean invokeCustomListeners = Context.getProcessEngineConfiguration().isInvokeCustomVariableListeners();
        final Map<String, List<VariableListener<?>>> listeners = this.getActivity().getVariableListeners(variableEvent.getEventName(), invokeCustomListeners);
        if (!listeners.isEmpty()) {
            this.getCaseInstance().queueVariableEvent(variableEvent, invokeCustomListeners);
        }
    }

    protected void queueVariableEvent(final VariableEvent variableEvent, final boolean includeCustomerListeners) {
        final Queue<VariableEvent> variableEventsQueue = this.getVariableEventQueue();
        variableEventsQueue.add(variableEvent);
        if (variableEventsQueue.size() == 1) {
            this.invokeVariableListeners(includeCustomerListeners);
        }
    }

    protected void invokeVariableListeners(final boolean includeCustomerListeners) {
        final Queue<VariableEvent> variableEventsQueue = this.getVariableEventQueue();
        while (!variableEventsQueue.isEmpty()) {
            final VariableEvent nextEvent = variableEventsQueue.peek();
            final CmmnExecution sourceExecution = (CmmnExecution) nextEvent.getSourceScope();
            final DelegateCaseVariableInstanceImpl delegateVariable = DelegateCaseVariableInstanceImpl.fromVariableInstance(nextEvent.getVariableInstance());
            delegateVariable.setEventName(nextEvent.getEventName());
            delegateVariable.setSourceExecution(sourceExecution);
            final Map<String, List<VariableListener<?>>> listenersByActivity = sourceExecution.getActivity().getVariableListeners(delegateVariable.getEventName(), includeCustomerListeners);
            for (CmmnExecution currentExecution = sourceExecution; currentExecution != null; currentExecution = currentExecution.getParent()) {
                if (currentExecution.getActivityId() != null) {
                    final List<VariableListener<?>> listeners = listenersByActivity.get(currentExecution.getActivityId());
                    if (listeners != null) {
                        delegateVariable.setScopeExecution(currentExecution);
                        for (final VariableListener<?> listener : listeners) {
                            try {
                                final CaseVariableListener caseVariableListener = (CaseVariableListener) listener;
                                final CaseVariableListenerInvocation invocation = new CaseVariableListenerInvocation(caseVariableListener, delegateVariable, currentExecution);
                                Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
                            } catch (Exception e) {
                                throw CmmnExecution.LOG.invokeVariableListenerException(e);
                            }
                        }
                    }
                }
            }
            variableEventsQueue.remove();
        }
    }

    protected Queue<VariableEvent> getVariableEventQueue() {
        if (this.variableEventsQueue == null) {
            this.variableEventsQueue = new LinkedList<VariableEvent>();
        }
        return this.variableEventsQueue;
    }

    @Override
    public String toString() {
        if (this.isCaseInstanceExecution()) {
            return "CaseInstance[" + this.getToStringIdentity() + "]";
        }
        return "CmmnExecution[" + this.getToStringIdentity() + "]";
    }

    protected String getToStringIdentity() {
        return this.id;
    }

    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
