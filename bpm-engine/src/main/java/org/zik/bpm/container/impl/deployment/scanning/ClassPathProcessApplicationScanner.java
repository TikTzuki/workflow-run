// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.scanning;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.io.Closeable;
import org.zik.bpm.engine.impl.util.IoUtil;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.deployment.scanning.spi.ProcessApplicationScanner;

public class ClassPathProcessApplicationScanner implements ProcessApplicationScanner
{
    private static final ContainerIntegrationLogger LOG;
    
    @Override
    public Map<String, byte[]> findResources(final ClassLoader classLoader, final String paResourceRootPath, final URL metaFileUrl) {
        return this.findResources(classLoader, paResourceRootPath, metaFileUrl, null);
    }
    
    @Override
    public Map<String, byte[]> findResources(final ClassLoader classLoader, final String paResourceRootPath, final URL metaFileUrl, final String[] additionalResourceSuffixes) {
        final Map<String, byte[]> resourceMap = new HashMap<String, byte[]>();
        this.scanPaResourceRootPath(classLoader, metaFileUrl, paResourceRootPath, additionalResourceSuffixes, resourceMap);
        return resourceMap;
    }
    
    public void scanPaResourceRootPath(final ClassLoader classLoader, final URL metaFileUrl, final String paResourceRootPath, final Map<String, byte[]> resourceMap) {
        this.scanPaResourceRootPath(classLoader, metaFileUrl, paResourceRootPath, null, resourceMap);
    }
    
    public void scanPaResourceRootPath(final ClassLoader classLoader, final URL metaFileUrl, final String paResourceRootPath, final String[] additionalResourceSuffixes, final Map<String, byte[]> resourceMap) {
        if (paResourceRootPath != null && !paResourceRootPath.startsWith("pa:")) {
            String strippedPath = paResourceRootPath.replace("classpath:", "");
            strippedPath = (strippedPath.endsWith("/") ? strippedPath : (strippedPath + "/"));
            final Enumeration<URL> resourceRoots = this.loadClasspathResourceRoots(classLoader, strippedPath);
            while (resourceRoots.hasMoreElements()) {
                final URL resourceRoot = resourceRoots.nextElement();
                this.scanUrl(resourceRoot, strippedPath, false, additionalResourceSuffixes, resourceMap);
            }
        }
        else {
            String strippedPaResourceRootPath = null;
            if (paResourceRootPath != null) {
                strippedPaResourceRootPath = paResourceRootPath.replace("pa:", "");
                strippedPaResourceRootPath = (strippedPaResourceRootPath.endsWith("/") ? strippedPaResourceRootPath : (strippedPaResourceRootPath + "/"));
            }
            this.scanUrl(metaFileUrl, strippedPaResourceRootPath, true, additionalResourceSuffixes, resourceMap);
        }
    }
    
    protected void scanUrl(final URL url, final String paResourceRootPath, final boolean isPaLocal, final String[] additionalResourceSuffixes, final Map<String, byte[]> resourceMap) {
        String urlPath = url.toExternalForm();
        if (isPaLocal) {
            if (urlPath.startsWith("file:") || urlPath.startsWith("jar:") || urlPath.startsWith("wsjar:") || urlPath.startsWith("zip:")) {
                urlPath = url.getPath();
                final int withinArchive = urlPath.indexOf(33);
                if (withinArchive != -1) {
                    urlPath = urlPath.substring(0, withinArchive);
                }
                else {
                    final File file = new File(urlPath);
                    urlPath = file.getParentFile().getParent();
                }
            }
        }
        else if (urlPath.startsWith("file:") || urlPath.startsWith("jar:") || urlPath.startsWith("wsjar:") || urlPath.startsWith("zip:")) {
            urlPath = url.getPath();
            final int withinArchive = urlPath.indexOf(33);
            if (withinArchive != -1) {
                urlPath = urlPath.substring(0, withinArchive);
            }
        }
        try {
            urlPath = URLDecoder.decode(urlPath, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw ClassPathProcessApplicationScanner.LOG.cannotDecodePathName(e);
        }
        ClassPathProcessApplicationScanner.LOG.debugRootPath(urlPath);
        this.scanPath(urlPath, paResourceRootPath, isPaLocal, additionalResourceSuffixes, resourceMap);
    }
    
    protected void scanPath(String urlPath, final String paResourceRootPath, final boolean isPaLocal, final String[] additionalResourceSuffixes, final Map<String, byte[]> resourceMap) {
        if (urlPath.startsWith("file:")) {
            urlPath = urlPath.substring(5);
        }
        if (urlPath.indexOf(33) > 0) {
            urlPath = urlPath.substring(0, urlPath.indexOf(33));
        }
        final File file = new File(urlPath);
        if (file.isDirectory()) {
            final String path = file.getPath();
            final String rootPath = path.endsWith(File.separator) ? path : (path + File.separator);
            this.handleDirectory(file, rootPath, paResourceRootPath, paResourceRootPath, isPaLocal, additionalResourceSuffixes, resourceMap);
        }
        else {
            this.handleArchive(file, paResourceRootPath, additionalResourceSuffixes, resourceMap);
        }
    }
    
    protected void handleArchive(final File file, final String paResourceRootPath, final String[] additionalResourceSuffixes, final Map<String, byte[]> resourceMap) {
        try {
            final ZipFile zipFile = new ZipFile(file);
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
                final String modelFileName = zipEntry.getName();
                if (ProcessApplicationScanningUtil.isDeployable(modelFileName, additionalResourceSuffixes) && this.isBelowPath(modelFileName, paResourceRootPath)) {
                    String resourceName = modelFileName;
                    if (paResourceRootPath != null && paResourceRootPath.length() > 0) {
                        resourceName = modelFileName.replaceFirst(paResourceRootPath, "");
                    }
                    this.addResource(zipFile.getInputStream(zipEntry), resourceMap, file.getName() + "!", resourceName);
                    final Enumeration<? extends ZipEntry> entries2 = zipFile.entries();
                    while (entries2.hasMoreElements()) {
                        final ZipEntry zipEntry2 = (ZipEntry)entries2.nextElement();
                        String diagramFileName = zipEntry2.getName();
                        if (ProcessApplicationScanningUtil.isDiagram(diagramFileName, modelFileName)) {
                            if (paResourceRootPath != null && paResourceRootPath.length() > 0) {
                                diagramFileName = diagramFileName.replaceFirst(paResourceRootPath, "");
                            }
                            this.addResource(zipFile.getInputStream(zipEntry), resourceMap, file.getName() + "!", diagramFileName);
                        }
                    }
                }
            }
            zipFile.close();
        }
        catch (IOException e) {
            throw ClassPathProcessApplicationScanner.LOG.exceptionWhileScanning(file.getAbsolutePath(), e);
        }
    }
    
