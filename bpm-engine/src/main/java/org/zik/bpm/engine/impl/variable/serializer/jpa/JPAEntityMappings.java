// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer.jpa;

import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.EntityManager;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.lang.reflect.InvocationTargetException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.HashMap;
import java.util.Map;

public class JPAEntityMappings
{
    private Map<String, EntityMetaData> classMetaDatamap;
    private JPAEntityScanner enitityScanner;
    
    public JPAEntityMappings() {
        this.classMetaDatamap = new HashMap<String, EntityMetaData>();
        this.enitityScanner = new JPAEntityScanner();
    }
    
    public boolean isJPAEntity(final Object value) {
        return value != null && this.getEntityMetaData(value.getClass()).isJPAEntity();
    }
    
    private EntityMetaData getEntityMetaData(final Class<?> clazz) {
        EntityMetaData metaData = this.classMetaDatamap.get(clazz.getName());
        if (metaData == null) {
            metaData = this.scanClass(clazz);
            this.classMetaDatamap.put(clazz.getName(), metaData);
        }
        return metaData;
    }
    
    private EntityMetaData scanClass(final Class<?> clazz) {
        return this.enitityScanner.scanClass(clazz);
    }
    
    public String getJPAClassString(final Object value) {
        EnsureUtil.ensureNotNull("null value cannot be saved", "value", value);
        final EntityMetaData metaData = this.getEntityMetaData(value.getClass());
        if (!metaData.isJPAEntity()) {
            throw new ProcessEngineException("Object is not a JPA Entity: class='" + value.getClass() + "', " + value);
        }
        return metaData.getEntityClass().getName();
    }
    
    public String getJPAIdString(final Object value) {
        final EntityMetaData metaData = this.getEntityMetaData(value.getClass());
        if (!metaData.isJPAEntity()) {
            throw new ProcessEngineException("Object is not a JPA Entity: class='" + value.getClass() + "', " + value);
        }
        final Object idValue = this.getIdValue(value, metaData);
        return this.getIdString(idValue);
    }
    
    private Object getIdValue(final Object value, final EntityMetaData metaData) {
        try {
            if (metaData.getIdMethod() != null) {
                return metaData.getIdMethod().invoke(value, new Object[0]);
            }
            if (metaData.getIdField() != null) {
                return metaData.getIdField().get(value);
            }
        }
        catch (IllegalArgumentException iae) {
            throw new ProcessEngineException("Illegal argument exception when getting value from id method/field on JPAEntity", iae);
        }
        catch (IllegalAccessException iae2) {
            throw new ProcessEngineException("Cannot access id method/field for JPA Entity", iae2);
        }
        catch (InvocationTargetException ite) {
            throw new ProcessEngineException("Exception occured while getting value from id field/method on JPAEntity: " + ite.getCause().getMessage(), ite.getCause());
        }
        throw new ProcessEngineException("Cannot get id from JPA Entity, no id method/field set");
    }
    
    public Object getJPAEntity(final String className, final String idString) {
        Class<?> entityClass = null;
        entityClass = ReflectUtil.loadClass(className);
        final EntityMetaData metaData = this.getEntityMetaData(entityClass);
        EnsureUtil.ensureNotNull("Class is not a JPA-entity: " + className, "metaData", metaData);
        final Object primaryKey = this.createId(metaData, idString);
        return this.findEntity(entityClass, primaryKey);
    }
    
    private Object findEntity(final Class<?> entityClass, final Object primaryKey) {
        final EntityManager em = Context.getCommandContext().getSession(EntityManagerSession.class).getEntityManager();
        final Object entity = em.find((Class)entityClass, primaryKey);
        EnsureUtil.ensureNotNull("Entity does not exist: " + entityClass.getName() + " - " + primaryKey, "entity", entity);
        return entity;
    }
    
    public Object createId(final EntityMetaData metaData, final String string) {
        final Class<?> type = metaData.getIdType();
        if (type == Long.class || type == Long.TYPE) {
            return Long.parseLong(string);
        }
        if (type == String.class) {
            return string;
        }
        if (type == Byte.class || type == Byte.TYPE) {
            return Byte.parseByte(string);
        }
        if (type == Short.class || type == Short.TYPE) {
            return Short.parseShort(string);
        }
        if (type == Integer.class || type == Integer.TYPE) {
            return Integer.parseInt(string);
        }
        if (type == Float.class || type == Float.TYPE) {
            return Float.parseFloat(string);
        }
        if (type == Double.class || type == Double.TYPE) {
            return Double.parseDouble(string);
        }
        if (type == Character.class || type == Character.TYPE) {
            return new Character(string.charAt(0));
        }
        if (type == Date.class) {
            return new Date(Long.parseLong(string));
        }
        if (type == java.sql.Date.class) {
            return new java.sql.Date(Long.parseLong(string));
        }
        if (type == BigDecimal.class) {
            return new BigDecimal(string);
        }
        if (type == BigInteger.class) {
            return new BigInteger(string);
        }
        throw new ProcessEngineException("Unsupported Primary key type for JPA-Entity: " + type.getName());
    }
    
    public String getIdString(final Object value) {
        EnsureUtil.ensureNotNull("Value of primary key for JPA-Entity", value);
        if (value instanceof Date) {
            return "" + ((Date)value).getTime();
        }
        if (value instanceof java.sql.Date) {
            return "" + ((java.sql.Date)value).getTime();
        }
        if (value instanceof Long || value instanceof String || value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Float || value instanceof Double || value instanceof Character || value instanceof BigDecimal || value instanceof BigInteger) {
            return value.toString();
        }
        throw new ProcessEngineException("Unsupported Primary key type for JPA-Entity: " + value.getClass().getName());
    }
}
