// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.transformer;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.handler.ItemHandler;
import org.camunda.bpm.model.cmmn.instance.EventListener;
import org.camunda.bpm.model.cmmn.instance.Milestone;
import org.camunda.bpm.model.cmmn.instance.Task;
import org.camunda.bpm.model.cmmn.instance.DecisionTask;
import org.camunda.bpm.model.cmmn.instance.CaseTask;
import org.camunda.bpm.model.cmmn.instance.ProcessTask;
import org.camunda.bpm.model.cmmn.instance.HumanTask;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.zik.bpm.engine.impl.cmmn.model.CmmnSentryDeclaration;
import org.zik.bpm.engine.impl.cmmn.handler.SentryHandler;
import org.camunda.bpm.model.cmmn.instance.Sentry;
import org.camunda.bpm.model.cmmn.instance.PlanningTable;
import org.camunda.bpm.model.cmmn.instance.PlanFragment;
import org.camunda.bpm.model.cmmn.instance.Stage;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.zik.bpm.engine.impl.cmmn.handler.CasePlanModelHandler;
import org.camunda.bpm.model.cmmn.instance.CasePlanModel;
import org.zik.bpm.engine.impl.cmmn.handler.CmmnElementHandler;
import java.util.Collection;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.Case;
import java.util.Iterator;
import org.camunda.bpm.model.cmmn.instance.Definitions;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.cmmn.CmmnModelException;
import java.io.InputStream;
import org.camunda.bpm.model.cmmn.Cmmn;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cmmn.handler.CmmnHandlerContext;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.handler.DefaultCmmnElementHandlerRegistry;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.core.transformer.Transform;

public class CmmnTransform implements Transform<CaseDefinitionEntity>
{
    protected static final CmmnTransformerLogger LOG;
    protected CmmnTransformer transformer;
    protected ExpressionManager expressionManager;
    protected DefaultCmmnElementHandlerRegistry handlerRegistry;
    protected List<CmmnTransformListener> transformListeners;
    protected ResourceEntity resource;
    protected DeploymentEntity deployment;
    protected CmmnModelInstance model;
    protected CmmnHandlerContext context;
    protected List<CaseDefinitionEntity> caseDefinitions;
    
    public CmmnTransform(final CmmnTransformer transformer) {
        this.context = new CmmnHandlerContext();
        this.caseDefinitions = new ArrayList<CaseDefinitionEntity>();
        this.transformer = transformer;
        this.expressionManager = transformer.getExpressionManager();
        this.handlerRegistry = transformer.getCmmnElementHandlerRegistry();
        this.transformListeners = transformer.getTransformListeners();
    }
    
    public CmmnTransform deployment(final DeploymentEntity deployment) {
        this.deployment = deployment;
        return this;
    }
    
    public CmmnTransform resource(final ResourceEntity resource) {
        this.resource = resource;
        return this;
    }
    
