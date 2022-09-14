// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.scope;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;

public class SimpleVariableInstance implements CoreVariableInstance
{
    protected String name;
    protected TypedValue value;
    
    public SimpleVariableInstance(final String name, final TypedValue value) {
        this.name = name;
        this.value = value;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public TypedValue getTypedValue(final boolean deserialize) {
        return this.value;
    }
    
    @Override
    public void setValue(final TypedValue value) {
        this.value = value;
    }
    
    public static class SimpleVariableInstanceFactory implements VariableInstanceFactory<SimpleVariableInstance>
    {
        public static final SimpleVariableInstanceFactory INSTANCE;
        
        @Override
        public SimpleVariableInstance build(final String name, final TypedValue value, final boolean isTransient) {
            return new SimpleVariableInstance(name, value);
        }
        
        static {
            INSTANCE = new SimpleVariableInstanceFactory();
        }
    }
}
