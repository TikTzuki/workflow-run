// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.util.Iterator;
import java.util.Stack;
import java.io.PrintWriter;

public class NodePrinter
{
    private static boolean isLastSibling(final Node node, final Node parent) {
        return parent == null || node == parent.getChild(parent.getCardinality() - 1);
    }
    
    private static void dump(final PrintWriter writer, final Node node, final Stack<Node> predecessors) {
        if (!predecessors.isEmpty()) {
            Node parent = null;
            for (final Node predecessor : predecessors) {
                if (isLastSibling(predecessor, parent)) {
                    writer.print("   ");
                }
                else {
                    writer.print("|  ");
                }
                parent = predecessor;
            }
            writer.println("|");
        }
        Node parent = null;
        for (final Node predecessor : predecessors) {
            if (isLastSibling(predecessor, parent)) {
                writer.print("   ");
            }
            else {
                writer.print("|  ");
            }
            parent = predecessor;
        }
        writer.print("+- ");
        writer.println(node.toString());
        predecessors.push(node);
        for (int i = 0; i < node.getCardinality(); ++i) {
            dump(writer, node.getChild(i), predecessors);
        }
        predecessors.pop();
    }
    
    public static void dump(final PrintWriter writer, final Node node) {
        dump(writer, node, new Stack<Node>());
    }
}
