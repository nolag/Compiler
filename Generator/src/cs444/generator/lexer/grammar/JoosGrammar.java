package cs444.generator.lexer.grammar;

import cs444.generator.lexer.grammar.TokenMetadata.Type;
import cs444.generator.lexer.nfa.NFA;

public class JoosGrammar extends LexicalGrammar {

    public JoosGrammar() {

        addPattern("WHITESPACE", NFA.union(NFA.literal(" "), NFA.literal("\t"),
                                           NFA.literal("\n"), NFA.literal("\r")), Type.IGNORE);

        // Traditional comment pattern: /\*([^*]|(\*+([^*/])))*\*+/
        addPattern("COMMENT", NFA.concatenate(NFA.literal("/*"),
        		NFA.zeroOrMore(NFA.union(anythingButStar(),
        				NFA.concatenate(NFA.oneOrMore(NFA.literal("*")), anythingButSlash()))),
        		NFA.oneOrMore(NFA.literal("*")), NFA.literal("/")), Type.IGNORE);

        addPattern("END_LINE_COMMENT", NFA.concatenate(NFA.literal("//"),
                                                       NFA.zeroOrMore(anythingButEndOfLine()),
                                                       NFA.union(NFA.literal("\r"), NFA.literal("\n"))), Type.IGNORE);

        addPattern("ID", NFA.concatenate(NFA.letter(),
                                         NFA.zeroOrMore(NFA.union(NFA.letter(),
                                                                  NFA.digit()))), Type.VALID);

        addPattern("IF", NFA.literal("if"), Type.VALID);
        addPattern("ABSTRACT", NFA.literal("abstract"), Type.VALID);
        addPattern("CONTINUE", NFA.literal("continue"), Type.VALID);
        addPattern("FOR", NFA.literal("for"), Type.VALID);
        addPattern("NEW", NFA.literal("new"), Type.VALID);
        addPattern("SWITCH", NFA.literal("switch"), Type.VALID);
        addPattern("ASSERT", NFA.literal("assert"), Type.VALID);
        addPattern("DEFAULT", NFA.literal("default"), Type.VALID);
        addPattern("PACKAGE", NFA.literal("package"), Type.VALID);
        addPattern("SYNCHRONIZED", NFA.literal("synchronized"), Type.VALID);
        addPattern("BOOLEAN", NFA.literal("boolean"), Type.VALID);
        addPattern("DO", NFA.literal("do"), Type.VALID);
        addPattern("GOTO", NFA.literal("goto"), Type.VALID);
        addPattern("PRIVATE", NFA.literal("private"), Type.VALID);
        addPattern("THIS", NFA.literal("this"), Type.VALID);
        addPattern("BREAK", NFA.literal("break"), Type.VALID);
        addPattern("DOUBLE", NFA.literal("double"), Type.VALID);
        addPattern("IMPLEMENTS", NFA.literal("implements"), Type.VALID);
        addPattern("PROTECTED", NFA.literal("protected"), Type.VALID);
        addPattern("THROW", NFA.literal("throw"), Type.VALID);
        addPattern("BYTE", NFA.literal("byte"), Type.VALID);
        addPattern("ELSE", NFA.literal("else"), Type.VALID);
        addPattern("IMPORT", NFA.literal("import"), Type.VALID);
        addPattern("PUBLIC", NFA.literal("public"), Type.VALID);
        addPattern("THROWS", NFA.literal("throws"), Type.VALID);
        addPattern("CASE", NFA.literal("case"), Type.VALID);
        addPattern("ENUM", NFA.literal("enum"), Type.VALID);
        addPattern("INSTANCEOF", NFA.literal("instanceof"), Type.VALID);
        addPattern("RETURN", NFA.literal("return"), Type.VALID);
        addPattern("TRANSIENT", NFA.literal("transient"), Type.VALID);
        addPattern("CATCH", NFA.literal("catch"), Type.VALID);
        addPattern("EXTENDS", NFA.literal("extends"), Type.SYNTAX_ONLY);
        addPattern("INT", NFA.literal("int"), Type.VALID);
        addPattern("SHORT", NFA.literal("short"), Type.VALID);
        addPattern("TRY", NFA.literal("try"), Type.VALID);
        addPattern("CHAR", NFA.literal("char"), Type.VALID);
        addPattern("FINAL", NFA.literal("final"), Type.VALID);
        addPattern("INTERFACE", NFA.literal("interface"), Type.SYNTAX_ONLY);
        addPattern("STATIC", NFA.literal("static"), Type.VALID);
        addPattern("VOID", NFA.literal("void"), Type.VALID);
        addPattern("CLASS", NFA.literal("class"), Type.SYNTAX_ONLY);
        addPattern("FINALLY", NFA.literal("finally"), Type.VALID);
        addPattern("LONG", NFA.literal("long"), Type.VALID);
        addPattern("STRICTFP", NFA.literal("strictfp"), Type.VALID);
        addPattern("VOLATILE", NFA.literal("volatile"), Type.VALID);
        addPattern("CONST", NFA.literal("const"), Type.VALID);
        addPattern("FLOAT", NFA.literal("float"), Type.VALID);
        addPattern("NATIVE", NFA.literal("native"), Type.VALID);
        addPattern("SUPER", NFA.literal("super"), Type.VALID);
        addPattern("WHILE", NFA.literal("while"), Type.VALID);

        //Literals
        addPattern("DECIMAL_INTEGER_LITERAL", NFA.union(NFA.literal("0"),
                                                        NFA.concatenate(NFA.nonZeroDigit(),
                                                                        NFA.zeroOrMore(NFA.digit()))), Type.VALID);

        addPattern("TRUE", NFA.literal("true"), Type.VALID);
        addPattern("FALSE", NFA.literal("false"), Type.VALID);

        addPattern("CHAR_LITERAL", NFA.concatenate(NFA.literal("'"),
                                                   NFA.union(NFA.singleChar(), escapeSequence()),
                                                   NFA.literal("'")), Type.VALID);

        addPattern("STR_LITERAL", NFA.concatenate(NFA.literal("\""),
                                                  NFA.zeroOrMore(NFA.union(NFA.stringCharacter(),
                                                                           escapeSequence())),
                                                  NFA.literal("\"")), Type.VALID);

        addPattern("NULL", NFA.literal("null"), Type.VALID);

        //Separators
        addPattern("LPAREN", NFA.literal("("), Type.SYNTAX_ONLY);
        addPattern("RPAREN", NFA.literal(")"), Type.SYNTAX_ONLY);
        addPattern("LBRACE", NFA.literal("{"), Type.SYNTAX_ONLY);
        addPattern("RBRACE", NFA.literal("}"), Type.SYNTAX_ONLY);
        addPattern("LBRACKET", NFA.literal("["), Type.SYNTAX_ONLY);
        addPattern("RBRACKET", NFA.literal("]"), Type.SYNTAX_ONLY);
        addPattern("SEMI", NFA.literal(";"), Type.SYNTAX_ONLY);
        addPattern("COMMA", NFA.literal(","), Type.SYNTAX_ONLY);
        addPattern("DOT", NFA.literal("."), Type.SYNTAX_ONLY);

        //Operators
        addPattern("BECOMES", NFA.literal("="), Type.VALID);
        //arithmetic operators
        addPattern("PLUS", NFA.literal("+"), Type.VALID);
        addPattern("MINUS", NFA.literal("-"), Type.VALID);
        addPattern("STAR", NFA.literal("*"), Type.VALID);
        addPattern("SLASH", NFA.literal("/"), Type.VALID);
        addPattern("PCT", NFA.literal("%"), Type.VALID);

        //comparison operators
        addPattern("LT", NFA.literal("<"), Type.VALID);
        addPattern("GT", NFA.literal(">"), Type.VALID);
        addPattern("LE", NFA.literal("<="), Type.VALID);
        addPattern("GE", NFA.literal(">="), Type.VALID);
        addPattern("EQ", NFA.literal("=="), Type.VALID);
        addPattern("NE", NFA.literal("!="), Type.VALID);

        //eager boolean operators
        addPattern("AMPERSAND", NFA.literal("&"), Type.VALID);
        addPattern("PIPE", NFA.literal("|"), Type.VALID);
        addPattern("EXCLAMATION", NFA.literal("!"), Type.VALID);

        //lazy boolean operators
        addPattern("DAMPERSAND", NFA.literal("&&"), Type.VALID);
        addPattern("DPIPE", NFA.literal("||"), Type.VALID);
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
