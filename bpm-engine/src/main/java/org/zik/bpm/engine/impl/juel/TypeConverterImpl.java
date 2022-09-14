// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.math.BigInteger;
import java.math.BigDecimal;
import org.zik.bpm.engine.impl.javax.el.ELException;

public class TypeConverterImpl implements TypeConverter
{
    private static final long serialVersionUID = 1L;
    
    protected Boolean coerceToBoolean(final Object value) {
        if (value == null || "".equals(value)) {
            return Boolean.FALSE;
        }
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        if (value instanceof String) {
            return Boolean.valueOf((String)value);
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), Boolean.class));
    }
    
    protected Character coerceToCharacter(final Object value) {
        if (value == null || "".equals(value)) {
            return '\0';
        }
        if (value instanceof Character) {
            return (Character)value;
        }
        if (value instanceof Number) {
            return (char)((Number)value).shortValue();
        }
        if (value instanceof String) {
            return ((String)value).charAt(0);
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), Character.class));
    }
    
    protected BigDecimal coerceToBigDecimal(final Object value) {
        if (value == null || "".equals(value)) {
            return BigDecimal.valueOf(0L);
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal)value;
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger)value);
        }
        if (value instanceof Number) {
            return new BigDecimal(((Number)value).doubleValue());
        }
        if (value instanceof String) {
            try {
                return new BigDecimal((String)value);
            }
            catch (NumberFormatException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, BigDecimal.class));
            }
        }
        if (value instanceof Character) {
            return new BigDecimal((short)(char)value);
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), BigDecimal.class));
    }
    
    protected BigInteger coerceToBigInteger(final Object value) {
        if (value == null || "".equals(value)) {
            return BigInteger.valueOf(0L);
        }
        if (value instanceof BigInteger) {
            return (BigInteger)value;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).toBigInteger();
        }
        if (value instanceof Number) {
            return BigInteger.valueOf(((Number)value).longValue());
        }
        if (value instanceof String) {
            try {
                return new BigInteger((String)value);
            }
            catch (NumberFormatException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, BigInteger.class));
            }
        }
        if (value instanceof Character) {
            return BigInteger.valueOf((short)(char)value);
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), BigInteger.class));
    }
    
    protected Double coerceToDouble(final Object value) {
        if (value == null || "".equals(value)) {
            return 0.0;
        }
        if (value instanceof Double) {
            return (Double)value;
        }
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.valueOf((String)value);
            }
            catch (NumberFormatException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, Double.class));
            }
        }
        if (value instanceof Character) {
            return (double)(short)(char)value;
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), Double.class));
    }
    
    protected Float coerceToFloat(final Object value) {
        if (value == null || "".equals(value)) {
            return 0.0f;
        }
        if (value instanceof Float) {
            return (Float)value;
        }
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        }
        if (value instanceof String) {
            try {
                return Float.valueOf((String)value);
            }
            catch (NumberFormatException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, Float.class));
            }
        }
        if (value instanceof Character) {
            return (float)(short)(char)value;
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), Float.class));
    }
    
    protected Long coerceToLong(final Object value) {
        if (value == null || "".equals(value)) {
            return 0L;
        }
        if (value instanceof Long) {
            return (Long)value;
        }
        if (value instanceof Number) {
            return ((Number)value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.valueOf((String)value);
            }
            catch (NumberFormatException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, Long.class));
            }
        }
        if (value instanceof Character) {
            return (long)(short)(char)value;
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), Long.class));
    }
    
    protected Integer coerceToInteger(final Object value) {
        if (value == null || "".equals(value)) {
            return 0;
        }
        if (value instanceof Integer) {
            return (Integer)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.valueOf((String)value);
            }
            catch (NumberFormatException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, Integer.class));
            }
        }
        if (value instanceof Character) {
            return (int)(short)(char)value;
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), Integer.class));
    }
    
    protected Short coerceToShort(final Object value) {
        if (value == null || "".equals(value)) {
            return 0;
        }
        if (value instanceof Short) {
            return (Short)value;
        }
        if (value instanceof Number) {
            return ((Number)value).shortValue();
        }
        if (value instanceof String) {
            try {
                return Short.valueOf((String)value);
            }
            catch (NumberFormatException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, Short.class));
            }
        }
        if (value instanceof Character) {
            return (short)(char)value;
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), Short.class));
    }
    
    protected Byte coerceToByte(final Object value) {
        if (value == null || "".equals(value)) {
            return 0;
        }
        if (value instanceof Byte) {
            return (Byte)value;
        }
        if (value instanceof Number) {
            return ((Number)value).byteValue();
        }
        if (value instanceof String) {
            try {
                return Byte.valueOf((String)value);
            }
            catch (NumberFormatException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, Byte.class));
            }
        }
        if (value instanceof Character) {
            return Short.valueOf((short)(char)value).byteValue();
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), Byte.class));
    }
    
    protected String coerceToString(final Object value) {
        if (value == null) {
            return "";
        }
        if (value instanceof String) {
            return (String)value;
        }
        if (value instanceof Enum) {
            return ((Enum)value).name();
        }
        return value.toString();
    }
    
    protected <T extends Enum<T>> T coerceToEnum(final Object value, final Class<T> type) {
        if (value == null || "".equals(value)) {
            return null;
        }
        if (type.isInstance(value)) {
            return (T)value;
        }
        if (value instanceof String) {
            try {
                return Enum.valueOf(type, (String)value);
            }
            catch (IllegalArgumentException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, type));
            }
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), type));
    }
    
    protected Object coerceStringToType(final String value, final Class<?> type) {
        final PropertyEditor editor = PropertyEditorManager.findEditor(type);
        if (editor != null) {
            if ("".equals(value)) {
                try {
                    editor.setAsText(value);
                    return editor.getValue();
                }
                catch (IllegalArgumentException e) {
                    return null;
                }
            }
            try {
                editor.setAsText(value);
            }
            catch (IllegalArgumentException e) {
                throw new ELException(LocalMessages.get("error.coerce.value", value, type));
            }
            return editor.getValue();
        }
        if ("".equals(value)) {
            return null;
        }
        throw new ELException(LocalMessages.get("error.coerce.type", String.class, type));
    }
    
    protected Object coerceToType(final Object value, final Class<?> type) {
        if (type == String.class) {
            return this.coerceToString(value);
        }
        if (type == Long.class || type == Long.TYPE) {
            return this.coerceToLong(value);
        }
        if (type == Double.class || type == Double.TYPE) {
            return this.coerceToDouble(value);
        }
        if (type == Boolean.class || type == Boolean.TYPE) {
            return this.coerceToBoolean(value);
        }
        if (type == Integer.class || type == Integer.TYPE) {
            return this.coerceToInteger(value);
        }
        if (type == Float.class || type == Float.TYPE) {
            return this.coerceToFloat(value);
        }
        if (type == Short.class || type == Short.TYPE) {
            return this.coerceToShort(value);
        }
        if (type == Byte.class || type == Byte.TYPE) {
            return this.coerceToByte(value);
        }
        if (type == Character.class || type == Character.TYPE) {
            return this.coerceToCharacter(value);
        }
        if (type == BigDecimal.class) {
            return this.coerceToBigDecimal(value);
        }
        if (type == BigInteger.class) {
            return this.coerceToBigInteger(value);
        }
        if (type.getSuperclass() == Enum.class) {
            return this.coerceToEnum(value, type);
        }
        if (value == null || value.getClass() == type || type.isInstance(value)) {
            return value;
        }
        if (value instanceof String) {
            return this.coerceStringToType((String)value, type);
        }
        throw new ELException(LocalMessages.get("error.coerce.type", value.getClass(), type));
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj.getClass().equals(this.getClass());
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
    
    @Override
    public <T> T convert(final Object value, final Class<T> type) throws ELException {
        return (T)this.coerceToType(value, type);
    }
}
