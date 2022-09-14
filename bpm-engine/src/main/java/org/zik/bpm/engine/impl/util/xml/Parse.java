// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util.xml;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import org.zik.bpm.engine.BpmnParseException;
import org.zik.bpm.engine.impl.xml.ProblemImpl;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.SAXParser;
import org.zik.bpm.engine.impl.util.io.StringStreamSource;
import org.zik.bpm.engine.impl.util.io.ResourceStreamSource;
import java.net.MalformedURLException;
import org.zik.bpm.engine.impl.util.io.UrlStreamSource;
import java.net.URL;
import org.zik.bpm.engine.impl.util.io.InputStreamSource;
import java.io.InputStream;
import java.util.ArrayList;
import org.zik.bpm.engine.Problem;
import java.util.List;
import org.zik.bpm.engine.impl.util.io.StreamSource;
import org.zik.bpm.engine.impl.util.EngineUtilLogger;
import org.xml.sax.helpers.DefaultHandler;

public abstract class Parse extends DefaultHandler
{
    protected static final EngineUtilLogger LOG;
    protected static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    protected static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    protected static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    protected static final String JAXP_ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema";
    protected static final String JAXP_ACCESS_EXTERNAL_SCHEMA_SYSTEM_PROPERTY = "javax.xml.accessExternalSchema";
    protected static final String JAXP_ACCESS_EXTERNAL_SCHEMA_ALL = "all";
    protected Parser parser;
    protected String name;
    protected StreamSource streamSource;
    protected Element rootElement;
    protected List<Problem> errors;
    protected List<Problem> warnings;
    protected String schemaResource;
    
    public Parse(final Parser parser) {
        this.rootElement = null;
        this.errors = new ArrayList<Problem>();
        this.warnings = new ArrayList<Problem>();
        this.parser = parser;
    }
    
    public Parse name(final String name) {
        this.name = name;
        return this;
    }
    
    public Parse sourceInputStream(final InputStream inputStream) {
        if (this.name == null) {
            this.name("inputStream");
        }
        this.setStreamSource(new InputStreamSource(inputStream));
        return this;
    }
    
    public Parse sourceResource(final String resource) {
        return this.sourceResource(resource, null);
    }
    
    public Parse sourceUrl(final URL url) {
        if (this.name == null) {
            this.name(url.toString());
        }
        this.setStreamSource(new UrlStreamSource(url));
        return this;
    }
    
    public Parse sourceUrl(final String url) {
        try {
            return this.sourceUrl(new URL(url));
        }
        catch (MalformedURLException e) {
            throw Parse.LOG.malformedUrlException(url, e);
        }
    }
    
    public Parse sourceResource(final String resource, final ClassLoader classLoader) {
        if (this.name == null) {
            this.name(resource);
        }
        this.setStreamSource(new ResourceStreamSource(resource, classLoader));
        return this;
    }
    
    public Parse sourceString(final String string) {
        if (this.name == null) {
            this.name("string");
        }
        this.setStreamSource(new StringStreamSource(string));
        return this;
    }
    
    protected void setStreamSource(final StreamSource streamSource) {
        if (this.streamSource != null) {
            throw Parse.LOG.multipleSourcesException(this.streamSource, streamSource);
        }
        this.streamSource = streamSource;
    }
    
    public void setSchemaResource(final String schemaResource) {
        final boolean schemaResourceSet = schemaResource != null;
        this.parser.enableSchemaValidation(schemaResourceSet);
        this.schemaResource = schemaResource;
    }
    
    public Parse execute() {
        try {
            final InputStream inputStream = this.streamSource.getInputStream();
            final SAXParser saxParser = this.parser.getSaxParser();
            try {
                saxParser.setProperty("http://javax.xml.XMLConstants/property/accessExternalSchema", this.resolveAccessExternalSchemaProperty());
            }
            catch (Exception e) {
                Parse.LOG.logAccessExternalSchemaNotSupported(e);
            }
            if (this.schemaResource != null) {
                saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
                saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", this.schemaResource);
            }
            saxParser.parse(inputStream, new ParseHandler(this));
        }
        catch (Exception e2) {
            throw Parse.LOG.parsingFailureException(this.name, e2);
        }
        return this;
    }
    
    protected String resolveAccessExternalSchemaProperty() {
        final String systemProperty = System.getProperty("javax.xml.accessExternalSchema");
        if (systemProperty != null) {
            return systemProperty;
        }
        return "all";
    }
    
    public Element getRootElement() {
        return this.rootElement;
    }
    
    public List<Problem> getProblems() {
        return this.errors;
    }
    
    public void addError(final SAXParseException e) {
        this.errors.add(new ProblemImpl(e));
    }
    
    public void addError(final String errorMessage, final Element element) {
        this.errors.add(new ProblemImpl(errorMessage, element));
    }
    
    public void addError(final String errorMessage, final Element element, final String... elementIds) {
        this.errors.add(new ProblemImpl(errorMessage, element, elementIds));
    }
    
    public void addError(final BpmnParseException e) {
        this.errors.add(new ProblemImpl(e));
    }
    
    public void addError(final BpmnParseException e, final String elementId) {
        this.errors.add(new ProblemImpl(e, elementId));
    }
    
    public boolean hasErrors() {
        return this.errors != null && !this.errors.isEmpty();
    }
    
    public void addWarning(final SAXParseException e) {
        this.warnings.add(new ProblemImpl(e));
    }
    
    public void addWarning(final String errorMessage, final Element element) {
        this.warnings.add(new ProblemImpl(errorMessage, element));
    }
    
    public void addWarning(final String errorMessage, final Element element, final String... elementIds) {
        this.warnings.add(new ProblemImpl(errorMessage, element, elementIds));
    }
    
    public boolean hasWarnings() {
        return this.warnings != null && !this.warnings.isEmpty();
    }
    
    public void logWarnings() {
        final StringBuilder builder = new StringBuilder();
        for (final Problem warning : this.warnings) {
            builder.append("\n* ");
            builder.append(warning.getMessage());
            builder.append(" | resource " + this.name);
            builder.append(warning.toString());
        }
        Parse.LOG.logParseWarnings(builder.toString());
    }
    
    public void throwExceptionForErrors() {
        final StringBuilder strb = new StringBuilder();
        for (final Problem error : this.errors) {
            strb.append("\n* ");
            strb.append(error.getMessage());
            strb.append(" | resource " + this.name);
            strb.append(error.toString());
        }
        throw Parse.LOG.exceptionDuringParsing(strb.toString(), this.name, this.errors, this.warnings);
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
    }
}
