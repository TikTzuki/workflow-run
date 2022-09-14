// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.Collection;
import java.util.List;
import java.util.Arrays;

public class CompareUtil
{
    public static <T extends Comparable<T>> boolean areNotInAscendingOrder(final T... values) {
        boolean excluding = false;
        if (values != null) {
            excluding = areNotInAscendingOrder((List<Comparable>)Arrays.asList((T[])values));
        }
        return excluding;
    }
    
    public static <T extends Comparable<T>> boolean areNotInAscendingOrder(final List<T> values) {
        int lastNotNull = -1;
        for (int i = 0; i < values.size(); ++i) {
            final T value = values.get(i);
            if (value != null) {
                if (lastNotNull != -1 && values.get(lastNotNull).compareTo(value) > 0) {
                    return true;
                }
                lastNotNull = i;
            }
        }
        return false;
    }
    
    public static <T> boolean elementIsNotContainedInList(final T element, final Collection<T> values) {
        return element != null && values != null && !values.contains(element);
    }
    
    public static <T> boolean elementIsNotContainedInArray(final T element, final T... values) {
        return element != null && values != null && elementIsNotContainedInList(element, Arrays.asList(values));
    }
    
    public static <T> boolean elementIsContainedInList(final T element, final Collection<T> values) {
        return element != null && values != null && values.contains(element);
    }
    
    public static <T> boolean elementIsContainedInArray(final T element, final T... values) {
        return element != null && values != null && elementIsContainedInList(element, Arrays.asList(values));
    }
    
    public static <T extends Comparable<T>> T min(final T obj1, final T obj2) {
        return (obj1.compareTo(obj2) <= 0) ? obj1 : obj2;
    }
    
    public static <T extends Comparable<T>> T max(final T obj1, final T obj2) {
        return (obj1.compareTo(obj2) >= 0) ? obj1 : obj2;
    }
}
