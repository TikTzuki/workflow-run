// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.test.mock;

import java.util.HashMap;
import java.util.Map;

public class Mocks
{
    private static ThreadLocal<Map<String, Object>> mockContainer;
    
    public static Map<String, Object> getMocks() {
        Map<String, Object> mocks = Mocks.mockContainer.get();
        if (mocks == null) {
            mocks = new HashMap<String, Object>();
            Mocks.mockContainer.set(mocks);
        }
        return mocks;
    }
    
    public static void register(final String key, final Object value) {
        getMocks().put(key, value);
    }
    
    public static Object get(final Object key) {
        return getMocks().get(key);
    }
    
    public static void reset() {
        if (getMocks() != null) {
            getMocks().clear();
        }
    }
    
    static {
        Mocks.mockContainer = new ThreadLocal<Map<String, Object>>();
    }
}
