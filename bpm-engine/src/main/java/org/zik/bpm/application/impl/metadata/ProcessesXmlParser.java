// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.metadata;

import org.zik.bpm.engine.impl.util.xml.Parse;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import org.zik.bpm.engine.impl.util.xml.Parser;

public class ProcessesXmlParser extends Parser
{
    public static final String PROCESS_APP_NS = "http://www.camunda.org/schema/1.0/ProcessApplication";
    public static final String PROCESS_APPLICATION_XSD = "ProcessApplication.xsd";
    
    @Override
    public ProcessesXmlParse createParse() {
        final ProcessesXmlParse processesXmlParse = new ProcessesXmlParse(this);
        processesXmlParse.setSchemaResource(ReflectUtil.getResourceUrlAsString("ProcessApplication.xsd"));
        return processesXmlParse;
    }
}
