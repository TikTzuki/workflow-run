// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class AstChoice extends AstRightValue
{
    private final AstNode question;
    private final AstNode yes;
    private final AstNode no;
    
    public AstChoice(final AstNode question, final AstNode yes, final AstNode no) {
        this.question = question;
        this.yes = yes;
        this.no = no;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) throws ELException {
        final Boolean value = bindings.convert(this.question.eval(bindings, context), Boolean.class);
        return value ? this.yes.eval(bindings, context) : this.no.eval(bindings, context);
    }
    
    @Override
    public String toString() {
        return "?";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        this.question.appendStructure(b, bindings);
        b.append(" ? ");
        this.yes.appendStructure(b, bindings);
        b.append(" : ");
        this.no.appendStructure(b, bindings);
    }
    
    @Override
    public int getCardinality() {
        return 3;
    }
    
    @Override
    public AstNode getChild(final int i) {
        return (i == 0) ? this.question : ((i == 1) ? this.yes : ((i == 2) ? this.no : null));
    }
}
