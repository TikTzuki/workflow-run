// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.scripting.engine;

import java.util.HashSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Collection;
import java.util.Map;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.List;
import java.util.Set;
import javax.script.Bindings;

public class ScriptBindings implements Bindings
{
    protected static final Set<String> UNSTORED_KEYS;
    protected List<Resolver> scriptResolvers;
    protected VariableScope variableScope;
    protected Bindings wrappedBindings;
    protected boolean autoStoreScriptVariables;
    
    public ScriptBindings(final List<Resolver> scriptResolvers, final VariableScope variableScope, final Bindings wrappedBindings) {
        this.scriptResolvers = scriptResolvers;
        this.variableScope = variableScope;
        this.wrappedBindings = wrappedBindings;
        this.autoStoreScriptVariables = this.isAutoStoreScriptVariablesEnabled();
    }
    
    protected boolean isAutoStoreScriptVariablesEnabled() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        return processEngineConfiguration != null && processEngineConfiguration.isAutoStoreScriptVariables();
    }
    
    @Override
    public boolean containsKey(final Object key) {
        for (final Resolver scriptResolver : this.scriptResolvers) {
            if (scriptResolver.containsKey(key)) {
                return true;
            }
        }
        return this.wrappedBindings.containsKey(key);
    }
    
    @Override
    public Object get(final Object key) {
        Object result = null;
        if (this.wrappedBindings.containsKey(key)) {
            result = this.wrappedBindings.get(key);
        }
        else {
            for (final Resolver scriptResolver : this.scriptResolvers) {
                if (scriptResolver.containsKey(key)) {
                    result = scriptResolver.get(key);
                }
            }
        }
        return result;
    }
    
    @Override
    public Object put(final String name, final Object value) {
        if (this.autoStoreScriptVariables && !ScriptBindings.UNSTORED_KEYS.contains(name)) {
            final Object oldValue = this.variableScope.getVariable(name);
            this.variableScope.setVariable(name, value);
            return oldValue;
        }
        return this.wrappedBindings.put(name, value);
    }
    
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return this.calculateBindingMap().entrySet();
    }
    
    @Override
    public Set<String> keySet() {
        return this.calculateBindingMap().keySet();
    }
    
    @Override
    public int size() {
        return this.calculateBindingMap().size();
    }
    
    @Override
    public Collection<Object> values() {
        return this.calculateBindingMap().values();
    }
    
    @Override
    public void putAll(final Map<? extends String, ?> toMerge) {
        for (final Entry<? extends String, ?> entry : toMerge.entrySet()) {
            this.put((String)entry.getKey(), (Object)entry.getValue());
        }
    }
    
    @Override
    public Object remove(final Object key) {
        if (ScriptBindings.UNSTORED_KEYS.contains(key)) {
            return null;
        }
        return this.wrappedBindings.remove(key);
    }
    
    @Override
    public void clear() {
        this.wrappedBindings.clear();
    }
    
    @Override
    public boolean containsValue(final Object value) {
        return this.calculateBindingMap().containsValue(value);
    }
    
    @Override
    public boolean isEmpty() {
        return this.calculateBindingMap().isEmpty();
    }
    
    protected Map<String, Object> calculateBindingMap() {
        final Map<String, Object> bindingMap = new HashMap<String, Object>();
        for (final Resolver resolver : this.scriptResolvers) {
            for (final String key : resolver.keySet()) {
                bindingMap.put(key, resolver.get(key));
            }
        }
        final Set<Entry<String, Object>> wrappedBindingsEntries = this.wrappedBindings.entrySet();
        for (final Entry<String, Object> entry : wrappedBindingsEntries) {
            bindingMap.put(entry.getKey(), entry.getValue());
        }
        return bindingMap;
    }
    
    static {
        UNSTORED_KEYS = new HashSet<String>(Arrays.asList("out", "out:print", "lang:import", "context", "elcontext", "print", "println", "S", "XML", "JSON", "javax.script.argv", "execution", "__doc__"));
    }
}
