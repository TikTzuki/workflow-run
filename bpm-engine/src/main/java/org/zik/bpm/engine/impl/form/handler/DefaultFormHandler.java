// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import java.util.Arrays;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.camunda.bpm.engine.variable.value.SerializableValue;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.form.FormField;
import org.zik.bpm.engine.form.FormProperty;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.form.FormDataImpl;
import org.zik.bpm.engine.impl.form.validator.FormFieldValidator;
import java.util.Map;
import java.util.LinkedHashMap;
import org.zik.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.zik.bpm.engine.impl.form.type.FormTypes;
import org.zik.bpm.engine.delegate.Expression;
import java.util.Iterator;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.util.xml.Element;
import java.util.ArrayList;
import java.util.List;

public class DefaultFormHandler implements FormHandler
{
    public static final String FORM_FIELD_ELEMENT = "formField";
    public static final String FORM_PROPERTY_ELEMENT = "formProperty";
    private static final String BUSINESS_KEY_ATTRIBUTE = "businessKey";
    public static final String FORM_REF_BINDING_DEPLOYMENT = "deployment";
    public static final String FORM_REF_BINDING_LATEST = "latest";
    public static final String FORM_REF_BINDING_VERSION = "version";
    public static final List<String> ALLOWED_FORM_REF_BINDINGS;
    protected String deploymentId;
    protected String businessKeyFieldId;
    protected List<FormPropertyHandler> formPropertyHandlers;
    protected List<FormFieldHandler> formFieldHandlers;
    
    public DefaultFormHandler() {
        this.formPropertyHandlers = new ArrayList<FormPropertyHandler>();
        this.formFieldHandlers = new ArrayList<FormFieldHandler>();
    }
    
    @Override
    public void parseConfiguration(final Element activityElement, final DeploymentEntity deployment, final ProcessDefinitionEntity processDefinition, final BpmnParse bpmnParse) {
        this.deploymentId = deployment.getId();
        final ExpressionManager expressionManager = Context.getProcessEngineConfiguration().getExpressionManager();
        final Element extensionElement = activityElement.element("extensionElements");
        if (extensionElement != null) {
            this.parseFormProperties(bpmnParse, expressionManager, extensionElement);
            this.parseFormData(bpmnParse, expressionManager, extensionElement);
        }
    }
    
