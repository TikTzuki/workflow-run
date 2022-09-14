// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.instance;

import org.zik.bpm.engine.impl.migration.instance.MigratingCompensationEventSubscriptionInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingProcessInstance;
import org.zik.bpm.engine.impl.migration.instance.MigratingEventScopeInstance;

public interface MigratingCompensationInstanceValidator
{
    void validate(final MigratingEventScopeInstance p0, final MigratingProcessInstance p1, final MigratingActivityInstanceValidationReportImpl p2);
    
    void validate(final MigratingCompensationEventSubscriptionInstance p0, final MigratingProcessInstance p1, final MigratingActivityInstanceValidationReportImpl p2);
}
