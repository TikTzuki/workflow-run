package org.camunda.bpm.run.stuff;

public class TaskEntity {
    protected transient VariableStore<VariableInstanceEntity> variableStore;
    protected VariableStore<? extends CoreVariableInstance> getVariableStore() {
        return this.variableStore;
    }
}
