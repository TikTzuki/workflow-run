// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.engine;

import java.util.List;
import org.zik.bpm.engine.form.FormFieldValidationConstraint;
import java.util.Map;
import org.zik.bpm.engine.impl.form.type.EnumFormType;
import java.util.Iterator;
import org.zik.bpm.engine.form.FormProperty;
import org.zik.bpm.engine.form.FormField;
import org.zik.bpm.engine.form.FormData;

public abstract class AbstractRenderFormDelegate
{
    protected static final String FORM_ELEMENT = "form";
    protected static final String DIV_ELEMENT = "div";
    protected static final String SPAN_ELEMENT = "span";
    protected static final String LABEL_ELEMENT = "label";
    protected static final String INPUT_ELEMENT = "input";
    protected static final String BUTTON_ELEMENT = "button";
    protected static final String SELECT_ELEMENT = "select";
    protected static final String OPTION_ELEMENT = "option";
    protected static final String I_ELEMENT = "i";
    protected static final String SCRIPT_ELEMENT = "script";
    protected static final String NAME_ATTRIBUTE = "name";
    protected static final String CLASS_ATTRIBUTE = "class";
    protected static final String ROLE_ATTRIBUTE = "role";
    protected static final String FOR_ATTRIBUTE = "for";
    protected static final String VALUE_ATTRIBUTE = "value";
    protected static final String TYPE_ATTRIBUTE = "type";
    protected static final String SELECTED_ATTRIBUTE = "selected";
    protected static final String IS_OPEN_ATTRIBUTE = "is-open";
    protected static final String DATEPICKER_POPUP_ATTRIBUTE = "datepicker-popup";
    protected static final String CAM_VARIABLE_TYPE_ATTRIBUTE = "cam-variable-type";
    protected static final String CAM_VARIABLE_NAME_ATTRIBUTE = "cam-variable-name";
    protected static final String CAM_SCRIPT_ATTRIBUTE = "cam-script";
    protected static final String NG_CLICK_ATTRIBUTE = "ng-click";
    protected static final String NG_IF_ATTRIBUTE = "ng-if";
    protected static final String NG_SHOW_ATTRIBUTE = "ng-show";
    protected static final String FORM_GROUP_CLASS = "form-group";
    protected static final String FORM_CONTROL_CLASS = "form-control";
    protected static final String INPUT_GROUP_CLASS = "input-group";
    protected static final String INPUT_GROUP_BTN_CLASS = "input-group-btn";
    protected static final String BUTTON_DEFAULT_CLASS = "btn btn-default";
    protected static final String HAS_ERROR_CLASS = "has-error";
    protected static final String HELP_BLOCK_CLASS = "help-block";
    protected static final String TEXT_INPUT_TYPE = "text";
    protected static final String CHECKBOX_INPUT_TYPE = "checkbox";
    protected static final String BUTTON_BUTTON_TYPE = "button";
    protected static final String TEXT_FORM_SCRIPT_TYPE = "text/form-script";
    protected static final String CALENDAR_GLYPHICON = "glyphicon glyphicon-calendar";
    protected static final String GENERATED_FORM_NAME = "generatedForm";
    protected static final String FORM_ROLE = "form";
    protected static final String REQUIRED_ERROR_TYPE = "required";
    protected static final String DATE_ERROR_TYPE = "date";
    protected static final String FORM_ELEMENT_SELECTOR = "this.generatedForm.%s";
    protected static final String INVALID_EXPRESSION = "this.generatedForm.%s.$invalid";
    protected static final String DIRTY_EXPRESSION = "this.generatedForm.%s.$dirty";
    protected static final String ERROR_EXPRESSION = "this.generatedForm.%s.$error";
    protected static final String DATE_ERROR_EXPRESSION = "this.generatedForm.%s.$error.date";
    protected static final String REQUIRED_ERROR_EXPRESSION = "this.generatedForm.%s.$error.required";
    protected static final String TYPE_ERROR_EXPRESSION = "this.generatedForm.%s.$error.camVariableType";
    protected static final String DATE_FIELD_OPENED_ATTRIBUTE = "dateFieldOpened%s";
    protected static final String OPEN_DATEPICKER_SNIPPET = "$scope.open%s = function ($event) { $event.preventDefault(); $event.stopPropagation(); $scope.dateFieldOpened%s = true; };";
    protected static final String OPEN_DATEPICKER_FUNCTION_SNIPPET = "open%s($event)";
    protected static final String DATE_FORMAT = "dd/MM/yyyy";
    protected static final String REQUIRED_FIELD_MESSAGE = "Required field";
    protected static final String TYPE_FIELD_MESSAGE = "Only a %s value is allowed";
    protected static final String INVALID_DATE_FIELD_MESSAGE = "Invalid date format: the date should have the pattern 'dd/MM/yyyy'";
    
