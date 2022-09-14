// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.VariableUtil;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.core.variable.event.VariableEventDispatcher;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.javax.el.ELContext;

import java.io.Serializable;
import java.util.*;

public abstract class AbstractVariableScope<T extends CoreVariableInstance> implements Serializable, VariableScope, VariableEventDispatcher {
    private static final long serialVersionUID = 1L;
    protected ELContext cachedElContext;

    protected abstract VariableStore<T> getVariableStore();

    protected abstract VariableInstanceFactory<T> getVariableInstanceFactory();

    protected abstract List<VariableInstanceLifecycleListener<T>> getVariableInstanceLifecycleListeners();

    public abstract AbstractVariableScope getParentVariableScope();

    public void initializeVariableStore(final Map<String, Object> variables) {
        for (final String variableName : variables.keySet()) {
            final TypedValue value = Variables.untypedValue(variables.get(variableName));
            final T variableValue = this.getVariableInstanceFactory().build(variableName, value, false);
            this.getVariableStore().addVariable(variableValue);
        }
    }

    @Override
    public String getVariableScopeKey() {
        return "scope";
    }

    public VariableMapImpl getVariables() {
        return this.getVariablesTyped();
    }

    public VariableMapImpl getVariablesTyped() {
        return this.getVariablesTyped(true);
    }

    public VariableMapImpl getVariablesTyped(final boolean deserializeValues) {
        final VariableMapImpl variableMap = new VariableMapImpl();
        this.collectVariables(variableMap, null, false, deserializeValues);
        return variableMap;
    }

    public VariableMapImpl getVariablesLocal() {
        return this.getVariablesLocalTyped();
    }

    public VariableMapImpl getVariablesLocalTyped() {
        return this.getVariablesLocalTyped(true);
    }

    public VariableMapImpl getVariablesLocalTyped(final boolean deserializeObjectValues) {
        final VariableMapImpl variables = new VariableMapImpl();
        this.collectVariables(variables, null, true, deserializeObjectValues);
        return variables;
    }

    public void collectVariables(final VariableMapImpl resultVariables, final Collection<String> variableNames, final boolean isLocal, final boolean deserializeValues) {
        final boolean collectAll = variableNames == null;
        final List<T> localVariables = this.getVariableInstancesLocal(variableNames);
        for (final CoreVariableInstance var : localVariables) {
            if (!resultVariables.containsKey((Object) var.getName()) && (collectAll || variableNames.contains(var.getName()))) {
                resultVariables.put(var.getName(), (Object) var.getTypedValue(deserializeValues));
            }
        }
        if (!isLocal) {
            final AbstractVariableScope parentScope = this.getParentVariableScope();
            if (parentScope != null && (collectAll || !resultVariables.keySet().equals(variableNames))) {
                parentScope.collectVariables(resultVariables, variableNames, isLocal, deserializeValues);
            }
        }
    }

    @Override
    public Object getVariable(final String variableName) {
        return this.getVariable(variableName, true);
    }

    public Object getVariable(final String variableName, final boolean deserializeObjectValue) {
        return this.getValueFromVariableInstance(deserializeObjectValue, this.getVariableInstance(variableName));
    }

    @Override
    public Object getVariableLocal(final String variableName) {
        return this.getVariableLocal(variableName, true);
    }

    public Object getVariableLocal(final String variableName, final boolean deserializeObjectValue) {
        return this.getValueFromVariableInstance(deserializeObjectValue, this.getVariableInstanceLocal(variableName));
    }

    protected Object getValueFromVariableInstance(final boolean deserializeObjectValue, final CoreVariableInstance variableInstance) {
        if (variableInstance != null) {
            final TypedValue typedValue = variableInstance.getTypedValue(deserializeObjectValue);
            if (typedValue != null) {
                return typedValue.getValue();
            }
        }
        return null;
    }

    @Override
    public <T extends TypedValue> T getVariableTyped(final String variableName) {
        return this.getVariableTyped(variableName, true);
    }

