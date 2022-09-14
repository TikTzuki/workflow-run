// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import java.util.ArrayList;
import org.zik.bpm.engine.form.FormField;
import org.zik.bpm.engine.form.FormProperty;
import java.util.List;
import org.zik.bpm.engine.form.CamundaFormRef;
import java.io.Serializable;
import org.zik.bpm.engine.form.FormData;

public abstract class FormDataImpl implements FormData, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String formKey;
    protected CamundaFormRef camundaFormRef;
    protected String deploymentId;
    protected List<FormProperty> formProperties;
    protected List<FormField> formFields;
    
    public FormDataImpl() {
        this.formProperties = new ArrayList<FormProperty>();
        this.formFields = new ArrayList<FormField>();
    }
    
    @Override
    public String getFormKey() {
        return this.formKey;
    }
    
    public void setFormKey(final String formKey) {
        this.formKey = formKey;
    }
    
    @Override
    public CamundaFormRef getCamundaFormRef() {
        return this.camundaFormRef;
    }
    
    public void setCamundaFormRef(final CamundaFormRef camundaFormRef) {
        this.camundaFormRef = camundaFormRef;
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Override
    public List<FormProperty> getFormProperties() {
        return this.formProperties;
    }
    
    public void setFormProperties(final List<FormProperty> formProperties) {
        this.formProperties = formProperties;
    }
    
    @Override
    public List<FormField> getFormFields() {
        return this.formFields;
    }
    
    public void setFormFields(final List<FormField> formFields) {
        this.formFields = formFields;
    }
}
