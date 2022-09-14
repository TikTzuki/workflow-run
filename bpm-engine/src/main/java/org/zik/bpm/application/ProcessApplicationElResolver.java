// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

import java.util.Comparator;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public interface ProcessApplicationElResolver
{
    public static final int SPRING_RESOLVER = 100;
    public static final int CDI_RESOLVER = 200;
    
    Integer getPrecedence();
    
    ELResolver getElResolver(final AbstractProcessApplication p0);
    
    public static class ProcessApplicationElResolverSorter implements Comparator<ProcessApplicationElResolver>
    {
        @Override
        public int compare(final ProcessApplicationElResolver o1, final ProcessApplicationElResolver o2) {
            return -1 * o1.getPrecedence().compareTo(o2.getPrecedence());
        }
    }
}
