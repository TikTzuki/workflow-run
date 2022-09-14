// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.camunda.bpm.model.cmmn.instance.Milestone;
import org.camunda.bpm.model.cmmn.instance.CaseTask;
import org.camunda.bpm.model.cmmn.instance.DecisionTask;
import org.camunda.bpm.model.cmmn.instance.ProcessTask;
import org.camunda.bpm.model.cmmn.instance.HumanTask;
import org.camunda.bpm.model.cmmn.instance.Task;
import org.camunda.bpm.model.cmmn.instance.CasePlanModel;
import org.camunda.bpm.model.cmmn.instance.Stage;
import org.camunda.bpm.model.cmmn.instance.Case;
import java.util.HashMap;
import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import java.util.Map;

public class DefaultCmmnElementHandlerRegistry
{
    protected Map<Class<? extends CmmnElement>, CmmnElementHandler<? extends CmmnElement, ? extends CmmnActivity>> definitionElementHandlers;
    protected Map<Class<? extends PlanItemDefinition>, ItemHandler> planItemElementHandlers;
    protected Map<Class<? extends PlanItemDefinition>, ItemHandler> discretionaryElementHandlers;
    protected CaseHandler caseHandler;
    protected StageItemHandler stagePlanItemHandler;
    protected CasePlanModelHandler casePlanModelHandler;
    protected TaskItemHandler taskPlanItemHandler;
    protected HumanTaskItemHandler humanTaskPlanItemHandler;
    protected ProcessTaskItemHandler processTaskPlanItemHandler;
    protected CaseTaskItemHandler caseTaskPlanItemHandler;
    protected DecisionTaskItemHandler decisionTaskPlanItemHandler;
    protected MilestoneItemHandler milestonePlanItemHandler;
    protected EventListenerItemHandler eventListenerPlanItemHandler;
    protected StageItemHandler stageDiscretionaryItemHandler;
    protected HumanTaskItemHandler humanTaskDiscretionaryItemHandler;
    protected SentryHandler sentryHandler;
    
    public DefaultCmmnElementHandlerRegistry() {
        this.caseHandler = new CaseHandler();
        this.stagePlanItemHandler = new StageItemHandler();
        this.casePlanModelHandler = new CasePlanModelHandler();
        this.taskPlanItemHandler = new TaskItemHandler();
        this.humanTaskPlanItemHandler = new HumanTaskItemHandler();
        this.processTaskPlanItemHandler = new ProcessTaskItemHandler();
        this.caseTaskPlanItemHandler = new CaseTaskItemHandler();
        this.decisionTaskPlanItemHandler = new DecisionTaskItemHandler();
        this.milestonePlanItemHandler = new MilestoneItemHandler();
        this.eventListenerPlanItemHandler = new EventListenerItemHandler();
        this.stageDiscretionaryItemHandler = new StageItemHandler();
        this.humanTaskDiscretionaryItemHandler = new HumanTaskItemHandler();
        this.sentryHandler = new SentryHandler();
        (this.definitionElementHandlers = new HashMap<Class<? extends CmmnElement>, CmmnElementHandler<? extends CmmnElement, ? extends CmmnActivity>>()).put((Class<? extends CmmnElement>)Case.class, (CmmnElementHandler<? extends CmmnElement, ? extends CmmnActivity>)this.caseHandler);
        (this.planItemElementHandlers = new HashMap<Class<? extends PlanItemDefinition>, ItemHandler>()).put((Class<? extends PlanItemDefinition>)Stage.class, this.stagePlanItemHandler);
        this.planItemElementHandlers.put((Class<? extends PlanItemDefinition>)CasePlanModel.class, this.casePlanModelHandler);
        this.planItemElementHandlers.put((Class<? extends PlanItemDefinition>)Task.class, this.taskPlanItemHandler);
        this.planItemElementHandlers.put((Class<? extends PlanItemDefinition>)HumanTask.class, this.humanTaskPlanItemHandler);
        this.planItemElementHandlers.put((Class<? extends PlanItemDefinition>)ProcessTask.class, this.processTaskPlanItemHandler);
        this.planItemElementHandlers.put((Class<? extends PlanItemDefinition>)DecisionTask.class, this.decisionTaskPlanItemHandler);
        this.planItemElementHandlers.put((Class<? extends PlanItemDefinition>)CaseTask.class, this.caseTaskPlanItemHandler);
        this.planItemElementHandlers.put((Class<? extends PlanItemDefinition>)Milestone.class, this.milestonePlanItemHandler);
        (this.discretionaryElementHandlers = new HashMap<Class<? extends PlanItemDefinition>, ItemHandler>()).put((Class<? extends PlanItemDefinition>)Stage.class, this.stageDiscretionaryItemHandler);
        this.discretionaryElementHandlers.put((Class<? extends PlanItemDefinition>)HumanTask.class, this.humanTaskDiscretionaryItemHandler);
    }
    
    public Map<Class<? extends CmmnElement>, CmmnElementHandler<? extends CmmnElement, ? extends CmmnActivity>> getDefinitionElementHandlers() {
        return this.definitionElementHandlers;
    }
    
    public void setDefinitionElementHandlers(final Map<Class<? extends CmmnElement>, CmmnElementHandler<? extends CmmnElement, ? extends CmmnActivity>> definitionElementHandlers) {
        this.definitionElementHandlers = definitionElementHandlers;
    }
    
    public Map<Class<? extends PlanItemDefinition>, ItemHandler> getPlanItemElementHandlers() {
        return this.planItemElementHandlers;
    }
    
    public void setPlanItemElementHandlers(final Map<Class<? extends PlanItemDefinition>, ItemHandler> planItemElementHandlers) {
        this.planItemElementHandlers = planItemElementHandlers;
    }
    
    public Map<Class<? extends PlanItemDefinition>, ItemHandler> getDiscretionaryElementHandlers() {
        return this.discretionaryElementHandlers;
    }
    
    public void setDiscretionaryElementHandlers(final Map<Class<? extends PlanItemDefinition>, ItemHandler> discretionaryElementHandlers) {
        this.discretionaryElementHandlers = discretionaryElementHandlers;
    }
    
    public SentryHandler getSentryHandler() {
        return this.sentryHandler;
    }
    
    public void setSentryHandler(final SentryHandler sentryHandler) {
        this.sentryHandler = sentryHandler;
    }
}
