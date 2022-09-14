// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import org.zik.bpm.engine.impl.tree.ReferenceWalker;

import java.util.Collection;
import java.util.LinkedList;

public class MigratingProcessElementInstanceTopDownWalker extends ReferenceWalker<MigratingProcessElementInstanceTopDownWalker.MigrationContext> {
    public MigratingProcessElementInstanceTopDownWalker(final MigratingActivityInstance activityInstance) {
        super(new MigrationContext(activityInstance, new MigratingScopeInstanceBranch()));
    }

    @Override
    protected Collection<MigrationContext> nextElements() {
        final Collection<MigrationContext> nextElements = new LinkedList<MigrationContext>();
        final MigrationContext currentElement = this.getCurrentElement();
        if (currentElement.processElementInstance instanceof MigratingScopeInstance) {
            final MigratingScopeInstanceBranch childrenScopeBranch = currentElement.scopeInstanceBranch.copy();
            final MigratingScopeInstanceBranch childrenCompensationScopeBranch = currentElement.scopeInstanceBranch.copy();
            final MigratingScopeInstance scopeInstance = (MigratingScopeInstance) currentElement.processElementInstance;
            childrenScopeBranch.visited(scopeInstance);
            childrenCompensationScopeBranch.visited(scopeInstance);
            for (final MigratingProcessElementInstance child : scopeInstance.getChildren()) {
                MigratingScopeInstanceBranch instanceBranch = null;
                if (child instanceof MigratingEventScopeInstance || child instanceof MigratingCompensationEventSubscriptionInstance) {
                    instanceBranch = childrenCompensationScopeBranch;
                } else {
                    instanceBranch = childrenScopeBranch;
                }
                nextElements.add(new MigrationContext(child, instanceBranch));
            }
        }
        return nextElements;
    }

    public static class MigrationContext {
        protected MigratingProcessElementInstance processElementInstance;
        protected MigratingScopeInstanceBranch scopeInstanceBranch;

        public MigrationContext(final MigratingProcessElementInstance processElementInstance, final MigratingScopeInstanceBranch scopeInstanceBranch) {
            this.processElementInstance = processElementInstance;
            this.scopeInstanceBranch = scopeInstanceBranch;
        }

        public MigratingProcessElementInstance getProcessElementInstance() {
            return this.processElementInstance;
        }

        public MigratingScopeInstanceBranch getScopeInstanceBranch() {
            return this.scopeInstanceBranch;
        }
    }
}
