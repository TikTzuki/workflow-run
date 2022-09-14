// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;
import java.util.List;

public class Parser
{
    private static final String EXPR_FIRST;
    protected final Builder context;
    protected final Scanner scanner;
    private List<IdentifierNode> identifiers;
    private List<FunctionNode> functions;
    private List<LookaheadToken> lookahead;
    private Scanner.Token token;
    private int position;
    protected Map<Scanner.ExtensionToken, ExtensionHandler> extensions;
    
    public Parser(final Builder context, final String input) {
        this.identifiers = Collections.emptyList();
        this.functions = Collections.emptyList();
        this.lookahead = Collections.emptyList();
        this.extensions = Collections.emptyMap();
        this.context = context;
        this.scanner = this.createScanner(input);
    }
    
    protected Scanner createScanner(final String expression) {
        return new Scanner(expression);
    }
    
    public void putExtensionHandler(final Scanner.ExtensionToken token, final ExtensionHandler extension) {
        if (this.extensions.isEmpty()) {
            this.extensions = new HashMap<Scanner.ExtensionToken, ExtensionHandler>(16);
        }
        this.extensions.put(token, extension);
    }
    
    protected ExtensionHandler getExtensionHandler(final Scanner.Token token) {
        return this.extensions.get(token);
    }
    
    protected Number parseInteger(final String string) throws ParseException {
        try {
            return Long.valueOf(string);
        }
        catch (NumberFormatException e) {
            this.fail(Scanner.Symbol.INTEGER);
            return null;
        }
    }
    
    protected Number parseFloat(final String string) throws ParseException {
        try {
            return Double.valueOf(string);
        }
        catch (NumberFormatException e) {
            this.fail(Scanner.Symbol.FLOAT);
            return null;
        }
    }
    
    protected AstBinary createAstBinary(final AstNode left, final AstNode right, final AstBinary.Operator operator) {
        return new AstBinary(left, right, operator);
    }
    
    protected AstBracket createAstBracket(final AstNode base, final AstNode property, final boolean lvalue, final boolean strict) {
        return new AstBracket(base, property, lvalue, strict);
    }
    
    protected AstChoice createAstChoice(final AstNode question, final AstNode yes, final AstNode no) {
        return new AstChoice(question, yes, no);
    }
    
    protected AstComposite createAstComposite(final List<AstNode> nodes) {
        return new AstComposite(nodes);
    }
    
    protected AstDot createAstDot(final AstNode base, final String property, final boolean lvalue) {
        return new AstDot(base, property, lvalue);
    }
    
    protected AstFunction createAstFunction(final String name, final int index, final AstParameters params) {
        return new AstFunction(name, index, params, this.context.isEnabled(Builder.Feature.VARARGS));
    }
    
    protected AstIdentifier createAstIdentifier(final String name, final int index) {
        return new AstIdentifier(name, index);
    }
    
    protected AstMethod createAstMethod(final AstProperty property, final AstParameters params) {
        return new AstMethod(property, params);
    }
    
    protected AstUnary createAstUnary(final AstNode child, final AstUnary.Operator operator) {
        return new AstUnary(child, operator);
    }
    
    protected final List<FunctionNode> getFunctions() {
        return this.functions;
    }
    
    protected final List<IdentifierNode> getIdentifiers() {
        return this.identifiers;
    }
    
    protected final Scanner.Token getToken() {
        return this.token;
    }
    
    protected void fail(final String expected) throws ParseException {
        throw new ParseException(this.position, "'" + this.token.getImage() + "'", expected);
    }
    
    protected void fail(final Scanner.Symbol expected) throws ParseException {
        this.fail(expected.toString());
    }
    
    protected final Scanner.Token lookahead(final int index) throws Scanner.ScanException, ParseException {
        if (this.lookahead.isEmpty()) {
            this.lookahead = new LinkedList<LookaheadToken>();
        }
        while (index >= this.lookahead.size()) {
            this.lookahead.add(new LookaheadToken(this.scanner.next(), this.scanner.getPosition()));
        }
        return this.lookahead.get(index).token;
    }
    
