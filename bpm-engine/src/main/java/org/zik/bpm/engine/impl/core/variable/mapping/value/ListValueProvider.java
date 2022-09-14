// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.mapping.value;

import java.util.Iterator;
import java.util.ArrayList;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.List;

public class ListValueProvider implements ParameterValueProvider
{
    protected List<ParameterValueProvider> providerList;
    
    public ListValueProvider(final List<ParameterValueProvider> providerList) {
        this.providerList = providerList;
    }
    
    @Override
    public Object getValue(final VariableScope variableScope) {
        final List<Object> valueList = new ArrayList<Object>();
        for (final ParameterValueProvider provider : this.providerList) {
            valueList.add(provider.getValue(variableScope));
        }
        return valueList;
    }
    
    public List<ParameterValueProvider> getProviderList() {
        return this.providerList;
    }
    
    public void setProviderList(final List<ParameterValueProvider> providerList) {
        this.providerList = providerList;
    }
    
    @Override
    public boolean isDynamic() {
        return true;
    }
}
