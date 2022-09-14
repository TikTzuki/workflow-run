// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.util.HashSet;
import java.util.Collection;
import java.util.Map;
import org.zik.bpm.engine.impl.javax.el.ELException;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Set;

public class BooleanOperations
{
    private static final Set<Class<? extends Number>> SIMPLE_INTEGER_TYPES;
    private static final Set<Class<? extends Number>> SIMPLE_FLOAT_TYPES;
    
    private static final boolean lt0(final TypeConverter converter, final Object o1, final Object o2) {
        final Class<?> t1 = o1.getClass();
        final Class<?> t2 = o2.getClass();
        if (BigDecimal.class.isAssignableFrom(t1) || BigDecimal.class.isAssignableFrom(t2)) {
            return converter.convert(o1, BigDecimal.class).compareTo(converter.convert(o2, BigDecimal.class)) < 0;
        }
        if (BooleanOperations.SIMPLE_FLOAT_TYPES.contains(t1) || BooleanOperations.SIMPLE_FLOAT_TYPES.contains(t2)) {
            return converter.convert(o1, Double.class) < converter.convert(o2, Double.class);
        }
        if (BigInteger.class.isAssignableFrom(t1) || BigInteger.class.isAssignableFrom(t2)) {
            return converter.convert(o1, BigInteger.class).compareTo(converter.convert(o2, BigInteger.class)) < 0;
        }
        if (BooleanOperations.SIMPLE_INTEGER_TYPES.contains(t1) || BooleanOperations.SIMPLE_INTEGER_TYPES.contains(t2)) {
            return converter.convert(o1, Long.class) < converter.convert(o2, Long.class);
        }
        if (t1 == String.class || t2 == String.class) {
            return converter.convert(o1, String.class).compareTo((String)converter.convert(o2, String.class)) < 0;
        }
        if (o1 instanceof Comparable) {
            return ((Comparable)o1).compareTo(o2) < 0;
        }
        if (o2 instanceof Comparable) {
            return ((Comparable)o2).compareTo(o1) > 0;
        }
        throw new ELException(LocalMessages.get("error.compare.types", o1.getClass(), o2.getClass()));
    }
    
    private static final boolean gt0(final TypeConverter converter, final Object o1, final Object o2) {
        final Class<?> t1 = o1.getClass();
        final Class<?> t2 = o2.getClass();
        if (BigDecimal.class.isAssignableFrom(t1) || BigDecimal.class.isAssignableFrom(t2)) {
            return converter.convert(o1, BigDecimal.class).compareTo(converter.convert(o2, BigDecimal.class)) > 0;
        }
        if (BooleanOperations.SIMPLE_FLOAT_TYPES.contains(t1) || BooleanOperations.SIMPLE_FLOAT_TYPES.contains(t2)) {
            return converter.convert(o1, Double.class) > converter.convert(o2, Double.class);
        }
        if (BigInteger.class.isAssignableFrom(t1) || BigInteger.class.isAssignableFrom(t2)) {
            return converter.convert(o1, BigInteger.class).compareTo(converter.convert(o2, BigInteger.class)) > 0;
        }
        if (BooleanOperations.SIMPLE_INTEGER_TYPES.contains(t1) || BooleanOperations.SIMPLE_INTEGER_TYPES.contains(t2)) {
            return converter.convert(o1, Long.class) > converter.convert(o2, Long.class);
        }
        if (t1 == String.class || t2 == String.class) {
            return converter.convert(o1, String.class).compareTo((String)converter.convert(o2, String.class)) > 0;
        }
        if (o1 instanceof Comparable) {
            return ((Comparable)o1).compareTo(o2) > 0;
        }
        if (o2 instanceof Comparable) {
            return ((Comparable)o2).compareTo(o1) < 0;
        }
        throw new ELException(LocalMessages.get("error.compare.types", o1.getClass(), o2.getClass()));
    }
    
    public static final boolean lt(final TypeConverter converter, final Object o1, final Object o2) {
        return o1 != o2 && o1 != null && o2 != null && lt0(converter, o1, o2);
    }
    
    public static final boolean gt(final TypeConverter converter, final Object o1, final Object o2) {
        return o1 != o2 && o1 != null && o2 != null && gt0(converter, o1, o2);
    }
    
    public static final boolean ge(final TypeConverter converter, final Object o1, final Object o2) {
        return o1 == o2 || (o1 != null && o2 != null && !lt0(converter, o1, o2));
    }
    
    public static final boolean le(final TypeConverter converter, final Object o1, final Object o2) {
        return o1 == o2 || (o1 != null && o2 != null && !gt0(converter, o1, o2));
    }
    
    public static final boolean eq(final TypeConverter converter, final Object o1, final Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        final Class<?> t1 = o1.getClass();
        final Class<?> t2 = o2.getClass();
        if (BigDecimal.class.isAssignableFrom(t1) || BigDecimal.class.isAssignableFrom(t2)) {
            return converter.convert(o1, BigDecimal.class).equals(converter.convert(o2, BigDecimal.class));
        }
        if (BooleanOperations.SIMPLE_FLOAT_TYPES.contains(t1) || BooleanOperations.SIMPLE_FLOAT_TYPES.contains(t2)) {
            return converter.convert(o1, Double.class).equals(converter.convert(o2, Double.class));
        }
        if (BigInteger.class.isAssignableFrom(t1) || BigInteger.class.isAssignableFrom(t2)) {
            return converter.convert(o1, BigInteger.class).equals(converter.convert(o2, BigInteger.class));
        }
        if (BooleanOperations.SIMPLE_INTEGER_TYPES.contains(t1) || BooleanOperations.SIMPLE_INTEGER_TYPES.contains(t2)) {
            return converter.convert(o1, Long.class).equals(converter.convert(o2, Long.class));
        }
        if (t1 == Boolean.class || t2 == Boolean.class) {
            return converter.convert(o1, Boolean.class).equals(converter.convert(o2, Boolean.class));
        }
        if (o1 instanceof Enum) {
            return o1 == converter.convert(o2, o1.getClass());
        }
        if (o2 instanceof Enum) {
            return converter.convert(o1, o2.getClass()) == o2;
        }
        if (t1 == String.class || t2 == String.class) {
            return converter.convert(o1, String.class).equals(converter.convert(o2, String.class));
        }
        return o1.equals(o2);
    }
    
    public static final boolean ne(final TypeConverter converter, final Object o1, final Object o2) {
        return !eq(converter, o1, o2);
    }
    
    public static final boolean empty(final TypeConverter converter, final Object o) {
        if (o == null || "".equals(o)) {
            return true;
        }
        if (o instanceof Object[]) {
            return ((Object[])o).length == 0;
        }
        if (o instanceof Map) {
            return ((Map)o).isEmpty();
        }
        return o instanceof Collection && ((Collection)o).isEmpty();
    }
    
    static {
        SIMPLE_INTEGER_TYPES = new HashSet<Class<? extends Number>>();
        SIMPLE_FLOAT_TYPES = new HashSet<Class<? extends Number>>();
        BooleanOperations.SIMPLE_INTEGER_TYPES.add(Byte.class);
        BooleanOperations.SIMPLE_INTEGER_TYPES.add(Short.class);
        BooleanOperations.SIMPLE_INTEGER_TYPES.add(Integer.class);
        BooleanOperations.SIMPLE_INTEGER_TYPES.add(Long.class);
        BooleanOperations.SIMPLE_FLOAT_TYPES.add(Float.class);
        BooleanOperations.SIMPLE_FLOAT_TYPES.add(Double.class);
    }
}
