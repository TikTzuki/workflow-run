// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil
{
    protected static final EngineUtilLogger LOG;
    
    public static Properties getProperties(final String propertiesFile) {
        final Properties productProperties = new Properties();
        try {
            final InputStream inputStream = ProductPropertiesUtil.class.getResourceAsStream(propertiesFile);
            try {
                productProperties.load(inputStream);
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (Throwable t) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (IOException | NullPointerException ex2) {
            final Exception ex;
            final Exception e = ex;
            PropertiesUtil.LOG.logMissingPropertiesFile(propertiesFile);
        }
        return productProperties;
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
    }
}
