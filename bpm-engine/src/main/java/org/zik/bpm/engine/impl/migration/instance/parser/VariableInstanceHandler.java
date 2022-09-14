// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance.parser;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.migration.instance.MigratingInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingVariableInstance;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import java.util.List;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessElementInstance;

public class VariableInstanceHandler implements MigratingDependentInstanceParseHandler<MigratingProcessElementInstance, List<VariableInstanceEntity>>
{
    @Override
    public void handle(final MigratingInstanceParseContext parseContext, final MigratingProcessElementInstance owningInstance, final List<VariableInstanceEntity> variables) {
        final ExecutionEntity representativeExecution = owningInstance.resolveRepresentativeExecution();
        for (final VariableInstanceEntity variable : variables) {
            parseContext.consume(variable);
            final boolean isConcurrentLocalInParentScope = (variable.getExecution() == representativeExecution.getParent() && variable.isConcurrentLocal()) || representativeExecution.isConcurrent();
            owningInstance.addMigratingDependentInstance(new MigratingVariableInstance(variable, isConcurrentLocalInParentScope));
        }
    }
}
