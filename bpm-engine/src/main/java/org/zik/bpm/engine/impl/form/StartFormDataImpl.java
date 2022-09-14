// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.form.StartFormData;

public class StartFormDataImpl extends FormDataImpl implements StartFormData
{
    private static final long serialVersionUID = 1L;
    protected ProcessDefinition processDefinition;
    
    @Override
    public ProcessDefinition getProcessDefinition() {
        return this.processDefinition;
    }
    
    public void setProcessDefinition(final ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }
}
