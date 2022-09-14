// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.query;

import java.util.List;

public interface Query<T extends Query<?, ?>, U>
{
    T asc();
    
    T desc();
    
    long count();
    
    U singleResult();
    
    List<U> list();
    
    List<U> unlimitedList();
    
    List<U> listPage(final int p0, final int p1);
}
