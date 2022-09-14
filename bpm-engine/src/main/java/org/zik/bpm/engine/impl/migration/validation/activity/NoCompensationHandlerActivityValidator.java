// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.activity;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;

public class NoCompensationHandlerActivityValidator implements MigrationActivityValidator
{
    public static NoCompensationHandlerActivityValidator INSTANCE;
    
    @Override
    public boolean valid(final ActivityImpl activity) {
        return !activity.isCompensationHandler();
    }
    
    static {
        NoCompensationHandlerActivityValidator.INSTANCE = new NoCompensationHandlerActivityValidator();
    }
}
