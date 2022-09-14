// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public interface MigrationActivityMatcher
{
    boolean matchActivities(final ActivityImpl p0, final ActivityImpl p1);
}
