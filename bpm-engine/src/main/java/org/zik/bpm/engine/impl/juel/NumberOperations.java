// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import java.math.BigInteger;
import java.math.BigDecimal;

public class NumberOperations
{
    private static final Long LONG_ZERO;
    
    private static final boolean isDotEe(final String value) {
        final int length = value.length();
        int i = 0;
        while (i < length) {
            switch (value.charAt(i)) {
                case '.':
                case 'E':
                case 'e': {
                    return true;
                }
                default: {
                    ++i;
                    continue;
                }
            }
        }
        return false;
    }
    
    private static final boolean isDotEe(final Object value) {
        return value instanceof String && isDotEe((String)value);
    }
    
    private static final boolean isFloatOrDouble(final Object value) {
        return value instanceof Float || value instanceof Double;
    }
    
    private static final boolean isFloatOrDoubleOrDotEe(final Object value) {
        return isFloatOrDouble(value) || isDotEe(value);
    }
    
    private static final boolean isBigDecimalOrBigInteger(final Object value) {
        return value instanceof BigDecimal || value instanceof BigInteger;
    }
    
    private static final boolean isBigDecimalOrFloatOrDoubleOrDotEe(final Object value) {
        return value instanceof BigDecimal || isFloatOrDoubleOrDotEe(value);
    }
    
    public static final Number add(final TypeConverter converter, final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return NumberOperations.LONG_ZERO;
        }
        if (o1 instanceof BigDecimal || o2 instanceof BigDecimal) {
            return converter.convert(o1, BigDecimal.class).add(converter.convert(o2, BigDecimal.class));
        }
        if (isFloatOrDoubleOrDotEe(o1) || isFloatOrDoubleOrDotEe(o2)) {
            if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
                return converter.convert(o1, BigDecimal.class).add(converter.convert(o2, BigDecimal.class));
            }
            return converter.convert(o1, Double.class) + converter.convert(o2, Double.class);
        }
        else {
            if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
                return converter.convert(o1, BigInteger.class).add(converter.convert(o2, BigInteger.class));
            }
            return converter.convert(o1, Long.class) + converter.convert(o2, Long.class);
        }
    }
    
    public static final Number sub(final TypeConverter converter, final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return NumberOperations.LONG_ZERO;
        }
        if (o1 instanceof BigDecimal || o2 instanceof BigDecimal) {
            return converter.convert(o1, BigDecimal.class).subtract(converter.convert(o2, BigDecimal.class));
        }
        if (isFloatOrDoubleOrDotEe(o1) || isFloatOrDoubleOrDotEe(o2)) {
            if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
                return converter.convert(o1, BigDecimal.class).subtract(converter.convert(o2, BigDecimal.class));
            }
            return converter.convert(o1, Double.class) - converter.convert(o2, Double.class);
        }
        else {
            if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
                return converter.convert(o1, BigInteger.class).subtract(converter.convert(o2, BigInteger.class));
            }
            return converter.convert(o1, Long.class) - converter.convert(o2, Long.class);
        }
    }
    
    public static final Number mul(final TypeConverter converter, final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return NumberOperations.LONG_ZERO;
        }
        if (o1 instanceof BigDecimal || o2 instanceof BigDecimal) {
            return converter.convert(o1, BigDecimal.class).multiply(converter.convert(o2, BigDecimal.class));
        }
        if (isFloatOrDoubleOrDotEe(o1) || isFloatOrDoubleOrDotEe(o2)) {
            if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
                return converter.convert(o1, BigDecimal.class).multiply(converter.convert(o2, BigDecimal.class));
            }
            return converter.convert(o1, Double.class) * converter.convert(o2, Double.class);
        }
        else {
            if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
                return converter.convert(o1, BigInteger.class).multiply(converter.convert(o2, BigInteger.class));
            }
            return converter.convert(o1, Long.class) * converter.convert(o2, Long.class);
        }
    }
    
    public static final Number div(final TypeConverter converter, final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return NumberOperations.LONG_ZERO;
        }
        if (isBigDecimalOrBigInteger(o1) || isBigDecimalOrBigInteger(o2)) {
            return converter.convert(o1, BigDecimal.class).divide(converter.convert(o2, BigDecimal.class), 4);
        }
        return converter.convert(o1, Double.class) / converter.convert(o2, Double.class);
    }
    
    public static final Number mod(final TypeConverter converter, final Object o1, final Object o2) {
        if (o1 == null && o2 == null) {
            return NumberOperations.LONG_ZERO;
        }
        if (isBigDecimalOrFloatOrDoubleOrDotEe(o1) || isBigDecimalOrFloatOrDoubleOrDotEe(o2)) {
            return converter.convert(o1, Double.class) % converter.convert(o2, Double.class);
        }
        if (o1 instanceof BigInteger || o2 instanceof BigInteger) {
            return converter.convert(o1, BigInteger.class).remainder(converter.convert(o2, BigInteger.class));
        }
        return converter.convert(o1, Long.class) % converter.convert(o2, Long.class);
    }
    
    public static final Number neg(final TypeConverter converter, final Object value) {
        if (value == null) {
            return NumberOperations.LONG_ZERO;
        }
        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).negate();
        }
        if (value instanceof BigInteger) {
            return ((BigInteger)value).negate();
        }
        if (value instanceof Double) {
            return -(double)value;
        }
        if (value instanceof Float) {
            return -(float)value;
        }
        if (value instanceof String) {
            if (isDotEe((String)value)) {
                return -converter.convert(value, Double.class);
            }
            return -converter.convert(value, Long.class);
        }
        else {
            if (value instanceof Long) {
                return -(long)value;
            }
            if (value instanceof Integer) {
                return -(int)value;
            }
            if (value instanceof Short) {
                return (short)(-(short)value);
            }
            if (value instanceof Byte) {
                return (byte)(-(byte)value);
            }
            throw new ELException(LocalMessages.get("error.negate", value.getClass()));
        }
    }
    
    static {
        LONG_ZERO = 0L;
    }
}
