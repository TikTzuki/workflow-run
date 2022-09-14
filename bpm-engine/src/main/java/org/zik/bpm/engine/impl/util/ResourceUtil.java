// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.io.Closeable;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;

public final class ResourceUtil
{
    private static final EngineUtilLogger LOG;
    
    public static String loadResourceContent(final String resourcePath, final DeploymentEntity deployment) {
        final String[] pathSplit = resourcePath.split("://", 2);
        String resourceType;
        if (pathSplit.length == 1) {
            resourceType = "classpath";
        }
        else {
            resourceType = pathSplit[0];
        }
        final String resourceLocation = pathSplit[pathSplit.length - 1];
        byte[] resourceBytes = null;
        if (resourceType.equals("classpath")) {
            InputStream resourceAsStream = null;
            try {
                resourceAsStream = ReflectUtil.getResourceAsStream(resourceLocation);
                if (resourceAsStream != null) {
                    resourceBytes = IoUtil.readInputStream(resourceAsStream, resourcePath);
                }
            }
            finally {
                IoUtil.closeSilently(resourceAsStream);
            }
        }
        else if (resourceType.equals("deployment")) {
            final ResourceEntity resourceEntity = deployment.getResource(resourceLocation);
            if (resourceEntity != null) {
                resourceBytes = resourceEntity.getBytes();
            }
        }
        if (resourceBytes != null) {
            return new String(resourceBytes, Charset.forName("UTF-8"));
        }
        throw ResourceUtil.LOG.cannotFindResource(resourcePath);
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
    }
}
