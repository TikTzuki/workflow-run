// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.mapping.value;

import java.util.Iterator;
import java.util.Map;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.TreeMap;

public class MapValueProvider implements ParameterValueProvider
{
    protected TreeMap<ParameterValueProvider, ParameterValueProvider> providerMap;
    
    public MapValueProvider(final TreeMap<ParameterValueProvider, ParameterValueProvider> providerMap) {
        this.providerMap = providerMap;
    }
    
    @Override
    public Object getValue(final VariableScope variableScope) {
        final Map<Object, Object> valueMap = new TreeMap<Object, Object>();
        for (final Map.Entry<ParameterValueProvider, ParameterValueProvider> entry : this.providerMap.entrySet()) {
            valueMap.put(entry.getKey().getValue(variableScope), entry.getValue().getValue(variableScope));
        }
        return valueMap;
    }
    
    public TreeMap<ParameterValueProvider, ParameterValueProvider> getProviderMap() {
        return this.providerMap;
    }
    
    public void setProviderMap(final TreeMap<ParameterValueProvider, ParameterValueProvider> providerMap) {
        this.providerMap = providerMap;
    }
    
    @Override
    public boolean isDynamic() {
        return true;
    }
}
