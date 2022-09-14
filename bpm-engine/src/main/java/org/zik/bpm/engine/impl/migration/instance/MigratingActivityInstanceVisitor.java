// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import java.util.Iterator;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.List;

public class MigratingActivityInstanceVisitor extends MigratingProcessElementInstanceVisitor
{
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    
    public MigratingActivityInstanceVisitor(final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
    }
    
    @Override
    protected boolean canMigrate(final MigratingProcessElementInstance instance) {
        return instance instanceof MigratingActivityInstance || instance instanceof MigratingTransitionInstance;
    }
    
    @Override
    protected void instantiateScopes(final MigratingScopeInstance ancestorScopeInstance, final MigratingScopeInstanceBranch executionBranch, final List<ScopeImpl> scopesToInstantiate) {
        if (scopesToInstantiate.isEmpty()) {
            return;
        }
        final MigratingActivityInstance ancestorActivityInstance = (MigratingActivityInstance)ancestorScopeInstance;
        final ExecutionEntity newParentExecution = ancestorActivityInstance.createAttachableExecution();
        final Map<PvmActivity, PvmExecutionImpl> createdExecutions = newParentExecution.instantiateScopes((List<PvmActivity>)scopesToInstantiate, this.skipCustomListeners, this.skipIoMappings);
        for (final ScopeImpl scope : scopesToInstantiate) {
            final ExecutionEntity createdExecution = createdExecutions.get(scope);
            createdExecution.setActivity(null);
            createdExecution.setActive(false);
            executionBranch.visited(new MigratingActivityInstance(scope, createdExecution));
        }
    }
}
