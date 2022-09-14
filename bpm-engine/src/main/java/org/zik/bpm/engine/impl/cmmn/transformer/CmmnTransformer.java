// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.transformer;

import org.zik.bpm.engine.impl.core.transformer.Transform;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cmmn.handler.DefaultCmmnElementHandlerRegistry;
import java.util.List;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.core.transformer.Transformer;

public class CmmnTransformer implements Transformer<CmmnTransform>
{
    protected ExpressionManager expressionManager;
    protected CmmnTransformFactory factory;
    protected List<CmmnTransformListener> transformListeners;
    protected DefaultCmmnElementHandlerRegistry cmmnElementHandlerRegistry;
    
    public CmmnTransformer(final ExpressionManager expressionManager, final DefaultCmmnElementHandlerRegistry handlerRegistry, final CmmnTransformFactory factory) {
        this.transformListeners = new ArrayList<CmmnTransformListener>();
        this.expressionManager = expressionManager;
        this.factory = factory;
        this.cmmnElementHandlerRegistry = handlerRegistry;
    }
    
    @Override
    public CmmnTransform createTransform() {
        return this.factory.createTransform(this);
    }
    
    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }
    
    public void setExpressionManager(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }
    
    public CmmnTransformFactory getFactory() {
        return this.factory;
    }
    
    public void setFactory(final CmmnTransformFactory factory) {
        this.factory = factory;
    }
    
    public List<CmmnTransformListener> getTransformListeners() {
        return this.transformListeners;
    }
    
    public void setTransformListeners(final List<CmmnTransformListener> transformListeners) {
        this.transformListeners = transformListeners;
    }
    
    public DefaultCmmnElementHandlerRegistry getCmmnElementHandlerRegistry() {
        return this.cmmnElementHandlerRegistry;
    }
    
    public void setCmmnElementHandlerRegistry(final DefaultCmmnElementHandlerRegistry registry) {
        this.cmmnElementHandlerRegistry = registry;
    }
}
