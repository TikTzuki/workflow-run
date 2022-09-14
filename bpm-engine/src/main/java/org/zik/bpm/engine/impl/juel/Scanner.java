// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import java.util.HashMap;

public class Scanner
{
    private static final HashMap<String, Token> KEYMAP;
    private static final HashMap<Symbol, Token> FIXMAP;
    private Token token;
    private int position;
    private final String input;
    protected final StringBuilder builder;
    
    private static void addFixToken(final Token token) {
        Scanner.FIXMAP.put(token.getSymbol(), token);
    }
    
    private static void addKeyToken(final Token token) {
        Scanner.KEYMAP.put(token.getImage(), token);
    }
    
    protected Scanner(final String input) {
        this.builder = new StringBuilder();
        this.input = input;
    }
    
    public String getInput() {
        return this.input;
    }
    
    public Token getToken() {
        return this.token;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    protected boolean isDigit(final char c) {
        return c >= '0' && c <= '9';
    }
    
    protected Token keyword(final String s) {
        return Scanner.KEYMAP.get(s);
    }
    
    protected Token fixed(final Symbol symbol) {
        return Scanner.FIXMAP.get(symbol);
    }
    
    protected Token token(final Symbol symbol, final String value, final int length) {
        return new Token(symbol, value, length);
    }
    
    protected boolean isEval() {
        return this.token != null && this.token.getSymbol() != Symbol.TEXT && this.token.getSymbol() != Symbol.END_EVAL;
    }
    
    protected Token nextText() throws ScanException {
        this.builder.setLength(0);
        int i = this.position;
        final int l = this.input.length();
        boolean escaped = false;
        while (i < l) {
            final char c = this.input.charAt(i);
            switch (c) {
                case '\\': {
                    if (escaped) {
                        this.builder.append('\\');
                        break;
                    }
                    escaped = true;
                    break;
                }
                case '#':
                case '$': {
                    if (i + 1 < l && this.input.charAt(i + 1) == '{') {
                        if (!escaped) {
                            return this.token(Symbol.TEXT, this.builder.toString(), i - this.position);
                        }
                        this.builder.append(c);
                    }
                    else {
                        if (escaped) {
                            this.builder.append('\\');
                        }
                        this.builder.append(c);
                    }
                    escaped = false;
                    break;
                }
                default: {
                    if (escaped) {
                        this.builder.append('\\');
                    }
                    this.builder.append(c);
                    escaped = false;
                    break;
                }
            }
            ++i;
        }
        if (escaped) {
            this.builder.append('\\');
        }
        return this.token(Symbol.TEXT, this.builder.toString(), i - this.position);
    }
    
    protected Token nextString() throws ScanException {
        this.builder.setLength(0);
        final char quote = this.input.charAt(this.position);
        int i = this.position + 1;
        final int l = this.input.length();
        while (i < l) {
            char c = this.input.charAt(i++);
            if (c == '\\') {
                if (i == l) {
                    throw new ScanException(this.position, "unterminated string", quote + " or \\");
                }
                c = this.input.charAt(i++);
                if (c != '\\' && c != quote) {
                    throw new ScanException(this.position, "invalid escape sequence \\" + c, "\\" + quote + " or \\\\");
                }
                this.builder.append(c);
            }
            else {
                if (c == quote) {
                    return this.token(Symbol.STRING, this.builder.toString(), i - this.position);
                }
                this.builder.append(c);
            }
        }
        throw new ScanException(this.position, "unterminated string", String.valueOf(quote));
    }
    
    protected Token nextNumber() throws ScanException {
        int i;
        int l;
        for (i = this.position, l = this.input.length(); i < l && this.isDigit(this.input.charAt(i)); ++i) {}
        Symbol symbol = Symbol.INTEGER;
        if (i < l && this.input.charAt(i) == '.') {
            ++i;
            while (i < l && this.isDigit(this.input.charAt(i))) {
                ++i;
            }
            symbol = Symbol.FLOAT;
        }
        if (i < l && (this.input.charAt(i) == 'e' || this.input.charAt(i) == 'E')) {
            final int e = i;
            if (++i < l && (this.input.charAt(i) == '+' || this.input.charAt(i) == '-')) {
                ++i;
            }
            if (i < l && this.isDigit(this.input.charAt(i))) {
                ++i;
                while (i < l && this.isDigit(this.input.charAt(i))) {
                    ++i;
                }
                symbol = Symbol.FLOAT;
            }
            else {
                i = e;
            }
        }
        return this.token(symbol, this.input.substring(this.position, i), i - this.position);
    }
    
    protected Token nextEval() throws ScanException {
        final char c1 = this.input.charAt(this.position);
        final char c2 = (this.position < this.input.length() - 1) ? this.input.charAt(this.position + 1) : '\0';
        switch (c1) {
            case '*': {
                return this.fixed(Symbol.MUL);
            }
            case '/': {
                return this.fixed(Symbol.DIV);
            }
            case '%': {
                return this.fixed(Symbol.MOD);
            }
            case '+': {
                return this.fixed(Symbol.PLUS);
            }
            case '-': {
                return this.fixed(Symbol.MINUS);
            }
            case '?': {
                return this.fixed(Symbol.QUESTION);
            }
            case ':': {
                return this.fixed(Symbol.COLON);
            }
            case '[': {
                return this.fixed(Symbol.LBRACK);
            }
            case ']': {
                return this.fixed(Symbol.RBRACK);
            }
            case '(': {
                return this.fixed(Symbol.LPAREN);
            }
            case ')': {
                return this.fixed(Symbol.RPAREN);
            }
            case ',': {
                return this.fixed(Symbol.COMMA);
            }
            case '.': {
                if (!this.isDigit(c2)) {
                    return this.fixed(Symbol.DOT);
                }
                break;
            }
            case '=': {
                if (c2 == '=') {
                    return this.fixed(Symbol.EQ);
                }
                break;
            }
            case '&': {
                if (c2 == '&') {
                    return this.fixed(Symbol.AND);
                }
                break;
            }
            case '|': {
                if (c2 == '|') {
                    return this.fixed(Symbol.OR);
                }
                break;
            }
            case '!': {
                if (c2 == '=') {
                    return this.fixed(Symbol.NE);
                }
                return this.fixed(Symbol.NOT);
            }
            case '<': {
                if (c2 == '=') {
                    return this.fixed(Symbol.LE);
                }
                return this.fixed(Symbol.LT);
            }
            case '>': {
                if (c2 == '=') {
                    return this.fixed(Symbol.GE);
                }
                return this.fixed(Symbol.GT);
            }
            case '\"':
            case '\'': {
                return this.nextString();
            }
        }
        if (this.isDigit(c1) || c1 == '.') {
            return this.nextNumber();
        }
        if (Character.isJavaIdentifierStart(c1)) {
            int i = this.position + 1;
            for (int l = this.input.length(); i < l && Character.isJavaIdentifierPart(this.input.charAt(i)); ++i) {}
            final String name = this.input.substring(this.position, i);
            final Token keyword = this.keyword(name);
            return (keyword == null) ? this.token(Symbol.IDENTIFIER, name, i - this.position) : keyword;
        }
        throw new ScanException(this.position, "invalid character '" + c1 + "'", "expression token");
    }
    
    protected Token nextToken() throws ScanException {
        if (!this.isEval()) {
            if (this.position + 1 < this.input.length() && this.input.charAt(this.position + 1) == '{') {
                switch (this.input.charAt(this.position)) {
                    case '#': {
                        return this.fixed(Symbol.START_EVAL_DEFERRED);
                    }
                    case '$': {
                        return this.fixed(Symbol.START_EVAL_DYNAMIC);
                    }
                }
            }
            return this.nextText();
        }
        if (this.input.charAt(this.position) == '}') {
            return this.fixed(Symbol.END_EVAL);
        }
        return this.nextEval();
    }
    
    public Token next() throws ScanException {
        if (this.token != null) {
            this.position += this.token.getSize();
        }
        final int length = this.input.length();
        if (this.isEval()) {
            while (this.position < length && Character.isWhitespace(this.input.charAt(this.position))) {
                ++this.position;
            }
        }
        if (this.position == length) {
            return this.token = this.fixed(Symbol.EOF);
        }
        return this.token = this.nextToken();
    }
    
    static {
        KEYMAP = new HashMap<String, Token>();
        FIXMAP = new HashMap<Symbol, Token>();
        addFixToken(new Token(Symbol.PLUS, "+"));
        addFixToken(new Token(Symbol.MINUS, "-"));
        addFixToken(new Token(Symbol.MUL, "*"));
        addFixToken(new Token(Symbol.DIV, "/"));
        addFixToken(new Token(Symbol.MOD, "%"));
        addFixToken(new Token(Symbol.LPAREN, "("));
        addFixToken(new Token(Symbol.RPAREN, ")"));
        addFixToken(new Token(Symbol.NOT, "!"));
        addFixToken(new Token(Symbol.AND, "&&"));
        addFixToken(new Token(Symbol.OR, "||"));
        addFixToken(new Token(Symbol.EQ, "=="));
        addFixToken(new Token(Symbol.NE, "!="));
        addFixToken(new Token(Symbol.LT, "<"));
        addFixToken(new Token(Symbol.LE, "<="));
        addFixToken(new Token(Symbol.GT, ">"));
        addFixToken(new Token(Symbol.GE, ">="));
        addFixToken(new Token(Symbol.QUESTION, "?"));
        addFixToken(new Token(Symbol.COLON, ":"));
        addFixToken(new Token(Symbol.COMMA, ","));
        addFixToken(new Token(Symbol.DOT, "."));
        addFixToken(new Token(Symbol.LBRACK, "["));
        addFixToken(new Token(Symbol.RBRACK, "]"));
        addFixToken(new Token(Symbol.START_EVAL_DEFERRED, "#{"));
        addFixToken(new Token(Symbol.START_EVAL_DYNAMIC, "${"));
        addFixToken(new Token(Symbol.END_EVAL, "}"));
        addFixToken(new Token(Symbol.EOF, null, 0));
        addKeyToken(new Token(Symbol.NULL, "null"));
        addKeyToken(new Token(Symbol.TRUE, "true"));
        addKeyToken(new Token(Symbol.FALSE, "false"));
        addKeyToken(new Token(Symbol.EMPTY, "empty"));
        addKeyToken(new Token(Symbol.DIV, "div"));
        addKeyToken(new Token(Symbol.MOD, "mod"));
        addKeyToken(new Token(Symbol.NOT, "not"));
        addKeyToken(new Token(Symbol.AND, "and"));
        addKeyToken(new Token(Symbol.OR, "or"));
        addKeyToken(new Token(Symbol.LE, "le"));
        addKeyToken(new Token(Symbol.LT, "lt"));
        addKeyToken(new Token(Symbol.EQ, "eq"));
        addKeyToken(new Token(Symbol.NE, "ne"));
        addKeyToken(new Token(Symbol.GE, "ge"));
        addKeyToken(new Token(Symbol.GT, "gt"));
        addKeyToken(new Token(Symbol.INSTANCEOF, "instanceof"));
    }
    
    public static class ScanException extends Exception
    {
        final int position;
        final String encountered;
        final String expected;
        
        public ScanException(final int position, final String encountered, final String expected) {
            super(LocalMessages.get("error.scan", position, encountered, expected));
            this.position = position;
            this.encountered = encountered;
            this.expected = expected;
        }
    }
    
    public static class Token
    {
        private final Symbol symbol;
        private final String image;
        private final int length;
        
        public Token(final Symbol symbol, final String image) {
            this(symbol, image, image.length());
        }
        
        public Token(final Symbol symbol, final String image, final int length) {
            this.symbol = symbol;
            this.image = image;
            this.length = length;
        }
        
        public Symbol getSymbol() {
            return this.symbol;
        }
        
        public String getImage() {
            return this.image;
        }
        
        public int getSize() {
            return this.length;
        }
        
        @Override
        public String toString() {
            return this.symbol.toString();
        }
    }
    
    public static class ExtensionToken extends Token
    {
        public ExtensionToken(final String image) {
            super(Symbol.EXTENSION, image);
        }
    }
    
    public enum Symbol
    {
        EOF, 
        PLUS("'+'"), 
        MINUS("'-'"), 
        MUL("'*'"), 
        DIV("'/'|'div'"), 
        MOD("'%'|'mod'"), 
        LPAREN("'('"), 
        RPAREN("')'"), 
        IDENTIFIER, 
        NOT("'!'|'not'"), 
        AND("'&&'|'and'"), 
        OR("'||'|'or'"), 
        EMPTY("'empty'"), 
        INSTANCEOF("'instanceof'"), 
        INTEGER, 
        FLOAT, 
        TRUE("'true'"), 
        FALSE("'false'"), 
        STRING, 
        NULL("'null'"), 
        LE("'<='|'le'"), 
        LT("'<'|'lt'"), 
        GE("'>='|'ge'"), 
        GT("'>'|'gt'"), 
        EQ("'=='|'eq'"), 
        NE("'!='|'ne'"), 
        QUESTION("'?'"), 
        COLON("':'"), 
        TEXT, 
        DOT("'.'"), 
        LBRACK("'['"), 
        RBRACK("']'"), 
        COMMA("','"), 
        START_EVAL_DEFERRED("'#{'"), 
        START_EVAL_DYNAMIC("'${'"), 
        END_EVAL("'}'"), 
        EXTENSION;
        
        private final String string;
        
        private Symbol() {
            this(null);
        }
        
        private Symbol(final String string) {
            this.string = string;
        }
        
        @Override
        public String toString() {
            return (this.string == null) ? ("<" + this.name() + ">") : this.string;
        }
    }
}
