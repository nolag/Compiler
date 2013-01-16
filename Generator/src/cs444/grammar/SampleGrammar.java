package cs444.grammar;

import cs444.nfa.NFA;

public class SampleGrammar extends LexicalGrammar {

    public SampleGrammar() {
        addPattern("WHITESPACE", NFA.union(NFA.literal(" "), NFA.literal("\t"),
                NFA.literal("\n"), NFA.literal("\r")));

        //TODO: implement NFA.anything() and review END_LINE_COMMENT NFA
        //		addPattern("COMMENT", NFA.concatenate(NFA.literal("/"), NFA.literal("*"),
        //		                   NFA.zeroOrMore(NFA.anything()),
        //		                   NFA.literal("*"), NFA.literal("/")));
        //
        //		addPattern("END_LINE_COMMENT", NFA.concatenate(NFA.literal("/"), NFA.literal("/"),
        //		                            NFA.zeroOrMore(NFA.anything),
        //		                            NFA.union(NFA.literal("\r"), NFA.literal("\n"))));

        //TODO: add '_' and '$' to NFA.letter (http://docs.oracle.com/javase/specs/jls/se5.0/html/lexical.html#3.8)
        addPattern("ID", NFA.concatenate(NFA.letter(),
                NFA.zeroOrMore(NFA.union(NFA.letter(),
                        NFA.digit()))));

        // TODO: implement NFA.fromStr which create a NFA concatenating all the chars of the string
        //		    	addPattern("ABSTRACT", NFA.fromStr("abstract"));
        //		    	addPattern("CONTINUE", NFA.fromStr("continue"));
        //		    	addPattern("FOR", NFA.fromStr("for"));
        //		    	addPattern("NEW", NFA.fromStr("new"));
        //		    	addPattern("SWITCH", NFA.fromStr("switch"));
        //		    	addPattern("ASSERT", NFA.fromStr("assert"));
        //		    	addPattern("DEFAULT", NFA.fromStr("default"));
        //		    	addPattern("PACKAGE", NFA.fromStr("package"));
        //		    	addPattern("SYNCHRONIZED", NFA.fromStr("synchronized"));
        //		    	addPattern("BOOLEAN", NFA.fromStr("boolean"));
        //		    	addPattern("DO", NFA.fromStr("do"));
        //		    	addPattern("GOTO", NFA.fromStr("goto"));
        //		    	addPattern("PRIVATE", NFA.fromStr("private"));
        //		    	addPattern("THIS", NFA.fromStr("this"));
        //		    	addPattern("BREAK", NFA.fromStr("break"));
        //		    	addPattern("DOUBLE", NFA.fromStr("double"));
        //		    	addPattern("IMPLEMENTS", NFA.fromStr("implements"));
        //		    	addPattern("PROTECTED", NFA.fromStr("protected"));
        //		    	addPattern("THROW", NFA.fromStr("throw"));
        //		    	addPattern("BYTE", NFA.fromStr("byte"));
        //		    	addPattern("ELSE", NFA.fromStr("else"));
        //		    	addPattern("IMPORT", NFA.fromStr("import"));
        //		    	addPattern("PUBLIC", NFA.fromStr("public"));
        //		    	addPattern("THROWS", NFA.fromStr("throws"));
        //		    	addPattern("CASE", NFA.fromStr("case"));
        //		    	addPattern("ENUM", NFA.fromStr("enum"));
        //		    	addPattern("INSTANCEOF", NFA.fromStr("instanceof"));
        //		    	addPattern("RETURN", NFA.fromStr("return"));
        //		    	addPattern("TRANSIENT", NFA.fromStr("transient"));
        //		    	addPattern("CATCH", NFA.fromStr("catch"));
        //		    	addPattern("EXTENDS", NFA.fromStr("extends"));
        //		    	addPattern("INT", NFA.fromStr("int"));
        //		    	addPattern("SHORT", NFA.fromStr("short"));
        //		    	addPattern("TRY", NFA.fromStr("try"));
        //		    	addPattern("CHAR", NFA.fromStr("char"));
        //		    	addPattern("FINAL", NFA.fromStr("final"));
        //		    	addPattern("INTERFACE", NFA.fromStr("interface"));
        //		    	addPattern("STATIC", NFA.fromStr("static"));
        //		    	addPattern("VOID", NFA.fromStr("void"));
        //		    	addPattern("CLASS", NFA.fromStr("class"));
        //		    	addPattern("FINALLY", NFA.fromStr("finally"));
        //		    	addPattern("LONG", NFA.fromStr("long"));
        //		    	addPattern("STRICTFP", NFA.fromStr("strictfp"));
        //		    	addPattern("VOLATILE", NFA.fromStr("volatile"));
        //		    	addPattern("CONST", NFA.fromStr("const"));
        //		    	addPattern("FLOAT", NFA.fromStr("float"));
        //		    	addPattern("NATIVE", NFA.fromStr("super"));
        //		    	addPattern("SUPER", NFA.fromStr("super"));
        //		    	addPattern("WHILE", NFA.fromStr("while"));)

        //Literals
        //TODO: need NFA.nonZeroDigit
        //		    	addPattern("DECIMAL_INTEGER_LITERAL", NFA.union(NFA.literal("0"),
        //		    			NFA.concatenate(NFA.nonZeroDigit(),
        //		    					NFA.zeroOrMore(NFA.digit()))));
        //
        //		    	addPattern("TRUE", NFA.fromStr("true"));
        //		    	addPattern("FALSE", NFA.fromStr("false"));

        //		    	//TODO: singleChar() -> everthing but ', \, \n, or \r
        //		    	//escapeSequence -> \b|\t|\n|\f|\r|\"|\'|\\|(OctalEscape)
        //		    	//http://docs.oracle.com/javase/specs/jls/se5.0/html/lexical.html#101089
        //		    	addPattern("CHAR_LITERAL", NFA.concatenate(NFA.literal("'"),
        //		    			NFA.union(NFA.singleChar(), NFA.escapeSequence(),
        //		    					NFA.literal("'"))));

        //TODO: stringCharacter() -> everything but ", \, \n, or \r
        //		    	addPattern("STR_LITERAL", NFA.concatenate(NFA.literal("\""),
        //		    			NFA.zeroOrMore(NFA.union(NFA.stringCharacter(),
        //		    					NFA.escapeSequence())),
        //		    					NFA.literal("\"")));

        //		    	addPattern("NULL", NFA.fromStr("null"));

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
}
