// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import org.zik.bpm.engine.ProcessEngineException;

public class JPAEntityScanner
{
    public EntityMetaData scanClass(final Class<?> clazz) {
        final EntityMetaData metaData = new EntityMetaData();
        metaData.setEntityClass(clazz);
        final boolean isEntity = this.isEntityAnnotationPresent(clazz);
        metaData.setJPAEntity(isEntity);
        if (isEntity) {
            final Field idField = this.getIdField(clazz);
            if (idField != null) {
                metaData.setIdField(idField);
            }
            else {
                final Method idMethod = this.getIdMethod(clazz);
                if (idMethod == null) {
                    throw new ProcessEngineException("Cannot find field or method with annotation @Id on class '" + clazz.getName() + "', only single-valued primary keys are supported on JPA-enities");
                }
                metaData.setIdMethod(idMethod);
            }
        }
        return metaData;
    }
    
    private Method getIdMethod(final Class<?> clazz) {
        Method idMethod = null;
        final Method[] methods = clazz.getMethods();
        Id idAnnotation = null;
        for (final Method method : methods) {
            idAnnotation = method.getAnnotation(Id.class);
            if (idAnnotation != null) {
                idMethod = method;
                break;
            }
        }
        return idMethod;
    }
    
    private Field getIdField(final Class<?> clazz) {
        Field idField = null;
        final Field[] fields = clazz.getDeclaredFields();
        Id idAnnotation = null;
        for (final Field field : fields) {
            idAnnotation = field.getAnnotation(Id.class);
            if (idAnnotation != null) {
                idField = field;
                break;
            }
        }
        if (idField == null) {
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !superClass.equals(Object.class)) {
                idField = this.getIdField(superClass);
            }
        }
        return idField;
    }
    
    private boolean isEntityAnnotationPresent(final Class<?> clazz) {
        return clazz.getAnnotation(Entity.class) != null;
    }
}
