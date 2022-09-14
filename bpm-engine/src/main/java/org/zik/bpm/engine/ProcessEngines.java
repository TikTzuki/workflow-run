// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import java.util.ArrayList;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.Closeable;

import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.ProcessEngineInfo;
import org.zik.bpm.engine.impl.util.IoUtil;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.ProcessEngineInfoImpl;
import java.util.Iterator;
import java.util.Set;
import java.util.Enumeration;
import java.net.URL;
import java.util.HashSet;
import java.io.IOException;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public abstract class ProcessEngines
{
    private static final ProcessEngineLogger LOG;
    public static final String NAME_DEFAULT = "default";
    protected static boolean isInitialized;
    protected static Map<String, ProcessEngine> processEngines;
    protected static Map<String, ProcessEngineInfo> processEngineInfosByName;
    protected static Map<String, ProcessEngineInfo> processEngineInfosByResourceUrl;
    protected static List<ProcessEngineInfo> processEngineInfos;
    
    public static synchronized void init() {
        init(true);
    }
    
    public static synchronized void init(final boolean forceCreate) {
        if (!ProcessEngines.isInitialized) {
            if (ProcessEngines.processEngines == null) {
                ProcessEngines.processEngines = new HashMap<String, ProcessEngine>();
            }
            final ClassLoader classLoader = ReflectUtil.getClassLoader();
            Enumeration<URL> resources = null;
            try {
                resources = classLoader.getResources("camunda.cfg.xml");
            }
            catch (IOException e2) {
                try {
                    resources = classLoader.getResources("activiti.cfg.xml");
                }
                catch (IOException ex) {
                    if (forceCreate) {
                        throw new ProcessEngineException("problem retrieving camunda.cfg.xml and activiti.cfg.xml resources on the classpath: " + System.getProperty("java.class.path"), ex);
                    }
                    return;
                }
            }
            final Set<URL> configUrls = new HashSet<URL>();
            while (resources.hasMoreElements()) {
                configUrls.add(resources.nextElement());
            }
            for (final URL resource : configUrls) {
                initProcessEngineFromResource(resource);
            }
            try {
                resources = classLoader.getResources("activiti-context.xml");
            }
            catch (IOException e) {
                if (forceCreate) {
                    throw new ProcessEngineException("problem retrieving activiti-context.xml resources on the classpath: " + System.getProperty("java.class.path"), e);
                }
                return;
            }
            while (resources.hasMoreElements()) {
                final URL resource2 = resources.nextElement();
                initProcessEngineFromSpringResource(resource2);
            }
            ProcessEngines.isInitialized = true;
        }
        else {
            ProcessEngines.LOG.processEngineAlreadyInitialized();
        }
    }
    
    protected static void initProcessEngineFromSpringResource(final URL resource) {
        try {
            final Class<?> springConfigurationHelperClass = ReflectUtil.loadClass("org.camunda.bpm.engine.spring.SpringConfigurationHelper");
            final Method method = springConfigurationHelperClass.getMethod("buildProcessEngine", URL.class);
            final ProcessEngine processEngine = (ProcessEngine)method.invoke(null, resource);
            final String processEngineName = processEngine.getName();
            final ProcessEngineInfo processEngineInfo = new ProcessEngineInfoImpl(processEngineName, resource.toString(), null);
            ProcessEngines.processEngineInfosByName.put(processEngineName, processEngineInfo);
            ProcessEngines.processEngineInfosByResourceUrl.put(resource.toString(), processEngineInfo);
        }
        catch (Exception e) {
            throw new ProcessEngineException("couldn't initialize process engine from spring configuration resource " + resource.toString() + ": " + e.getMessage(), e);
        }
    }
    
    public static void registerProcessEngine(final ProcessEngine processEngine) {
        ProcessEngines.processEngines.put(processEngine.getName(), processEngine);
    }
    
    public static void unregister(final ProcessEngine processEngine) {
        ProcessEngines.processEngines.remove(processEngine.getName());
    }
    
    private static ProcessEngineInfo initProcessEngineFromResource(final URL resourceUrl) {
        ProcessEngineInfo processEngineInfo = ProcessEngines.processEngineInfosByResourceUrl.get(resourceUrl);
        if (processEngineInfo != null) {
            ProcessEngines.processEngineInfos.remove(processEngineInfo);
            if (processEngineInfo.getException() == null) {
                final String processEngineName = processEngineInfo.getName();
                ProcessEngines.processEngines.remove(processEngineName);
                ProcessEngines.processEngineInfosByName.remove(processEngineName);
            }
            ProcessEngines.processEngineInfosByResourceUrl.remove(processEngineInfo.getResourceUrl());
        }
        final String resourceUrlString = resourceUrl.toString();
        try {
            ProcessEngines.LOG.initializingProcessEngineForResource(resourceUrl);
            final ProcessEngine processEngine = buildProcessEngine(resourceUrl);
            final String processEngineName2 = processEngine.getName();
            ProcessEngines.LOG.initializingProcessEngine(processEngine.getName());
            processEngineInfo = new ProcessEngineInfoImpl(processEngineName2, resourceUrlString, null);
            ProcessEngines.processEngines.put(processEngineName2, processEngine);
            ProcessEngines.processEngineInfosByName.put(processEngineName2, processEngineInfo);
        }
        catch (Throwable e) {
            ProcessEngines.LOG.exceptionWhileInitializingProcessengine(e);
            processEngineInfo = new ProcessEngineInfoImpl(null, resourceUrlString, getExceptionString(e));
        }
        ProcessEngines.processEngineInfosByResourceUrl.put(resourceUrlString, processEngineInfo);
        ProcessEngines.processEngineInfos.add(processEngineInfo);
        return processEngineInfo;
    }
    
    private static String getExceptionString(final Throwable e) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
    
    private static ProcessEngine buildProcessEngine(final URL resource) {
        InputStream inputStream = null;
        try {
            inputStream = resource.openStream();
            final ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromInputStream(inputStream);
            return processEngineConfiguration.buildProcessEngine();
        }
        catch (IOException e) {
            throw new ProcessEngineException("couldn't open resource stream: " + e.getMessage(), e);
        }
        finally {
            IoUtil.closeSilently(inputStream);
        }
    }
    
    public static List<ProcessEngineInfo> getProcessEngineInfos() {
        return ProcessEngines.processEngineInfos;
    }
    
    public static ProcessEngineInfo getProcessEngineInfo(final String processEngineName) {
        return ProcessEngines.processEngineInfosByName.get(processEngineName);
    }
    
    public static ProcessEngine getDefaultProcessEngine() {
        return getDefaultProcessEngine(true);
    }
    
    public static ProcessEngine getDefaultProcessEngine(final boolean forceCreate) {
        return getProcessEngine("default", forceCreate);
    }
    
    public static ProcessEngine getProcessEngine(final String processEngineName) {
        return getProcessEngine(processEngineName, true);
    }
    
    public static ProcessEngine getProcessEngine(final String processEngineName, final boolean forceCreate) {
        if (!ProcessEngines.isInitialized) {
            init(forceCreate);
        }
        return ProcessEngines.processEngines.get(processEngineName);
    }
    
    public static ProcessEngineInfo retry(final String resourceUrl) {
        try {
            return initProcessEngineFromResource(new URL(resourceUrl));
        }
        catch (MalformedURLException e) {
            throw new ProcessEngineException("invalid url: " + resourceUrl, e);
        }
    }
    
    public static Map<String, ProcessEngine> getProcessEngines() {
        return ProcessEngines.processEngines;
    }
    
    public static synchronized void destroy() {
        if (ProcessEngines.isInitialized) {
            final Map<String, ProcessEngine> engines = new HashMap<String, ProcessEngine>(ProcessEngines.processEngines);
            ProcessEngines.processEngines = new HashMap<String, ProcessEngine>();
            for (final String processEngineName : engines.keySet()) {
                final ProcessEngine processEngine = engines.get(processEngineName);
                try {
                    processEngine.close();
                }
                catch (Exception e) {
                    ProcessEngines.LOG.exceptionWhileClosingProcessEngine((processEngineName == null) ? "the default process engine" : ("process engine " + processEngineName), e);
                }
            }
            ProcessEngines.processEngineInfosByName.clear();
            ProcessEngines.processEngineInfosByResourceUrl.clear();
            ProcessEngines.processEngineInfos.clear();
            ProcessEngines.isInitialized = false;
        }
    }
    
    static {
        LOG = ProcessEngineLogger.INSTANCE;
        ProcessEngines.isInitialized = false;
        ProcessEngines.processEngines = new HashMap<String, ProcessEngine>();
        ProcessEngines.processEngineInfosByName = new HashMap<String, ProcessEngineInfo>();
        ProcessEngines.processEngineInfosByResourceUrl = new HashMap<String, ProcessEngineInfo>();
        ProcessEngines.processEngineInfos = new ArrayList<ProcessEngineInfo>();
    }
}
