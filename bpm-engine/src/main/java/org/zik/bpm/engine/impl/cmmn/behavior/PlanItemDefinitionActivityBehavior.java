// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.pvm.PvmException;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import java.util.Arrays;
import org.zik.bpm.engine.impl.bpmn.helper.CmmnProperties;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import org.zik.bpm.engine.impl.cmmn.CaseControlRule;
import org.zik.bpm.engine.impl.cmmn.model.CmmnSentryDeclaration;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public abstract class PlanItemDefinitionActivityBehavior implements CmmnActivityBehavior
{
    protected static final CmmnBehaviorLogger LOG;
    
    @Override
    public void execute(final CmmnActivityExecution execution) throws Exception {
    }
    
    protected boolean isAtLeastOneEntryCriterionSatisfied(final CmmnActivityExecution execution) {
        if (execution.isEntryCriterionSatisfied()) {
            return true;
        }
        final CmmnActivity activity = this.getActivity(execution);
        final List<CmmnSentryDeclaration> criteria = activity.getEntryCriteria();
        return criteria == null || criteria.isEmpty();
    }
    
    protected void evaluateRequiredRule(final CmmnActivityExecution execution) {
        final CmmnActivity activity = execution.getActivity();
        final Object requiredRule = activity.getProperty("requiredRule");
        if (requiredRule != null) {
            final CaseControlRule rule = (CaseControlRule)requiredRule;
            final boolean required = rule.evaluate(execution);
            execution.setRequired(required);
        }
    }
    
    protected boolean evaluateRepetitionRule(final CmmnActivityExecution execution) {
        final CmmnActivity activity = execution.getActivity();
        final Object repetitionRule = activity.getProperty("repetitionRule");
        if (repetitionRule != null) {
            final CaseControlRule rule = (CaseControlRule)repetitionRule;
            return rule.evaluate(execution);
        }
        return false;
    }
    
    @Override
    public void onCreate(final CmmnActivityExecution execution) {
        this.ensureTransitionAllowed(execution, CaseExecutionState.NEW, CaseExecutionState.AVAILABLE, "create");
        this.creating(execution);
    }
    
    protected void creating(final CmmnActivityExecution execution) {
    }
    
    @Override
    public void started(final CmmnActivityExecution execution) {
    }
    
    protected void completing(final CmmnActivityExecution execution) {
    }
    
    protected void manualCompleting(final CmmnActivityExecution execution) {
    }
    
    @Override
    public void onClose(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        if (!execution.isCaseInstanceExecution()) {
            throw PlanItemDefinitionActivityBehavior.LOG.notACaseInstanceException("close", id);
        }
        if (execution.isClosed()) {
            throw PlanItemDefinitionActivityBehavior.LOG.alreadyClosedCaseException("close", id);
        }
        if (execution.isActive()) {
            throw PlanItemDefinitionActivityBehavior.LOG.wrongCaseStateException("close", id, "[completed|terminated|suspended]", "active");
        }
    }
    
    protected void performTerminate(final CmmnActivityExecution execution) {
        execution.performTerminate();
    }
    
    protected void performParentTerminate(final CmmnActivityExecution execution) {
        execution.performParentTerminate();
    }
    
    protected void performExit(final CmmnActivityExecution execution) {
        execution.performExit();
    }
    
    protected void performSuspension(final CmmnActivityExecution execution) {
        execution.performSuspension();
    }
    
    protected void performParentSuspension(final CmmnActivityExecution execution) {
        execution.performParentSuspension();
    }
    
    protected void resuming(final CmmnActivityExecution execution) {
    }
    
    @Override
    public void resumed(final CmmnActivityExecution execution) {
        if (execution.isAvailable()) {
            this.created(execution);
        }
    }
    
    @Override
    public void reactivated(final CmmnActivityExecution execution) {
    }
    
    @Override
    public void repeat(final CmmnActivityExecution execution, final String standardEvent) {
        final CmmnActivity activity = execution.getActivity();
        boolean repeat = false;
        if (activity.getEntryCriteria().isEmpty()) {
            final List<String> events = activity.getProperties().get(CmmnProperties.REPEAT_ON_STANDARD_EVENTS);
            if (events != null && events.contains(standardEvent)) {
                repeat = this.evaluateRepetitionRule(execution);
            }
        }
        else if ("enable".equals(standardEvent) || "start".equals(standardEvent) || "occur".equals(standardEvent)) {
            repeat = this.evaluateRepetitionRule(execution);
        }
        if (repeat) {
            final CmmnActivityExecution parent = execution.getParent();
            final List<CmmnExecution> children = parent.createChildExecutions(Arrays.asList(activity));
            parent.triggerChildExecutionsLifecycle(children);
        }
    }
    
    protected void ensureTransitionAllowed(final CmmnActivityExecution execution, final CaseExecutionState expected, final CaseExecutionState target, final String transition) {
        final String id = execution.getId();
        CaseExecutionState currentState = execution.getCurrentState();
        if (execution.isTerminating() || execution.isSuspending()) {
            currentState = execution.getPreviousState();
        }
        if (target.equals(currentState)) {
            throw PlanItemDefinitionActivityBehavior.LOG.isAlreadyInStateException(transition, id, target);
        }
        if (!expected.equals(currentState)) {
            throw PlanItemDefinitionActivityBehavior.LOG.unexpectedStateException(transition, id, expected, currentState);
        }
    }
    
    protected void ensureNotCaseInstance(final CmmnActivityExecution execution, final String transition) {
        if (execution.isCaseInstanceExecution()) {
            final String id = execution.getId();
            throw PlanItemDefinitionActivityBehavior.LOG.impossibleTransitionException(transition, id);
        }
    }
    
    protected CmmnActivity getActivity(final CmmnActivityExecution execution) {
        final String id = execution.getId();
        final CmmnActivity activity = execution.getActivity();
        EnsureUtil.ensureNotNull(PvmException.class, "Case execution '" + id + "': has no current activity.", "activity", activity);
        return activity;
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
