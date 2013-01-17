package cs444;

import java.io.IOException;
import java.io.Writer;

import cs444.grammar.LexicalGrammar;
import cs444.grammar.TokenMetadata;

public class LexerClassGenerator extends Generator {
    
    private final LexicalGrammar grammar;
    
    LexerClassGenerator(LexicalGrammar grammar, Writer writer) {
        super(writer);
        
        this.grammar = grammar;
    }
    
    public void generate() throws IOException {
        
        writeLine("public class Lexer {");
        indent();
        
        writeLine("private static Lexer instance;");
        writeLine("public static void load(Reader reader) throws IOException {");
        indent();
        writeLine("if (null == instance) instance = new Lexer(reader);");
        dedent();
        writeLine("}");
        
        writeLine("public static Lexer getInstance() {");
        indent();
        writeLine("return instance;");
        dedent();
        writeLine("}");    
        
        writeLine("private class LexerState {");
        indent();
        writeLine("private final LexerState[] transitions;");
        writeLine("private final boolean accepting;");
        writeLine("private Token.Type type;");
        writeLine("public LexerState(LexerState[] transitions, boolean accepting) {");
        indent();
        writeLine("this.transitions = transitions;");
        writeLine("this.accepting = accepting;");
        dedent();
        writeLine("}");
        writeLine("public LexerState(LexerState[] transitions, boolean accepting, Token.Type type) {");
        indent();
        writeLine("this(transitions, accepting);");
        writeLine("this.type = type;");
        dedent();
        writeLine("}");
        writeLine("public final LexerState getNextState(int ch) {");
        indent();
        writeLine("return transitions[ch];");
        dedent();
        writeLine("}");
        writeLine("public final boolean isAccepting() {");
        indent();
        writeLine("return accepting;");
        dedent();
        writeLine("}");
        writeLine("public final Token createToken(String lexeme) {");
        writeLine("return new Token(type, lexeme);");
        writeLine("}");
        dedent();
        writeLine("}");
        
        // States
        
        int[][] stateTable = grammar.getStateTable();
        int[] acceptTable = grammar.getAcceptTable();
        TokenMetadata[] data = grammar.getTokenMetadata();
        
        for (int i = 0; i < stateTable.length; i++) {
            
            String transitions = "{ ";
            
            int[] row = stateTable[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] != -1)
                    transitions += "State" + row[j] + ".getInstance()";
                else
                    transitions += "null";
                
                if (j < row.length - 1)
                    transitions += ", ";
            }
            
            transitions += " }";
            
            boolean accepting = (acceptTable[i] != -1);
            
            writeLine("private static final class State" + i + " {");
            indent();
            
            String instance = "private static final LexerState instance = Lexer.getInstance().new LexerState(new LexerState[] " +
                              transitions + ", " + accepting;
            
            if (accepting) {
                String type = grammar.getTokenName(acceptTable[i]);
                instance += ", Token.Type." + type;
            }
                              
            instance += ");";
            
            writeLine(instance);
            writeLine("public static LexerState getInstance() {");
            indent();
            writeLine("return instance;");
            dedent();
            writeLine("}");
            dedent();
            writeLine("}");
        }
        
        writeLine("private final Reader reader;");
        writeLine("private int nextChar;");
        
        writeLine("public Lexer(Reader reader) throws IOException {");
        indent();
                
        writeLine("this.reader = reader;");
        writeLine("nextChar = reader.read();");
        
        dedent();
        writeLine("}");
        
        writeLine("public Token getNextToken() throws Exception {");
        indent();
        
        writeLine("String lexeme = \"\";");
        writeLine("LexerState state = State0.getInstance();");
            
        writeLine("while (nextChar != -1) {");
        indent();    
        
        writeLine("LexerState previewState = state.getNextState(nextChar);");
            
        writeLine("if (null == previewState) {");
        indent();        
        
        writeLine("if (state.isAccepting())");
        indent();
        writeLine("return state.createToken(lexeme);");
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
        
        writeLine("if (state.isAccepting())");
        
        indent();
        writeLine("return state.createToken(lexeme);");
        dedent();
        
        writeLine("return null;");
        
        dedent();
        writeLine("}");
        
        dedent();
        writeLine("}");
    }
//
//    private void generateTokenFactory() throws IOException {
//        
//        writeLine("private Token createToken(int id, String lexeme) {");
//        indent();
//        
//        writeLine("switch (id) {");
//        indent();
//        
//        TokenMetadata[] data = grammar.getTokenMetadata();
//        for (int i = 0; i < data.length; i++) {
//            writeLine("case " + data[i].getPriority() + ": return new Token(Token.Type." + data[i].getName() + ", lexeme);");
//        }
//        
//        writeLine("default: return null;");
//        dedent();
//        writeLine("}");
//        
//        dedent();
//        writeLine("}");
//    }
//
//    private void generateStateTable() throws IOException {
//        
//        int[][] stateTable = grammar.getStateTable();
//        
//        indent();
//        for (int i = 0; i < stateTable.length; i++) {
//            
//            String line = "";
//            if (i == 0)
//                line += "{ { ";
//            else
//                line += "  { ";
//            
//            int[] row = stateTable[i];
//            for (int j = 0; j < row.length; j++) {
//                
//                line += row[j];
//                
//                if (j < row.length - 1)
//                    line += ", ";
//            }
//            
//            line += " }";
//            
//            if (i < stateTable.length - 1)
//                line += ",";
//            else
//                line += " };";
//            
//            writeLine(line);
//        }
//        
//        dedent();
//    }
//
//    private void generateAcceptTable() throws IOException {
//        
//        String result = "{ ";
//        int[] acceptTable = grammar.getAcceptTable();
//        
//        for (int i = 0; i < acceptTable.length; i++) {
//            result += acceptTable[i];
//            if (i < acceptTable.length - 1)
//                result += ", ";
//        }
//        
//        result += " };";
//        writeLine(result);
//    }
}
