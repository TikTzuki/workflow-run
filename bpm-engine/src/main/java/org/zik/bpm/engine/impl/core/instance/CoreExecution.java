// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.instance;

import org.zik.bpm.engine.delegate.DelegateListener;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.core.CoreLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;

public abstract class CoreExecution extends AbstractVariableScope implements BaseDelegateExecution
{
    private static final long serialVersionUID = 1L;
    private static final CoreLogger LOG;
    protected String id;
    protected String businessKey;
    protected String businessKeyWithoutCascade;
    protected String tenantId;
    protected String eventName;
    protected CoreModelElement eventSource;
    protected int listenerIndex;
    protected boolean skipCustomListeners;
    protected boolean skipIoMapping;
    protected boolean skipSubprocesses;
    
    public CoreExecution() {
        this.listenerIndex = 0;
    }
    
    public <T extends CoreExecution> void performOperation(final CoreAtomicOperation<T> operation) {
        CoreExecution.LOG.debugPerformingAtomicOperation(operation, this);
        operation.execute((T)this);
    }
    
    public <T extends CoreExecution> void performOperationSync(final CoreAtomicOperation<T> operation) {
        CoreExecution.LOG.debugPerformingAtomicOperation(operation, this);
        operation.execute((T)this);
    }
    
    @Override
    public String getEventName() {
        return this.eventName;
    }
    
    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }
    
    public CoreModelElement getEventSource() {
        return this.eventSource;
    }
    
    public void setEventSource(final CoreModelElement eventSource) {
        this.eventSource = eventSource;
    }
    
    public int getListenerIndex() {
        return this.listenerIndex;
    }
    
    public void setListenerIndex(final int listenerIndex) {
        this.listenerIndex = listenerIndex;
    }
    
    public void invokeListener(final DelegateListener listener) throws Exception {
        listener.notify(this);
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getBusinessKeyWithoutCascade() {
        return this.businessKeyWithoutCascade;
    }
    
    public void setBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
        this.businessKeyWithoutCascade = businessKey;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public boolean isSkipCustomListeners() {
        return this.skipCustomListeners;
    }
    
    public void setSkipCustomListeners(final boolean skipCustomListeners) {
        this.skipCustomListeners = skipCustomListeners;
    }
    
    public boolean isSkipIoMappings() {
        return this.skipIoMapping;
    }
    
    public void setSkipIoMappings(final boolean skipIoMappings) {
        this.skipIoMapping = skipIoMappings;
    }
    
    public boolean isSkipSubprocesses() {
        return this.skipSubprocesses;
    }
    
    public void setSkipSubprocesseses(final boolean skipSubprocesses) {
        this.skipSubprocesses = skipSubprocesses;
    }
    
    static {
        LOG = CoreLogger.CORE_LOGGER;
    }
}
