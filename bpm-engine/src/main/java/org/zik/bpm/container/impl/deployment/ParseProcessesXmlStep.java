// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.application.impl.metadata.ProcessesXmlParser;
import java.io.InputStream;
import java.io.Closeable;
import org.zik.bpm.engine.impl.util.IoUtil;
import org.zik.bpm.application.ProcessApplication;
import java.util.Enumeration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import org.zik.bpm.application.impl.metadata.spi.ProcessesXml;
import java.net.URL;
import java.util.Map;
import org.zik.bpm.application.AbstractProcessApplication;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public class ParseProcessesXmlStep extends DeploymentOperationStep
{
    private static final ContainerIntegrationLogger LOG;
    
    @Override
    public String getName() {
        return "Parse processes.xml deployment descriptor files.";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final AbstractProcessApplication processApplication = operationContext.getAttachment("processApplication");
        final Map<URL, ProcessesXml> parsedFiles = this.parseProcessesXmlFiles(processApplication);
        operationContext.addAttachment("processesXmlList", parsedFiles);
    }
    
    protected Map<URL, ProcessesXml> parseProcessesXmlFiles(final AbstractProcessApplication processApplication) {
        final String[] deploymentDescriptors = this.getDeploymentDescriptorLocations(processApplication);
        final List<URL> processesXmlUrls = this.getProcessesXmlUrls(deploymentDescriptors, processApplication);
        final Map<URL, ProcessesXml> parsedFiles = new HashMap<URL, ProcessesXml>();
        for (final URL url : processesXmlUrls) {
            ParseProcessesXmlStep.LOG.foundProcessesXmlFile(url.toString());
            if (this.isEmptyFile(url)) {
                parsedFiles.put(url, ProcessesXml.EMPTY_PROCESSES_XML);
                ParseProcessesXmlStep.LOG.emptyProcessesXml();
            }
            else {
                parsedFiles.put(url, this.parseProcessesXml(url));
            }
        }
        if (parsedFiles.isEmpty()) {
            ParseProcessesXmlStep.LOG.noProcessesXmlForPa(processApplication.getName());
        }
        return parsedFiles;
    }
    
    protected List<URL> getProcessesXmlUrls(final String[] deploymentDescriptors, final AbstractProcessApplication processApplication) {
        final ClassLoader processApplicationClassloader = processApplication.getProcessApplicationClassloader();
        final List<URL> result = new ArrayList<URL>();
        for (final String deploymentDescriptor : deploymentDescriptors) {
            Enumeration<URL> processesXmlFileLocations = null;
            try {
                processesXmlFileLocations = processApplicationClassloader.getResources(deploymentDescriptor);
            }
            catch (IOException e) {
                throw ParseProcessesXmlStep.LOG.exceptionWhileReadingProcessesXml(deploymentDescriptor, e);
            }
            while (processesXmlFileLocations.hasMoreElements()) {
                result.add(processesXmlFileLocations.nextElement());
            }
        }
        return result;
    }
    
    protected String[] getDeploymentDescriptorLocations(final AbstractProcessApplication processApplication) {
        final ProcessApplication annotation = processApplication.getClass().getAnnotation(ProcessApplication.class);
        if (annotation == null) {
            return new String[] { "META-INF/processes.xml" };
        }
        return annotation.deploymentDescriptors();
    }
    
    protected boolean isEmptyFile(final URL url) {
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
            return inputStream.available() == 0;
        }
        catch (IOException e) {
            throw ParseProcessesXmlStep.LOG.exceptionWhileReadingProcessesXml(url.toString(), e);
        }
        finally {
            IoUtil.closeSilently(inputStream);
        }
    }
    
    protected ProcessesXml parseProcessesXml(final URL url) {
        final ProcessesXmlParser processesXmlParser = new ProcessesXmlParser();
        final ProcessesXml processesXml = processesXmlParser.createParse().sourceUrl(url).execute().getProcessesXml();
        return processesXml;
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
