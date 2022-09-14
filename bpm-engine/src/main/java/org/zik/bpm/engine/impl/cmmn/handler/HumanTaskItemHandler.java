// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.handler;

import org.zik.bpm.engine.impl.core.handler.HandlerContext;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.cmmn.instance.PlanItemDefinition;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnActivityBehavior;
import org.zik.bpm.engine.impl.scripting.ExecutableScript;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaScript;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaField;
import java.util.Collection;
import org.zik.bpm.engine.impl.task.listener.ScriptTaskListener;
import org.zik.bpm.engine.impl.task.listener.DelegateExpressionTaskListener;
import org.zik.bpm.engine.impl.task.listener.ExpressionTaskListener;
import org.zik.bpm.engine.impl.task.listener.ClassDelegateTaskListener;
import org.zik.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.model.cmmn.instance.camunda.CamundaTaskListener;
import java.util.Iterator;
import java.util.List;
import org.camunda.bpm.model.cmmn.instance.Role;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.impl.form.handler.TaskFormHandler;
import org.zik.bpm.engine.impl.form.handler.DefaultTaskFormHandler;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.cmmn.behavior.HumanTaskActivityBehavior;
import org.zik.bpm.engine.impl.task.TaskDecorator;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.camunda.bpm.model.cmmn.instance.HumanTask;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;

public class HumanTaskItemHandler extends TaskItemHandler
{
    @Override
    public CmmnActivity handleElement(final CmmnElement element, final CmmnHandlerContext context) {
        final HumanTask definition = this.getDefinition(element);
        if (!definition.isBlocking()) {
            return null;
        }
        return super.handleElement(element, context);
    }
    
    @Override
    protected void initializeActivity(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context) {
        super.initializeActivity(element, activity, context);
        final TaskDefinition taskDefinition = this.createTaskDefinition(element, context);
        final CaseDefinitionEntity caseDefinition = (CaseDefinitionEntity)context.getCaseDefinition();
        caseDefinition.getTaskDefinitions().put(taskDefinition.getKey(), taskDefinition);
        final ExpressionManager expressionManager = context.getExpressionManager();
        final TaskDecorator taskDecorator = new TaskDecorator(taskDefinition, expressionManager);
        final HumanTaskActivityBehavior behavior = (HumanTaskActivityBehavior)activity.getActivityBehavior();
        behavior.setTaskDecorator(taskDecorator);
        this.initializeTaskListeners(element, activity, context, taskDefinition);
    }
    
    protected TaskDefinition createTaskDefinition(final CmmnElement element, final CmmnHandlerContext context) {
        final Deployment deployment = context.getDeployment();
        final String deploymentId = deployment.getId();
        final DefaultTaskFormHandler taskFormHandler = new DefaultTaskFormHandler();
        taskFormHandler.setDeploymentId(deploymentId);
        final TaskDefinition taskDefinition = new TaskDefinition(taskFormHandler);
        final String taskDefinitionKey = element.getId();
        taskDefinition.setKey(taskDefinitionKey);
        this.initializeTaskDefinitionName(element, taskDefinition, context);
        this.initializeTaskDefinitionDueDate(element, taskDefinition, context);
        this.initializeTaskDefinitionFollowUpDate(element, taskDefinition, context);
        this.initializeTaskDefinitionPriority(element, taskDefinition, context);
        this.initializeTaskDefinitionAssignee(element, taskDefinition, context);
        this.initializeTaskDefinitionCandidateUsers(element, taskDefinition, context);
        this.initializeTaskDefinitionCandidateGroups(element, taskDefinition, context);
        this.initializeTaskDefinitionFormKey(element, taskDefinition, context);
        this.initializeTaskDescription(element, taskDefinition, context);
        return taskDefinition;
    }
    
    protected void initializeTaskDefinitionName(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        String name = this.getName(element);
        if (name == null) {
            final HumanTask definition = this.getDefinition(element);
            name = definition.getName();
        }
        if (name != null) {
            final ExpressionManager expressionManager = context.getExpressionManager();
            final Expression nameExpression = expressionManager.createExpression(name);
            taskDefinition.setNameExpression(nameExpression);
        }
    }
    
    protected void initializeTaskDefinitionFormKey(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        final HumanTask definition = this.getDefinition(element);
        final String formKey = definition.getCamundaFormKey();
        if (formKey != null) {
            final ExpressionManager expressionManager = context.getExpressionManager();
            final Expression formKeyExpression = expressionManager.createExpression(formKey);
            taskDefinition.setFormKey(formKeyExpression);
        }
    }
    
    protected void initializeTaskDefinitionAssignee(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        final HumanTask definition = this.getDefinition(element);
        final Role performer = definition.getPerformer();
        String assignee = null;
        if (performer != null) {
            assignee = performer.getName();
        }
        else {
            assignee = definition.getCamundaAssignee();
        }
        if (assignee != null) {
            final ExpressionManager expressionManager = context.getExpressionManager();
            final Expression assigneeExpression = expressionManager.createExpression(assignee);
            taskDefinition.setAssigneeExpression(assigneeExpression);
        }
    }
    
    protected void initializeTaskDefinitionCandidateUsers(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        final HumanTask definition = this.getDefinition(element);
        final ExpressionManager expressionManager = context.getExpressionManager();
        final List<String> candidateUsers = (List<String>)definition.getCamundaCandidateUsersList();
        for (final String candidateUser : candidateUsers) {
            final Expression candidateUserExpression = expressionManager.createExpression(candidateUser);
            taskDefinition.addCandidateUserIdExpression(candidateUserExpression);
        }
    }
    
