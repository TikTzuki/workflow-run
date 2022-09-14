// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.zik.bpm.engine.impl.management.PurgeReporting;

public class CachePurgeReport implements PurgeReporting<Set<String>>
{
    public static final String PROCESS_DEF_CACHE = "PROC_DEF_CACHE";
    public static final String BPMN_MODEL_INST_CACHE = "BPMN_MODEL_INST_CACHE";
    public static final String CASE_DEF_CACHE = "CASE_DEF_CACHE";
    public static final String CASE_MODEL_INST_CACHE = "CASE_MODEL_INST_CACHE";
    public static final String DMN_DEF_CACHE = "DMN_DEF_CACHE";
    public static final String DMN_REQ_DEF_CACHE = "DMN_REQ_DEF_CACHE";
    public static final String DMN_MODEL_INST_CACHE = "DMN_MODEL_INST_CACHE";
    Map<String, Set<String>> deletedCache;
    
    public CachePurgeReport() {
        this.deletedCache = new HashMap<String, Set<String>>();
    }
    
    @Override
    public void addPurgeInformation(final String key, final Set<String> value) {
        this.deletedCache.put(key, new HashSet<String>(value));
    }
    
    @Override
    public Map<String, Set<String>> getPurgeReport() {
        return this.deletedCache;
    }
    
    @Override
    public String getPurgeReportAsString() {
        final StringBuilder builder = new StringBuilder();
        for (final String key : this.deletedCache.keySet()) {
            builder.append("Cache: ").append(key).append(" contains: ").append(this.getReportValue(key)).append("\n");
        }
        return builder.toString();
    }
    
    @Override
    public Set<String> getReportValue(final String key) {
        return this.deletedCache.get(key);
    }
    
    @Override
    public boolean containsReport(final String key) {
        return this.deletedCache.containsKey(key);
    }
    
    @Override
    public boolean isEmpty() {
        return this.deletedCache.isEmpty();
    }
}
