// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.form;

import java.util.List;

public interface FormData
{
    String getFormKey();
    
    CamundaFormRef getCamundaFormRef();
    
    String getDeploymentId();
    
    @Deprecated
    List<FormProperty> getFormProperties();
    
    List<FormField> getFormFields();
}
