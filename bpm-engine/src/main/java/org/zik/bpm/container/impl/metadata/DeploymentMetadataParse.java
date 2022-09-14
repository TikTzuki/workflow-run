// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.Map;
import org.zik.bpm.container.impl.metadata.spi.ProcessEnginePluginXml;
import java.util.ArrayList;
import java.util.HashMap;
import org.zik.bpm.container.impl.metadata.spi.ProcessEngineXml;
import java.util.List;
import org.zik.bpm.engine.impl.util.xml.Element;
import org.zik.bpm.engine.impl.util.xml.Parser;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.engine.impl.util.xml.Parse;

public abstract class DeploymentMetadataParse extends Parse
{
    private static final ContainerIntegrationLogger LOG;
    
    public DeploymentMetadataParse(final Parser parser) {
        super(parser);
    }
    
    @Override
    public Parse execute() {
        super.execute();
        try {
            this.parseRootElement();
        }
        catch (Exception e) {
            throw DeploymentMetadataParse.LOG.unknownExceptionWhileParsingDeploymentDescriptor(e);
        }
        finally {
            if (this.hasWarnings()) {
                this.logWarnings();
            }
            if (this.hasErrors()) {
                this.throwExceptionForErrors();
            }
        }
        return this;
    }
    
    protected abstract void parseRootElement();
    
    protected void parseProcessEngine(final Element element, final List<ProcessEngineXml> parsedProcessEngines) {
        final ProcessEngineXmlImpl processEngine = new ProcessEngineXmlImpl();
        processEngine.setName(element.attribute("name"));
        final String defaultValue = element.attribute("default");
        if (defaultValue == null || defaultValue.isEmpty()) {
            processEngine.setDefault(false);
        }
        else {
            processEngine.setDefault(Boolean.parseBoolean(defaultValue));
        }
        final Map<String, String> properties = new HashMap<String, String>();
        final List<ProcessEnginePluginXml> plugins = new ArrayList<ProcessEnginePluginXml>();
        for (final Element childElement : element.elements()) {
            if ("configuration".equals(childElement.getTagName())) {
                processEngine.setConfigurationClass(childElement.getText());
            }
            else if ("datasource".equals(childElement.getTagName())) {
                processEngine.setDatasource(childElement.getText());
            }
            else if ("job-acquisition".equals(childElement.getTagName())) {
                processEngine.setJobAcquisitionName(childElement.getText());
            }
            else if ("properties".equals(childElement.getTagName())) {
                this.parseProperties(childElement, properties);
            }
            else {
                if (!"plugins".equals(childElement.getTagName())) {
                    continue;
                }
                this.parseProcessEnginePlugins(childElement, plugins);
            }
        }
        processEngine.setProperties(properties);
        processEngine.setPlugins(plugins);
        parsedProcessEngines.add(processEngine);
    }
    
    protected void parseProcessEnginePlugins(final Element element, final List<ProcessEnginePluginXml> plugins) {
        for (final Element chidElement : element.elements()) {
            if ("plugin".equals(chidElement.getTagName())) {
                this.parseProcessEnginePlugin(chidElement, plugins);
            }
        }
    }
    
    protected void parseProcessEnginePlugin(final Element element, final List<ProcessEnginePluginXml> plugins) {
        final ProcessEnginePluginXmlImpl plugin = new ProcessEnginePluginXmlImpl();
        final Map<String, String> properties = new HashMap<String, String>();
        for (final Element childElement : element.elements()) {
            if ("class".equals(childElement.getTagName())) {
                plugin.setPluginClass(childElement.getText());
            }
            else {
                if (!"properties".equals(childElement.getTagName())) {
                    continue;
                }
                this.parseProperties(childElement, properties);
            }
        }
        plugin.setProperties(properties);
        plugins.add(plugin);
    }
    
    protected void parseProperties(final Element element, final Map<String, String> properties) {
        for (final Element childElement : element.elements()) {
            if ("property".equals(childElement.getTagName())) {
                final String resolved = PropertyHelper.resolveProperty(System.getProperties(), childElement.getText());
                properties.put(childElement.attribute("name"), resolved);
            }
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
