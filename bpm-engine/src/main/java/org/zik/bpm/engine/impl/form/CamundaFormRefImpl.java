// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import org.zik.bpm.engine.form.CamundaFormRef;

public class CamundaFormRefImpl implements CamundaFormRef
{
    String key;
    String binding;
    Integer version;
    
    public CamundaFormRefImpl(final String key, final String binding) {
        this.key = key;
        this.binding = binding;
    }
    
    @Override
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    @Override
    public String getBinding() {
        return this.binding;
    }
    
    public void setBinding(final String binding) {
        this.binding = binding;
    }
    
    @Override
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(final Integer version) {
        this.version = version;
    }
    
    @Override
    public String toString() {
        return "CamundaFormRefImpl [key=" + this.key + ", binding=" + this.binding + ", version=" + this.version + "]";
    }
}
