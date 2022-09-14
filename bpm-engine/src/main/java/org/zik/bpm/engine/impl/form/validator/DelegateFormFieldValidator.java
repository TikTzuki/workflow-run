// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.application.InvocationContext;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.delegate.Expression;

public class DelegateFormFieldValidator implements FormFieldValidator
{
    protected String clazz;
    protected Expression delegateExpression;
    
    public DelegateFormFieldValidator(final Expression expression) {
        this.delegateExpression = expression;
    }
    
    public DelegateFormFieldValidator(final String clazz) {
        this.clazz = clazz;
    }
    
    public DelegateFormFieldValidator() {
    }
    
    @Override
    public boolean validate(final Object submittedValue, final FormFieldValidatorContext validatorContext) {
        final DelegateExecution execution = validatorContext.getExecution();
        if (this.shouldPerformPaContextSwitch(validatorContext.getExecution())) {
            final ProcessApplicationReference processApplicationReference = ProcessApplicationContextUtil.getTargetProcessApplication((ExecutionEntity)execution);
            return Context.executeWithinProcessApplication((Callable<Boolean>)new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return DelegateFormFieldValidator.this.doValidate(submittedValue, validatorContext);
                }
            }, processApplicationReference, new InvocationContext(execution));
        }
        return this.doValidate(submittedValue, validatorContext);
    }
    
    protected boolean shouldPerformPaContextSwitch(final DelegateExecution execution) {
        if (execution == null) {
            return false;
        }
        final ProcessApplicationReference targetPa = ProcessApplicationContextUtil.getTargetProcessApplication((ExecutionEntity)execution);
        return targetPa != null && !targetPa.equals(Context.getCurrentProcessApplication());
    }
    
    protected boolean doValidate(final Object submittedValue, final FormFieldValidatorContext validatorContext) {
        FormFieldValidator validator;
        if (this.clazz != null) {
            final Object validatorObject = ReflectUtil.instantiate(this.clazz);
            if (!(validatorObject instanceof FormFieldValidator)) {
                throw new ProcessEngineException("Validator class '" + this.clazz + "' is not an instance of " + FormFieldValidator.class.getName());
            }
            validator = (FormFieldValidator)validatorObject;
        }
        else {
            final Object validatorObject = this.delegateExpression.getValue(validatorContext.getExecution());
            if (!(validatorObject instanceof FormFieldValidator)) {
                throw new ProcessEngineException("Validator expression '" + this.delegateExpression + "' does not resolve to instance of " + FormFieldValidator.class.getName());
            }
            validator = (FormFieldValidator)validatorObject;
        }
        final FormFieldValidatorInvocation invocation = new FormFieldValidatorInvocation(validator, submittedValue, validatorContext);
        try {
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException(e2);
        }
        return invocation.getInvocationResult();
    }
}
