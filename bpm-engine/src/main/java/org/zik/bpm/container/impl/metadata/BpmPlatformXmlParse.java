// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import org.zik.bpm.engine.impl.util.xml.Parse;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.container.impl.metadata.spi.JobAcquisitionXml;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.container.impl.metadata.spi.JobExecutorXml;
import org.zik.bpm.engine.impl.util.xml.Element;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.xml.Parser;
import org.zik.bpm.container.impl.metadata.spi.BpmPlatformXml;

public class BpmPlatformXmlParse extends DeploymentMetadataParse
{
    protected BpmPlatformXml bpmPlatformXml;
    
    public BpmPlatformXmlParse(final Parser parser) {
        super(parser);
    }
    
    @Override
    public BpmPlatformXmlParse execute() {
        super.execute();
        return this;
    }
    
    @Override
    protected void parseRootElement() {
        final JobExecutorXmlImpl jobExecutor = new JobExecutorXmlImpl();
        final List<ProcessEngineXml> processEngines = new ArrayList<ProcessEngineXml>();
        for (final Element element : this.rootElement.elements()) {
            if ("job-executor".equals(element.getTagName())) {
                this.parseJobExecutor(element, jobExecutor);
            }
            else {
                if (!"process-engine".equals(element.getTagName())) {
                    continue;
                }
                this.parseProcessEngine(element, processEngines);
            }
        }
        this.bpmPlatformXml = new BpmPlatformXmlImpl(jobExecutor, processEngines);
    }
    
    protected void parseJobExecutor(final Element element, final JobExecutorXmlImpl jobExecutorXml) {
        final List<JobAcquisitionXml> jobAcquisitions = new ArrayList<JobAcquisitionXml>();
        final Map<String, String> properties = new HashMap<String, String>();
        for (final Element childElement : element.elements()) {
            if ("job-acquisition".equals(childElement.getTagName())) {
                this.parseJobAcquisition(childElement, jobAcquisitions);
            }
            else {
                if (!"properties".equals(childElement.getTagName())) {
                    continue;
                }
                this.parseProperties(childElement, properties);
            }
        }
        jobExecutorXml.setJobAcquisitions(jobAcquisitions);
        jobExecutorXml.setProperties(properties);
    }
    
    protected void parseJobAcquisition(final Element element, final List<JobAcquisitionXml> jobAcquisitions) {
        final JobAcquisitionXmlImpl jobAcquisition = new JobAcquisitionXmlImpl();
        jobAcquisition.setName(element.attribute("name"));
        final Map<String, String> properties = new HashMap<String, String>();
        for (final Element childElement : element.elements()) {
            if ("job-executor-class".equals(childElement.getTagName())) {
                jobAcquisition.setJobExecutorClassName(childElement.getText());
            }
            else {
                if (!"properties".equals(childElement.getTagName())) {
                    continue;
                }
                this.parseProperties(childElement, properties);
            }
        }
        jobAcquisition.setProperties(properties);
        jobAcquisitions.add(jobAcquisition);
    }
    
    public BpmPlatformXml getBpmPlatformXml() {
        return this.bpmPlatformXml;
    }
    
    @Override
    public BpmPlatformXmlParse sourceUrl(final URL url) {
        super.sourceUrl(url);
        return this;
    }
}
