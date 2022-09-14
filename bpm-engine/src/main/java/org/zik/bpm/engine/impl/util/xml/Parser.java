// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.xml;

import java.util.function.Supplier;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.zik.bpm.engine.impl.util.EngineUtilLogger;

public abstract class Parser
{
    protected static final EngineUtilLogger LOG;
    protected static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    protected static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
    protected static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    protected static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
    protected static final String NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    protected static ThreadLocal<SAXParserFactory> SAX_PARSER_FACTORY_INSTANCE;
    
    public abstract Parse createParse();
    
    protected SAXParser getSaxParser() throws Exception {
        final SAXParserFactory saxParserFactory = this.getSaxParserFactoryLazily();
        this.setXxeProcessing(saxParserFactory);
        return saxParserFactory.newSAXParser();
    }
    
    protected SAXParserFactory getSaxParserFactoryLazily() {
        return Parser.SAX_PARSER_FACTORY_INSTANCE.get();
    }
    
    protected void enableSchemaValidation(final boolean enableSchemaValidation) {
        final SAXParserFactory saxParserFactory = this.getSaxParserFactoryLazily();
        saxParserFactory.setNamespaceAware(enableSchemaValidation);
        saxParserFactory.setValidating(enableSchemaValidation);
        try {
            saxParserFactory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
        }
        catch (Exception e) {
            Parser.LOG.unableToSetSchemaResource(e);
        }
    }
    
    protected void setXxeProcessing(final SAXParserFactory saxParserFactory) {
        final boolean enableXxeProcessing = this.isEnableXxeProcessing();
        saxParserFactory.setXIncludeAware(enableXxeProcessing);
        try {
            saxParserFactory.setFeature("http://xml.org/sax/features/external-general-entities", enableXxeProcessing);
            saxParserFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", !enableXxeProcessing);
            saxParserFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", enableXxeProcessing);
            saxParserFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", enableXxeProcessing);
            saxParserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        }
        catch (Exception e) {
            throw Parser.LOG.exceptionWhileSettingXxeProcessing(e);
        }
    }
    
    public Boolean isEnableXxeProcessing() {
        final ProcessEngineConfigurationImpl engineConfig = Context.getProcessEngineConfiguration();
        if (engineConfig != null) {
            return engineConfig.isEnableXxeProcessing();
        }
        return false;
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
        Parser.SAX_PARSER_FACTORY_INSTANCE = ThreadLocal.withInitial((Supplier<? extends SAXParserFactory>)SAXParserFactory::newInstance);
    }
}
