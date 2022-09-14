// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.query;

import java.util.List;

public interface NativeQuery<T extends NativeQuery<?, ?>, U>
{
    T sql(final String p0);
    
    T parameter(final String p0, final Object p1);
    
    long count();
    
    U singleResult();
    
    List<U> list();
    
    List<U> listPage(final int p0, final int p1);
}