    protected String renderFormData(final FormData formData) {
        if (formData == null || ((formData.getFormFields() == null || formData.getFormFields().isEmpty()) && (formData.getFormProperties() == null || formData.getFormProperties().isEmpty()))) {
            return null;
        }
        final HtmlElementWriter formElement = new HtmlElementWriter("form").attribute("name", "generatedForm").attribute("role", "form");
        final HtmlDocumentBuilder documentBuilder = new HtmlDocumentBuilder(formElement);
        for (final FormField formField : formData.getFormFields()) {
            this.renderFormField(formField, documentBuilder);
        }
        for (final FormProperty formProperty : formData.getFormProperties()) {
            this.renderFormField(new FormPropertyAdapter(formProperty), documentBuilder);
        }
        documentBuilder.endElement();
        return documentBuilder.getHtmlString();
    }
    
    protected void renderFormField(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final HtmlElementWriter divElement = new HtmlElementWriter("div").attribute("class", "form-group");
        documentBuilder.startElement(divElement);
        final String formFieldId = formField.getId();
        final String formFieldLabel = formField.getLabel();
        if (formFieldLabel != null && !formFieldLabel.isEmpty()) {
            final HtmlElementWriter labelElement = new HtmlElementWriter("label").attribute("for", formFieldId).textContent(formFieldLabel);
            documentBuilder.startElement(labelElement).endElement();
        }
        if (this.isEnum(formField)) {
            this.renderSelectBox(formField, documentBuilder);
        }
        else if (this.isDate(formField)) {
            this.renderDatePicker(formField, documentBuilder);
        }
        else {
            this.renderInputField(formField, documentBuilder);
        }
        this.renderInvalidMessageElement(formField, documentBuilder);
        documentBuilder.endElement();
    }
    
    protected HtmlElementWriter createInputField(final FormField formField) {
        final HtmlElementWriter inputField = new HtmlElementWriter("input", true);
        this.addCommonFormFieldAttributes(formField, inputField);
        inputField.attribute("type", "text");
        return inputField;
    }
    
    protected void renderDatePicker(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final boolean isReadOnly = this.isReadOnly(formField);
        final HtmlElementWriter inputGroupDivElement = new HtmlElementWriter("div").attribute("class", "input-group");
        final String formFieldId = formField.getId();
        documentBuilder.startElement(inputGroupDivElement);
        final HtmlElementWriter inputField = this.createInputField(formField);
        if (!isReadOnly) {
            inputField.attribute("datepicker-popup", "dd/MM/yyyy").attribute("is-open", String.format("dateFieldOpened%s", formFieldId));
        }
        documentBuilder.startElement(inputField).endElement();
        if (!isReadOnly) {
            final HtmlElementWriter addonElement = new HtmlElementWriter("div").attribute("class", "input-group-btn");
            documentBuilder.startElement(addonElement);
            final HtmlElementWriter buttonElement = new HtmlElementWriter("button").attribute("type", "button").attribute("class", "btn btn-default").attribute("ng-click", String.format("open%s($event)", formFieldId));
            documentBuilder.startElement(buttonElement);
            final HtmlElementWriter iconElement = new HtmlElementWriter("i").attribute("class", "glyphicon glyphicon-calendar");
            documentBuilder.startElement(iconElement).endElement();
            documentBuilder.endElement();
            documentBuilder.endElement();
            final HtmlElementWriter scriptElement = new HtmlElementWriter("script").attribute("cam-script", null).attribute("type", "text/form-script").textContent(String.format("$scope.open%s = function ($event) { $event.preventDefault(); $event.stopPropagation(); $scope.dateFieldOpened%s = true; };", formFieldId, formFieldId));
            documentBuilder.startElement(scriptElement).endElement();
        }
        documentBuilder.endElement();
    }
    
    protected void renderInputField(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final HtmlElementWriter inputField = new HtmlElementWriter("input", true);
        this.addCommonFormFieldAttributes(formField, inputField);
        final String inputType = this.isBoolean(formField) ? "checkbox" : "text";
        inputField.attribute("type", inputType);
        final Object defaultValue = formField.getDefaultValue();
        if (defaultValue != null) {
            inputField.attribute("value", defaultValue.toString());
        }
        documentBuilder.startElement(inputField).endElement();
    }
    
