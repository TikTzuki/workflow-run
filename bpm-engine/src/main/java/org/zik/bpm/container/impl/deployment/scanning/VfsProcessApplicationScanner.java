// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.scanning;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.io.InputStream;
import java.io.Closeable;
import org.zik.bpm.engine.impl.util.IoUtil;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;
import org.jboss.vfs.VirtualFileFilter;
import java.net.URISyntaxException;
import org.jboss.vfs.VFS;
import org.jboss.vfs.VirtualFile;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.deployment.scanning.spi.ProcessApplicationScanner;

public class VfsProcessApplicationScanner implements ProcessApplicationScanner
{
    private static final ContainerIntegrationLogger LOG;
    
    @Override
    public Map<String, byte[]> findResources(final ClassLoader classLoader, final String resourceRootPath, final URL processesXml) {
        return this.findResources(classLoader, resourceRootPath, processesXml, null);
    }
    
    @Override
    public Map<String, byte[]> findResources(final ClassLoader classLoader, final String resourceRootPath, final URL processesXml, final String[] additionalResourceSuffixes) {
        final Map<String, byte[]> resources = new HashMap<String, byte[]>();
        if (resourceRootPath != null && !resourceRootPath.startsWith("pa:")) {
            final String strippedPath = resourceRootPath.replace("classpath:", "");
            final Enumeration<URL> resourceRoots = this.loadClasspathResourceRoots(classLoader, strippedPath);
            if (!resourceRoots.hasMoreElements()) {
                VfsProcessApplicationScanner.LOG.cannotFindResourcesForPath(resourceRootPath, classLoader);
            }
            while (resourceRoots.hasMoreElements()) {
                final URL resourceRoot = resourceRoots.nextElement();
                final VirtualFile virtualRoot = this.getVirtualFileForUrl(resourceRoot);
                this.scanRoot(virtualRoot, additionalResourceSuffixes, resources);
            }
        }
        else if (processesXml != null) {
            final VirtualFile virtualFile = this.getVirtualFileForUrl(processesXml);
            VirtualFile resourceRoot2 = virtualFile.getParent().getParent();
            if (resourceRootPath != null) {
                final String strippedPath2 = resourceRootPath.replace("pa:", "");
                resourceRoot2 = resourceRoot2.getChild(strippedPath2);
            }
            this.scanRoot(resourceRoot2, additionalResourceSuffixes, resources);
        }
        return resources;
    }
    
    protected VirtualFile getVirtualFileForUrl(final URL url) {
        try {
            return VFS.getChild(url.toURI());
        }
        catch (URISyntaxException e) {
            throw VfsProcessApplicationScanner.LOG.exceptionWhileGettingVirtualFolder(url, e);
        }
    }
    
    protected void scanRoot(final VirtualFile processArchiveRoot, final String[] additionalResourceSuffixes, final Map<String, byte[]> resources) {
        try {
            final List<VirtualFile> processes = (List<VirtualFile>)processArchiveRoot.getChildrenRecursively((VirtualFileFilter)new VirtualFileFilter() {
                public boolean accepts(final VirtualFile file) {
                    return file.isFile() && ProcessApplicationScanningUtil.isDeployable(file.getName(), additionalResourceSuffixes);
                }
            });
            for (final VirtualFile process : processes) {
                this.addResource(process, processArchiveRoot, resources);
                final List<VirtualFile> diagrams = (List<VirtualFile>)process.getParent().getChildren((VirtualFileFilter)new VirtualFileFilter() {
                    public boolean accepts(final VirtualFile file) {
                        return ProcessApplicationScanningUtil.isDiagram(file.getName(), process.getName());
                    }
                });
                for (final VirtualFile diagram : diagrams) {
                    this.addResource(diagram, processArchiveRoot, resources);
                }
            }
        }
        catch (IOException e) {
            VfsProcessApplicationScanner.LOG.cannotScanVfsRoot(processArchiveRoot, e);
        }
    }
    
    private void addResource(final VirtualFile virtualFile, final VirtualFile processArchiveRoot, final Map<String, byte[]> resources) {
        final String resourceName = virtualFile.getPathNameRelativeTo(processArchiveRoot);
        try {
            final InputStream inputStream = virtualFile.openStream();
            final byte[] bytes = IoUtil.readInputStream(inputStream, resourceName);
            IoUtil.closeSilently(inputStream);
            resources.put(resourceName, bytes);
        }
        catch (IOException e) {
            VfsProcessApplicationScanner.LOG.cannotReadInputStreamForFile(resourceName, processArchiveRoot, e);
        }
    }
    
    protected Enumeration<URL> loadClasspathResourceRoots(final ClassLoader classLoader, final String strippedPaResourceRootPath) {
        try {
            return classLoader.getResources(strippedPaResourceRootPath);
        }
        catch (IOException e) {
            throw VfsProcessApplicationScanner.LOG.exceptionWhileLoadingCpRoots(strippedPaResourceRootPath, classLoader, e);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
