// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.WeakHashMap;
import java.util.Map;

public final class Cache implements TreeCache
{
    private final Map<String, Tree> primary;
    private final Map<String, Tree> secondary;
    
    public Cache(final int size) {
        this(size, new WeakHashMap<String, Tree>());
    }
    
    public Cache(final int size, final Map<String, Tree> secondary) {
        this.primary = Collections.synchronizedMap((Map<String, Tree>)new LinkedHashMap<String, Tree>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(final Map.Entry<String, Tree> eldest) {
                if (this.size() > size) {
                    if (Cache.this.secondary != null) {
                        Cache.this.secondary.put(eldest.getKey(), eldest.getValue());
                    }
                    return true;
                }
                return false;
            }
        });
        this.secondary = ((secondary == null) ? null : Collections.synchronizedMap(secondary));
    }
    
    @Override
    public Tree get(final String expression) {
        if (this.secondary == null) {
            return this.primary.get(expression);
        }
        Tree tree = this.primary.get(expression);
        if (tree == null) {
            tree = this.secondary.get(expression);
        }
        return tree;
    }
    
    @Override
    public void put(final String expression, final Tree tree) {
        this.primary.put(expression, tree);
    }
}