    protected void renderSelectBox(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final HtmlElementWriter selectBox = new HtmlElementWriter("select", false);
        this.addCommonFormFieldAttributes(formField, selectBox);
        documentBuilder.startElement(selectBox);
        this.renderSelectOptions(formField, documentBuilder);
        documentBuilder.endElement();
    }
    
    protected void renderSelectOptions(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final EnumFormType enumFormType = (EnumFormType)formField.getType();
        final Map<String, String> values = enumFormType.getValues();
        for (final Map.Entry<String, String> value : values.entrySet()) {
            final HtmlElementWriter option = new HtmlElementWriter("option", false).attribute("value", value.getKey()).textContent(value.getValue());
            documentBuilder.startElement(option).endElement();
        }
    }
    
    protected void renderInvalidMessageElement(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final HtmlElementWriter divElement = new HtmlElementWriter("div");
        final String formFieldId = formField.getId();
        final String ifExpression = String.format("this.generatedForm.%s.$invalid && this.generatedForm.%s.$dirty", formFieldId, formFieldId);
        divElement.attribute("ng-if", ifExpression).attribute("class", "has-error");
        documentBuilder.startElement(divElement);
        if (!this.isDate(formField)) {
            this.renderInvalidValueMessage(formField, documentBuilder);
            this.renderInvalidTypeMessage(formField, documentBuilder);
        }
        else {
            this.renderInvalidDateMessage(formField, documentBuilder);
        }
        documentBuilder.endElement();
    }
    
    protected void renderInvalidValueMessage(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final HtmlElementWriter divElement = new HtmlElementWriter("div");
        final String formFieldId = formField.getId();
        final String expression = String.format("this.generatedForm.%s.$error.required", formFieldId);
        divElement.attribute("ng-show", expression).attribute("class", "help-block").textContent("Required field");
        documentBuilder.startElement(divElement).endElement();
    }
    
    protected void renderInvalidTypeMessage(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final HtmlElementWriter divElement = new HtmlElementWriter("div");
        final String formFieldId = formField.getId();
        final String expression = String.format("this.generatedForm.%s.$error.camVariableType", formFieldId);
        String typeName = formField.getTypeName();
        if (this.isEnum(formField)) {
            typeName = "string";
        }
        divElement.attribute("ng-show", expression).attribute("class", "help-block").textContent(String.format("Only a %s value is allowed", typeName));
        documentBuilder.startElement(divElement).endElement();
    }
    
    protected void renderInvalidDateMessage(final FormField formField, final HtmlDocumentBuilder documentBuilder) {
        final String formFieldId = formField.getId();
        final HtmlElementWriter firstDivElement = new HtmlElementWriter("div");
        final String firstExpression = String.format("this.generatedForm.%s.$error.required && !this.generatedForm.%s.$error.date", formFieldId, formFieldId);
        firstDivElement.attribute("ng-show", firstExpression).attribute("class", "help-block").textContent("Required field");
        documentBuilder.startElement(firstDivElement).endElement();
        final HtmlElementWriter secondDivElement = new HtmlElementWriter("div");
        final String secondExpression = String.format("this.generatedForm.%s.$error.date", formFieldId);
        secondDivElement.attribute("ng-show", secondExpression).attribute("class", "help-block").textContent("Invalid date format: the date should have the pattern 'dd/MM/yyyy'");
        documentBuilder.startElement(secondDivElement).endElement();
    }
    
    protected void addCommonFormFieldAttributes(final FormField formField, final HtmlElementWriter formControl) {
        String typeName = formField.getTypeName();
        if (this.isEnum(formField) || this.isDate(formField)) {
            typeName = "string";
        }
        typeName = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
        final String formFieldId = formField.getId();
        formControl.attribute("class", "form-control").attribute("name", formFieldId).attribute("cam-variable-type", typeName).attribute("cam-variable-name", formFieldId);
        for (final FormFieldValidationConstraint constraint : formField.getValidationConstraints()) {
            final String constraintName = constraint.getName();
            final String configuration = (String)constraint.getConfiguration();
            formControl.attribute(constraintName, configuration);
        }
    }
    
    protected boolean isEnum(final FormField formField) {
        return "enum".equals(formField.getTypeName());
    }
    
    protected boolean isDate(final FormField formField) {
        return "date".equals(formField.getTypeName());
    }
    
    protected boolean isBoolean(final FormField formField) {
        return "boolean".equals(formField.getTypeName());
    }
    
    protected boolean isReadOnly(final FormField formField) {
        final List<FormFieldValidationConstraint> validationConstraints = formField.getValidationConstraints();
        if (validationConstraints != null) {
            for (final FormFieldValidationConstraint validationConstraint : validationConstraints) {
                if ("readonly".equals(validationConstraint.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
