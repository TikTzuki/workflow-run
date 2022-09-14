// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.camunda.commons.utils.cache.Cache;

public interface CacheFactory
{
     <T> Cache<String, T> createCache(final int p0);
}