    @Override
    public <T extends TypedValue> T getVariableTyped(final String variableName, final boolean deserializeValue) {
        return this.getTypedValueFromVariableInstance(deserializeValue, this.getVariableInstance(variableName));
    }

    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String variableName) {
        return this.getVariableLocalTyped(variableName, true);
    }

    @Override
    public <T extends TypedValue> T getVariableLocalTyped(final String variableName, final boolean deserializeValue) {
        return this.getTypedValueFromVariableInstance(deserializeValue, this.getVariableInstanceLocal(variableName));
    }

    private <T extends TypedValue> T getTypedValueFromVariableInstance(final boolean deserializeValue, final CoreVariableInstance variableInstance) {
        if (variableInstance != null) {
            return (T) variableInstance.getTypedValue(deserializeValue);
        }
        return null;
    }

    public CoreVariableInstance getVariableInstance(final String variableName) {
        final CoreVariableInstance variableInstance = this.getVariableInstanceLocal(variableName);
        if (variableInstance != null) {
            return variableInstance;
        }
        final AbstractVariableScope parentScope = this.getParentVariableScope();
        if (parentScope != null) {
            return parentScope.getVariableInstance(variableName);
        }
        return null;
    }

    public CoreVariableInstance getVariableInstanceLocal(final String name) {
        return this.getVariableStore().getVariable(name);
    }

    public List<T> getVariableInstancesLocal() {
        return this.getVariableStore().getVariables();
    }

    public List<T> getVariableInstancesLocal(final Collection<String> variableNames) {
        return this.getVariableStore().getVariables(variableNames);
    }

    @Override
    public boolean hasVariables() {
        if (!this.getVariableStore().isEmpty()) {
            return true;
        }
        final AbstractVariableScope parentScope = this.getParentVariableScope();
        return parentScope != null && parentScope.hasVariables();
    }

    @Override
    public boolean hasVariablesLocal() {
        return !this.getVariableStore().isEmpty();
    }

    @Override
    public boolean hasVariable(final String variableName) {
        if (this.hasVariableLocal(variableName)) {
            return true;
        }
        final AbstractVariableScope parentScope = this.getParentVariableScope();
        return parentScope != null && parentScope.hasVariable(variableName);
    }

    @Override
    public boolean hasVariableLocal(final String variableName) {
        return this.getVariableStore().containsKey(variableName);
    }

    protected Set<String> collectVariableNames(final Set<String> variableNames) {
        final AbstractVariableScope parentScope = this.getParentVariableScope();
        if (parentScope != null) {
            variableNames.addAll(parentScope.collectVariableNames(variableNames));
        }
        for (final CoreVariableInstance variableInstance : this.getVariableStore().getVariables()) {
            variableNames.add(variableInstance.getName());
        }
        return variableNames;
    }

    @Override
    public Set<String> getVariableNames() {
        return this.collectVariableNames(new HashSet<String>());
    }

    @Override
    public Set<String> getVariableNamesLocal() {
        return this.getVariableStore().getKeys();
    }

    public void setVariables(final Map<String, ?> variables, final boolean skipJavaSerializationFormatCheck) {
        VariableUtil.setVariables(variables, (name, value) -> this.setVariable(name, value, skipJavaSerializationFormatCheck));
    }

    @Override
    public void setVariables(final Map<String, ?> variables) {
        this.setVariables(variables, false);
    }

    public void setVariablesLocal(final Map<String, ?> variables, final boolean skipJavaSerializationFormatCheck) {
        VariableUtil.setVariables(variables, (name, value) -> this.setVariableLocal(name, value, skipJavaSerializationFormatCheck));
    }

    @Override
    public void setVariablesLocal(final Map<String, ?> variables) {
        this.setVariablesLocal(variables, false);
    }

    @Override
    public void removeVariables() {
        for (final T variableInstance : this.getVariableStore().getVariables()) {
            this.invokeVariableLifecycleListenersDelete(variableInstance, this.getSourceActivityVariableScope());
        }
        this.getVariableStore().removeVariables();
    }

    @Override
    public void removeVariablesLocal() {
        final List<String> variableNames = new ArrayList<String>(this.getVariableNamesLocal());
        for (final String variableName : variableNames) {
            this.removeVariableLocal(variableName);
        }
    }

    @Override
    public void removeVariables(final Collection<String> variableNames) {
        if (variableNames != null) {
            for (final String variableName : variableNames) {
                this.removeVariable(variableName);
            }
        }
    }

    @Override
    public void removeVariablesLocal(final Collection<String> variableNames) {
        if (variableNames != null) {
            for (final String variableName : variableNames) {
                this.removeVariableLocal(variableName);
            }
        }
    }

    public void setVariable(final String variableName, final Object value, final boolean skipJavaSerializationFormatCheck) {
        final TypedValue typedValue = Variables.untypedValue(value);
        this.setVariable(variableName, typedValue, this.getSourceActivityVariableScope(), skipJavaSerializationFormatCheck);
    }

    @Override
    public void setVariable(final String variableName, final Object value) {
        this.setVariable(variableName, value, false);
    }

    protected void setVariable(final String variableName, final TypedValue value, final AbstractVariableScope sourceActivityVariableScope, final boolean skipJavaSerializationFormatCheck) {
        if (this.hasVariableLocal(variableName)) {
            this.setVariableLocal(variableName, value, sourceActivityVariableScope, skipJavaSerializationFormatCheck);
            return;
        }
        final AbstractVariableScope parentVariableScope = this.getParentVariableScope();
        if (parentVariableScope != null) {
            if (sourceActivityVariableScope == null) {
                parentVariableScope.setVariable(variableName, value, skipJavaSerializationFormatCheck);
            } else {
                parentVariableScope.setVariable(variableName, value, sourceActivityVariableScope, skipJavaSerializationFormatCheck);
            }
            return;
        }
        this.setVariableLocal(variableName, value, sourceActivityVariableScope, skipJavaSerializationFormatCheck);
    }

    protected void setVariable(final String variableName, final TypedValue value, final AbstractVariableScope sourceActivityVariableScope) {
        this.setVariable(variableName, value, sourceActivityVariableScope, false);
    }

    public void setVariableLocal(final String variableName, final TypedValue value, final AbstractVariableScope sourceActivityExecution, final boolean skipJavaSerializationFormatCheck) {
        if (!skipJavaSerializationFormatCheck) {
            VariableUtil.checkJavaSerialization(variableName, value);
        }
        final VariableStore<T> variableStore = this.getVariableStore();
        if (variableStore.containsKey(variableName)) {
            final T existingInstance = variableStore.getVariable(variableName);
            final TypedValue previousValue = existingInstance.getTypedValue(false);
            if (value.isTransient() != previousValue.isTransient()) {
                throw ProcessEngineLogger.CORE_LOGGER.transientVariableException(variableName);
            }
            existingInstance.setValue(value);
            this.invokeVariableLifecycleListenersUpdate(existingInstance, sourceActivityExecution);
        } else if (!value.isTransient() && variableStore.isRemoved(variableName)) {
            final T existingInstance = variableStore.getRemovedVariable(variableName);
            existingInstance.setValue(value);
            this.getVariableStore().addVariable(existingInstance);
            this.invokeVariableLifecycleListenersUpdate(existingInstance, sourceActivityExecution);
            final DbEntityManager dbEntityManager = Context.getCommandContext().getDbEntityManager();
            dbEntityManager.undoDelete((DbEntity) existingInstance);
        } else {
            final T variableValue = this.getVariableInstanceFactory().build(variableName, value, value.isTransient());
            this.getVariableStore().addVariable(variableValue);
            this.invokeVariableLifecycleListenersCreate(variableValue, sourceActivityExecution);
        }
    }

    protected void invokeVariableLifecycleListenersCreate(final T variableInstance, final AbstractVariableScope sourceScope) {
        this.invokeVariableLifecycleListenersCreate(variableInstance, sourceScope, this.getVariableInstanceLifecycleListeners());
    }

    protected void invokeVariableLifecycleListenersCreate(final T variableInstance, final AbstractVariableScope sourceScope, final List<VariableInstanceLifecycleListener<T>> lifecycleListeners) {
        for (final VariableInstanceLifecycleListener<T> lifecycleListener : lifecycleListeners) {
            lifecycleListener.onCreate(variableInstance, sourceScope);
        }
    }

    protected void invokeVariableLifecycleListenersDelete(final T variableInstance, final AbstractVariableScope sourceScope) {
        this.invokeVariableLifecycleListenersDelete(variableInstance, sourceScope, this.getVariableInstanceLifecycleListeners());
    }

    protected void invokeVariableLifecycleListenersDelete(final T variableInstance, final AbstractVariableScope sourceScope, final List<VariableInstanceLifecycleListener<T>> lifecycleListeners) {
        for (final VariableInstanceLifecycleListener<T> lifecycleListener : lifecycleListeners) {
            lifecycleListener.onDelete(variableInstance, sourceScope);
        }
    }

    protected void invokeVariableLifecycleListenersUpdate(final T variableInstance, final AbstractVariableScope sourceScope) {
        this.invokeVariableLifecycleListenersUpdate(variableInstance, sourceScope, this.getVariableInstanceLifecycleListeners());
    }

    protected void invokeVariableLifecycleListenersUpdate(final T variableInstance, final AbstractVariableScope sourceScope, final List<VariableInstanceLifecycleListener<T>> lifecycleListeners) {
        for (final VariableInstanceLifecycleListener<T> lifecycleListener : lifecycleListeners) {
            lifecycleListener.onUpdate(variableInstance, sourceScope);
        }
    }

    public void setVariableLocal(final String variableName, final Object value, final boolean skipJavaSerializationFormatCheck) {
        final TypedValue typedValue = Variables.untypedValue(value);
        this.setVariableLocal(variableName, typedValue, this.getSourceActivityVariableScope(), skipJavaSerializationFormatCheck);
    }

    @Override
    public void setVariableLocal(final String variableName, final Object value) {
        this.setVariableLocal(variableName, value, false);
    }

    @Override
    public void removeVariable(final String variableName) {
        this.removeVariable(variableName, this.getSourceActivityVariableScope());
    }

    protected void removeVariable(final String variableName, final AbstractVariableScope sourceActivityExecution) {
        if (this.getVariableStore().containsKey(variableName)) {
            this.removeVariableLocal(variableName, sourceActivityExecution);
            return;
        }
        final AbstractVariableScope parentVariableScope = this.getParentVariableScope();
        if (parentVariableScope != null) {
            if (sourceActivityExecution == null) {
                parentVariableScope.removeVariable(variableName);
            } else {
                parentVariableScope.removeVariable(variableName, sourceActivityExecution);
            }
        }
    }

    @Override
    public void removeVariableLocal(final String variableName) {
        this.removeVariableLocal(variableName, this.getSourceActivityVariableScope());
    }

    protected AbstractVariableScope getSourceActivityVariableScope() {
        return this;
    }

    protected void removeVariableLocal(final String variableName, final AbstractVariableScope sourceActivityExecution) {
        if (this.getVariableStore().containsKey(variableName)) {
            final T variableInstance = this.getVariableStore().getVariable(variableName);
            this.invokeVariableLifecycleListenersDelete(variableInstance, sourceActivityExecution);
            this.getVariableStore().removeVariable(variableName);
        }
    }

    public ELContext getCachedElContext() {
        return this.cachedElContext;
    }

    public void setCachedElContext(final ELContext cachedElContext) {
        this.cachedElContext = cachedElContext;
    }

    @Override
    public void dispatchEvent(final VariableEvent variableEvent) {
    }
}
