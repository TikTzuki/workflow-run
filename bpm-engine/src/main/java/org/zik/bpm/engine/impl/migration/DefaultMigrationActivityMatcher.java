// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public class DefaultMigrationActivityMatcher implements MigrationActivityMatcher
{
    @Override
    public boolean matchActivities(final ActivityImpl source, final ActivityImpl target) {
        return source != null && target != null && this.equalId(source, target);
    }
    
    protected boolean equalId(final ActivityImpl source, final ActivityImpl target) {
        return source.getId() != null && source.getId().equals(target.getId());
    }
}
