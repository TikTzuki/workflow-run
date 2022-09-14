// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.metadata;

import org.zik.bpm.engine.impl.util.xml.Parse;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.util.xml.Element;
import org.zik.bpm.application.impl.metadata.spi.ProcessArchiveXml;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.xml.Parser;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;
import org.zik.bpm.container.impl.metadata.DeploymentMetadataParse;

public class ProcessesXmlParse extends DeploymentMetadataParse
{
    protected ProcessesXml processesXml;
    
    public ProcessesXmlParse(final Parser parser) {
        super(parser);
    }
    
    @Override
    public ProcessesXmlParse execute() {
        super.execute();
        return this;
    }
    
    @Override
    protected void parseRootElement() {
        final List<ProcessEngineXml> processEngines = new ArrayList<ProcessEngineXml>();
        final List<ProcessArchiveXml> processArchives = new ArrayList<ProcessArchiveXml>();
        for (final Element element : this.rootElement.elements()) {
            if ("process-engine".equals(element.getTagName())) {
                this.parseProcessEngine(element, processEngines);
            }
            else {
                if (!"process-archive".equals(element.getTagName())) {
                    continue;
                }
                this.parseProcessArchive(element, processArchives);
            }
        }
        this.processesXml = new ProcessesXmlImpl(processEngines, processArchives);
    }
    
    protected void parseProcessArchive(final Element element, final List<ProcessArchiveXml> parsedProcessArchives) {
        final ProcessArchiveXmlImpl processArchive = new ProcessArchiveXmlImpl();
        processArchive.setName(element.attribute("name"));
        processArchive.setTenantId(element.attribute("tenantId"));
        final List<String> processResourceNames = new ArrayList<String>();
        final Map<String, String> properties = new HashMap<String, String>();
        for (final Element childElement : element.elements()) {
            if ("process-engine".equals(childElement.getTagName())) {
                processArchive.setProcessEngineName(childElement.getText());
            }
            else if ("process".equals(childElement.getTagName()) || "resource".equals(childElement.getTagName())) {
                processResourceNames.add(childElement.getText());
            }
            else {
                if (!"properties".equals(childElement.getTagName())) {
                    continue;
                }
                this.parseProperties(childElement, properties);
            }
        }
        processArchive.setProperties(properties);
        processArchive.setProcessResourceNames(processResourceNames);
        parsedProcessArchives.add(processArchive);
    }
    
    public ProcessesXml getProcessesXml() {
        return this.processesXml;
    }
    
    @Override
    public ProcessesXmlParse sourceUrl(final URL url) {
        super.sourceUrl(url);
        return this;
    }
}
