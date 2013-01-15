package cs444.grammar;

import cs444.nfa.NFA;

public class SampleGrammar extends LexicalGrammar {

    public SampleGrammar() {
        
        addPattern("Number", NFA.oneOrMore(NFA.digit()));
        addPattern("Identifier", NFA.oneOrMore(NFA.letter()));
        addPattern("Declaration", NFA.literal("int"));
        addPattern("Equals", NFA.literal("="));
        addPattern("Plus", NFA.literal("+"));
        addPattern("Minus", NFA.literal("-"));
        addPattern("WhiteSpace", NFA.union(NFA.literal(" "), NFA.literal("\t"), NFA.literal("\n"), NFA.literal("\r"), NFA.literal("\r\n")));
    }
}
