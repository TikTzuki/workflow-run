// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.mapping;

import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.core.CoreLogger;

public class OutputParameter extends IoParameter
{
    private static final CoreLogger LOG;
    
    public OutputParameter(final String name, final ParameterValueProvider valueProvider) {
        super(name, valueProvider);
    }
    
    @Override
    protected void execute(final AbstractVariableScope innerScope, final AbstractVariableScope outerScope) {
        final Object value = this.valueProvider.getValue(innerScope);
        OutputParameter.LOG.debugMappingValuefromInnerScopeToOuterScope(value, innerScope, this.name, outerScope);
        outerScope.setVariable(this.name, value);
    }
    
    static {
        LOG = CoreLogger.CORE_LOGGER;
    }
}
