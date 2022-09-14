// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingTransitionInstance;

public interface MigratingTransitionInstanceValidator
{
    void validate(final MigratingTransitionInstance p0, final MigratingProcessInstance p1, final MigratingTransitionInstanceValidationReportImpl p2);
}
