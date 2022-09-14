// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.application.ProcessApplicationUnavailableException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.javax.el.ELResolver;

public class ProcessApplicationElResolverDelegate extends AbstractElResolverDelegate
{
    @Override
    protected ELResolver getElResolverDelegate() {
        final ProcessApplicationReference processApplicationReference = Context.getCurrentProcessApplication();
        if (processApplicationReference != null) {
            try {
                final ProcessApplicationInterface processApplication = processApplicationReference.getProcessApplication();
                return processApplication.getElResolver();
            }
            catch (ProcessApplicationUnavailableException e) {
                throw new ProcessEngineException("Cannot access process application '" + processApplicationReference.getName() + "'", e);
            }
        }
        return null;
    }
}
