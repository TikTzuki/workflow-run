// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import java.util.Properties;

public class ProductPropertiesUtil
{
    protected static final String PROPERTIES_FILE_PATH = "/org/camunda/bpm/engine/product-info.properties";
    protected static final String VERSION_PROPERTY = "camunda.version";
    protected static final Properties INSTANCE;
    
    protected ProductPropertiesUtil() {
    }
    
    public static String getProductVersion() {
        return ProductPropertiesUtil.INSTANCE.getProperty("camunda.version", ProductPropertiesUtil.class.getPackage().getImplementationVersion());
    }
    
    static {
        INSTANCE = PropertiesUtil.getProperties("/org/camunda/bpm/engine/product-info.properties");
    }
}
