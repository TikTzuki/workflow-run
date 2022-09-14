// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.MalformedURLException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.container.impl.metadata.spi.BpmPlatformXml;
import java.net.URL;
import org.zik.bpm.container.impl.metadata.BpmPlatformXmlParser;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.container.impl.spi.DeploymentOperation;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.DeploymentOperationStep;

public abstract class AbstractParseBpmPlatformXmlStep extends DeploymentOperationStep
{
    private static final ContainerIntegrationLogger LOG;
    public static final String BPM_PLATFORM_XML_FILE = "bpm-platform.xml";
    public static final String BPM_PLATFORM_XML_LOCATION = "bpm-platform-xml";
    public static final String BPM_PLATFORM_XML_ENVIRONMENT_VARIABLE = "BPM_PLATFORM_XML";
    public static final String BPM_PLATFORM_XML_SYSTEM_PROPERTY = "bpm.platform.xml";
    public static final String BPM_PLATFORM_XML_RESOURCE_LOCATION = "META-INF/bpm-platform.xml";
    
    @Override
    public String getName() {
        return "Parsing bpm-platform.xml file";
    }
    
    @Override
    public void performOperationStep(final DeploymentOperation operationContext) {
        final URL bpmPlatformXmlSource = this.getBpmPlatformXmlStream(operationContext);
        EnsureUtil.ensureNotNull("Unable to find bpm-platform.xml. This file is necessary for deploying the Camunda Platform", "bpmPlatformXmlSource", bpmPlatformXmlSource);
        final BpmPlatformXml bpmPlatformXml = new BpmPlatformXmlParser().createParse().sourceUrl(bpmPlatformXmlSource).execute().getBpmPlatformXml();
        operationContext.addAttachment("bpmPlatformXml", bpmPlatformXml);
    }
    
    public URL checkValidBpmPlatformXmlResourceLocation(String url) {
        url = this.autoCompleteUrl(url);
        URL fileLocation = null;
        try {
            fileLocation = this.checkValidUrlLocation(url);
            if (fileLocation == null) {
                fileLocation = this.checkValidFileLocation(url);
            }
        }
        catch (MalformedURLException e) {
            throw new ProcessEngineException("'" + url + "' is not a valid Camunda Platform configuration resource location.", e);
        }
        return fileLocation;
    }
    
    public String autoCompleteUrl(String url) {
        if (url != null) {
            AbstractParseBpmPlatformXmlStep.LOG.debugAutoCompleteUrl(url);
            if (!url.endsWith("bpm-platform.xml")) {
                String appender;
                if (url.contains("/")) {
                    appender = "/";
                }
                else {
                    appender = "\\";
                }
                if (!url.endsWith("/") && !url.endsWith("\\\\")) {
                    url += appender;
                }
                url += "bpm-platform.xml";
            }
            AbstractParseBpmPlatformXmlStep.LOG.debugAutoCompletedUrl(url);
        }
        return url;
    }
    
    public URL checkValidUrlLocation(final String url) throws MalformedURLException {
        if (url == null || url.isEmpty()) {
            return null;
        }
        final Pattern urlPattern = Pattern.compile("^(https?://).*/bpm-platform\\.xml$", 66);
        final Matcher urlMatcher = urlPattern.matcher(url);
        if (urlMatcher.matches()) {
            return new URL(url);
        }
        return null;
    }
    
    public URL checkValidFileLocation(final String url) throws MalformedURLException {
        if (url == null || url.isEmpty()) {
            return null;
        }
        final Pattern filePattern = Pattern.compile("^(/|[A-z]://?|[A-z]:\\\\).*[/|\\\\]bpm-platform\\.xml$", 66);
        final Matcher fileMatcher = filePattern.matcher(url);
        if (fileMatcher.matches()) {
            final File configurationLocation = new File(url);
            if (configurationLocation.isAbsolute() && configurationLocation.exists()) {
                return configurationLocation.toURI().toURL();
            }
        }
        return null;
    }
    
    public URL lookupBpmPlatformXmlLocationFromJndi() {
        final String jndi = "java:comp/env/bpm-platform-xml";
        try {
            final String bpmPlatformXmlLocation = InitialContext.doLookup(jndi);
            final URL fileLocation = this.checkValidBpmPlatformXmlResourceLocation(bpmPlatformXmlLocation);
            if (fileLocation != null) {
                AbstractParseBpmPlatformXmlStep.LOG.foundConfigJndi(jndi, fileLocation.toString());
            }
            return fileLocation;
        }
        catch (NamingException e) {
            AbstractParseBpmPlatformXmlStep.LOG.debugExceptionWhileGettingConfigFromJndi(jndi, e);
            return null;
        }
    }
    
    public URL lookupBpmPlatformXmlLocationFromEnvironmentVariable() {
        String bpmPlatformXmlLocation = System.getenv("BPM_PLATFORM_XML");
        String logStatement = "environment variable [BPM_PLATFORM_XML]";
        if (bpmPlatformXmlLocation == null) {
            bpmPlatformXmlLocation = System.getProperty("bpm.platform.xml");
            logStatement = "system property [bpm.platform.xml]";
        }
        final URL fileLocation = this.checkValidBpmPlatformXmlResourceLocation(bpmPlatformXmlLocation);
        if (fileLocation != null) {
            AbstractParseBpmPlatformXmlStep.LOG.foundConfigAtLocation(logStatement, fileLocation.toString());
        }
        return fileLocation;
    }
    
    public URL lookupBpmPlatformXmlFromClassPath(final String resourceLocation) {
        final URL fileLocation = ClassLoaderUtil.getClassloader(this.getClass()).getResource(resourceLocation);
        if (fileLocation != null) {
            AbstractParseBpmPlatformXmlStep.LOG.foundConfigAtLocation(resourceLocation, fileLocation.toString());
        }
        return fileLocation;
    }
    
    public URL lookupBpmPlatformXmlFromClassPath() {
        return this.lookupBpmPlatformXmlFromClassPath("META-INF/bpm-platform.xml");
    }
    
    public URL lookupBpmPlatformXml() {
        URL fileLocation = this.lookupBpmPlatformXmlLocationFromJndi();
        if (fileLocation == null) {
            fileLocation = this.lookupBpmPlatformXmlLocationFromEnvironmentVariable();
        }
        if (fileLocation == null) {
            fileLocation = this.lookupBpmPlatformXmlFromClassPath();
        }
        return fileLocation;
    }
    
    public abstract URL getBpmPlatformXmlStream(final DeploymentOperation p0);
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
