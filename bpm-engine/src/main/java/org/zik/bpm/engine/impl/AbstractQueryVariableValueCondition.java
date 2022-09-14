// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.variable.serializer.VariableSerializers;

public abstract class AbstractQueryVariableValueCondition
{
    protected QueryVariableValue wrappedQueryValue;
    
    public AbstractQueryVariableValueCondition(final QueryVariableValue variableValue) {
        this.wrappedQueryValue = variableValue;
    }
    
    public abstract void initializeValue(final VariableSerializers p0, final String p1);
    
    public abstract List<SingleQueryVariableValueCondition> getDisjunctiveConditions();
}
