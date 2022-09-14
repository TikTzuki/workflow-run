// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.ProcessEngineInfo;
import java.io.Serializable;

public class ProcessEngineInfoImpl implements Serializable, ProcessEngineInfo
{
    private static final long serialVersionUID = 1L;
    String name;
    String resourceUrl;
    String exception;
    
    public ProcessEngineInfoImpl(final String name, final String resourceUrl, final String exception) {
        this.name = name;
        this.resourceUrl = resourceUrl;
        this.exception = exception;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getResourceUrl() {
        return this.resourceUrl;
    }
    
    @Override
    public String getException() {
        return this.exception;
    }
}
