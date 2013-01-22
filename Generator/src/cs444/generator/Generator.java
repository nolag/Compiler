package cs444.generator;

import java.io.IOException;
import java.io.Writer;

import cs444.generator.parser.exceptions.UndeclaredRuleException;

public abstract class Generator {

    private final Writer writer;
    private int indent;

    public Generator(Writer writer) {

        this.writer = writer;
    }

    protected void indent() {
        indent += 4;
    }

    protected void dedent() {
        indent -= 4;
    }

    protected void writeLine(String str) throws IOException {

        for (int i = 0; i < indent; i++)
            writer.write(' ');

        writer.write(str + '\n');
    }

    public abstract void generate() throws IOException, UndeclaredRuleException;
}
