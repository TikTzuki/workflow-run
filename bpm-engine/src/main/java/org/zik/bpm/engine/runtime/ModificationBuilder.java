// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.batch.Batch;
import java.util.List;

public interface ModificationBuilder extends InstantiationBuilder<ModificationBuilder>
{
    ModificationBuilder cancelAllForActivity(final String p0);
    
    ModificationBuilder cancelAllForActivity(final String p0, final boolean p1);
    
    ModificationBuilder processInstanceIds(final List<String> p0);
    
    ModificationBuilder processInstanceIds(final String... p0);
    
    ModificationBuilder processInstanceQuery(final ProcessInstanceQuery p0);
    
    ModificationBuilder skipCustomListeners();
    
    ModificationBuilder skipIoMappings();
    
    ModificationBuilder setAnnotation(final String p0);
    
    void execute();
    
    Batch executeAsync();
}
