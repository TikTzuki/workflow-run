// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.tree.ReferenceWalker;
import java.util.LinkedList;
import org.zik.bpm.engine.impl.tree.FlowScopeWalker;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.List;
import org.zik.bpm.engine.impl.tree.TreeVisitor;

public abstract class MigratingProcessElementInstanceVisitor implements TreeVisitor<MigratingProcessElementInstanceTopDownWalker.MigrationContext>
{
    @Override
    public void visit(final MigratingProcessElementInstanceTopDownWalker.MigrationContext obj) {
        if (this.canMigrate(obj.processElementInstance)) {
            this.migrateProcessElementInstance(obj.processElementInstance, obj.scopeInstanceBranch);
        }
    }
    
    protected abstract boolean canMigrate(final MigratingProcessElementInstance p0);
    
    protected abstract void instantiateScopes(final MigratingScopeInstance p0, final MigratingScopeInstanceBranch p1, final List<ScopeImpl> p2);
    
    protected void migrateProcessElementInstance(final MigratingProcessElementInstance migratingInstance, final MigratingScopeInstanceBranch migratingInstanceBranch) {
        final MigratingScopeInstance parentMigratingInstance = migratingInstance.getParent();
        final ScopeImpl sourceScope = migratingInstance.getSourceScope();
        final ScopeImpl targetScope = migratingInstance.getTargetScope();
        final ScopeImpl targetFlowScope = targetScope.getFlowScope();
        final ScopeImpl parentActivityInstanceTargetScope = (parentMigratingInstance != null) ? parentMigratingInstance.getTargetScope() : null;
        if (sourceScope != sourceScope.getProcessDefinition() && targetFlowScope != parentActivityInstanceTargetScope) {
            final List<ScopeImpl> nonExistingScopes = this.collectNonExistingFlowScopes(targetFlowScope, migratingInstanceBranch);
            final ScopeImpl existingScope = nonExistingScopes.isEmpty() ? targetFlowScope : nonExistingScopes.get(0).getFlowScope();
            final MigratingScopeInstance ancestorScopeInstance = migratingInstanceBranch.getInstance(existingScope);
            this.instantiateScopes(ancestorScopeInstance, migratingInstanceBranch, nonExistingScopes);
            final MigratingScopeInstance targetFlowScopeInstance = migratingInstanceBranch.getInstance(targetFlowScope);
            migratingInstance.detachState();
            migratingInstance.attachState(targetFlowScopeInstance);
        }
        migratingInstance.migrateState();
        migratingInstance.migrateDependentEntities();
    }
    
    protected List<ScopeImpl> collectNonExistingFlowScopes(final ScopeImpl scope, final MigratingScopeInstanceBranch migratingExecutionBranch) {
        final FlowScopeWalker walker = new FlowScopeWalker(scope);
        final List<ScopeImpl> result = new LinkedList<ScopeImpl>();
        walker.addPreVisitor(new TreeVisitor<ScopeImpl>() {
            @Override
            public void visit(final ScopeImpl obj) {
                result.add(0, obj);
            }
        });
        walker.walkWhile(new ReferenceWalker.WalkCondition<ScopeImpl>() {
            @Override
            public boolean isFulfilled(final ScopeImpl element) {
                return migratingExecutionBranch.hasInstance(element);
            }
        });
        return result;
    }
}
