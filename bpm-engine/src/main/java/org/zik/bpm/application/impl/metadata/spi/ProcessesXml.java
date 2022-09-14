// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.metadata.spi;

import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.application.impl.metadata.ProcessArchiveXmlImpl;
import java.util.ArrayList;
import java.util.Collections;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.List;

public interface ProcessesXml
{
    public static final ProcessesXml EMPTY_PROCESSES_XML = new ProcessesXml() {
        @Override
        public List<ProcessEngineXml> getProcessEngines() {
            return Collections.emptyList();
        }
        
        @Override
        public List<ProcessArchiveXml> getProcessArchives() {
            final List<ProcessArchiveXml> processArchives = new ArrayList<ProcessArchiveXml>();
            final ProcessArchiveXmlImpl pa = new ProcessArchiveXmlImpl();
            processArchives.add(pa);
            pa.setProcessResourceNames(Collections.emptyList());
            final HashMap<String, String> properties = new HashMap<String, String>();
            pa.setProperties(properties);
            properties.put("isDeleteUponUndeploy", Boolean.FALSE.toString());
            properties.put("isScanForProcessDefinitions", Boolean.TRUE.toString());
            properties.put("isDeployChangedOnly", Boolean.FALSE.toString());
            properties.put("resumePreviousBy", "process-definition-key");
            return processArchives;
        }
    };
    
    List<ProcessEngineXml> getProcessEngines();
    
    List<ProcessArchiveXml> getProcessArchives();
}
