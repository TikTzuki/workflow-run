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

public interface CmmnTransformListener
{
    void transformRootElement(final Definitions p0, final List<? extends CmmnCaseDefinition> p1);
    
    void transformCase(final Case p0, final CmmnCaseDefinition p1);
    
    @Deprecated
    void transformCasePlanModel(final CasePlanModel p0, final CmmnActivity p1);
    
    void transformCasePlanModel(final org.camunda.bpm.model.cmmn.instance.CasePlanModel p0, final CmmnActivity p1);
    
    void transformHumanTask(final PlanItem p0, final HumanTask p1, final CmmnActivity p2);
    
    void transformProcessTask(final PlanItem p0, final ProcessTask p1, final CmmnActivity p2);
    
    void transformCaseTask(final PlanItem p0, final CaseTask p1, final CmmnActivity p2);
    
    void transformDecisionTask(final PlanItem p0, final DecisionTask p1, final CmmnActivity p2);
    
    void transformTask(final PlanItem p0, final Task p1, final CmmnActivity p2);
    
    void transformStage(final PlanItem p0, final Stage p1, final CmmnActivity p2);
    
    void transformMilestone(final PlanItem p0, final Milestone p1, final CmmnActivity p2);
    
    void transformEventListener(final PlanItem p0, final EventListener p1, final CmmnActivity p2);
    
    void transformSentry(final Sentry p0, final CmmnSentryDeclaration p1);
}
