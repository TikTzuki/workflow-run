// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

public class MinValidator extends AbstractNumericValidator
{
    @Override
    protected boolean validate(final Integer submittedValue, final Integer configuration) {
        return submittedValue >= configuration;
    }
    
    @Override
    protected boolean validate(final Long submittedValue, final Long configuration) {
        return submittedValue >= configuration;
    }
    
    @Override
    protected boolean validate(final Double submittedValue, final Double configuration) {
        return submittedValue >= configuration;
    }
    
    @Override
    protected boolean validate(final Float submittedValue, final Float configuration) {
        return submittedValue >= configuration;
    }
    
    @Override
    protected boolean validate(final Short submittedValue, final Short configuration) {
        return submittedValue >= configuration;
    }
}
