// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;

public class VariableStore<T extends CoreVariableInstance>
{
    protected VariablesProvider<T> variablesProvider;
    protected Map<String, T> variables;
    protected Map<String, T> removedVariables;
    protected List<VariableStoreObserver<T>> observers;

    public VariableStore() {
        this(VariableCollectionProvider.emptyVariables(), new VariableStoreObserver[0]);
    }

    public VariableStore(final VariablesProvider<T> provider, final VariableStoreObserver<T>... observers) {
        this.removedVariables = new HashMap<String, T>();
        this.variablesProvider = provider;
        (this.observers = new ArrayList<VariableStoreObserver<T>>()).addAll(Arrays.asList(observers));
    }

    public void setVariablesProvider(final VariablesProvider<T> variablesProvider) {
        if (this.variables != null) {
            return;
        }
        this.variablesProvider = variablesProvider;
    }

    protected Map<String, T> getVariablesMap() {
        this.forceInitialization();
        return this.variables;
    }

    protected Map<String, T> getVariablesMap(final Collection<String> variableNames) {
        if (variableNames == null) {
            return this.getVariablesMap();
        }
        final Map<String, T> result = new HashMap<String, T>();
        if (this.isInitialized()) {
            for (final String variableName : variableNames) {
                if (this.variables.containsKey(variableName)) {
                    result.put(variableName, this.variables.get(variableName));
                }
            }
        }
        else {
            for (final T variable : this.variablesProvider.provideVariables(variableNames)) {
                result.put(variable.getName(), variable);
            }
        }
        return result;
    }

    public T getRemovedVariable(final String name) {
        return this.removedVariables.get(name);
    }

    public T getVariable(final String name) {
        return this.getVariablesMap().get(name);
    }

    public List<T> getVariables() {
        return new ArrayList<T>((Collection<? extends T>)this.getVariablesMap().values());
    }

    public List<T> getVariables(final Collection<String> variableNames) {
        return new ArrayList<T>((Collection<? extends T>)this.getVariablesMap(variableNames).values());
    }

    public void addVariable(final T value) {
        if (this.containsKey(value.getName())) {
            throw ProcessEngineLogger.CORE_LOGGER.duplicateVariableInstanceException(value);
        }
        this.getVariablesMap().put(value.getName(), value);
        for (final VariableStoreObserver<T> listener : this.observers) {
            listener.onAdd(value);
        }
        if (this.removedVariables.containsKey(value.getName())) {
            this.removedVariables.remove(value.getName());
        }
    }

    public void updateVariable(final T value) {
        if (!this.containsKey(value.getName())) {
            throw ProcessEngineLogger.CORE_LOGGER.duplicateVariableInstanceException(value);
        }
    }

    public boolean isEmpty() {
        return this.getVariablesMap().isEmpty();
    }

    public boolean containsValue(final T value) {
        return this.getVariablesMap().containsValue(value);
    }

    public boolean containsKey(final String key) {
        return this.getVariablesMap().containsKey(key);
    }

    public Set<String> getKeys() {
        return new HashSet<String>(this.getVariablesMap().keySet());
    }

    public boolean isInitialized() {
        return this.variables != null;
    }

    public void forceInitialization() {
        if (!this.isInitialized()) {
            this.variables = new HashMap<String, T>();
            for (final T variable : this.variablesProvider.provideVariables()) {
                this.variables.put(variable.getName(), variable);
            }
        }
    }

    public T removeVariable(final String variableName) {
        if (!this.getVariablesMap().containsKey(variableName)) {
            return null;
        }
        final T value = this.getVariablesMap().remove(variableName);
        for (final VariableStoreObserver<T> observer : this.observers) {
            observer.onRemove(value);
        }
        this.removedVariables.put(variableName, value);
        return value;
    }

    public void removeVariables() {
        final Iterator<T> valuesIt = this.getVariablesMap().values().iterator();
        this.removedVariables.putAll((Map<? extends String, ? extends T>)this.variables);
        while (valuesIt.hasNext()) {
            final T nextVariable = valuesIt.next();
            valuesIt.remove();
            for (final VariableStoreObserver<T> observer : this.observers) {
                observer.onRemove(nextVariable);
            }
        }
    }

    public void addObserver(final VariableStoreObserver<T> observer) {
        this.observers.add(observer);
    }

    public void removeObserver(final VariableStoreObserver<T> observer) {
        this.observers.remove(observer);
    }

    public boolean isRemoved(final String variableName) {
        return this.removedVariables.containsKey(variableName);
    }

    public interface VariablesProvider<T extends CoreVariableInstance>
    {
        Collection<T> provideVariables();

        Collection<T> provideVariables(final Collection<String> p0);
    }

    public interface VariableStoreObserver<T extends CoreVariableInstance>
    {
        void onAdd(final T p0);

        void onRemove(final T p0);
    }
}
