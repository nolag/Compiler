package cs444;

import java.io.IOException;
import java.io.Writer;

import cs444.dfa.DFAState;
import cs444.grammar.LexicalGrammar;

public class LexerClassGenerator extends Generator {
    
    private final LexicalGrammar grammar;
    
    LexerClassGenerator(LexicalGrammar grammar, Writer writer) {
        super(writer);
        
        this.grammar = grammar;
    }
    
    public void generate() throws IOException {
        
        writeLine("public class Lexer {");
        indent();
        
        generateTokenFactories();
        
        writeLine("private final boolean[] acceptTable =");
        generateAcceptTable();
        
        writeLine("private final int[][] stateTable =");
        generateStateTable();
        
        writeLine("private final int initialState;");
        writeLine("private final Reader reader;");
        writeLine("private int nextChar;");
        
        writeLine("public Lexer(Reader reader) throws IOException {");
        indent();
                
        writeLine("this.reader = reader;");
        writeLine("nextChar = reader.read();");
        writeLine("initialState = 0;");
        
        dedent();
        writeLine("}");
        
        writeLine("public Token getNextToken() throws Exception {");
        indent();
        
        writeLine("String lexeme = \"\";");
        writeLine("int state = initialState;");
            
        writeLine("while (nextChar != -1) {");
        indent();    
        
        writeLine("int previewState = stateTable[state][nextChar];");
            
        writeLine("if (previewState == -1) {");
        indent();        
        
        writeLine("if (acceptTable[state])");
        indent();
        writeLine("return new Token(Token.Id.Declaration, lexeme);");
        dedent();
        writeLine("else");
        indent();
        writeLine("throw new Exception(\"Lexer Error\");");
        dedent();
        dedent();
        writeLine("} else {");
        indent();
        writeLine("lexeme += (char)nextChar;");
        writeLine("nextChar = reader.read();");
        writeLine("state = previewState;");
        dedent();
        writeLine("}");
        
        dedent();
        writeLine("}");
        
        writeLine("if (acceptTable[state])");
        
        indent();
        writeLine("return new Token(Token.Id.Declaration, lexeme);");
        dedent();
        
        writeLine("return null;");
        
        dedent();
        writeLine("}");
        
        dedent();
        writeLine("}");
    }

    private void generateTokenFactories() throws IOException {
        
        for (String name : grammar.getTokenNames()) {
            
            writeLine("private final Token.Factory " + name + "Factory = new Token.Factory {");
            indent();    
            writeLine("@Override");
            writeLine("public Token create(String lexeme) {");
            indent();
            writeLine("return new Token(Token.Id." + name + ", lexeme);");
            dedent();
            writeLine("}");
            dedent();
            writeLine("}");
        }

        writeLine("private static Token.Factory getFactory(int state) {");
        indent();
        writeLine("switch (state) {");
           
        for (String name : grammar.getTokenNames()) {
            writeLine("case " + grammar.getAcceptingState(name) + ": return " + name + "Factory;");
        }
                
        writeLine("}");
        writeLine("return null;");
        dedent();
        writeLine("}");
    }

    private void generateStateTable() {
        
//result += "{ ";
//        
//        for (int i = 0; i < states.size(); i++) {
//            
//            result += "  { ";
//            
//            int[] row = states.get(i).getTransitionRow();
//            for (int j = 0; j < row.length; j++) {
//                
//                result += row[j];
//                
//                if (j < row.length - 1)
//                    result += ", ";
//            }
//            
//            result += " }";
//            
//            if (i < states.size() - 1)
//                result += ",\n";
//        }
//        
//        result += " };\n";
//        
//        return result;
    }

    private void generateAcceptTable() {
        
/*        result += "{ ";
        
        for (DFAState state : states) {
            result += state.isAccepting();
            
            if (state.getId() != states.size() - 1)
                result += ", ";
        }
        
        result += " };\n\n";*/
    }
}
