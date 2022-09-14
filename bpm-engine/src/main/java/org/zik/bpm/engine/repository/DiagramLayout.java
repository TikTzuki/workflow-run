// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class DiagramLayout implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, DiagramElement> elements;
    
    public DiagramLayout(final Map<String, DiagramElement> elements) {
        this.setElements(elements);
    }
    
    public DiagramNode getNode(final String id) {
        final DiagramElement element = this.getElements().get(id);
        if (element instanceof DiagramNode) {
            return (DiagramNode)element;
        }
        return null;
    }
    
    public DiagramEdge getEdge(final String id) {
        final DiagramElement element = this.getElements().get(id);
        if (element instanceof DiagramEdge) {
            return (DiagramEdge)element;
        }
        return null;
    }
    
    public Map<String, DiagramElement> getElements() {
        return this.elements;
    }
    
    public void setElements(final Map<String, DiagramElement> elements) {
        this.elements = elements;
    }
    
    public List<DiagramNode> getNodes() {
        final List<DiagramNode> nodes = new ArrayList<DiagramNode>();
        for (final Map.Entry<String, DiagramElement> entry : this.getElements().entrySet()) {
            final DiagramElement element = entry.getValue();
            if (element instanceof DiagramNode) {
                nodes.add((DiagramNode)element);
            }
        }
        return nodes;
    }
}
