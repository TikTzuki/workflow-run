// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.camunda.bpm.engine.variable.type.ValueType;
import java.util.Map;
import org.camunda.bpm.engine.variable.type.ValueTypeResolver;

public class ValueTypeResolverImpl implements ValueTypeResolver
{
    protected Map<String, ValueType> knownTypes;
    
    public ValueTypeResolverImpl() {
        this.knownTypes = new HashMap<String, ValueType>();
        this.addType((ValueType)ValueType.BOOLEAN);
        this.addType((ValueType)ValueType.BYTES);
        this.addType((ValueType)ValueType.DATE);
        this.addType((ValueType)ValueType.DOUBLE);
        this.addType((ValueType)ValueType.INTEGER);
        this.addType((ValueType)ValueType.LONG);
        this.addType((ValueType)ValueType.NULL);
        this.addType((ValueType)ValueType.SHORT);
        this.addType((ValueType)ValueType.STRING);
        this.addType((ValueType)ValueType.OBJECT);
        this.addType((ValueType)ValueType.NUMBER);
        this.addType((ValueType)ValueType.FILE);
    }
    
    public void addType(final ValueType type) {
        this.knownTypes.put(type.getName(), type);
    }
    
    public ValueType typeForName(final String typeName) {
        return this.knownTypes.get(typeName);
    }
    
    public Collection<ValueType> getSubTypes(final ValueType type) {
        final List<ValueType> types = new ArrayList<ValueType>();
        final Set<ValueType> validParents = new HashSet<ValueType>();
        validParents.add(type);
        for (final ValueType knownType : this.knownTypes.values()) {
            if (validParents.contains(knownType.getParent())) {
                validParents.add(knownType);
                if (knownType.isAbstract()) {
                    continue;
                }
                types.add(knownType);
            }
        }
        return types;
    }
}
