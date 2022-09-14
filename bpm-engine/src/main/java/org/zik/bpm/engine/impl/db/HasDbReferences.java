// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public interface HasDbReferences
{
    Set<String> getReferencedEntityIds();
    
    Map<String, Class> getReferencedEntitiesIdAndClass();
    
    default Map<String, Class> getDependentEntities() {
        return (Map<String, Class>)Collections.EMPTY_MAP;
    }
}
