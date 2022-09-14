// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.zik.bpm.engine.impl.util.xml.Element;
import java.util.HashMap;
import java.util.Map;

public class FormValidators
{
    protected Map<String, Class<? extends FormFieldValidator>> validators;
    
    public FormValidators() {
        this.validators = new HashMap<String, Class<? extends FormFieldValidator>>();
    }
    
    public FormFieldValidator createValidator(final Element constraint, final BpmnParse bpmnParse, final ExpressionManager expressionManager) {
        final String name = constraint.attribute("name");
        final String config = constraint.attribute("config");
        if ("validator".equals(name)) {
            if (config == null || config.isEmpty()) {
                bpmnParse.addError("validator configuration needs to provide either a fully qualified classname or an expression resolving to a custom FormFieldValidator implementation.", constraint);
            }
            else {
                if (StringUtil.isExpression(config)) {
                    final Expression validatorExpression = expressionManager.createExpression(config);
                    return new DelegateFormFieldValidator(validatorExpression);
                }
                return new DelegateFormFieldValidator(config);
            }
        }
        else {
            final Class<? extends FormFieldValidator> validator = this.validators.get(name);
            if (validator != null) {
                final FormFieldValidator validatorInstance = this.createValidatorInstance(validator);
                return validatorInstance;
            }
            bpmnParse.addError("Cannot find validator implementation for name '" + name + "'.", constraint);
        }
        return null;
    }
    
    protected FormFieldValidator createValidatorInstance(final Class<? extends FormFieldValidator> validator) {
        try {
            return (FormFieldValidator)validator.newInstance();
        }
        catch (InstantiationException e) {
            throw new ProcessEngineException("Could not instantiate validator", e);
        }
        catch (IllegalAccessException e2) {
            throw new ProcessEngineException("Could not instantiate validator", e2);
        }
    }
    
    public void addValidator(final String name, final Class<? extends FormFieldValidator> validatorType) {
        this.validators.put(name, validatorType);
    }
    
    public Map<String, Class<? extends FormFieldValidator>> getValidators() {
        return this.validators;
    }
}
