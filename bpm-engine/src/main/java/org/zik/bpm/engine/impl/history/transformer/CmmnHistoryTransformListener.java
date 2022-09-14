// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.transformer;

import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import org.zik.bpm.engine.impl.cmmn.handler.ItemHandler;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.cmmn.model.CmmnSentryDeclaration;
import org.camunda.bpm.model.cmmn.instance.Sentry;
import org.camunda.bpm.model.cmmn.instance.EventListener;
import org.camunda.bpm.model.cmmn.instance.Milestone;
import org.camunda.bpm.model.cmmn.instance.Stage;
import org.camunda.bpm.model.cmmn.instance.Task;
import org.camunda.bpm.model.cmmn.instance.DecisionTask;
import org.camunda.bpm.model.cmmn.instance.CaseTask;
import org.camunda.bpm.model.cmmn.instance.ProcessTask;
import org.camunda.bpm.model.cmmn.instance.HumanTask;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.impl.instance.CasePlanModel;
import org.camunda.bpm.model.cmmn.instance.Case;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import java.util.List;
import org.camunda.bpm.model.cmmn.instance.Definitions;
import org.zik.bpm.engine.impl.history.producer.CmmnHistoryEventProducer;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.delegate.CaseExecutionListener;
import org.zik.bpm.engine.impl.cmmn.transformer.CmmnTransformListener;

public class CmmnHistoryTransformListener implements CmmnTransformListener
{
    protected CaseExecutionListener CASE_INSTANCE_CREATE_LISTENER;
    protected CaseExecutionListener CASE_INSTANCE_UPDATE_LISTENER;
    protected CaseExecutionListener CASE_INSTANCE_CLOSE_LISTENER;
    protected CaseExecutionListener CASE_ACTIVITY_INSTANCE_CREATE_LISTENER;
    protected CaseExecutionListener CASE_ACTIVITY_INSTANCE_UPDATE_LISTENER;
    protected CaseExecutionListener CASE_ACTIVITY_INSTANCE_END_LISTENER;
    protected HistoryLevel historyLevel;
    
    public CmmnHistoryTransformListener(final CmmnHistoryEventProducer historyEventProducer) {
        this.initCaseExecutionListeners(historyEventProducer);
    }
    
    protected void initCaseExecutionListeners(final CmmnHistoryEventProducer historyEventProducer) {
        this.CASE_INSTANCE_CREATE_LISTENER = new CaseInstanceCreateListener(historyEventProducer);
        this.CASE_INSTANCE_UPDATE_LISTENER = new CaseInstanceUpdateListener(historyEventProducer);
        this.CASE_INSTANCE_CLOSE_LISTENER = new CaseInstanceCloseListener(historyEventProducer);
        this.CASE_ACTIVITY_INSTANCE_CREATE_LISTENER = new CaseActivityInstanceCreateListener(historyEventProducer);
        this.CASE_ACTIVITY_INSTANCE_UPDATE_LISTENER = new CaseActivityInstanceUpdateListener(historyEventProducer);
        this.CASE_ACTIVITY_INSTANCE_END_LISTENER = new CaseActivityInstanceEndListener(historyEventProducer);
    }
    
    @Override
    public void transformRootElement(final Definitions definitions, final List<? extends CmmnCaseDefinition> caseDefinitions) {
    }
    
    @Override
    public void transformCase(final Case element, final CmmnCaseDefinition caseDefinition) {
    }
    
    @Override
    public void transformCasePlanModel(final CasePlanModel casePlanModel, final CmmnActivity caseActivity) {
        this.transformCasePlanModel((org.camunda.bpm.model.cmmn.instance.CasePlanModel)casePlanModel, caseActivity);
    }
    
    @Override
    public void transformCasePlanModel(final org.camunda.bpm.model.cmmn.instance.CasePlanModel casePlanModel, final CmmnActivity caseActivity) {
        this.addCasePlanModelHandlers(caseActivity);
    }
    
    @Override
    public void transformHumanTask(final PlanItem planItem, final HumanTask humanTask, final CmmnActivity caseActivity) {
        this.addTaskOrStageHandlers(caseActivity);
    }
    
