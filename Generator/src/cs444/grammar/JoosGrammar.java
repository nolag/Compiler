package cs444.grammar;

import cs444.nfa.NFA;

public class JoosGrammar extends LexicalGrammar {

    public JoosGrammar() {

        addPattern("WHITESPACE", NFA.union(NFA.literal(" "), NFA.literal("\t"),
                                           NFA.literal("\n"), NFA.literal("\r")));

        // Traditional comment pattern: /\*([^*]|(\*+([^*/])))*\*+/
        addPattern("COMMENT", NFA.concatenate(NFA.literal("/*"), 
        		NFA.zeroOrMore(NFA.union(anythingButStar(), 
        				NFA.concatenate(NFA.oneOrMore(NFA.literal("*")), anythingButSlash()))),
        		NFA.oneOrMore(NFA.literal("*")), NFA.literal("/")));

        addPattern("END_LINE_COMMENT", NFA.concatenate(NFA.literal("//"),
                                                       NFA.zeroOrMore(anythingButEndOfLine()),
                                                       NFA.union(NFA.literal("\r"), NFA.literal("\n"))));

        addPattern("ID", NFA.concatenate(NFA.letter(),
                                         NFA.zeroOrMore(NFA.union(NFA.letter(),
                                                                  NFA.digit()))));

        addPattern("IF", NFA.literal("if"));
        addPattern("ABSTRACT", NFA.literal("abstract"));
        addPattern("CONTINUE", NFA.literal("continue"));
        addPattern("FOR", NFA.literal("for"));
        addPattern("NEW", NFA.literal("new"));
        addPattern("SWITCH", NFA.literal("switch"));
        addPattern("ASSERT", NFA.literal("assert"));
        addPattern("DEFAULT", NFA.literal("default"));
        addPattern("PACKAGE", NFA.literal("package"));
        addPattern("SYNCHRONIZED", NFA.literal("synchronized"));
        addPattern("BOOLEAN", NFA.literal("boolean"));
        addPattern("DO", NFA.literal("do"));
        addPattern("GOTO", NFA.literal("goto"));
        addPattern("PRIVATE", NFA.literal("private"));
        addPattern("THIS", NFA.literal("this"));
        addPattern("BREAK", NFA.literal("break"));
        addPattern("DOUBLE", NFA.literal("double"));
        addPattern("IMPLEMENTS", NFA.literal("implements"));
        addPattern("PROTECTED", NFA.literal("protected"));
        addPattern("THROW", NFA.literal("throw"));
        addPattern("BYTE", NFA.literal("byte"));
        addPattern("ELSE", NFA.literal("else"));
        addPattern("IMPORT", NFA.literal("import"));
        addPattern("PUBLIC", NFA.literal("public"));
        addPattern("THROWS", NFA.literal("throws"));
        addPattern("CASE", NFA.literal("case"));
        addPattern("ENUM", NFA.literal("enum"));
        addPattern("INSTANCEOF", NFA.literal("instanceof"));
        addPattern("RETURN", NFA.literal("return"));
        addPattern("TRANSIENT", NFA.literal("transient"));
        addPattern("CATCH", NFA.literal("catch"));
        addPattern("EXTENDS", NFA.literal("extends"));
        addPattern("INT", NFA.literal("int"));
        addPattern("SHORT", NFA.literal("short"));
        addPattern("TRY", NFA.literal("try"));
        addPattern("CHAR", NFA.literal("char"));
        addPattern("FINAL", NFA.literal("final"));
        addPattern("INTERFACE", NFA.literal("interface"));
        addPattern("STATIC", NFA.literal("static"));
        addPattern("VOID", NFA.literal("void"));
        addPattern("CLASS", NFA.literal("class"));
        addPattern("FINALLY", NFA.literal("finally"));
        addPattern("LONG", NFA.literal("long"));
        addPattern("STRICTFP", NFA.literal("strictfp"));
        addPattern("VOLATILE", NFA.literal("volatile"));
        addPattern("CONST", NFA.literal("const"));
        addPattern("FLOAT", NFA.literal("float"));
        addPattern("NATIVE", NFA.literal("native"));
        addPattern("SUPER", NFA.literal("super"));
        addPattern("WHILE", NFA.literal("while"));

        //Literals
        addPattern("DECIMAL_INTEGER_LITERAL", NFA.union(NFA.literal("0"),
                                                        NFA.concatenate(NFA.nonZeroDigit(),
                                                                        NFA.zeroOrMore(NFA.digit()))));

        addPattern("TRUE", NFA.literal("true"));
        addPattern("FALSE", NFA.literal("false"));

        addPattern("CHAR_LITERAL", NFA.concatenate(NFA.literal("'"),
                                                   NFA.union(NFA.singleChar(), escapeSequence()),
                                                   NFA.literal("'")));

        addPattern("STR_LITERAL", NFA.concatenate(NFA.literal("\""),
                                                  NFA.zeroOrMore(NFA.union(NFA.stringCharacter(),
                                                                           escapeSequence())),
                                                  NFA.literal("\"")));

        addPattern("NULL", NFA.literal("null"));

        //Separators
        addPattern("LPAREN", NFA.literal("("));
        addPattern("RPAREN", NFA.literal(")"));
        addPattern("LBRACE", NFA.literal("{"));
        addPattern("RBRACE", NFA.literal("}"));
        addPattern("LBRACKET", NFA.literal("["));
        addPattern("RBRACKET", NFA.literal("]"));
        addPattern("SEMI", NFA.literal(";"));
        addPattern("COMMA", NFA.literal(","));
        addPattern("DOT", NFA.literal("."));

        //Operators
        addPattern("BECOMES", NFA.literal("="));
        //arithmetic operators
        addPattern("PLUS", NFA.literal("+"));
        addPattern("MINUS", NFA.literal("-"));
        addPattern("STAR", NFA.literal("*"));
        addPattern("SLASH", NFA.literal("/"));
        addPattern("PCT", NFA.literal("%"));

        //comparison operators
        addPattern("LT", NFA.literal("<"));
        addPattern("GT", NFA.literal(">"));
        addPattern("LE", NFA.literal("<="));
        addPattern("GE", NFA.literal(">="));
        addPattern("EQ", NFA.literal("=="));
        addPattern("NE", NFA.literal("!="));

        //eager boolean operators
        addPattern("AMPERSAND", NFA.literal("&"));
        addPattern("PIPE", NFA.literal("|"));
        addPattern("EXCLAMATION", NFA.literal("!"));

        //lazy boolean operators
        addPattern("DAMPERSAND", NFA.literal("&&"));
        addPattern("DPIPE", NFA.literal("||"));
    }

