// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.delegate;

import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.migration.instance.parser.MigratingInstanceParseContext;

public interface MigrationObserverBehavior
{
    void migrateScope(final ActivityExecution p0);
    
    void onParseMigratingInstance(final MigratingInstanceParseContext p0, final MigratingActivityInstance p1);
}