    @Override
    public List<CaseDefinitionEntity> transform() {
        final String resourceName = this.resource.getName();
        final byte[] bytes = this.resource.getBytes();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            this.model = Cmmn.readModelFromStream((InputStream)inputStream);
        }
        catch (CmmnModelException e) {
            throw CmmnTransform.LOG.transformResourceException(resourceName, (Throwable)e);
        }
        this.context.setModel(this.model);
        this.context.setDeployment(this.deployment);
        this.context.setExpressionManager(this.expressionManager);
        try {
            this.transformRootElement();
        }
        catch (Exception e2) {
            throw CmmnTransform.LOG.parseProcessException(resourceName, e2);
        }
        return this.caseDefinitions;
    }
    
    protected void transformRootElement() {
        this.transformImports();
        this.transformCaseDefinitions();
        final Definitions definitions = this.model.getDefinitions();
        for (final CmmnTransformListener transformListener : this.transformListeners) {
            transformListener.transformRootElement(definitions, this.caseDefinitions);
        }
    }
    
    protected void transformImports() {
    }
    
    protected void transformCaseDefinitions() {
        final Definitions definitions = this.model.getDefinitions();
        final Collection<Case> cases = (Collection<Case>)definitions.getCases();
        for (final Case currentCase : cases) {
            this.context.setCaseDefinition(null);
            this.context.setParent(null);
            final CmmnCaseDefinition caseDefinition = this.transformCase(currentCase);
            this.caseDefinitions.add((CaseDefinitionEntity)caseDefinition);
        }
    }
    
    protected CaseDefinitionEntity transformCase(final Case element) {
        final CmmnElementHandler<Case, CmmnActivity> caseTransformer = this.getDefinitionHandler(Case.class);
        final CmmnActivity definition = caseTransformer.handleElement(element, this.context);
        this.context.setCaseDefinition((CmmnCaseDefinition)definition);
        this.context.setParent(definition);
        final CasePlanModel casePlanModel = element.getCasePlanModel();
        this.transformCasePlanModel(casePlanModel);
        for (final CmmnTransformListener transformListener : this.transformListeners) {
            transformListener.transformCase(element, (CmmnCaseDefinition)definition);
        }
        return (CaseDefinitionEntity)definition;
    }
    
    protected void transformCasePlanModel(final CasePlanModel casePlanModel) {
        final CasePlanModelHandler transformer = (CasePlanModelHandler)this.getPlanItemHandler((Class<? extends PlanItemDefinition>)CasePlanModel.class);
        final CmmnActivity newActivity = transformer.handleElement((CmmnElement)casePlanModel, this.context);
        this.context.setParent(newActivity);
        this.transformStage((Stage)casePlanModel, newActivity);
        this.context.setParent(newActivity);
        transformer.initializeExitCriterias(casePlanModel, newActivity, this.context);
        for (final CmmnTransformListener transformListener : this.transformListeners) {
            transformListener.transformCasePlanModel((org.camunda.bpm.model.cmmn.impl.instance.CasePlanModel)casePlanModel, newActivity);
        }
    }
    
    protected void transformStage(final Stage stage, final CmmnActivity parent) {
        this.context.setParent(parent);
        this.transformSentries(stage);
        this.transformPlanItems((PlanFragment)stage, parent);
        this.transformSentryOnParts(stage);
        this.transformPlanningTable(stage.getPlanningTable(), parent);
    }
    
    protected void transformPlanningTable(final PlanningTable planningTable, final CmmnActivity parent) {
    }
    
    protected void transformSentries(final Stage stage) {
        final Collection<Sentry> sentries = (Collection<Sentry>)stage.getSentrys();
        if (sentries != null && !sentries.isEmpty()) {
            final SentryHandler handler = this.getSentryHandler();
            for (final Sentry sentry : sentries) {
                handler.handleElement(sentry, this.context);
            }
        }
    }
    
    protected void transformSentryOnParts(final Stage stage) {
        final Collection<Sentry> sentries = (Collection<Sentry>)stage.getSentrys();
        if (sentries != null && !sentries.isEmpty()) {
            final SentryHandler handler = this.getSentryHandler();
            for (final Sentry sentry : sentries) {
                handler.initializeOnParts(sentry, this.context);
                final CmmnSentryDeclaration sentryDeclaration = this.context.getParent().getSentry(sentry.getId());
                for (final CmmnTransformListener transformListener : this.transformListeners) {
                    transformListener.transformSentry(sentry, sentryDeclaration);
                }
            }
        }
    }
    
    protected void transformPlanItems(final PlanFragment planFragment, final CmmnActivity parent) {
        final Collection<PlanItem> planItems = (Collection<PlanItem>)planFragment.getPlanItems();
        for (final PlanItem planItem : planItems) {
            this.transformPlanItem(planItem, parent);
        }
    }
    
    protected void transformPlanItem(final PlanItem planItem, final CmmnActivity parent) {
        final PlanItemDefinition definition = planItem.getDefinition();
        ItemHandler planItemTransformer = null;
        if (definition instanceof HumanTask) {
            planItemTransformer = this.getPlanItemHandler((Class<? extends PlanItemDefinition>)HumanTask.class);
        }
        else if (definition instanceof ProcessTask) {
            planItemTransformer = this.getPlanItemHandler((Class<? extends PlanItemDefinition>)ProcessTask.class);
        }
        else if (definition instanceof CaseTask) {
            planItemTransformer = this.getPlanItemHandler((Class<? extends PlanItemDefinition>)CaseTask.class);
        }
        else if (definition instanceof DecisionTask) {
            planItemTransformer = this.getPlanItemHandler((Class<? extends PlanItemDefinition>)DecisionTask.class);
        }
        else if (definition instanceof Task) {
            planItemTransformer = this.getPlanItemHandler((Class<? extends PlanItemDefinition>)Task.class);
        }
        else if (definition instanceof Stage) {
            planItemTransformer = this.getPlanItemHandler((Class<? extends PlanItemDefinition>)Stage.class);
        }
        else if (definition instanceof Milestone) {
            planItemTransformer = this.getPlanItemHandler((Class<? extends PlanItemDefinition>)Milestone.class);
        }
        else if (definition instanceof EventListener) {
            planItemTransformer = this.getPlanItemHandler((Class<? extends PlanItemDefinition>)EventListener.class);
        }
        if (planItemTransformer != null) {
            final CmmnActivity newActivity = planItemTransformer.handleElement((CmmnElement)planItem, this.context);
            if (definition instanceof Stage) {
                final Stage stage = (Stage)definition;
                this.transformStage(stage, newActivity);
                this.context.setParent(parent);
            }
            else if (definition instanceof HumanTask) {
                final HumanTask humanTask = (HumanTask)definition;
                final Collection<PlanningTable> planningTables = (Collection<PlanningTable>)humanTask.getPlanningTables();
                for (final PlanningTable planningTable : planningTables) {
                    this.transformPlanningTable(planningTable, parent);
                }
            }
            for (final CmmnTransformListener transformListener : this.transformListeners) {
                if (definition instanceof HumanTask) {
                    transformListener.transformHumanTask(planItem, (HumanTask)definition, newActivity);
                }
                else if (definition instanceof ProcessTask) {
                    transformListener.transformProcessTask(planItem, (ProcessTask)definition, newActivity);
                }
                else if (definition instanceof CaseTask) {
                    transformListener.transformCaseTask(planItem, (CaseTask)definition, newActivity);
                }
                else if (definition instanceof DecisionTask) {
                    transformListener.transformDecisionTask(planItem, (DecisionTask)definition, newActivity);
                }
                else if (definition instanceof Task) {
                    transformListener.transformTask(planItem, (Task)definition, newActivity);
                }
                else if (definition instanceof Stage) {
                    transformListener.transformStage(planItem, (Stage)definition, newActivity);
                }
                else if (definition instanceof Milestone) {
                    transformListener.transformMilestone(planItem, (Milestone)definition, newActivity);
                }
                else {
                    if (!(definition instanceof EventListener)) {
                        continue;
                    }
                    transformListener.transformEventListener(planItem, (EventListener)definition, newActivity);
                }
            }
        }
    }
    
    public DeploymentEntity getDeployment() {
        return this.deployment;
    }
    
    public void setDeployment(final DeploymentEntity deployment) {
        this.deployment = deployment;
    }
    
    public ResourceEntity getResource() {
        return this.resource;
    }
    
    public void setResource(final ResourceEntity resource) {
        this.resource = resource;
    }
    
    public DefaultCmmnElementHandlerRegistry getHandlerRegistry() {
        return this.handlerRegistry;
    }
    
    public void setHandlerRegistry(final DefaultCmmnElementHandlerRegistry handlerRegistry) {
        this.handlerRegistry = handlerRegistry;
    }
    
    protected <V extends CmmnElement> CmmnElementHandler<V, CmmnActivity> getDefinitionHandler(final Class<V> cls) {
        return (CmmnElementHandler<V, CmmnActivity>)this.getHandlerRegistry().getDefinitionElementHandlers().get(cls);
    }
    
    protected ItemHandler getPlanItemHandler(final Class<? extends PlanItemDefinition> cls) {
        return this.getHandlerRegistry().getPlanItemElementHandlers().get(cls);
    }
    
    protected ItemHandler getDiscretionaryItemHandler(final Class<? extends PlanItemDefinition> cls) {
        return this.getHandlerRegistry().getDiscretionaryElementHandlers().get(cls);
    }
    
    protected SentryHandler getSentryHandler() {
        return this.getHandlerRegistry().getSentryHandler();
    }
    
    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }
    
    public void setExpressionManager(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }
    
    static {
        LOG = ProcessEngineLogger.CMMN_TRANSFORMER_LOGGER;
    }
}
