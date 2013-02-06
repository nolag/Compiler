package cs444.cfgrulesgenerator.lexer;

import cs444.generator.lexer.grammar.LexicalGrammar;
import cs444.generator.lexer.grammar.TokenMetadata.Type;
import cs444.generator.lexer.nfa.NFA;

public class BNFGrammar extends LexicalGrammar {

    public BNFGrammar() {

        addPattern("EPSILON", NFA.literal(""), Type.VALID);

        addPattern("WHITESPACE", NFA.union(NFA.literal(" "), NFA.literal("\t")), Type.IGNORE);

        addPattern("NEWLINE", NFA.union(NFA.literal("\n"), NFA.literal("\r")), Type.VALID);


        addPattern("END_LINE_COMMENT", NFA.concatenate(NFA.literal("//"),
                                                       NFA.zeroOrMore(anythingButEndOfLine()),
                                                       NFA.union(NFA.literal("\r"),
                                                                 NFA.literal("\n"))),
                   Type.IGNORE);

        addPattern("LHS", NFA.concatenate(NFA.capitalLetter(),
                                          NFA.zeroOrMore(NFA.union(NFA.letter(), NFA.digit())),
                                          NFA.literal(":")), Type.VALID);

        // this covers all keywords, and also lparen, rparen, lbrace, rbrace, lbracket,
        // rbracket, pipe, literals
        addPattern("TERMINAL", NFA.oneOrMore(NFA.union(NFA.literal("_"),
                                                       NFA.smallLetter())), Type.VALID);

        addPattern("NON_TERMINAL", NFA.concatenate(NFA.capitalLetter(),
                                                   NFA.zeroOrMore(NFA.union(NFA.literal("_"),
                                                                            NFA.letter(),
                                                                            NFA.digit()))),
                   Type.VALID);

        // Special characters. This are chars that have meaning in BNF, so for the actual symbols in
        // the Joos grammar, we will be using TERMINAL token
        addPattern("PIPE", NFA.literal("|"), Type.SYNTAX_ONLY);
        addPattern("LPAREN", NFA.literal("("), Type.SYNTAX_ONLY);
        addPattern("RPAREN", NFA.literal(")"), Type.SYNTAX_ONLY);
        addPattern("LBRACE", NFA.literal("{"), Type.SYNTAX_ONLY);
        addPattern("RBRACE", NFA.literal("}"), Type.SYNTAX_ONLY);
        addPattern("LBRACKET", NFA.literal("["), Type.SYNTAX_ONLY);
        addPattern("RBRACKET", NFA.literal("]"), Type.SYNTAX_ONLY);

        // Other terminals
        addPattern("SEMI", NFA.literal(";"), Type.VALID);
        addPattern("COMMA", NFA.literal(","), Type.VALID);
        addPattern("DOT", NFA.literal("."), Type.VALID);

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
}