    protected void parseFormData(final BpmnParse bpmnParse, final ExpressionManager expressionManager, final Element extensionElement) {
        final Element formData = extensionElement.elementNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "formData");
        if (formData != null) {
            this.businessKeyFieldId = formData.attribute("businessKey");
            this.parseFormFields(formData, bpmnParse, expressionManager);
        }
    }
    
    protected void parseFormFields(final Element formData, final BpmnParse bpmnParse, final ExpressionManager expressionManager) {
        final List<Element> formFields = formData.elementsNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "formField");
        for (final Element formField : formFields) {
            this.parseFormField(formField, bpmnParse, expressionManager);
        }
    }
    
    protected void parseFormField(final Element formField, final BpmnParse bpmnParse, final ExpressionManager expressionManager) {
        final FormFieldHandler formFieldHandler = new FormFieldHandler();
        final String id = formField.attribute("id");
        if (id == null || id.isEmpty()) {
            bpmnParse.addError("attribute id must be set for FormFieldGroup and must have a non-empty value", formField);
        }
        else {
            formFieldHandler.setId(id);
        }
        if (id.equals(this.businessKeyFieldId)) {
            formFieldHandler.setBusinessKey(true);
        }
        final String name = formField.attribute("label");
        if (name != null) {
            final Expression nameExpression = expressionManager.createExpression(name);
            formFieldHandler.setLabel(nameExpression);
        }
        this.parseProperties(formField, formFieldHandler, bpmnParse, expressionManager);
        this.parseValidation(formField, formFieldHandler, bpmnParse, expressionManager);
        final FormTypes formTypes = this.getFormTypes();
        final AbstractFormFieldType formType = formTypes.parseFormPropertyType(formField, bpmnParse);
        formFieldHandler.setType(formType);
        final String defaultValue = formField.attribute("defaultValue");
        if (defaultValue != null) {
            final Expression defaultValueExpression = expressionManager.createExpression(defaultValue);
            formFieldHandler.setDefaultValueExpression(defaultValueExpression);
        }
        this.formFieldHandlers.add(formFieldHandler);
    }
    
    protected void parseProperties(final Element formField, final FormFieldHandler formFieldHandler, final BpmnParse bpmnParse, final ExpressionManager expressionManager) {
        final Element propertiesElement = formField.elementNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "properties");
        if (propertiesElement != null) {
            final List<Element> propertyElements = propertiesElement.elementsNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "property");
            final Map<String, String> propertyMap = new LinkedHashMap<String, String>();
            for (final Element property : propertyElements) {
                final String id = property.attribute("id");
                final String value = property.attribute("value");
                propertyMap.put(id, value);
            }
            formFieldHandler.setProperties(propertyMap);
        }
    }
    
    protected void parseValidation(final Element formField, final FormFieldHandler formFieldHandler, final BpmnParse bpmnParse, final ExpressionManager expressionManager) {
        final Element validationElement = formField.elementNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "validation");
        if (validationElement != null) {
            final List<Element> constraintElements = validationElement.elementsNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "constraint");
            for (final Element property : constraintElements) {
                final FormFieldValidator validator = Context.getProcessEngineConfiguration().getFormValidators().createValidator(property, bpmnParse, expressionManager);
                final String validatorName = property.attribute("name");
                final String validatorConfig = property.attribute("config");
                if (validator != null) {
                    final FormFieldValidationConstraintHandler handler = new FormFieldValidationConstraintHandler();
                    handler.setName(validatorName);
                    handler.setConfig(validatorConfig);
                    handler.setValidator(validator);
                    formFieldHandler.getValidationHandlers().add(handler);
                }
            }
        }
    }
    
    protected FormTypes getFormTypes() {
        final FormTypes formTypes = Context.getProcessEngineConfiguration().getFormTypes();
        return formTypes;
    }
    
    protected void parseFormProperties(final BpmnParse bpmnParse, final ExpressionManager expressionManager, final Element extensionElement) {
        final FormTypes formTypes = this.getFormTypes();
        final List<Element> formPropertyElements = extensionElement.elementsNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "formProperty");
        for (final Element formPropertyElement : formPropertyElements) {
            final FormPropertyHandler formPropertyHandler = new FormPropertyHandler();
            final String id = formPropertyElement.attribute("id");
            if (id == null) {
                bpmnParse.addError("attribute 'id' is required", formPropertyElement);
            }
            formPropertyHandler.setId(id);
            final String name = formPropertyElement.attribute("name");
            formPropertyHandler.setName(name);
            final AbstractFormFieldType type = formTypes.parseFormPropertyType(formPropertyElement, bpmnParse);
            formPropertyHandler.setType(type);
            final String requiredText = formPropertyElement.attribute("required", "false");
            final Boolean required = bpmnParse.parseBooleanAttribute(requiredText);
            if (required != null) {
                formPropertyHandler.setRequired(required);
            }
            else {
                bpmnParse.addError("attribute 'required' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
            }
            final String readableText = formPropertyElement.attribute("readable", "true");
            final Boolean readable = bpmnParse.parseBooleanAttribute(readableText);
            if (readable != null) {
                formPropertyHandler.setReadable(readable);
            }
            else {
                bpmnParse.addError("attribute 'readable' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
            }
            final String writableText = formPropertyElement.attribute("writable", "true");
            final Boolean writable = bpmnParse.parseBooleanAttribute(writableText);
            if (writable != null) {
                formPropertyHandler.setWritable(writable);
            }
            else {
                bpmnParse.addError("attribute 'writable' must be one of {on|yes|true|enabled|active|off|no|false|disabled|inactive}", formPropertyElement);
            }
            final String variableName = formPropertyElement.attribute("variable");
            formPropertyHandler.setVariableName(variableName);
            final String expressionText = formPropertyElement.attribute("expression");
            if (expressionText != null) {
                final Expression expression = expressionManager.createExpression(expressionText);
                formPropertyHandler.setVariableExpression(expression);
            }
            final String defaultExpressionText = formPropertyElement.attribute("default");
            if (defaultExpressionText != null) {
                final Expression defaultExpression = expressionManager.createExpression(defaultExpressionText);
                formPropertyHandler.setDefaultExpression(defaultExpression);
            }
            this.formPropertyHandlers.add(formPropertyHandler);
        }
    }
    
    protected void initializeFormProperties(final FormDataImpl formData, final ExecutionEntity execution) {
        final List<FormProperty> formProperties = new ArrayList<FormProperty>();
        for (final FormPropertyHandler formPropertyHandler : this.formPropertyHandlers) {
            if (formPropertyHandler.isReadable()) {
                final FormProperty formProperty = formPropertyHandler.createFormProperty(execution);
                formProperties.add(formProperty);
            }
        }
        formData.setFormProperties(formProperties);
    }
    
    protected void initializeFormFields(final FormDataImpl taskFormData, final ExecutionEntity execution) {
        final List<FormField> formFields = taskFormData.getFormFields();
        for (final FormFieldHandler formFieldHandler : this.formFieldHandlers) {
            formFields.add(formFieldHandler.createFormField(execution));
        }
    }
    
    @Override
    public void submitFormVariables(final VariableMap properties, final VariableScope variableScope) {
        final boolean userOperationLogEnabled = Context.getCommandContext().isUserOperationLogEnabled();
        Context.getCommandContext().enableUserOperationLog();
        final VariableMap propertiesCopy = (VariableMap)new VariableMapImpl((Map)properties);
        for (final FormPropertyHandler formPropertyHandler : this.formPropertyHandlers) {
            formPropertyHandler.submitFormProperty(variableScope, propertiesCopy);
        }
        for (final FormFieldHandler formFieldHandler : this.formFieldHandlers) {
            if (!formFieldHandler.isBusinessKey()) {
                formFieldHandler.handleSubmit(variableScope, propertiesCopy, properties);
            }
        }
        for (final String propertyId : propertiesCopy.keySet()) {
            variableScope.setVariable(propertyId, propertiesCopy.getValueTyped(propertyId));
        }
        this.fireFormPropertyHistoryEvents(properties, variableScope);
        Context.getCommandContext().setLogUserOperationEnabled(userOperationLogEnabled);
    }
    
    protected void fireFormPropertyHistoryEvents(final VariableMap properties, final VariableScope variableScope) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = processEngineConfiguration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.FORM_PROPERTY_UPDATE, variableScope)) {
            ExecutionEntity executionEntity;
            String taskId;
            if (variableScope instanceof ExecutionEntity) {
                executionEntity = (ExecutionEntity)variableScope;
                taskId = null;
            }
            else if (variableScope instanceof TaskEntity) {
                final TaskEntity task = (TaskEntity)variableScope;
                executionEntity = task.getExecution();
                taskId = task.getId();
            }
            else {
                executionEntity = null;
                taskId = null;
            }
            if (executionEntity != null) {
                for (final String variableName : properties.keySet()) {
                    final TypedValue value = properties.getValueTyped(variableName);
                    if (!(value instanceof SerializableValue) && value.getValue() != null && value.getValue() instanceof String) {
                        final String stringValue = (String)value.getValue();
                        HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                            @Override
                            public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                                return producer.createFormPropertyUpdateEvt(executionEntity, variableName, stringValue, taskId);
                            }
                        });
                    }
                }
            }
        }
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public List<FormPropertyHandler> getFormPropertyHandlers() {
        return this.formPropertyHandlers;
    }
    
    public void setFormPropertyHandlers(final List<FormPropertyHandler> formPropertyHandlers) {
        this.formPropertyHandlers = formPropertyHandlers;
    }
    
    public String getBusinessKeyFieldId() {
        return this.businessKeyFieldId;
    }
    
    public void setBusinessKeyFieldId(final String businessKeyFieldId) {
        this.businessKeyFieldId = businessKeyFieldId;
    }
    
    static {
        ALLOWED_FORM_REF_BINDINGS = Arrays.asList("deployment", "latest", "version");
    }
}
