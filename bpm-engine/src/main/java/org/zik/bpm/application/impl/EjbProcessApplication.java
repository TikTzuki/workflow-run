// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import javax.naming.NamingException;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import org.zik.bpm.application.ProcessApplicationExecutionException;
import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import java.util.concurrent.Callable;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.application.ProcessApplicationInterface;
import org.zik.bpm.application.AbstractProcessApplication;

public class EjbProcessApplication extends AbstractProcessApplication
{
    private static ProcessApplicationLogger LOG;
    protected static String MODULE_NAME_PATH;
    protected static String JAVA_APP_APP_NAME_PATH;
    protected static String EJB_CONTEXT_PATH;
    private EjbProcessApplicationReference ejbProcessApplicationReference;
    private ProcessApplicationInterface selfReference;
    
    @Override
    public ProcessApplicationReference getReference() {
        this.ensureInitialized();
        return this.ejbProcessApplicationReference;
    }
    
    @Override
    protected String autodetectProcessApplicationName() {
        return this.lookupEeApplicationName();
    }
    
    protected Class<? extends ProcessApplicationInterface> getBusinessInterface() {
        return ProcessApplicationInterface.class;
    }
    
    @Override
    public <T> T execute(final Callable<T> callable) throws ProcessApplicationExecutionException {
        final ClassLoader originalClassloader = ClassLoaderUtil.getContextClassloader();
        final ClassLoader processApplicationClassloader = this.getProcessApplicationClassloader();
        try {
            if (originalClassloader != processApplicationClassloader) {
                ClassLoaderUtil.setContextClassloader(processApplicationClassloader);
            }
            return callable.call();
        }
        catch (Exception e) {
            throw EjbProcessApplication.LOG.processApplicationExecutionException(e);
        }
        finally {
            ClassLoaderUtil.setContextClassloader(originalClassloader);
        }
    }
    
    protected void ensureInitialized() {
        if (this.selfReference == null) {
            this.selfReference = this.lookupSelfReference();
        }
        if (this.ejbProcessApplicationReference == null) {
            this.ejbProcessApplicationReference = new EjbProcessApplicationReference(this.selfReference, this.getName());
        }
    }
    
    protected ProcessApplicationInterface lookupSelfReference() {
        try {
            final InitialContext ic = new InitialContext();
            final SessionContext sctxLookup = (SessionContext)ic.lookup(EjbProcessApplication.EJB_CONTEXT_PATH);
            return (ProcessApplicationInterface)sctxLookup.getBusinessObject((Class)this.getBusinessInterface());
        }
        catch (NamingException e) {
            throw EjbProcessApplication.LOG.ejbPaCannotLookupSelfReference(e);
        }
    }
    
    protected String lookupEeApplicationName() {
        try {
            final InitialContext initialContext = new InitialContext();
            final String appName = (String)initialContext.lookup(EjbProcessApplication.JAVA_APP_APP_NAME_PATH);
            final String moduleName = (String)initialContext.lookup(EjbProcessApplication.MODULE_NAME_PATH);
            if (moduleName != null && !moduleName.equals(appName)) {
                return appName + "/" + moduleName;
            }
            return appName;
        }
        catch (NamingException e) {
            throw EjbProcessApplication.LOG.ejbPaCannotAutodetectName(e);
        }
    }
    
    static {
        EjbProcessApplication.LOG = ProcessEngineLogger.PROCESS_APPLICATION_LOGGER;
        EjbProcessApplication.MODULE_NAME_PATH = "java:module/ModuleName";
        EjbProcessApplication.JAVA_APP_APP_NAME_PATH = "java:app/AppName";
        EjbProcessApplication.EJB_CONTEXT_PATH = "java:comp/EJBContext";
    }
}
