// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.javax.el.CompositeELResolver;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.ServiceLoader;
import org.zik.bpm.application.ProcessApplicationElResolver;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import org.zik.bpm.application.AbstractProcessApplication;

public class DefaultElResolverLookup
{
    private static final ProcessApplicationLogger LOG;
    
    public static final ELResolver lookupResolver(final AbstractProcessApplication processApplication) {
        final ServiceLoader<ProcessApplicationElResolver> providers = ServiceLoader.load(ProcessApplicationElResolver.class);
        final List<ProcessApplicationElResolver> sortedProviders = new ArrayList<ProcessApplicationElResolver>();
        for (final ProcessApplicationElResolver provider : providers) {
            sortedProviders.add(provider);
        }
        if (sortedProviders.isEmpty()) {
            return null;
        }
        Collections.sort(sortedProviders, new ProcessApplicationElResolver.ProcessApplicationElResolverSorter());
        final CompositeELResolver compositeResolver = new CompositeELResolver();
        final StringBuilder summary = new StringBuilder();
        summary.append(String.format("ElResolvers found for Process Application %s", processApplication.getName()));
        for (final ProcessApplicationElResolver processApplicationElResolver : sortedProviders) {
            final ELResolver elResolver = processApplicationElResolver.getElResolver(processApplication);
            if (elResolver != null) {
                compositeResolver.add(elResolver);
                summary.append(String.format("Class %s", processApplicationElResolver.getClass().getName()));
            }
            else {
                DefaultElResolverLookup.LOG.noElResolverProvided(processApplication.getName(), processApplicationElResolver.getClass().getName());
            }
        }
        DefaultElResolverLookup.LOG.paElResolversDiscovered(summary.toString());
        return compositeResolver;
    }
    
    static {
        LOG = ProcessEngineLogger.PROCESS_APPLICATION_LOGGER;
    }
}