    @Override
    public void transformProcessTask(final PlanItem planItem, final ProcessTask processTask, final CmmnActivity caseActivity) {
        this.addTaskOrStageHandlers(caseActivity);
    }
    
    @Override
    public void transformCaseTask(final PlanItem planItem, final CaseTask caseTask, final CmmnActivity caseActivity) {
        this.addTaskOrStageHandlers(caseActivity);
    }
    
    @Override
    public void transformDecisionTask(final PlanItem planItem, final DecisionTask decisionTask, final CmmnActivity caseActivity) {
        this.addTaskOrStageHandlers(caseActivity);
    }
    
    @Override
    public void transformTask(final PlanItem planItem, final Task task, final CmmnActivity caseActivity) {
        this.addTaskOrStageHandlers(caseActivity);
    }
    
    @Override
    public void transformStage(final PlanItem planItem, final Stage stage, final CmmnActivity caseActivity) {
        this.addTaskOrStageHandlers(caseActivity);
    }
    
    @Override
    public void transformMilestone(final PlanItem planItem, final Milestone milestone, final CmmnActivity caseActivity) {
        this.addEventListenerOrMilestoneHandlers(caseActivity);
    }
    
    @Override
    public void transformEventListener(final PlanItem planItem, final EventListener eventListener, final CmmnActivity caseActivity) {
        this.addEventListenerOrMilestoneHandlers(caseActivity);
    }
    
    @Override
    public void transformSentry(final Sentry sentry, final CmmnSentryDeclaration sentryDeclaration) {
    }
    
    protected void addCasePlanModelHandlers(final CmmnActivity caseActivity) {
        this.ensureHistoryLevelInitialized();
        if (caseActivity != null) {
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_INSTANCE_CREATE, null)) {
                for (final String event : ItemHandler.CASE_PLAN_MODEL_CREATE_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_INSTANCE_CREATE_LISTENER);
                }
            }
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_INSTANCE_UPDATE, null)) {
                for (final String event : ItemHandler.CASE_PLAN_MODEL_UPDATE_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_INSTANCE_UPDATE_LISTENER);
                }
            }
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_INSTANCE_CLOSE, null)) {
                for (final String event : ItemHandler.CASE_PLAN_MODEL_CLOSE_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_INSTANCE_CLOSE_LISTENER);
                }
            }
        }
    }
    
    protected void addTaskOrStageHandlers(final CmmnActivity caseActivity) {
        this.ensureHistoryLevelInitialized();
        if (caseActivity != null) {
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_CREATE, null)) {
                for (final String event : ItemHandler.TASK_OR_STAGE_CREATE_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_ACTIVITY_INSTANCE_CREATE_LISTENER);
                }
            }
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_UPDATE, null)) {
                for (final String event : ItemHandler.TASK_OR_STAGE_UPDATE_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_ACTIVITY_INSTANCE_UPDATE_LISTENER);
                }
            }
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_END, null)) {
                for (final String event : ItemHandler.TASK_OR_STAGE_END_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_ACTIVITY_INSTANCE_END_LISTENER);
                }
            }
        }
    }
    
    protected void addEventListenerOrMilestoneHandlers(final CmmnActivity caseActivity) {
        this.ensureHistoryLevelInitialized();
        if (caseActivity != null) {
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_CREATE, null)) {
                for (final String event : ItemHandler.EVENT_LISTENER_OR_MILESTONE_CREATE_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_ACTIVITY_INSTANCE_CREATE_LISTENER);
                }
            }
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_UPDATE, null)) {
                for (final String event : ItemHandler.EVENT_LISTENER_OR_MILESTONE_UPDATE_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_ACTIVITY_INSTANCE_UPDATE_LISTENER);
                }
            }
            if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_END, null)) {
                for (final String event : ItemHandler.EVENT_LISTENER_OR_MILESTONE_END_EVENTS) {
                    caseActivity.addBuiltInListener(event, this.CASE_ACTIVITY_INSTANCE_END_LISTENER);
                }
            }
        }
    }
    
    protected void ensureHistoryLevelInitialized() {
        if (this.historyLevel == null) {
            this.historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        }
    }
}
