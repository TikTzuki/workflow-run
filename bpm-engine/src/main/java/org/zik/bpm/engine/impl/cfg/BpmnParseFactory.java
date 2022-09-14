// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.zik.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParser;

public interface BpmnParseFactory
{
    BpmnParse createBpmnParse(final BpmnParser p0);
}
