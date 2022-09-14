// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import org.zik.bpm.engine.impl.interceptor.Session;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.interceptor.SessionFactory;

public class GenericManagerFactory implements SessionFactory
{
    protected static final EnginePersistenceLogger LOG;
    protected Class<? extends Session> managerImplementation;
    
    public GenericManagerFactory(final Class<? extends Session> managerImplementation) {
        this.managerImplementation = managerImplementation;
    }
    
    public GenericManagerFactory(final String classname) {
        this.managerImplementation = (Class<? extends Session>)ReflectUtil.loadClass(classname);
    }
    
    @Override
    public Class<?> getSessionType() {
        return this.managerImplementation;
    }
    
    @Override
    public Session openSession() {
        try {
            return (Session)this.managerImplementation.newInstance();
        }
        catch (Exception e) {
            throw GenericManagerFactory.LOG.instantiateSessionException(this.managerImplementation.getName(), e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
