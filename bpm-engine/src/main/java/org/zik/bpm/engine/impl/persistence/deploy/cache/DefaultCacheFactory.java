// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.camunda.commons.utils.cache.ConcurrentLruCache;
import org.camunda.commons.utils.cache.Cache;

public class DefaultCacheFactory implements CacheFactory
{
    @Override
    public <T> Cache<String, T> createCache(final int maxNumberOfElementsInCache) {
        return (Cache<String, T>)new ConcurrentLruCache(maxNumberOfElementsInCache);
    }
}
