// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.application.ProcessApplicationExecutionException;
import java.util.concurrent.Callable;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.application.AbstractProcessApplication;

public class EmbeddedProcessApplication extends AbstractProcessApplication
{
    public static final String DEFAULT_NAME = "Process Application";
    private static ProcessApplicationLogger LOG;
    
    @Override
    protected String autodetectProcessApplicationName() {
        return "Process Application";
    }
    
    @Override
    public ProcessApplicationReference getReference() {
        return new EmbeddedProcessApplicationReferenceImpl(this);
    }
    
    @Override
    public <T> T execute(final Callable<T> callable) throws ProcessApplicationExecutionException {
        try {
            return callable.call();
        }
        catch (Exception e) {
            throw EmbeddedProcessApplication.LOG.processApplicationExecutionException(e);
        }
    }
    
    static {
        EmbeddedProcessApplication.LOG = ProcessEngineLogger.PROCESS_APPLICATION_LOGGER;
    }
}