    protected void handleDirectory(final File directory, final String rootPath, String localPath, final String paResourceRootPath, final boolean isPaLocal, final String[] additionalResourceSuffixes, final Map<String, byte[]> resourceMap) {
        final File[] paths = directory.listFiles();
        String currentPathSegment = localPath;
        if (localPath != null && localPath.length() > 0) {
            if (localPath.indexOf(47) > 0) {
                currentPathSegment = localPath.substring(0, localPath.indexOf(47));
                localPath = localPath.substring(localPath.indexOf(47) + 1, localPath.length());
            }
            else {
                localPath = null;
            }
        }
        for (final File path : paths) {
            if (isPaLocal && currentPathSegment != null && currentPathSegment.length() > 0) {
                if (path.isDirectory() && path.getName().equals(currentPathSegment)) {
                    this.handleDirectory(path, rootPath, localPath, paResourceRootPath, isPaLocal, additionalResourceSuffixes, resourceMap);
                }
            }
            else {
                final String modelFileName = path.getPath();
                if (!path.isDirectory() && ProcessApplicationScanningUtil.isDeployable(modelFileName, additionalResourceSuffixes)) {
                    this.addResource(path, resourceMap, paResourceRootPath, modelFileName.replace(rootPath, "").replace("\\", "/"));
                    for (final File file : paths) {
                        final String diagramFileName = file.getPath();
                        if (!path.isDirectory() && ProcessApplicationScanningUtil.isDiagram(diagramFileName, modelFileName)) {
                            this.addResource(file, resourceMap, paResourceRootPath, diagramFileName.replace(rootPath, "").replace("\\", "/"));
                        }
                    }
                }
                else if (path.isDirectory()) {
                    this.handleDirectory(path, rootPath, localPath, paResourceRootPath, isPaLocal, additionalResourceSuffixes, resourceMap);
                }
            }
        }
    }
    
    protected void addResource(final Object source, final Map<String, byte[]> resourceMap, final String resourceRootPath, final String resourceName) {
        final String resourcePath = ((resourceRootPath == null) ? "" : resourceRootPath).concat(resourceName);
        ClassPathProcessApplicationScanner.LOG.debugDiscoveredResource(resourcePath);
        InputStream inputStream = null;
        try {
            Label_0075: {
                if (source instanceof File) {
                    try {
                        inputStream = new FileInputStream((File)source);
                        break Label_0075;
                    }
                    catch (IOException e) {
                        throw ClassPathProcessApplicationScanner.LOG.cannotOpenFileInputStream(((File)source).getAbsolutePath(), e);
                    }
                }
                inputStream = (InputStream)source;
            }
            final byte[] bytes = IoUtil.readInputStream(inputStream, resourcePath);
            resourceMap.put(resourceName, bytes);
        }
        finally {
            if (inputStream != null) {
                IoUtil.closeSilently(inputStream);
            }
        }
    }
    
    protected Enumeration<URL> loadClasspathResourceRoots(final ClassLoader classLoader, final String strippedPaResourceRootPath) {
        Enumeration<URL> resourceRoots;
        try {
            resourceRoots = classLoader.getResources(strippedPaResourceRootPath);
        }
        catch (IOException e) {
            throw ClassPathProcessApplicationScanner.LOG.couldNotGetResource(strippedPaResourceRootPath, classLoader, e);
        }
        return resourceRoots;
    }
    
    protected boolean isBelowPath(final String processFileName, final String paResourceRootPath) {
        return paResourceRootPath == null || paResourceRootPath.length() == 0 || processFileName.startsWith(paResourceRootPath);
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
