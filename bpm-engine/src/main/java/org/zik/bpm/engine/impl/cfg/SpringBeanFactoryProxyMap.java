// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Collections;
import java.util.Set;
import org.springframework.beans.factory.BeanFactory;
import java.util.Map;

public class SpringBeanFactoryProxyMap implements Map<Object, Object>
{
    protected BeanFactory beanFactory;
    
    public SpringBeanFactoryProxyMap(final BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    @Override
    public Object get(final Object key) {
        if (key == null || !String.class.isAssignableFrom(key.getClass())) {
            return null;
        }
        return this.beanFactory.getBean((String)key);
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return key != null && String.class.isAssignableFrom(key.getClass()) && this.beanFactory.containsBean((String)key);
    }
    
    @Override
    public Set<Object> keySet() {
        return Collections.emptySet();
    }
    
    @Override
    public void clear() {
        throw new ProcessEngineException("can't clear configuration beans");
    }
    
    @Override
    public boolean containsValue(final Object value) {
        throw new ProcessEngineException("can't search values in configuration beans");
    }
    
    @Override
    public Set<Entry<Object, Object>> entrySet() {
        throw new ProcessEngineException("unsupported operation on configuration beans");
    }
    
    @Override
    public boolean isEmpty() {
        throw new ProcessEngineException("unsupported operation on configuration beans");
    }
    
    @Override
    public Object put(final Object key, final Object value) {
        throw new ProcessEngineException("unsupported operation on configuration beans");
    }
    
    @Override
    public void putAll(final Map<?, ?> m) {
        throw new ProcessEngineException("unsupported operation on configuration beans");
    }
    
    @Override
    public Object remove(final Object key) {
        throw new ProcessEngineException("unsupported operation on configuration beans");
    }
    
    @Override
    public int size() {
        throw new ProcessEngineException("unsupported operation on configuration beans");
    }
    
    @Override
    public Collection<Object> values() {
        throw new ProcessEngineException("unsupported operation on configuration beans");
    }
}
