package cs444;

import java.io.IOException;
import java.io.Writer;

import cs444.grammar.LexicalGrammar;

public class TokenClassGenerator extends Generator {

    private final LexicalGrammar grammar;
    
    TokenClassGenerator(LexicalGrammar grammar, Writer writer) {
        super(writer);
        
        this.grammar = grammar;
    }
    
    public void generate() throws IOException {
        
        writeLine("public class Token {");
        indent();
        
        writeLine("public interface Factory {");
        indent();
        writeLine("public Token create(String lexeme);");
        dedent();
        writeLine("}");
        
        writeLine("public enum Id {");
        indent();
        
        for (String name : grammar.getTokenNames()) {
            writeLine(name + ",");
        }
        
        dedent();
        writeLine("}");
        
        writeLine("public final Id id;");
        writeLine("public final String lexeme;");
        
        writeLine("public Token(Id id, String lexeme) {");
        indent();
        
        writeLine("this.id = id;");
        writeLine("this.lexeme = lexeme;");
        
        dedent();
        writeLine("}");
        
        dedent();
        writeLine("}");
    }
}
