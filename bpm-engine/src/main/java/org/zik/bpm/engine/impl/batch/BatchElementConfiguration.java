// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.TreeMap;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.Comparator;

public class BatchElementConfiguration
{
    protected static final Comparator<String> NULLS_LAST_STRING_COMPARATOR;
    protected SortedMap<String, Set<String>> collectedMappings;
    protected List<String> ids;
    protected DeploymentMappings mappings;
    
    public BatchElementConfiguration() {
        this.collectedMappings = new TreeMap<String, Set<String>>(BatchElementConfiguration.NULLS_LAST_STRING_COMPARATOR);
    }
    
    public void addDeploymentMappings(final List<ImmutablePair<String, String>> mappings) {
        this.addDeploymentMappings(mappings, null);
    }
    
    public void addDeploymentMappings(final List<ImmutablePair<String, String>> mappingsList, final Collection<String> idList) {
        if (this.ids != null) {
            this.ids = null;
            this.mappings = null;
        }
        final Set<String> missingIds = (idList == null) ? null : new HashSet<String>(idList);
        final String deploymentId;
        Set<String> idSet;
        final Set set;
        mappingsList.forEach(pair -> {
            deploymentId = pair.getLeft();
            idSet = this.collectedMappings.get(deploymentId);
            if (idSet == null) {
                idSet = new HashSet<String>();
                this.collectedMappings.put(deploymentId, idSet);
            }
            idSet.add((String)pair.getRight());
            if (set != null) {
                set.remove(pair.getRight());
            }
            return;
        });
        if (missingIds != null && !missingIds.isEmpty()) {
            Set<String> nullIds = this.collectedMappings.get(null);
            if (nullIds == null) {
                nullIds = new HashSet<String>();
                this.collectedMappings.put(null, nullIds);
            }
            nullIds.addAll(missingIds);
        }
    }
    
    public List<String> getIds() {
        if (this.ids == null) {
            this.createDeploymentMappings();
        }
        return this.ids;
    }
    
    public DeploymentMappings getMappings() {
        if (this.mappings == null) {
            this.createDeploymentMappings();
        }
        return this.mappings;
    }
    
    public boolean isEmpty() {
        return this.collectedMappings.isEmpty();
    }
    
    protected void createDeploymentMappings() {
        this.ids = new ArrayList<String>();
        this.mappings = new DeploymentMappings();
        for (final Map.Entry<String, Set<String>> mapping : this.collectedMappings.entrySet()) {
            this.ids.addAll(mapping.getValue());
            this.mappings.add(new DeploymentMapping(mapping.getKey(), mapping.getValue().size()));
        }
    }
    
    static {
        NULLS_LAST_STRING_COMPARATOR = Comparator.nullsLast(String::compareToIgnoreCase);
    }
}
