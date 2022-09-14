// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import java.util.Iterator;
import java.lang.reflect.Method;
import java.util.List;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;

public class CompositeFunctionMapper extends FunctionMapper
{
    protected List<FunctionMapper> delegateMappers;
    
    public CompositeFunctionMapper(final List<FunctionMapper> delegateMappers) {
        this.delegateMappers = delegateMappers;
    }
    
    @Override
    public Method resolveFunction(final String prefix, final String localName) {
        for (final FunctionMapper mapper : this.delegateMappers) {
            final Method resolvedFunction = mapper.resolveFunction(prefix, localName);
            if (resolvedFunction != null) {
                return resolvedFunction;
            }
        }
        return null;
    }
}
