// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.transformer;

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

public class AbstractCmmnTransformListener implements CmmnTransformListener
{
    @Override
    public void transformRootElement(final Definitions definitions, final List<? extends CmmnCaseDefinition> caseDefinitions) {
    }
    
    @Override
    public void transformCase(final Case element, final CmmnCaseDefinition caseDefinition) {
    }
    
    @Override
    public void transformCasePlanModel(final CasePlanModel casePlanModel, final CmmnActivity activity) {
        this.transformCasePlanModel((org.camunda.bpm.model.cmmn.instance.CasePlanModel)casePlanModel, activity);
    }
    
    @Override
    public void transformCasePlanModel(final org.camunda.bpm.model.cmmn.instance.CasePlanModel casePlanModel, final CmmnActivity activity) {
    }
    
    @Override
    public void transformHumanTask(final PlanItem planItem, final HumanTask humanTask, final CmmnActivity activity) {
    }
    
    @Override
    public void transformProcessTask(final PlanItem planItem, final ProcessTask processTask, final CmmnActivity activity) {
    }
    
    @Override
    public void transformCaseTask(final PlanItem planItem, final CaseTask caseTask, final CmmnActivity activity) {
    }
    
    @Override
    public void transformDecisionTask(final PlanItem planItem, final DecisionTask decisionTask, final CmmnActivity activity) {
    }
    
    @Override
    public void transformTask(final PlanItem planItem, final Task task, final CmmnActivity activity) {
    }
    
    @Override
    public void transformStage(final PlanItem planItem, final Stage stage, final CmmnActivity activity) {
    }
    
    @Override
    public void transformMilestone(final PlanItem planItem, final Milestone milestone, final CmmnActivity activity) {
    }
    
    @Override
    public void transformEventListener(final PlanItem planItem, final EventListener eventListener, final CmmnActivity activity) {
    }
    
    @Override
    public void transformSentry(final Sentry sentry, final CmmnSentryDeclaration sentryDeclaration) {
    }
}