    private NFA anythingButEndOfLine() {
		return NFA.union(NFA.acceptRange((char)0, (char)9),
				NFA.acceptRange((char)11, (char)12),
				NFA.acceptRange((char)14, (char)127));
	}

	private NFA anythingButSlash() {
		// / is 47
    	return NFA.union(NFA.acceptRange((char)0, (char)46),
    			NFA.acceptRange((char)48, (char)127));
	}

	private NFA anythingButStar() {
		// * is 42
		return NFA.union(NFA.acceptRange((char)0, (char)41), 
				NFA.acceptRange((char)43, (char)127));
	}

	private NFA escapeSequence() {
        // escapeSequence -> \b|\t|\n|\f|\r|\"|\'|\\|(OctalEscape)
        // http://docs.oracle.com/javase/specs/jls/se5.0/html/lexical.html#101089
        return NFA.concatenate(NFA.literal("\\"),
                               NFA.union(NFA.literal("b"),
                                         NFA.literal("t"),
                                         NFA.literal("n"),
                                         NFA.literal("f"),
                                         NFA.literal("r"),
                                         NFA.literal("\""),
                                         NFA.literal("'"),
                                         NFA.literal("\\"),
                                         octal()));
    }

    private NFA octal() {
        return NFA.union(NFA.acceptRange('0', '7'),
                         NFA.concatenate(NFA.acceptRange('0', '7'),
                                         NFA.acceptRange('0', '7')),
                         NFA.concatenate(NFA.acceptRange('0', '3'),
                                         NFA.acceptRange('0', '7'),
                                         NFA.acceptRange('0', '7')));
    }
}
