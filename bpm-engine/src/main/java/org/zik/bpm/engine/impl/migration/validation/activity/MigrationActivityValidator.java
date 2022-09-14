// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.activity;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public interface MigrationActivityValidator
{
    boolean valid(final ActivityImpl p0);
}
