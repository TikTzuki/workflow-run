// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import org.zik.bpm.engine.delegate.Expression;

public class FormDefinition
{
    protected Expression formKey;
    protected Expression camundaFormDefinitionKey;
    protected String camundaFormDefinitionBinding;
    protected Expression camundaFormDefinitionVersion;
    
    public Expression getFormKey() {
        return this.formKey;
    }
    
    public void setFormKey(final Expression formKey) {
        this.formKey = formKey;
    }
    
    public Expression getCamundaFormDefinitionKey() {
        return this.camundaFormDefinitionKey;
    }
    
    public void setCamundaFormDefinitionKey(final Expression camundaFormDefinitionKey) {
        this.camundaFormDefinitionKey = camundaFormDefinitionKey;
    }
    
    public String getCamundaFormDefinitionBinding() {
        return this.camundaFormDefinitionBinding;
    }
    
    public void setCamundaFormDefinitionBinding(final String camundaFormDefinitionBinding) {
        this.camundaFormDefinitionBinding = camundaFormDefinitionBinding;
    }
    
    public Expression getCamundaFormDefinitionVersion() {
        return this.camundaFormDefinitionVersion;
    }
    
    public void setCamundaFormDefinitionVersion(final Expression camundaFormDefinitionVersion) {
        this.camundaFormDefinitionVersion = camundaFormDefinitionVersion;
    }
}
