// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.zik.bpm.engine.delegate.VariableListener;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public abstract class CoreModelElement implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String name;
    protected Properties properties;
    protected Map<String, List<DelegateListener<? extends BaseDelegateExecution>>> builtInListeners;
    protected Map<String, List<DelegateListener<? extends BaseDelegateExecution>>> listeners;
    protected Map<String, List<VariableListener<?>>> builtInVariableListeners;
    protected Map<String, List<VariableListener<?>>> variableListeners;
    
    public CoreModelElement(final String id) {
        this.properties = new Properties();
        this.builtInListeners = new HashMap<String, List<DelegateListener<? extends BaseDelegateExecution>>>();
        this.listeners = new HashMap<String, List<DelegateListener<? extends BaseDelegateExecution>>>();
        this.builtInVariableListeners = new HashMap<String, List<VariableListener<?>>>();
        this.variableListeners = new HashMap<String, List<VariableListener<?>>>();
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setProperty(final String name, final Object value) {
        this.properties.set(new PropertyKey<Object>(name), value);
    }
    
    public Object getProperty(final String name) {
        return this.properties.get(new PropertyKey<Object>(name));
    }
    
    public Properties getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Properties properties) {
        this.properties = properties;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public List<DelegateListener<? extends BaseDelegateExecution>> getListeners(final String eventName) {
        final List<DelegateListener<? extends BaseDelegateExecution>> listenerList = this.getListeners().get(eventName);
        if (listenerList != null) {
            return listenerList;
        }
        return Collections.emptyList();
    }
    
    public List<DelegateListener<? extends BaseDelegateExecution>> getBuiltInListeners(final String eventName) {
        final List<DelegateListener<? extends BaseDelegateExecution>> listenerList = this.getBuiltInListeners().get(eventName);
        if (listenerList != null) {
            return listenerList;
        }
        return Collections.emptyList();
    }
    
    public List<VariableListener<?>> getVariableListenersLocal(final String eventName) {
        final List<VariableListener<?>> listenerList = this.getVariableListeners().get(eventName);
        if (listenerList != null) {
            return listenerList;
        }
        return Collections.emptyList();
    }
    
    public List<VariableListener<?>> getBuiltInVariableListenersLocal(final String eventName) {
        final List<VariableListener<?>> listenerList = this.getBuiltInVariableListeners().get(eventName);
        if (listenerList != null) {
            return listenerList;
        }
        return Collections.emptyList();
    }
    
    public void addListener(final String eventName, final DelegateListener<? extends BaseDelegateExecution> listener) {
        this.addListener(eventName, listener, -1);
    }
    
    public void addBuiltInListener(final String eventName, final DelegateListener<? extends BaseDelegateExecution> listener) {
        this.addBuiltInListener(eventName, listener, -1);
    }
    
    public void addBuiltInListener(final String eventName, final DelegateListener<? extends BaseDelegateExecution> listener, final int index) {
        this.addListenerToMap(this.listeners, eventName, listener, index);
        this.addListenerToMap(this.builtInListeners, eventName, listener, index);
    }
    
    public void addListener(final String eventName, final DelegateListener<? extends BaseDelegateExecution> listener, final int index) {
        this.addListenerToMap(this.listeners, eventName, listener, index);
    }
    
    protected <T> void addListenerToMap(final Map<String, List<T>> listenerMap, final String eventName, final T listener, final int index) {
        List<T> listeners = listenerMap.get(eventName);
        if (listeners == null) {
            listeners = new ArrayList<T>();
            listenerMap.put(eventName, listeners);
        }
        if (index < 0) {
            listeners.add(listener);
        }
        else {
            listeners.add(index, listener);
        }
    }
    
    public void addVariableListener(final String eventName, final VariableListener<?> listener) {
        this.addVariableListener(eventName, listener, -1);
    }
    
    public void addVariableListener(final String eventName, final VariableListener<?> listener, final int index) {
        this.addListenerToMap(this.variableListeners, eventName, listener, index);
    }
    
    public void addBuiltInVariableListener(final String eventName, final VariableListener<?> listener) {
        this.addBuiltInVariableListener(eventName, listener, -1);
    }
    
    public void addBuiltInVariableListener(final String eventName, final VariableListener<?> listener, final int index) {
        this.addListenerToMap(this.variableListeners, eventName, listener, index);
        this.addListenerToMap(this.builtInVariableListeners, eventName, listener, index);
    }
    
    public Map<String, List<DelegateListener<? extends BaseDelegateExecution>>> getListeners() {
        return this.listeners;
    }
    
    public Map<String, List<DelegateListener<? extends BaseDelegateExecution>>> getBuiltInListeners() {
        return this.builtInListeners;
    }
    
    public Map<String, List<VariableListener<?>>> getBuiltInVariableListeners() {
        return this.builtInVariableListeners;
    }
    
    public Map<String, List<VariableListener<?>>> getVariableListeners() {
        return this.variableListeners;
    }
}