    protected final Scanner.Token consumeToken() throws Scanner.ScanException, ParseException {
        final Scanner.Token result = this.token;
        if (this.lookahead.isEmpty()) {
            this.token = this.scanner.next();
            this.position = this.scanner.getPosition();
        }
        else {
            final LookaheadToken next = this.lookahead.remove(0);
            this.token = next.token;
            this.position = next.position;
        }
        return result;
    }
    
    protected final Scanner.Token consumeToken(final Scanner.Symbol expected) throws Scanner.ScanException, ParseException {
        if (this.token.getSymbol() != expected) {
            this.fail(expected);
        }
        return this.consumeToken();
    }
    
    public Tree tree() throws Scanner.ScanException, ParseException {
        this.consumeToken();
        AstNode t = this.text();
        if (this.token.getSymbol() == Scanner.Symbol.EOF) {
            if (t == null) {
                t = new AstText("");
            }
            return new Tree(t, this.functions, this.identifiers, false);
        }
        final AstEval e = this.eval();
        if (this.token.getSymbol() == Scanner.Symbol.EOF && t == null) {
            return new Tree(e, this.functions, this.identifiers, e.isDeferred());
        }
        final ArrayList<AstNode> list = new ArrayList<AstNode>();
        if (t != null) {
            list.add(t);
        }
        list.add(e);
        t = this.text();
        if (t != null) {
            list.add(t);
        }
        while (this.token.getSymbol() != Scanner.Symbol.EOF) {
            if (e.isDeferred()) {
                list.add(this.eval(true, true));
            }
            else {
                list.add(this.eval(true, false));
            }
            t = this.text();
            if (t != null) {
                list.add(t);
            }
        }
        return new Tree(this.createAstComposite(list), this.functions, this.identifiers, e.isDeferred());
    }
    
    protected AstNode text() throws Scanner.ScanException, ParseException {
        AstNode v = null;
        if (this.token.getSymbol() == Scanner.Symbol.TEXT) {
            v = new AstText(this.token.getImage());
            this.consumeToken();
        }
        return v;
    }
    
    protected AstEval eval() throws Scanner.ScanException, ParseException {
        AstEval e = this.eval(false, false);
        if (e == null) {
            e = this.eval(false, true);
            if (e == null) {
                this.fail(Scanner.Symbol.START_EVAL_DEFERRED + "|" + Scanner.Symbol.START_EVAL_DYNAMIC);
            }
        }
        return e;
    }
    
    protected AstEval eval(final boolean required, final boolean deferred) throws Scanner.ScanException, ParseException {
        AstEval v = null;
        final Scanner.Symbol start_eval = deferred ? Scanner.Symbol.START_EVAL_DEFERRED : Scanner.Symbol.START_EVAL_DYNAMIC;
        if (this.token.getSymbol() == start_eval) {
            this.consumeToken();
            v = new AstEval(this.expr(true), deferred);
            this.consumeToken(Scanner.Symbol.END_EVAL);
        }
        else if (required) {
            this.fail(start_eval);
        }
        return v;
    }
    
    protected AstNode expr(final boolean required) throws Scanner.ScanException, ParseException {
        AstNode v = this.or(required);
        if (v == null) {
            return null;
        }
        if (this.token.getSymbol() == Scanner.Symbol.QUESTION) {
            this.consumeToken();
            final AstNode a = this.expr(true);
            this.consumeToken(Scanner.Symbol.COLON);
            final AstNode b = this.expr(true);
            v = this.createAstChoice(v, a, b);
        }
        return v;
    }
    
