// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CollectionUtil
{
    private CollectionUtil() {
    }
    
    public static Map<String, Object> singletonMap(final String key, final Object value) {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        return map;
    }
    
    public static <T> List<T> asArrayList(final T[] values) {
        final ArrayList<T> result = new ArrayList<T>();
        Collections.addAll(result, values);
        return result;
    }
    
    public static <T> Set<T> asHashSet(final T... elements) {
        final Set<T> set = new HashSet<T>();
        Collections.addAll(set, elements);
        return set;
    }
    
    public static <S, T> void addToMapOfLists(final Map<S, List<T>> map, final S key, final T value) {
        List<T> list = map.get(key);
        if (list == null) {
            list = new ArrayList<T>();
            map.put(key, list);
        }
        list.add(value);
    }
    
    public static <S, T> void addToMapOfSets(final Map<S, Set<T>> map, final S key, final T value) {
        Set<T> set = map.get(key);
        if (set == null) {
            set = new HashSet<T>();
            map.put(key, set);
        }
        set.add(value);
    }
    
    public static <S, T> void addCollectionToMapOfSets(final Map<S, Set<T>> map, final S key, final Collection<T> values) {
        Set<T> set = map.get(key);
        if (set == null) {
            set = new HashSet<T>();
            map.put(key, set);
        }
        set.addAll((Collection<? extends T>)values);
    }
    
    public static <T> List<List<T>> partition(final List<T> list, final int partitionSize) {
        final List<List<T>> parts = new ArrayList<List<T>>();
        final int listSize = list.size();
        if (listSize <= partitionSize) {
            parts.add(list);
        }
        else {
            for (int i = 0; i < listSize; i += partitionSize) {
                parts.add(new ArrayList<T>((Collection<? extends T>)list.subList(i, Math.min(listSize, i + partitionSize))));
            }
        }
        return parts;
    }
    
    public static <T> List<T> collectInList(final Iterator<T> iterator) {
        final List<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
    }
    
    public static boolean isEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
