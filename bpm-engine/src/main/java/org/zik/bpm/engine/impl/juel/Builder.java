// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.javax.el.ELResolver;
import org.zik.bpm.engine.impl.javax.el.FunctionMapper;
import org.zik.bpm.engine.impl.javax.el.VariableMapper;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.EnumSet;

public class Builder implements TreeBuilder
{
    private static final long serialVersionUID = 1L;
    protected final EnumSet<Feature> features;
    
    public Builder() {
        this.features = EnumSet.noneOf(Feature.class);
    }
    
    public Builder(final Feature... features) {
        if (features == null || features.length == 0) {
            this.features = EnumSet.noneOf(Feature.class);
        }
        else if (features.length == 1) {
            this.features = EnumSet.of(features[0]);
        }
        else {
            final Feature[] rest = new Feature[features.length - 1];
            for (int i = 1; i < features.length; ++i) {
                rest[i - 1] = features[i];
            }
            this.features = EnumSet.of(features[0], rest);
        }
    }
    
    public boolean isEnabled(final Feature feature) {
        return this.features.contains(feature);
    }
    
    @Override
    public Tree build(final String expression) throws TreeBuilderException {
        try {
            return this.createParser(expression).tree();
        }
        catch (Scanner.ScanException e) {
            throw new TreeBuilderException(expression, e.position, e.encountered, e.expected, e.getMessage());
        }
        catch (Parser.ParseException e2) {
            throw new TreeBuilderException(expression, e2.position, e2.encountered, e2.expected, e2.getMessage());
        }
    }
    
    protected Parser createParser(final String expression) {
        return new Parser(this, expression);
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj.getClass() == this.getClass() && this.features.equals(((Builder)obj).features);
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
    
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.err.println("usage: java " + Builder.class.getName() + " <expression string>");
            System.exit(1);
        }
        final PrintWriter out = new PrintWriter(System.out);
        Tree tree = null;
        try {
            tree = new Builder(new Feature[] { Feature.METHOD_INVOCATIONS }).build(args[0]);
        }
        catch (TreeBuilderException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        NodePrinter.dump(out, tree.getRoot());
        if (!tree.getFunctionNodes().iterator().hasNext() && !tree.getIdentifierNodes().iterator().hasNext()) {
            final ELContext context = new ELContext() {
                @Override
                public VariableMapper getVariableMapper() {
                    return null;
                }
                
                @Override
                public FunctionMapper getFunctionMapper() {
                    return null;
                }
                
                @Override
                public ELResolver getELResolver() {
                    return null;
                }
            };
            out.print(">> ");
            try {
                out.println(tree.getRoot().getValue(new Bindings(null, null), context, null));
            }
            catch (ELException e2) {
                out.println(e2.getMessage());
            }
        }
        out.flush();
    }
    
    public enum Feature
    {
        METHOD_INVOCATIONS, 
        NULL_PROPERTIES, 
        VARARGS;
    }
}
