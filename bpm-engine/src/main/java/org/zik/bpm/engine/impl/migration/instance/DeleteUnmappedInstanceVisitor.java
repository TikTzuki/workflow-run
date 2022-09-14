// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.instance;

import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.tree.TreeVisitor;

public class DeleteUnmappedInstanceVisitor implements TreeVisitor<MigratingScopeInstance>
{
    protected Set<MigratingScopeInstance> visitedInstances;
    protected boolean skipCustomListeners;
    protected boolean skipIoMappings;
    
    public DeleteUnmappedInstanceVisitor(final boolean skipCustomListeners, final boolean skipIoMappings) {
        this.visitedInstances = new HashSet<MigratingScopeInstance>();
        this.skipCustomListeners = skipCustomListeners;
        this.skipIoMappings = skipIoMappings;
    }
    
    @Override
    public void visit(final MigratingScopeInstance currentInstance) {
        this.visitedInstances.add(currentInstance);
        if (!currentInstance.migrates()) {
            final Set<MigratingProcessElementInstance> children = new HashSet<MigratingProcessElementInstance>(currentInstance.getChildren());
            final MigratingScopeInstance parent = currentInstance.getParent();
            currentInstance.detachChildren();
            currentInstance.remove(this.skipCustomListeners, this.skipIoMappings);
            for (final MigratingProcessElementInstance child : children) {
                child.attachState(parent);
            }
        }
        else {
            currentInstance.removeUnmappedDependentInstances();
        }
    }
    
    public boolean hasVisitedAll(final Collection<MigratingScopeInstance> activityInstances) {
        return this.visitedInstances.containsAll(activityInstances);
    }
}
