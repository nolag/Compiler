package cs444;

import java.io.IOException;
import java.io.Writer;

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
    
        // States
        
        int[][] stateTable = grammar.getStateTable();
        int[] acceptTable = grammar.getAcceptTable();
        
        writeLine("private static final Map<Integer, LexerState> states;");
        writeLine("static {");
        indent();
        writeLine("HashMap<Integer, LexerState> map = new HashMap<Integer, LexerState>();");
          
        for (int i = 0; i < stateTable.length; i++) {
            writeLine("map.put(" + i + ", State" + i + ".instance);");
        }
        
        writeLine("states = Collections.unmodifiableMap(map);");
        dedent();
        writeLine("}");
        
        for (int i = 0; i < stateTable.length; i++) {
            
            String transitions = "{ ";
            
            int[] row = stateTable[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] != -1)
                    transitions += row[j];
                else
                    transitions += "-1";
                
                if (j < row.length - 1)
                    transitions += ", ";
            }
            
            transitions += " }";
            
            boolean accepting = (acceptTable[i] != -1);
            
            writeLine("private static final class State" + i + " {");
            indent();
            
            String instance = "public static final LexerState instance = new LexerState(new int[] " +
                              transitions + ", " + accepting;
            
            if (accepting) {
                String type = grammar.getTokenName(acceptTable[i]);
                instance += ", Token.Type." + type;
            }
                              
            instance += ");";
            
            writeLine(instance);
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
        writeLine("LexerState state = states.get(0);");
            
        writeLine("while (nextChar != -1) {");
        indent();    
        
        writeLine("int previewState = state.getNextState(nextChar);");
            
        writeLine("if (previewState == -1) {");
        indent();        
        
        writeLine("if (state.isAccepting())");
        indent();
        writeLine("return state.createToken(lexeme);");
        dedent();
        writeLine("else");
        indent();
        writeLine("throw new LexerException();");
        dedent();
        dedent();
        writeLine("} else {");
        indent();
        writeLine("lexeme += (char)nextChar;");
        writeLine("nextChar = reader.read();");
        writeLine("state = states.get(previewState);");
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
}
