// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class EntityMetaData
{
    private boolean isJPAEntity;
    private Class<?> entityClass;
    private Method idMethod;
    private Field idField;
    
    public EntityMetaData() {
        this.isJPAEntity = false;
    }
    
    public boolean isJPAEntity() {
        return this.isJPAEntity;
    }
    
    public void setJPAEntity(final boolean isJPAEntity) {
        this.isJPAEntity = isJPAEntity;
    }
    
    public Class<?> getEntityClass() {
        return this.entityClass;
    }
    
    public void setEntityClass(final Class<?> entityClass) {
        this.entityClass = entityClass;
    }
    
    public Method getIdMethod() {
        return this.idMethod;
    }
    
    public void setIdMethod(final Method idMethod) {
        (this.idMethod = idMethod).setAccessible(true);
    }
    
    public Field getIdField() {
        return this.idField;
    }
    
    public void setIdField(final Field idField) {
        (this.idField = idField).setAccessible(true);
    }
    
    public Class<?> getIdType() {
        Class<?> idType = null;
        if (this.idField != null) {
            idType = this.idField.getType();
        }
        else if (this.idMethod != null) {
            idType = this.idMethod.getReturnType();
        }
        return idType;
    }
}