    protected void initializeTaskDefinitionCandidateGroups(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        final HumanTask definition = this.getDefinition(element);
        final ExpressionManager expressionManager = context.getExpressionManager();
        final List<String> candidateGroups = (List<String>)definition.getCamundaCandidateGroupsList();
        for (final String candidateGroup : candidateGroups) {
            final Expression candidateGroupExpression = expressionManager.createExpression(candidateGroup);
            taskDefinition.addCandidateGroupIdExpression(candidateGroupExpression);
        }
    }
    
    protected void initializeTaskDefinitionDueDate(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        final HumanTask definition = this.getDefinition(element);
        final String dueDate = definition.getCamundaDueDate();
        if (dueDate != null) {
            final ExpressionManager expressionManager = context.getExpressionManager();
            final Expression dueDateExpression = expressionManager.createExpression(dueDate);
            taskDefinition.setDueDateExpression(dueDateExpression);
        }
    }
    
    protected void initializeTaskDefinitionFollowUpDate(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        final HumanTask definition = this.getDefinition(element);
        final String followUpDate = definition.getCamundaFollowUpDate();
        if (followUpDate != null) {
            final ExpressionManager expressionManager = context.getExpressionManager();
            final Expression followUpDateExpression = expressionManager.createExpression(followUpDate);
            taskDefinition.setFollowUpDateExpression(followUpDateExpression);
        }
    }
    
    protected void initializeTaskDefinitionPriority(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        final HumanTask definition = this.getDefinition(element);
        final String priority = definition.getCamundaPriority();
        if (priority != null) {
            final ExpressionManager expressionManager = context.getExpressionManager();
            final Expression priorityExpression = expressionManager.createExpression(priority);
            taskDefinition.setPriorityExpression(priorityExpression);
        }
    }
    
    protected void initializeTaskDescription(final CmmnElement element, final TaskDefinition taskDefinition, final CmmnHandlerContext context) {
        final String description = this.getDesciption(element);
        if (description != null && !description.isEmpty()) {
            final ExpressionManager expressionManager = context.getExpressionManager();
            final Expression descriptionExpression = expressionManager.createExpression(description);
            taskDefinition.setDescriptionExpression(descriptionExpression);
        }
        else {
            final String documentation = this.getDocumentation(element);
            if (documentation != null && !documentation.isEmpty()) {
                final ExpressionManager expressionManager2 = context.getExpressionManager();
                final Expression documentationExpression = expressionManager2.createExpression(documentation);
                taskDefinition.setDescriptionExpression(documentationExpression);
            }
        }
    }
    
    protected void initializeTaskListeners(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final TaskDefinition taskDefinition) {
        final HumanTask humanTask = this.getDefinition(element);
        final List<CamundaTaskListener> listeners = this.queryExtensionElementsByClass((CmmnElement)humanTask, CamundaTaskListener.class);
        for (final CamundaTaskListener listener : listeners) {
            final TaskListener taskListener = this.initializeTaskListener(element, activity, context, listener);
            final String eventName = listener.getCamundaEvent();
            if (eventName != null) {
                taskDefinition.addTaskListener(eventName, taskListener);
            }
            else {
                taskDefinition.addTaskListener("create", taskListener);
                taskDefinition.addTaskListener("assignment", taskListener);
                taskDefinition.addTaskListener("complete", taskListener);
                taskDefinition.addTaskListener("update", taskListener);
                taskDefinition.addTaskListener("delete", taskListener);
            }
        }
    }
    
    protected TaskListener initializeTaskListener(final CmmnElement element, final CmmnActivity activity, final CmmnHandlerContext context, final CamundaTaskListener listener) {
        final Collection<CamundaField> fields = (Collection<CamundaField>)listener.getCamundaFields();
        final List<FieldDeclaration> fieldDeclarations = this.initializeFieldDeclarations(element, activity, context, fields);
        final ExpressionManager expressionManager = context.getExpressionManager();
        TaskListener taskListener = null;
        final String className = listener.getCamundaClass();
        final String expression = listener.getCamundaExpression();
        final String delegateExpression = listener.getCamundaDelegateExpression();
        final CamundaScript scriptElement = listener.getCamundaScript();
        if (className != null) {
            taskListener = new ClassDelegateTaskListener(className, fieldDeclarations);
        }
        else if (expression != null) {
            final Expression expressionExp = expressionManager.createExpression(expression);
            taskListener = new ExpressionTaskListener(expressionExp);
        }
        else if (delegateExpression != null) {
            final Expression delegateExp = expressionManager.createExpression(delegateExpression);
            taskListener = new DelegateExpressionTaskListener(delegateExp, fieldDeclarations);
        }
        else if (scriptElement != null) {
            final ExecutableScript executableScript = this.initializeScript(element, activity, context, scriptElement);
            if (executableScript != null) {
                taskListener = new ScriptTaskListener(executableScript);
            }
        }
        return taskListener;
    }
    
    protected HumanTask getDefinition(final CmmnElement element) {
        return (HumanTask)super.getDefinition(element);
    }
    
    @Override
    protected CmmnActivityBehavior getActivityBehavior() {
        return new HumanTaskActivityBehavior();
    }
}
