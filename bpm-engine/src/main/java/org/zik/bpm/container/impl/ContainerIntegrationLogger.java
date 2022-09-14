// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl;

import java.net.MalformedURLException;
import java.util.concurrent.RejectedExecutionException;
import javax.management.ObjectName;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.Collection;
import javax.naming.NamingException;
import java.lang.reflect.Type;
import org.jboss.vfs.VirtualFile;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class ContainerIntegrationLogger extends ProcessEngineLogger
{
    public ProcessEngineException couldNotInstantiateJobExecutorClass(final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("001", "Could not instantiate job executor class", new Object[0]), e);
    }
    
    public ProcessEngineException couldNotLoadJobExecutorClass(final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("002", "Could not load job executor class", new Object[0]), e);
    }
    
    public void exceptionWhileStopping(final String serviceType, final String serviceName, final Throwable t) {
        this.logWarn("003", "Exception while stopping {} '{}': {}", new Object[] { serviceType, serviceName, t.getMessage(), t });
    }
    
    public void debugRootPath(final String urlPath) {
        this.logDebug("004", "Rootpath is {}", new Object[] { urlPath });
    }
    
    public ProcessEngineException cannotDecodePathName(final UnsupportedEncodingException e) {
        return new ProcessEngineException(this.exceptionMessage("005", "Could not decode pathname using utf-8 decoder.", new Object[0]), e);
    }
    
    public ProcessEngineException exceptionWhileScanning(final String file, final IOException e) {
        return new ProcessEngineException(this.exceptionMessage("006", "IOException while scanning archive '{}'.", new Object[] { file }), e);
    }
    
    public void debugDiscoveredResource(final String resourcePath) {
        this.logDebug("007", "Discovered resource {}", new Object[] { resourcePath });
    }
    
    public ProcessEngineException cannotOpenFileInputStream(final String absolutePath, final IOException e) {
        return new ProcessEngineException(this.exceptionMessage("008", "Cannot not open file for reading: {}.", new Object[] { e.getMessage() }), e);
    }
    
    public ProcessEngineException couldNotGetResource(final String strippedPaResourceRootPath, final ClassLoader cl, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("009", "Could not load resources at '{}' using classloaded '{}'", new Object[] { strippedPaResourceRootPath, cl }), e);
    }
    
    public void cannotFindResourcesForPath(final String resourceRootPath, final ClassLoader classLoader) {
        this.logWarn("010", "Could not find any resources for process archive resource root path '{}' using classloader '{}'.", new Object[] { resourceRootPath, classLoader });
    }
    
    public ProcessEngineException exceptionWhileGettingVirtualFolder(final URL url, final URISyntaxException e) {
        return new ProcessEngineException(this.exceptionMessage("011", "Could not load virtual file for url '{}'", new Object[] { url }), e);
    }
    
    public void cannotScanVfsRoot(final VirtualFile processArchiveRoot, final IOException e) {
        this.logWarn("012", "Cannot scan process archive root {}", new Object[] { processArchiveRoot, e });
    }
    
    public void cannotReadInputStreamForFile(final String resourceName, final VirtualFile processArchiveRoot, final IOException e) {
        this.logWarn("013", "Could not read input stream of file '{}' from process archive '{}'.", new Object[] { resourceName, processArchiveRoot, e });
    }
    
    public ProcessEngineException exceptionWhileLoadingCpRoots(final String strippedPaResourceRootPath, final ClassLoader classLoader, final IOException e) {
        return new ProcessEngineException(this.exceptionMessage("014", "Could not load resources at '{}' using classloaded '{}'", new Object[] { strippedPaResourceRootPath, classLoader }), e);
    }
    
    public ProcessEngineException unsuppoertedParameterType(final Type parameterType) {
        return new ProcessEngineException(this.exceptionMessage("015", "Unsupported parametertype {}", new Object[] { parameterType }));
    }
    
    public void debugAutoCompleteUrl(final String url) {
        this.logDebug("016", "Autocompleting url : [{}]", new Object[] { url });
    }
    
    public void debugAutoCompletedUrl(final String url) {
        this.logDebug("017", "Autocompleted url : [{}]", new Object[] { url });
    }
    
    public void foundConfigJndi(final String jndi, final String string) {
        this.logInfo("018", "Found Camunda Platform configuration in JNDI [{}] at {}", new Object[] { jndi, string });
    }
    
    public void debugExceptionWhileGettingConfigFromJndi(final String jndi, final NamingException e) {
        this.logDebug("019", "Failed to look up Camunda Platform configuration in JNDI [{}]: {}", new Object[] { jndi, e });
    }
    
    public void foundConfigAtLocation(final String logStatement, final String string) {
        this.logInfo("020", "Found Camunda Platform configuration through {}  at {} ", new Object[] { logStatement, string });
    }
    
    public void notCreatingPaDeployment(final String name) {
        this.logInfo("021", "Not creating a deployment for process archive '{}': no resources provided.", new Object[] { name });
    }
    
    public IllegalArgumentException illegalValueForResumePreviousByProperty(final String string) {
        return new IllegalArgumentException(this.exceptionMessage("022", string, new Object[0]));
    }
    
    public void deploymentSummary(final Collection<String> deploymentResourceNames, final String deploymentName) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Deployment summary for process archive '" + deploymentName + "': \n");
        builder.append("\n");
        for (final String resourceName : deploymentResourceNames) {
            builder.append("        " + resourceName);
            builder.append("\n");
        }
        this.logInfo("023", builder.toString(), new Object[0]);
    }
    
    public void foundProcessesXmlFile(final String string) {
        this.logInfo("024", "Found processes.xml file at {}", new Object[] { string });
    }
    
    public void emptyProcessesXml() {
        this.logInfo("025", "Detected empty processes.xml file, using default values", new Object[0]);
    }
    
    public void noProcessesXmlForPa(final String paName) {
        this.logInfo("026", "No processes.xml file found in process application '{}'", new Object[] { paName });
    }
    
    public ProcessEngineException exceptionWhileReadingProcessesXml(final String deploymentDescriptor, final IOException e) {
        return new ProcessEngineException(this.exceptionMessage("027", "Exception while reading {}", new Object[] { deploymentDescriptor }), e);
    }
    
    public ProcessEngineException exceptionWhileInvokingPaLifecycleCallback(final String methodName, final String paName, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("028", "Exception while invoking {} on Process Application {}: {}", new Object[] { methodName, paName, e.getMessage() }), e);
    }
    
    public void debugFoundPaLifecycleCallbackMethod(final String methodName, final String paName) {
        this.logDebug("029", "Found Process Application lifecycle callback method {} in application {}", new Object[] { methodName, paName });
    }
    
    public void debugPaLifecycleMethodNotFound(final String methodName, final String paName) {
        this.logDebug("030", "Process Application lifecycle callback method {} not found in application {}", new Object[] { methodName, paName });
    }
    
    public ProcessEngineException cannotLoadConfigurationClass(final String className, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("031", "Failed to load configuration class '{}': {}", new Object[] { className, e.getMessage() }), e);
    }
    
    public ProcessEngineException configurationClassHasWrongType(final String className, final Class<?> expectedType, final ClassCastException e) {
        return new ProcessEngineException(this.exceptionMessage("032", "Class '{}' has wrong type. Must extend {}", new Object[] { expectedType.getName() }), e);
    }
    
    public void timeoutDuringShutdownOfThreadPool(final int i, final TimeUnit seconds) {
        this.logError("033", "Timeout during shutdown of managed thread pool. The current running tasks could not end within {} {} after shutdown operation.", new Object[] { i, seconds });
    }
    
    public void interruptedWhileShuttingDownThreadPool(final InterruptedException e) {
        this.logError("034", "Interrupted while shutting down thread pool", new Object[] { e });
    }
    
    public ProcessEngineException cannotRegisterService(final ObjectName serviceName, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("035", "Cannot register service {} with MBean Server: {}", new Object[] { serviceName, e.getMessage() }), e);
    }
    
    public ProcessEngineException cannotComposeNameFor(final String serviceName, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("036", "Cannot compose name for service {}: {}", new Object[] { serviceName, e.getMessage() }), e);
    }
    
    public ProcessEngineException exceptionWhileUnregisteringService(final String canonicalName, final Throwable t) {
        return new ProcessEngineException(this.exceptionMessage("037", "Exception while unregistering service {} with the MBeanServer: {}", new Object[] { canonicalName, t }), t);
    }
    
    public ProcessEngineException unknownExceptionWhileParsingDeploymentDescriptor(final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("038", "Unknown exception while parsing deployment camunda descriptor: {}", new Object[] { e.getMessage() }), e);
    }
    
    public ProcessEngineException cannotSetValueForProperty(final String key, final String canonicalName, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("039", "Cannot set property '{}' on instance of class '{}'", new Object[] { key, canonicalName }), e);
    }
    
    public ProcessEngineException cannotFindSetterForProperty(final String key, final String canonicalName) {
        return new ProcessEngineException(this.exceptionMessage("040", "Cannot find setter for property '{}' on class '{}'", new Object[] { key, canonicalName }));
    }
    
    public void debugPerformOperationStep(final String stepName) {
        this.logDebug("041", "Performing deployment operation step '{}'", new Object[] { stepName });
    }
    
    public void debugSuccessfullyPerformedOperationStep(final String stepName) {
        this.logDebug("041", "Successfully performed deployment operation step '{}'", new Object[] { stepName });
    }
    
    public void exceptionWhileRollingBackOperation(final Exception e) {
        this.logError("042", "Exception while rolling back operation", new Object[] { e });
    }
    
    public ProcessEngineException exceptionWhilePerformingOperationStep(final String opName, final String stepName, final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("043", "Exception while performing '{}' => '{}': {}", new Object[] { opName, stepName, e.getMessage() }), e);
    }
    
    public void exceptionWhilePerformingOperationStep(final String name, final Exception e) {
        this.logError("044", "Exception while performing '{}': {}", new Object[] { name, e.getMessage(), e });
    }
    
    public void debugRejectedExecutionException(final RejectedExecutionException e) {
        this.logDebug("045", "RejectedExecutionException while scheduling work", new Object[] { e });
    }
    
    public void foundTomcatDeploymentDescriptor(final String bpmPlatformFileLocation, final String fileLocation) {
        this.logInfo("046", "Found Camunda Platform configuration in CATALINA_BASE/CATALINA_HOME conf directory [{}] at '{}'", new Object[] { bpmPlatformFileLocation, fileLocation });
    }
    
    public ProcessEngineException invalidDeploymentDescriptorLocation(final String bpmPlatformFileLocation, final MalformedURLException e) {
        throw new ProcessEngineException(this.exceptionMessage("047", "'{} is not a valid Camunda Platform configuration resource location.", new Object[] { bpmPlatformFileLocation }), e);
    }
    
    public void camundaBpmPlatformSuccessfullyStarted(final String serverInfo) {
        this.logInfo("048", "Camunda Platform sucessfully started at '{}'.", new Object[] { serverInfo });
    }
    
    public void camundaBpmPlatformStopped(final String serverInfo) {
        this.logInfo("049", "Camunda Platform stopped at '{}'", new Object[] { serverInfo });
    }
    
    public void paDeployed(final String name) {
        this.logInfo("050", "Process application {} successfully deployed", new Object[] { name });
    }
    
    public void paUndeployed(final String name) {
        this.logInfo("051", "Process application {} undeployed", new Object[] { name });
    }
}
