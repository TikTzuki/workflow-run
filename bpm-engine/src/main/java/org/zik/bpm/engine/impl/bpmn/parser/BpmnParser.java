// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.util.xml.Parse;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cfg.BpmnParseFactory;
import java.util.List;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.util.xml.Parser;

public class BpmnParser extends Parser
{
    public static final String BPMN20_NS = "http://www.omg.org/spec/BPMN/20100524/MODEL";
    public static final String BPMN_20_SCHEMA_LOCATION = "org/camunda/bpm/engine/impl/bpmn/parser/BPMN20.xsd";
    public static final String CAMUNDA_BPMN_EXTENSIONS_NS = "http://camunda.org/schema/1.0/bpmn";
    @Deprecated
    public static final String ACTIVITI_BPMN_EXTENSIONS_NS = "http://activiti.org/bpmn";
    public static final String BPMN_DI_NS = "http://www.omg.org/spec/BPMN/20100524/DI";
    public static final String BPMN_DC_NS = "http://www.omg.org/spec/DD/20100524/DC";
    public static final String OMG_DI_NS = "http://www.omg.org/spec/DD/20100524/DI";
    public static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";
    protected ExpressionManager expressionManager;
    protected List<BpmnParseListener> parseListeners;
    protected BpmnParseFactory bpmnParseFactory;
    
    public BpmnParser(final ExpressionManager expressionManager, final BpmnParseFactory bpmnParseFactory) {
        this.parseListeners = new ArrayList<BpmnParseListener>();
        this.expressionManager = expressionManager;
        this.bpmnParseFactory = bpmnParseFactory;
    }
    
    @Override
    public BpmnParse createParse() {
        return this.bpmnParseFactory.createBpmnParse(this);
    }
    
    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }
    
    public void setExpressionManager(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }
    
    public List<BpmnParseListener> getParseListeners() {
        return this.parseListeners;
    }
    
    public void setParseListeners(final List<BpmnParseListener> parseListeners) {
        this.parseListeners = parseListeners;
    }
}
