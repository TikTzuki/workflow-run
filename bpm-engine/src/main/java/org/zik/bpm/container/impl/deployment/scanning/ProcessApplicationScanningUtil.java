// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.deployment.scanning;

import org.zik.bpm.engine.impl.dmn.deployer.DecisionDefinitionDeployer;
import org.zik.bpm.engine.impl.cmmn.deployer.CmmnDeployer;
import org.zik.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.zik.bpm.container.impl.deployment.scanning.spi.ProcessApplicationScanner;
import java.util.Map;
import java.net.URL;

public class ProcessApplicationScanningUtil
{
    public static Map<String, byte[]> findResources(final ClassLoader classLoader, final String paResourceRootPath, final URL metaFileUrl) {
        return findResources(classLoader, paResourceRootPath, metaFileUrl, null);
    }
    
    public static Map<String, byte[]> findResources(final ClassLoader classLoader, final String paResourceRootPath, final URL metaFileUrl, final String[] additionalResourceSuffixes) {
        ProcessApplicationScanner scanner = null;
        try {
            classLoader.loadClass("org.jboss.vfs.VFS");
            scanner = new VfsProcessApplicationScanner();
        }
        catch (Throwable t) {
            scanner = new ClassPathProcessApplicationScanner();
        }
        return scanner.findResources(classLoader, paResourceRootPath, metaFileUrl, additionalResourceSuffixes);
    }
    
    public static boolean isDeployable(final String filename) {
        return hasSuffix(filename, BpmnDeployer.BPMN_RESOURCE_SUFFIXES) || hasSuffix(filename, CmmnDeployer.CMMN_RESOURCE_SUFFIXES) || hasSuffix(filename, DecisionDefinitionDeployer.DMN_RESOURCE_SUFFIXES);
    }
    
    public static boolean isDeployable(final String filename, final String[] additionalResourceSuffixes) {
        return isDeployable(filename) || hasSuffix(filename, additionalResourceSuffixes);
    }
    
    public static boolean hasSuffix(final String filename, final String[] suffixes) {
        if (suffixes == null || suffixes.length == 0) {
            return false;
        }
        for (final String suffix : suffixes) {
            if (filename.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isDiagram(final String fileName, final String modelFileName) {
        final boolean isBpmnDiagram = checkDiagram(fileName, modelFileName, BpmnDeployer.DIAGRAM_SUFFIXES, BpmnDeployer.BPMN_RESOURCE_SUFFIXES);
        final boolean isCmmnDiagram = checkDiagram(fileName, modelFileName, CmmnDeployer.DIAGRAM_SUFFIXES, CmmnDeployer.CMMN_RESOURCE_SUFFIXES);
        final boolean isDmnDiagram = checkDiagram(fileName, modelFileName, DecisionDefinitionDeployer.DIAGRAM_SUFFIXES, DecisionDefinitionDeployer.DMN_RESOURCE_SUFFIXES);
        return isBpmnDiagram || isCmmnDiagram || isDmnDiagram;
    }
    
    protected static boolean checkDiagram(final String fileName, final String modelFileName, final String[] diagramSuffixes, final String[] modelSuffixes) {
        for (final String modelSuffix : modelSuffixes) {
            if (modelFileName.endsWith(modelSuffix)) {
                final String caseFilePrefix = modelFileName.substring(0, modelFileName.length() - modelSuffix.length());
                if (fileName.startsWith(caseFilePrefix)) {
                    for (final String diagramResourceSuffix : diagramSuffixes) {
                        if (fileName.endsWith(diagramResourceSuffix)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