    protected AstNode or(final boolean required) throws Scanner.ScanException, ParseException {
        AstNode v = this.and(required);
        if (v == null) {
            return null;
        }
    Label_0122:
        while (true) {
            switch (this.token.getSymbol()) {
                case OR: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.and(true), AstBinary.OR);
                    continue;
                }
                case EXTENSION: {
                    if (this.getExtensionHandler(this.token).getExtensionPoint() == ExtensionPoint.OR) {
                        v = this.getExtensionHandler(this.consumeToken()).createAstNode(v, this.and(true));
                        continue;
                    }
                    break Label_0122;
                }
                default: {
                    break Label_0122;
                }
            }
        }
        return v;
    }
    
    protected AstNode and(final boolean required) throws Scanner.ScanException, ParseException {
        AstNode v = this.eq(required);
        if (v == null) {
            return null;
        }
    Label_0122:
        while (true) {
            switch (this.token.getSymbol()) {
                case AND: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.eq(true), AstBinary.AND);
                    continue;
                }
                case EXTENSION: {
                    if (this.getExtensionHandler(this.token).getExtensionPoint() == ExtensionPoint.AND) {
                        v = this.getExtensionHandler(this.consumeToken()).createAstNode(v, this.eq(true));
                        continue;
                    }
                    break Label_0122;
                }
                default: {
                    break Label_0122;
                }
            }
        }
        return v;
    }
    
    protected AstNode eq(final boolean required) throws Scanner.ScanException, ParseException {
        AstNode v = this.cmp(required);
        if (v == null) {
            return null;
        }
    Label_0148:
        while (true) {
            switch (this.token.getSymbol()) {
                case EQ: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.cmp(true), AstBinary.EQ);
                    continue;
                }
                case NE: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.cmp(true), AstBinary.NE);
                    continue;
                }
                case EXTENSION: {
                    if (this.getExtensionHandler(this.token).getExtensionPoint() == ExtensionPoint.EQ) {
                        v = this.getExtensionHandler(this.consumeToken()).createAstNode(v, this.cmp(true));
                        continue;
                    }
                    break Label_0148;
                }
                default: {
                    break Label_0148;
                }
            }
        }
        return v;
    }
    
    protected AstNode cmp(final boolean required) throws Scanner.ScanException, ParseException {
        AstNode v = this.add(required);
        if (v == null) {
            return null;
        }
    Label_0208:
        while (true) {
            switch (this.token.getSymbol()) {
                case LT: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.add(true), AstBinary.LT);
                    continue;
                }
                case LE: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.add(true), AstBinary.LE);
                    continue;
                }
                case GE: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.add(true), AstBinary.GE);
                    continue;
                }
                case GT: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.add(true), AstBinary.GT);
                    continue;
                }
                case EXTENSION: {
                    if (this.getExtensionHandler(this.token).getExtensionPoint() == ExtensionPoint.CMP) {
                        v = this.getExtensionHandler(this.consumeToken()).createAstNode(v, this.add(true));
                        continue;
                    }
                    break Label_0208;
                }
                default: {
                    break Label_0208;
                }
            }
        }
        return v;
    }
    
    protected AstNode add(final boolean required) throws Scanner.ScanException, ParseException {
        AstNode v = this.mul(required);
        if (v == null) {
            return null;
        }
    Label_0152:
        while (true) {
            switch (this.token.getSymbol()) {
                case PLUS: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.mul(true), AstBinary.ADD);
                    continue;
                }
                case MINUS: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.mul(true), AstBinary.SUB);
                    continue;
                }
                case EXTENSION: {
                    if (this.getExtensionHandler(this.token).getExtensionPoint() == ExtensionPoint.ADD) {
                        v = this.getExtensionHandler(this.consumeToken()).createAstNode(v, this.mul(true));
                        continue;
                    }
                    break Label_0152;
                }
                default: {
                    break Label_0152;
                }
            }
        }
        return v;
    }
    
    protected AstNode mul(final boolean required) throws Scanner.ScanException, ParseException {
        AstNode v = this.unary(required);
        if (v == null) {
            return null;
        }
    Label_0182:
        while (true) {
            switch (this.token.getSymbol()) {
                case MUL: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.unary(true), AstBinary.MUL);
                    continue;
                }
                case DIV: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.unary(true), AstBinary.DIV);
                    continue;
                }
                case MOD: {
                    this.consumeToken();
                    v = this.createAstBinary(v, this.unary(true), AstBinary.MOD);
                    continue;
                }
                case EXTENSION: {
                    if (this.getExtensionHandler(this.token).getExtensionPoint() == ExtensionPoint.MUL) {
                        v = this.getExtensionHandler(this.consumeToken()).createAstNode(v, this.unary(true));
                        continue;
                    }
                    break Label_0182;
                }
                default: {
                    break Label_0182;
                }
            }
        }
        return v;
    }
    
    protected AstNode unary(final boolean required) throws Scanner.ScanException, ParseException {
        AstNode v = null;
        Label_0172: {
            switch (this.token.getSymbol()) {
                case NOT: {
                    this.consumeToken();
                    v = this.createAstUnary(this.unary(true), AstUnary.NOT);
                    break Label_0172;
                }
                case MINUS: {
                    this.consumeToken();
                    v = this.createAstUnary(this.unary(true), AstUnary.NEG);
                    break Label_0172;
                }
                case EMPTY: {
                    this.consumeToken();
                    v = this.createAstUnary(this.unary(true), AstUnary.EMPTY);
                    break Label_0172;
                }
                case EXTENSION: {
                    if (this.getExtensionHandler(this.token).getExtensionPoint() == ExtensionPoint.UNARY) {
                        v = this.getExtensionHandler(this.consumeToken()).createAstNode(this.unary(true));
                        break Label_0172;
                    }
                    break;
                }
            }
            v = this.value();
        }
        if (v == null && required) {
            this.fail(Parser.EXPR_FIRST);
        }
        return v;
    }
    
    protected AstNode value() throws Scanner.ScanException, ParseException {
        boolean lvalue = true;
        AstNode v = this.nonliteral();
        if (v == null) {
            v = this.literal();
            if (v == null) {
                return null;
            }
            lvalue = false;
        }
        while (true) {
            switch (this.token.getSymbol()) {
                case DOT: {
                    this.consumeToken();
                    final String name = this.consumeToken(Scanner.Symbol.IDENTIFIER).getImage();
                    final AstDot dot = this.createAstDot(v, name, lvalue);
                    if (this.token.getSymbol() == Scanner.Symbol.LPAREN && this.context.isEnabled(Builder.Feature.METHOD_INVOCATIONS)) {
                        v = this.createAstMethod(dot, this.params());
                        continue;
                    }
                    v = dot;
                    continue;
                }
                case LBRACK: {
                    this.consumeToken();
                    final AstNode property = this.expr(true);
                    final boolean strict = !this.context.isEnabled(Builder.Feature.NULL_PROPERTIES);
                    this.consumeToken(Scanner.Symbol.RBRACK);
                    final AstBracket bracket = this.createAstBracket(v, property, lvalue, strict);
                    if (this.token.getSymbol() == Scanner.Symbol.LPAREN && this.context.isEnabled(Builder.Feature.METHOD_INVOCATIONS)) {
                        v = this.createAstMethod(bracket, this.params());
                        continue;
                    }
                    v = bracket;
                    continue;
                }
                default: {
                    return v;
                }
            }
        }
    }
    
    protected AstNode nonliteral() throws Scanner.ScanException, ParseException {
        AstNode v = null;
        switch (this.token.getSymbol()) {
            case IDENTIFIER: {
                String name = this.consumeToken().getImage();
                if (this.token.getSymbol() == Scanner.Symbol.COLON && this.lookahead(0).getSymbol() == Scanner.Symbol.IDENTIFIER && this.lookahead(1).getSymbol() == Scanner.Symbol.LPAREN) {
                    this.consumeToken();
                    name = name + ":" + this.token.getImage();
                    this.consumeToken();
                }
                if (this.token.getSymbol() == Scanner.Symbol.LPAREN) {
                    v = this.function(name, this.params());
                    break;
                }
                v = this.identifier(name);
                break;
            }
            case LPAREN: {
                this.consumeToken();
                v = this.expr(true);
                this.consumeToken(Scanner.Symbol.RPAREN);
                v = new AstNested(v);
                break;
            }
        }
        return v;
    }
    
    protected AstParameters params() throws Scanner.ScanException, ParseException {
        this.consumeToken(Scanner.Symbol.LPAREN);
        List<AstNode> l = Collections.emptyList();
        final AstNode v = this.expr(false);
        if (v != null) {
            l = new ArrayList<AstNode>();
            l.add(v);
            while (this.token.getSymbol() == Scanner.Symbol.COMMA) {
                this.consumeToken();
                l.add(this.expr(true));
            }
        }
        this.consumeToken(Scanner.Symbol.RPAREN);
        return new AstParameters(l);
    }
    
    protected AstNode literal() throws Scanner.ScanException, ParseException {
        AstNode v = null;
        switch (this.token.getSymbol()) {
            case TRUE: {
                v = new AstBoolean(true);
                this.consumeToken();
                break;
            }
            case FALSE: {
                v = new AstBoolean(false);
                this.consumeToken();
                break;
            }
            case STRING: {
                v = new AstString(this.token.getImage());
                this.consumeToken();
                break;
            }
            case INTEGER: {
                v = new AstNumber(this.parseInteger(this.token.getImage()));
                this.consumeToken();
                break;
            }
            case FLOAT: {
                v = new AstNumber(this.parseFloat(this.token.getImage()));
                this.consumeToken();
                break;
            }
            case NULL: {
                v = new AstNull();
                this.consumeToken();
                break;
            }
            case EXTENSION: {
                if (this.getExtensionHandler(this.token).getExtensionPoint() == ExtensionPoint.LITERAL) {
                    v = this.getExtensionHandler(this.consumeToken()).createAstNode(new AstNode[0]);
                    break;
                }
                break;
            }
        }
        return v;
    }
    
    protected final AstFunction function(final String name, final AstParameters params) {
        if (this.functions.isEmpty()) {
            this.functions = new ArrayList<FunctionNode>(4);
        }
        final AstFunction function = this.createAstFunction(name, this.functions.size(), params);
        this.functions.add(function);
        return function;
    }
    
    protected final AstIdentifier identifier(final String name) {
        if (this.identifiers.isEmpty()) {
            this.identifiers = new ArrayList<IdentifierNode>(4);
        }
        final AstIdentifier identifier = this.createAstIdentifier(name, this.identifiers.size());
        this.identifiers.add(identifier);
        return identifier;
    }
    
    static {
        EXPR_FIRST = Scanner.Symbol.IDENTIFIER + "|" + Scanner.Symbol.STRING + "|" + Scanner.Symbol.FLOAT + "|" + Scanner.Symbol.INTEGER + "|" + Scanner.Symbol.TRUE + "|" + Scanner.Symbol.FALSE + "|" + Scanner.Symbol.NULL + "|" + Scanner.Symbol.MINUS + "|" + Scanner.Symbol.NOT + "|" + Scanner.Symbol.EMPTY + "|" + Scanner.Symbol.LPAREN;
    }
    
    public static class ParseException extends Exception
    {
        final int position;
        final String encountered;
        final String expected;
        
        public ParseException(final int position, final String encountered, final String expected) {
            super(LocalMessages.get("error.parse", position, encountered, expected));
            this.position = position;
            this.encountered = encountered;
            this.expected = expected;
        }
    }
    
    private static final class LookaheadToken
    {
        final Scanner.Token token;
        final int position;
        
        LookaheadToken(final Scanner.Token token, final int position) {
            this.token = token;
            this.position = position;
        }
    }
    
    public enum ExtensionPoint
    {
        OR, 
        AND, 
        EQ, 
        CMP, 
        ADD, 
        MUL, 
        UNARY, 
        LITERAL;
    }
    
    public abstract static class ExtensionHandler
    {
        private final ExtensionPoint point;
        
        public ExtensionHandler(final ExtensionPoint point) {
            this.point = point;
        }
        
        public ExtensionPoint getExtensionPoint() {
            return this.point;
        }
        
        public abstract AstNode createAstNode(final AstNode... p0);
    }
}
