// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics.parser;

import org.camunda.bpm.model.cmmn.instance.Milestone;
import org.camunda.bpm.model.cmmn.instance.Stage;
import org.camunda.bpm.model.cmmn.instance.Task;
import org.camunda.bpm.model.cmmn.instance.DecisionTask;
import org.camunda.bpm.model.cmmn.instance.CaseTask;
import org.camunda.bpm.model.cmmn.instance.ProcessTask;
import org.camunda.bpm.model.cmmn.instance.HumanTask;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.transformer.AbstractCmmnTransformListener;

public class MetricsCmmnTransformListener extends AbstractCmmnTransformListener
{
    public static MetricsCaseExecutionListener listener;
    
    protected void addListeners(final CmmnActivity activity) {
        if (activity != null) {
            activity.addBuiltInListener("start", MetricsCmmnTransformListener.listener);
            activity.addBuiltInListener("manualStart", MetricsCmmnTransformListener.listener);
            activity.addBuiltInListener("occur", MetricsCmmnTransformListener.listener);
        }
    }
    
    @Override
    public void transformHumanTask(final PlanItem planItem, final HumanTask humanTask, final CmmnActivity activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void transformProcessTask(final PlanItem planItem, final ProcessTask processTask, final CmmnActivity activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void transformCaseTask(final PlanItem planItem, final CaseTask caseTask, final CmmnActivity activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void transformDecisionTask(final PlanItem planItem, final DecisionTask decisionTask, final CmmnActivity activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void transformTask(final PlanItem planItem, final Task task, final CmmnActivity activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void transformStage(final PlanItem planItem, final Stage stage, final CmmnActivity activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void transformMilestone(final PlanItem planItem, final Milestone milestone, final CmmnActivity activity) {
        this.addListeners(activity);
    }
    
    static {
        MetricsCmmnTransformListener.listener = new MetricsCaseExecutionListener();
    }
}
