// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.core.handler.HandlerContext;
import org.camunda.bpm.model.cmmn.instance.Documentation;
import org.camunda.bpm.model.cmmn.instance.DiscretionaryItem;
import org.camunda.bpm.model.cmmn.instance.PlanItem;
import org.camunda.bpm.model.cmmn.Query;
import org.camunda.bpm.model.cmmn.instance.ExtensionElements;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.zik.bpm.engine.impl.cmmn.model.CmmnSentryDeclaration;
import org.camunda.bpm.model.cmmn.instance.Sentry;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaExpression;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaString;
import org.zik.bpm.engine.impl.el.FixedValue;
import java.util.ArrayList;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.ScriptUtil;
import org.zik.bpm.engine.impl.variable.listener.ScriptCaseVariableListener;
import org.zik.bpm.engine.impl.variable.listener.DelegateExpressionCaseVariableListener;
import org.zik.bpm.engine.impl.variable.listener.ExpressionCaseVariableListener;
import org.zik.bpm.engine.impl.variable.listener.ClassDelegateCaseVariableListener;
import org.zik.bpm.engine.delegate.CaseVariableListener;
import org.zik.bpm.engine.delegate.VariableListener;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaVariableListener;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaScript;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaField;
import java.util.Collection;
import org.zik.bpm.engine.impl.cmmn.listener.ScriptCaseExecutionListener;
import org.zik.bpm.engine.impl.cmmn.listener.DelegateExpressionCaseExecutionListener;
import org.zik.bpm.engine.impl.cmmn.listener.ExpressionCaseExecutionListener;
import org.zik.bpm.engine.impl.cmmn.listener.ClassDelegateCaseExecutionListener;
import org.zik.bpm.engine.delegate.CaseExecutionListener;
import java.util.Iterator;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaCaseExecutionListener;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.cmmn.behavior.CaseControlRuleImpl;
import org.camunda.bpm.model.cmmn.instance.ConditionExpression;
import org.camunda.bpm.model.cmmn.instance.RepetitionRule;
import org.zik.bpm.engine.impl.bpmn.helper.CmmnProperties;
import java.util.Arrays;
import org.camunda.bpm.model.cmmn.instance.ManualActivationRule;
import org.zik.bpm.engine.impl.cmmn.CaseControlRule;
import org.camunda.bpm.model.cmmn.instance.RequiredRule;
import org.camunda.bpm.model.cmmn.instance.PlanItemControl;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public abstract class ItemHandler extends CmmnElementHandler<CmmnElement, CmmnActivity>
{
    public static final String PROPERTY_AUTO_COMPLETE = "autoComplete";
    public static final String PROPERTY_REQUIRED_RULE = "requiredRule";
    public static final String PROPERTY_MANUAL_ACTIVATION_RULE = "manualActivationRule";
    public static final String PROPERTY_REPETITION_RULE = "repetitionRule";
    public static final String PROPERTY_IS_BLOCKING = "isBlocking";
    public static final String PROPERTY_DISCRETIONARY = "discretionary";
    public static final String PROPERTY_ACTIVITY_TYPE = "activityType";
    public static final String PROPERTY_ACTIVITY_DESCRIPTION = "description";
    protected static final String PARENT_COMPLETE = "parentComplete";
    public static List<String> TASK_OR_STAGE_CREATE_EVENTS;
    public static List<String> TASK_OR_STAGE_UPDATE_EVENTS;
    public static List<String> TASK_OR_STAGE_END_EVENTS;
    public static List<String> TASK_OR_STAGE_EVENTS;
    public static List<String> EVENT_LISTENER_OR_MILESTONE_CREATE_EVENTS;
    public static List<String> EVENT_LISTENER_OR_MILESTONE_UPDATE_EVENTS;
    public static List<String> EVENT_LISTENER_OR_MILESTONE_END_EVENTS;
    public static List<String> EVENT_LISTENER_OR_MILESTONE_EVENTS;
    public static List<String> CASE_PLAN_MODEL_CREATE_EVENTS;
    public static List<String> CASE_PLAN_MODEL_UPDATE_EVENTS;
    public static List<String> CASE_PLAN_MODEL_CLOSE_EVENTS;
    public static List<String> CASE_PLAN_MODEL_EVENTS;
    public static List<String> DEFAULT_VARIABLE_EVENTS;
    
    protected CmmnActivity createActivity(final CmmnElement element, final CmmnHandlerContext context) {
        final String id = element.getId();
        final CmmnActivity parent = context.getParent();
        CmmnActivity newActivity = null;
        if (parent != null) {
            newActivity = parent.createActivity(id);
        }
        else {
            final CmmnCaseDefinition caseDefinition = context.getCaseDefinition();
            newActivity = new CmmnActivity(id, caseDefinition);
        }
        newActivity.setCmmnElement(element);
        final CmmnActivityBehavior behavior = this.getActivityBehavior();
        newActivity.setActivityBehavior(behavior);
        return newActivity;
    }
    
    protected CmmnActivityBehavior getActivityBehavior() {
        return null;
    }
    
    @Override
    public CmmnActivity handleElement(final CmmnElement element, final CmmnHandlerContext context) {
        final CmmnActivity newActivity = this.createActivity(element, context);
        this.initializeActivity(element, newActivity, context);
        return newActivity;
    }
    
    protected void initializeActivity(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        if (this.isDiscretionaryItem(element)) {
            activity.setProperty("discretionary", true);
        }
        String name = this.getName(element);
        if (name == null) {
            final PlanItemDefinition definition = this.getDefinition(element);
            if (definition != null) {
                name = definition.getName();
            }
        }
        activity.setName(name);
        this.initializeActivityType(element, activity, context);
        this.initializeDescription(element, activity, context);
        this.initializeAutoComplete(element, activity, context);
        this.initializeRequiredRule(element, activity, context);
        this.initializeManualActivationRule(element, activity, context);
        this.initializeRepetitionRule(element, activity, context);
        this.initializeCaseExecutionListeners(element, activity, context);
        this.initializeVariableListeners(element, activity, context);
        this.initializeEntryCriterias(element, activity, context);
        this.initializeExitCriterias(element, activity, context);
    }
    
    protected void initializeActivityType(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final PlanItemDefinition definition = this.getDefinition(element);
        String activityType = null;
        if (definition != null) {
            final ModelElementType elementType = definition.getElementType();
            if (elementType != null) {
                activityType = elementType.getTypeName();
            }
        }
        activity.setProperty("activityType", activityType);
    }
    
    protected void initializeDescription(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        String description = this.getDesciption(element);
        if (description == null) {
            description = this.getDocumentation(element);
        }
        activity.setProperty("description", description);
    }
    
    protected void initializeAutoComplete(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
    }
    
    protected void initializeRequiredRule(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final PlanItemControl itemControl = this.getItemControl(element);
        final PlanItemControl defaultControl = this.getDefaultControl(element);
        RequiredRule requiredRule = null;
        if (itemControl != null) {
            requiredRule = itemControl.getRequiredRule();
        }
        if (requiredRule == null && defaultControl != null) {
            requiredRule = defaultControl.getRequiredRule();
        }
        if (requiredRule != null) {
            final CaseControlRule caseRule = this.initializeCaseControlRule(requiredRule.getCondition(), context);
            activity.setProperty("requiredRule", caseRule);
        }
    }
    
    protected void initializeManualActivationRule(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final PlanItemControl itemControl = this.getItemControl(element);
        final PlanItemControl defaultControl = this.getDefaultControl(element);
        ManualActivationRule manualActivationRule = null;
        if (itemControl != null) {
            manualActivationRule = itemControl.getManualActivationRule();
        }
        if (manualActivationRule == null && defaultControl != null) {
            manualActivationRule = defaultControl.getManualActivationRule();
        }
        if (manualActivationRule != null) {
            final CaseControlRule caseRule = this.initializeCaseControlRule(manualActivationRule.getCondition(), context);
            activity.setProperty("manualActivationRule", caseRule);
        }
    }
    
    protected void initializeRepetitionRule(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final PlanItemControl itemControl = this.getItemControl(element);
        final PlanItemControl defaultControl = this.getDefaultControl(element);
        RepetitionRule repetitionRule = null;
        if (itemControl != null) {
            repetitionRule = itemControl.getRepetitionRule();
        }
        if (repetitionRule == null && defaultControl != null) {
            repetitionRule = defaultControl.getRepetitionRule();
        }
        if (repetitionRule != null) {
            final ConditionExpression condition = repetitionRule.getCondition();
            final CaseControlRule caseRule = this.initializeCaseControlRule(condition, context);
            activity.setProperty("repetitionRule", caseRule);
            List<String> events = Arrays.asList("terminate", "complete");
            final String repeatOnStandardEvent = repetitionRule.getCamundaRepeatOnStandardEvent();
            if (repeatOnStandardEvent != null && !repeatOnStandardEvent.isEmpty()) {
                events = Arrays.asList(repeatOnStandardEvent);
            }
            activity.getProperties().set(CmmnProperties.REPEAT_ON_STANDARD_EVENTS, events);
        }
    }
    
    protected CaseControlRule initializeCaseControlRule(final ConditionExpression condition, final CmmnHandlerContext context) {
        Expression expression = null;
        if (condition != null) {
            final String rule = condition.getText();
            if (rule != null && !rule.isEmpty()) {
                final ExpressionManager expressionManager = context.getExpressionManager();
                expression = expressionManager.createExpression(rule);
            }
        }
        return new CaseControlRuleImpl(expression);
    }
    
    protected void initializeCaseExecutionListeners(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final PlanItemDefinition definition = this.getDefinition(element);
        final List<CamundaCaseExecutionListener> listeners = this.queryExtensionElementsByClass((CmmnElement)definition, CamundaCaseExecutionListener.class);
        for (final CamundaCaseExecutionListener listener : listeners) {
            final CaseExecutionListener caseExecutionListener = this.initializeCaseExecutionListener(element, activity, context, listener);
            final String eventName = listener.getCamundaEvent();
            if (eventName != null) {
                activity.addListener(eventName, caseExecutionListener);
            }
            else {
                for (final String event : this.getStandardEvents(element)) {
                    activity.addListener(event, caseExecutionListener);
                }
            }
        }
    }
    
    protected CaseExecutionListener initializeCaseExecutionListener(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final CamundaCaseExecutionListener listener) {
        final Collection<CamundaField> fields = (Collection<CamundaField>)listener.getCamundaFields();
        final List<FieldDeclaration> fieldDeclarations = this.initializeFieldDeclarations(element, activity, context, fields);
        final ExpressionManager expressionManager = context.getExpressionManager();
        CaseExecutionListener caseExecutionListener = null;
        final String className = listener.getCamundaClass();
        final String expression = listener.getCamundaExpression();
        final String delegateExpression = listener.getCamundaDelegateExpression();
        final CamundaScript scriptElement = listener.getCamundaScript();
        if (className != null) {
            caseExecutionListener = new ClassDelegateCaseExecutionListener(className, fieldDeclarations);
        }
        else if (expression != null) {
            final Expression expressionExp = expressionManager.createExpression(expression);
            caseExecutionListener = new ExpressionCaseExecutionListener(expressionExp);
        }
        else if (delegateExpression != null) {
            final Expression delegateExp = expressionManager.createExpression(delegateExpression);
            caseExecutionListener = new DelegateExpressionCaseExecutionListener(delegateExp, fieldDeclarations);
        }
        else if (scriptElement != null) {
            final ExecutableScript executableScript = this.initializeScript(element, activity, context, scriptElement);
            if (executableScript != null) {
                caseExecutionListener = new ScriptCaseExecutionListener(executableScript);
            }
        }
        return caseExecutionListener;
    }
    
    protected void initializeVariableListeners(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final PlanItemDefinition definition = this.getDefinition(element);
        final List<CamundaVariableListener> listeners = this.queryExtensionElementsByClass((CmmnElement)definition, CamundaVariableListener.class);
        for (final CamundaVariableListener listener : listeners) {
            final CaseVariableListener variableListener = this.initializeVariableListener(element, activity, context, listener);
            final String eventName = listener.getCamundaEvent();
            if (eventName != null) {
                activity.addVariableListener(eventName, variableListener);
            }
            else {
                for (final String event : ItemHandler.DEFAULT_VARIABLE_EVENTS) {
                    activity.addVariableListener(event, variableListener);
                }
            }
        }
    }
    
    protected CaseVariableListener initializeVariableListener(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final CamundaVariableListener listener) {
        final Collection<CamundaField> fields = (Collection<CamundaField>)listener.getCamundaFields();
        final List<FieldDeclaration> fieldDeclarations = this.initializeFieldDeclarations(element, activity, context, fields);
        final ExpressionManager expressionManager = context.getExpressionManager();
        final String className = listener.getCamundaClass();
        final String expression = listener.getCamundaExpression();
        final String delegateExpression = listener.getCamundaDelegateExpression();
        final CamundaScript scriptElement = listener.getCamundaScript();
        CaseVariableListener variableListener = null;
        if (className != null) {
            variableListener = new ClassDelegateCaseVariableListener(className, fieldDeclarations);
        }
        else if (expression != null) {
            final Expression expressionExp = expressionManager.createExpression(expression);
            variableListener = new ExpressionCaseVariableListener(expressionExp);
        }
        else if (delegateExpression != null) {
            final Expression delegateExp = expressionManager.createExpression(delegateExpression);
            variableListener = new DelegateExpressionCaseVariableListener(delegateExp, fieldDeclarations);
        }
        else if (scriptElement != null) {
            final ExecutableScript executableScript = this.initializeScript(element, activity, context, scriptElement);
            if (executableScript != null) {
                variableListener = new ScriptCaseVariableListener(executableScript);
            }
        }
        return variableListener;
    }
    
    protected ExecutableScript initializeScript(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final CamundaScript script) {
        String language = script.getCamundaScriptFormat();
        final String resource = script.getCamundaResource();
        final String source = script.getTextContent();
        if (language == null) {
            language = "juel";
        }
        try {
            return ScriptUtil.getScript(language, source, resource, context.getExpressionManager());
        }
        catch (ProcessEngineException e) {
            return null;
        }
    }
    
    protected List<FieldDeclaration> initializeFieldDeclarations(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final Collection<CamundaField> fields) {
        final List<FieldDeclaration> fieldDeclarations = new ArrayList<FieldDeclaration>();
        for (final CamundaField field : fields) {
            final FieldDeclaration fieldDeclaration = this.initializeFieldDeclaration(element, activity, context, field);
            fieldDeclarations.add(fieldDeclaration);
        }
        return fieldDeclarations;
    }
    
    protected FieldDeclaration initializeFieldDeclaration(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final CamundaField field) {
        final String name = field.getCamundaName();
        final String type = Expression.class.getName();
        Object value = this.getFixedValue(field);
        if (value == null) {
            final ExpressionManager expressionManager = context.getExpressionManager();
            value = this.getExpressionValue(field, expressionManager);
        }
        return new FieldDeclaration(name, type, value);
    }
    
    protected FixedValue getFixedValue(final CamundaField field) {
        final CamundaString strg = field.getCamundaString();
        String value = null;
        if (strg != null) {
            value = strg.getTextContent();
        }
        if (value == null) {
            value = field.getCamundaStringValue();
        }
        if (value != null) {
            return new FixedValue(value);
        }
        return null;
    }
    
    protected Expression getExpressionValue(final CamundaField field, final ExpressionManager expressionManager) {
        final CamundaExpression expression = field.getCamundaExpressionChild();
        String value = null;
        if (expression != null) {
            value = expression.getTextContent();
        }
        if (value == null) {
            value = field.getCamundaExpression();
        }
        if (value != null) {
            return expressionManager.createExpression(value);
        }
        return null;
    }
    
    protected void initializeEntryCriterias(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final Collection<Sentry> entryCriterias = this.getEntryCriterias(element);
        if (!entryCriterias.isEmpty()) {
            final CmmnActivity parent = activity.getParent();
            if (parent != null) {
                for (final Sentry sentry : entryCriterias) {
                    final String sentryId = sentry.getId();
                    final CmmnSentryDeclaration sentryDeclaration = parent.getSentry(sentryId);
                    if (sentryDeclaration != null) {
                        activity.addEntryCriteria(sentryDeclaration);
                    }
                }
            }
        }
    }
    
    protected void initializeExitCriterias(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        final Collection<Sentry> exitCriterias = this.getExitCriterias(element);
        if (!exitCriterias.isEmpty()) {
            final CmmnActivity parent = activity.getParent();
            if (parent != null) {
                for (final Sentry sentry : exitCriterias) {
                    final String sentryId = sentry.getId();
                    final CmmnSentryDeclaration sentryDeclaration = parent.getSentry(sentryId);
                    if (sentryDeclaration != null) {
                        activity.addExitCriteria(sentryDeclaration);
                    }
                }
            }
        }
    }
    
    protected PlanItemControl getDefaultControl(final CmmnElement element) {
        final PlanItemDefinition definition = this.getDefinition(element);
        return (PlanItemControl)definition.getDefaultControl();
    }
    
    protected <V extends ModelElementInstance> List<V> queryExtensionElementsByClass(final CmmnElement element, final Class<V> cls) {
        final ExtensionElements extensionElements = this.getExtensionElements(element);
        if (extensionElements != null) {
            final Query<ModelElementInstance> query = (Query<ModelElementInstance>)extensionElements.getElementsQuery();
            return (List<V>)query.filterByType((Class)cls).list();
        }
        return new ArrayList<V>();
    }
    
    protected ExtensionElements getExtensionElements(final CmmnElement element) {
        return element.getExtensionElements();
    }
    
    protected PlanItemControl getItemControl(final CmmnElement element) {
        if (this.isPlanItem(element)) {
            final PlanItem planItem = (PlanItem)element;
            return (PlanItemControl)planItem.getItemControl();
        }
        if (this.isDiscretionaryItem(element)) {
            final DiscretionaryItem discretionaryItem = (DiscretionaryItem)element;
            return (PlanItemControl)discretionaryItem.getItemControl();
        }
        return null;
    }
    
    protected String getName(final CmmnElement element) {
        String name = null;
        if (this.isPlanItem(element)) {
            final PlanItem planItem = (PlanItem)element;
            name = planItem.getName();
        }
        if (name == null || name.isEmpty()) {
            final PlanItemDefinition definition = this.getDefinition(element);
            if (definition != null) {
                name = definition.getName();
            }
        }
        return name;
    }
    
    protected PlanItemDefinition getDefinition(final CmmnElement element) {
        if (this.isPlanItem(element)) {
            final PlanItem planItem = (PlanItem)element;
            return planItem.getDefinition();
        }
        if (this.isDiscretionaryItem(element)) {
            final DiscretionaryItem discretionaryItem = (DiscretionaryItem)element;
            return discretionaryItem.getDefinition();
        }
        return null;
    }
    
    protected Collection<Sentry> getEntryCriterias(final CmmnElement element) {
        if (this.isPlanItem(element)) {
            final PlanItem planItem = (PlanItem)element;
            return (Collection<Sentry>)planItem.getEntryCriteria();
        }
        return new ArrayList<Sentry>();
    }
    
    protected Collection<Sentry> getExitCriterias(final CmmnElement element) {
        if (this.isPlanItem(element)) {
            final PlanItem planItem = (PlanItem)element;
            return (Collection<Sentry>)planItem.getExitCriteria();
        }
        return new ArrayList<Sentry>();
    }
    
    protected String getDesciption(final CmmnElement element) {
        String description = element.getDescription();
        if (description == null) {
            final PlanItemDefinition definition = this.getDefinition(element);
            description = definition.getDescription();
        }
        return description;
    }
    
    protected String getDocumentation(final CmmnElement element) {
        Collection<Documentation> documentations = (Collection<Documentation>)element.getDocumentations();
        if (documentations.isEmpty()) {
            final PlanItemDefinition definition = this.getDefinition(element);
            documentations = (Collection<Documentation>)definition.getDocumentations();
        }
        if (documentations.isEmpty()) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        for (final Documentation doc : documentations) {
            final String content = doc.getTextContent();
            if (content != null) {
                if (content.isEmpty()) {
                    continue;
                }
                if (builder.length() != 0) {
                    builder.append("\n\n");
                }
                builder.append(content.trim());
            }
        }
        return builder.toString();
    }
    
    protected boolean isPlanItem(final CmmnElement element) {
        return element instanceof PlanItem;
    }
    
    protected boolean isDiscretionaryItem(final CmmnElement element) {
        return element instanceof DiscretionaryItem;
    }
    
    protected abstract List<String> getStandardEvents(final CmmnElement p0);
    
    static {
        ItemHandler.TASK_OR_STAGE_CREATE_EVENTS = Arrays.asList("create");
        ItemHandler.TASK_OR_STAGE_UPDATE_EVENTS = Arrays.asList("enable", "disable", "reenable", "start", "manualStart", "suspend", "parentSuspend", "resume", "parentResume");
        ItemHandler.TASK_OR_STAGE_END_EVENTS = Arrays.asList("terminate", "exit", "complete", "parentComplete");
        ItemHandler.TASK_OR_STAGE_EVENTS = new ArrayList<String>();
        ItemHandler.EVENT_LISTENER_OR_MILESTONE_CREATE_EVENTS = Arrays.asList("create");
        ItemHandler.EVENT_LISTENER_OR_MILESTONE_UPDATE_EVENTS = Arrays.asList("suspend", "resume");
        ItemHandler.EVENT_LISTENER_OR_MILESTONE_END_EVENTS = Arrays.asList("terminate", "parentTerminate", "occur", "parentComplete");
        ItemHandler.EVENT_LISTENER_OR_MILESTONE_EVENTS = new ArrayList<String>();
        ItemHandler.CASE_PLAN_MODEL_CREATE_EVENTS = Arrays.asList("create");
        ItemHandler.CASE_PLAN_MODEL_UPDATE_EVENTS = Arrays.asList("terminate", "suspend", "complete", "reactivate");
        ItemHandler.CASE_PLAN_MODEL_CLOSE_EVENTS = Arrays.asList("close");
        ItemHandler.CASE_PLAN_MODEL_EVENTS = new ArrayList<String>();
        ItemHandler.DEFAULT_VARIABLE_EVENTS = Arrays.asList("create", "delete", "update");
        ItemHandler.TASK_OR_STAGE_EVENTS.addAll(ItemHandler.TASK_OR_STAGE_CREATE_EVENTS);
        ItemHandler.TASK_OR_STAGE_EVENTS.addAll(ItemHandler.TASK_OR_STAGE_UPDATE_EVENTS);
        ItemHandler.TASK_OR_STAGE_EVENTS.addAll(ItemHandler.TASK_OR_STAGE_END_EVENTS);
        ItemHandler.EVENT_LISTENER_OR_MILESTONE_EVENTS.addAll(ItemHandler.EVENT_LISTENER_OR_MILESTONE_CREATE_EVENTS);
        ItemHandler.EVENT_LISTENER_OR_MILESTONE_EVENTS.addAll(ItemHandler.EVENT_LISTENER_OR_MILESTONE_UPDATE_EVENTS);
        ItemHandler.EVENT_LISTENER_OR_MILESTONE_EVENTS.addAll(ItemHandler.EVENT_LISTENER_OR_MILESTONE_END_EVENTS);
        ItemHandler.CASE_PLAN_MODEL_EVENTS.addAll(ItemHandler.CASE_PLAN_MODEL_CREATE_EVENTS);
        ItemHandler.CASE_PLAN_MODEL_EVENTS.addAll(ItemHandler.CASE_PLAN_MODEL_UPDATE_EVENTS);
        ItemHandler.CASE_PLAN_MODEL_EVENTS.addAll(ItemHandler.CASE_PLAN_MODEL_CLOSE_EVENTS);
    }
}
