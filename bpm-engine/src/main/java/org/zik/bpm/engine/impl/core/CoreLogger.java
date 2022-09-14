// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core;

import java.text.MessageFormat;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class CoreLogger extends ProcessEngineLogger
{
    public void debugMappingValueFromOuterScopeToInnerScope(final Object value, final AbstractVariableScope outerScope, final String name, final AbstractVariableScope innerScope) {
        this.logDebug("001", "Mapping value '{} from outer scope '{}' to variable '{}' in inner scope '{}'.", new Object[] { value, outerScope, name, innerScope });
    }
    
    public void debugMappingValuefromInnerScopeToOuterScope(final Object value, final AbstractVariableScope innerScope, final String name, final AbstractVariableScope outerScope) {
        this.logDebug("002", "Mapping value '{}' from inner scope '{}' to variable '{}' in outer scope '{}'.", new Object[] { value, innerScope, name, outerScope });
    }
    
    public void debugPerformingAtomicOperation(final CoreAtomicOperation<?> atomicOperation, final CoreExecution e) {
        this.logDebug("003", "Performing atomic operation {} on {}", new Object[] { atomicOperation, e });
    }
    
    public ProcessEngineException duplicateVariableInstanceException(final CoreVariableInstance variableInstance) {
        return new ProcessEngineException(this.exceptionMessage("004", "Cannot add variable instance with name {}. Variable already exists", new Object[] { variableInstance.getName() }));
    }
    
    public ProcessEngineException transientVariableException(final String variableName) {
        return new ProcessEngineException(this.exceptionMessage("006", "Cannot set transient variable with name {} to non-transient variable and vice versa.", new Object[] { variableName }));
    }
    
    public ProcessEngineException javaSerializationProhibitedException(final String variableName) {
        return new ProcessEngineException(this.exceptionMessage("007", MessageFormat.format("Cannot set variable with name {0}. Java serialization format is prohibited", variableName), new Object[0]));
    }
}
