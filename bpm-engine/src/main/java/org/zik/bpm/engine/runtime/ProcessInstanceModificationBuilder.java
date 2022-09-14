// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.batch.Batch;

public interface ProcessInstanceModificationBuilder extends InstantiationBuilder<ProcessInstanceModificationInstantiationBuilder>
{
    ProcessInstanceModificationInstantiationBuilder startBeforeActivity(final String p0, final String p1);
    
    ProcessInstanceModificationInstantiationBuilder startAfterActivity(final String p0, final String p1);
    
    ProcessInstanceModificationInstantiationBuilder startTransition(final String p0, final String p1);
    
    ProcessInstanceModificationBuilder cancelActivityInstance(final String p0);
    
    ProcessInstanceModificationBuilder cancelTransitionInstance(final String p0);
    
    ProcessInstanceModificationBuilder cancelAllForActivity(final String p0);
    
    ProcessInstanceModificationBuilder cancellationSourceExternal(final boolean p0);
    
    ProcessInstanceModificationBuilder setAnnotation(final String p0);
    
    void execute();
    
    void execute(final boolean p0, final boolean p1);
    
    Batch executeAsync();
    
    Batch executeAsync(final boolean p0, final boolean p1);
}
