// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.tomcat.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.net.MalformedURLException;
import java.io.File;
import java.net.URL;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.deployment.AbstractParseBpmPlatformXmlStep;

public class TomcatParseBpmPlatformXmlStep extends AbstractParseBpmPlatformXmlStep
{
    private static final ContainerIntegrationLogger LOG;
    public static final String CATALINA_BASE = "catalina.base";
    public static final String CATALINA_HOME = "catalina.home";
    
    @Override
    public URL getBpmPlatformXmlStream(final DeploymentOperation operationcontext) {
        URL fileLocation = this.lookupBpmPlatformXml();
        if (fileLocation == null) {
            fileLocation = this.lookupBpmPlatformXmlFromCatalinaConfDirectory();
        }
        return fileLocation;
    }
    
    public URL lookupBpmPlatformXmlFromCatalinaConfDirectory() {
        String catalinaHome = System.getProperty("catalina.base");
        if (catalinaHome == null) {
            catalinaHome = System.getProperty("catalina.home");
        }
        final String bpmPlatformFileLocation = catalinaHome + File.separator + "conf" + File.separator + "bpm-platform.xml";
        try {
            final URL fileLocation = this.checkValidFileLocation(bpmPlatformFileLocation);
            if (fileLocation != null) {
                TomcatParseBpmPlatformXmlStep.LOG.foundTomcatDeploymentDescriptor(bpmPlatformFileLocation, fileLocation.toString());
            }
            return fileLocation;
        }
        catch (MalformedURLException e) {
            throw TomcatParseBpmPlatformXmlStep.LOG.invalidDeploymentDescriptorLocation(bpmPlatformFileLocation, e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
