package cs444;

import java.io.IOException;
import java.io.Writer;

import cs444.grammar.LexicalGrammar;
import cs444.grammar.TokenMetadata;

public class TokenClassGenerator extends Generator {

    private final LexicalGrammar grammar;
    
    TokenClassGenerator(LexicalGrammar grammar, Writer writer) {
        super(writer);
        
        this.grammar = grammar;
    }
    
    public void generate() throws IOException {
        
        writeLine("public final class Token {");
        indent();
        
        writeLine("public enum Type {");
        indent();
        
        TokenMetadata[] data = grammar.getTokenMetadata();
        for (int i = 0; i < data.length; i++) {
            String line = data[i].getName();
            if (i < data.length - 1)
                line += ",";
            writeLine(line);
        }
        
        writeLine("EOF,");
        writeLine("EMPTY");
        
        dedent();
        writeLine("}");
        
        writeLine("private final Type type;");
        writeLine("private final String lexeme;");
        
        writeLine("public Token(Type type, String lexeme) {");
        indent();
        
        writeLine("this.type = type;");
        writeLine("this.lexeme = lexeme;");
        
        dedent();
        writeLine("}");
        
        writeLine("public Type getType() {");
        indent();
        writeLine("return type;");
        dedent();
        writeLine("}");
        
        writeLine("public String getLexeme() {");
        indent();
        writeLine("return lexeme;");
        dedent();
        writeLine("}");
        

        writeLine("@Override");
        writeLine("public String toString() {");
        indent();
        writeLine("return \"<\" + type.toString() + \", \" + lexeme.trim() + \">\";");
        dedent();
        writeLine("}");
        
        dedent();
        writeLine("}");
    }
}
