// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.metadata;

import org.zik.bpm.engine.impl.util.xml.Parse;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import org.zik.bpm.engine.impl.util.xml.Parser;

public class BpmPlatformXmlParser extends Parser
{
    public static final String BPM_PLATFORM_NS = "http://www.camunda.org/schema/1.0/BpmPlatform";
    public static final String BPM_PLATFORM_XSD = "BpmPlatform.xsd";
    
    @Override
    public BpmPlatformXmlParse createParse() {
        final BpmPlatformXmlParse parse = new BpmPlatformXmlParse(this);
        parse.setSchemaResource(ReflectUtil.getResourceUrlAsString("BpmPlatform.xsd"));
        return parse;
    }
}
