// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;

public class VariableCollectionProvider<T extends CoreVariableInstance> implements VariableStore.VariablesProvider<T>
{
    protected Collection<T> variables;
    
    public VariableCollectionProvider(final Collection<T> variables) {
        this.variables = variables;
    }
    
    @Override
    public Collection<T> provideVariables() {
        if (this.variables == null) {
            return new ArrayList<T>();
        }
        return this.variables;
    }
    
    @Override
    public Collection<T> provideVariables(final Collection<String> variablesNames) {
        if (variablesNames == null) {
            return this.provideVariables();
        }
        final List<T> result = new ArrayList<T>();
        if (this.variables != null) {
            for (final T variable : this.variables) {
                if (variablesNames.contains(variable.getName())) {
                    result.add(variable);
                }
            }
        }
        return result;
    }
    
    public static <T extends CoreVariableInstance> VariableCollectionProvider<T> emptyVariables() {
        return new VariableCollectionProvider<T>((Collection<T>)Collections.emptySet());
    }
}
