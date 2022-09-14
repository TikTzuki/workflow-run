// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.model;

import org.zik.bpm.engine.ProcessEngineException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Properties
{
    protected final Map<String, Object> properties;
    
    public Properties() {
        this(new HashMap<String, Object>());
    }
    
    public Properties(final Map<String, Object> properties) {
        this.properties = properties;
    }
    
    public <T> T get(final PropertyKey<T> property) {
        return (T)this.properties.get(property.getName());
    }
    
    public <T> List<T> get(final PropertyListKey<T> property) {
        if (this.contains(property)) {
            return (List<T>) this.properties.get(property.getName());
        }
        return new ArrayList<T>();
    }
    
    public <K, V> Map<K, V> get(final PropertyMapKey<K, V> property) {
        if (this.contains(property)) {
            return (Map<K, V>) this.properties.get(property.getName());
        }
        return new HashMap<K, V>();
    }
    
    public <T> void set(final PropertyKey<T> property, final T value) {
        this.properties.put(property.getName(), value);
    }
    
    public <T> void set(final PropertyListKey<T> property, final List<T> value) {
        this.properties.put(property.getName(), value);
    }
    
    public <K, V> void set(final PropertyMapKey<K, V> property, final Map<K, V> value) {
        this.properties.put(property.getName(), value);
    }
    
    public <T> void addListItem(final PropertyListKey<T> property, final T value) {
        final List<T> list = this.get(property);
        list.add(value);
        if (!this.contains(property)) {
            this.set(property, list);
        }
    }
    
    public <K, V> void putMapEntry(final PropertyMapKey<K, V> property, final K key, final V value) {
        final Map<K, V> map = this.get(property);
        if (!property.allowsOverwrite() && map.containsKey(key)) {
            throw new ProcessEngineException("Cannot overwrite property key " + key + ". Key already exists");
        }
        map.put(key, value);
        if (!this.contains(property)) {
            this.set(property, map);
        }
    }
    
    public boolean contains(final PropertyKey<?> property) {
        return this.properties.containsKey(property.getName());
    }
    
    public boolean contains(final PropertyListKey<?> property) {
        return this.properties.containsKey(property.getName());
    }
    
    public boolean contains(final PropertyMapKey<?, ?> property) {
        return this.properties.containsKey(property.getName());
    }
    
    public Map<String, Object> toMap() {
        return new HashMap<String, Object>(this.properties);
    }
    
    @Override
    public String toString() {
        return "Properties [properties=" + this.properties + "]";
    }
}
