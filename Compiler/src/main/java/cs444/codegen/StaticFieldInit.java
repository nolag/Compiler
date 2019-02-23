package cs444.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassResolver;

public class StaticFieldInit<E> {
    public static final String STATIC_FIELD_INIT_LBL = "__init_static_fields";
    private static final String STATIC_FIELD_INIT_FILE = "_static_init.s";

    public static <T extends Instruction<T>, E extends Enum<E>> void generateCode(final List<APkgClassResolver> resolvers,
            final Platform<T, E> platform, final boolean outputFile, final String directory) throws IOException {

        StaticFieldInit.<T, E> genCode(resolvers, platform);
        if (outputFile) {
            final File file = new File(directory, STATIC_FIELD_INIT_FILE);
            file.createNewFile();
            final PrintStream printer = new PrintStream(file);
            platform.getInstructionHolder().flush(platform, printer);
            printer.close();
        }
    }

    private static <T extends Instruction<T>, E extends Enum<E>> void genCode(final List<APkgClassResolver> resolvers,
            Platform<T, E> platform) {

        final InstructionHolder<T> instructions = platform.getInstructionHolder();
        platform.getEnterStaticField(instructions);

        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();

        for (final APkgClassResolver aPkgClassResolver : resolvers) {
            if (!(aPkgClassResolver instanceof PkgClassResolver)) continue;
            final PkgClassResolver resolver = (PkgClassResolver) aPkgClassResolver;
            if (!resolver.shouldGenCode()) continue;

            for (final DclSymbol fieldDcl : resolver.getUninheritedStaticFields()) {
                final String fieldNameLbl = PkgClassResolver.getUniqueNameFor(fieldDcl);
                instructions.add(platform.makeExtern(fieldNameLbl));

                if (fieldDcl.children.isEmpty()) {
                    final E size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));
                    if (fieldDcl.getType().value.equals(JoosNonTerminal.LONG)) {
                        platform.zeroStaticLong(fieldNameLbl, instructions);
                    } else {
                        platform.zeroStatic(fieldNameLbl, size, instructions);
                    }
                }
            }
        }

        for (final APkgClassResolver aPkgClassResolver : resolvers) {
            if (!(aPkgClassResolver instanceof PkgClassResolver)) continue;
            final PkgClassResolver resolver = (PkgClassResolver) aPkgClassResolver;
            if (!resolver.shouldGenCode()) continue;

            for (final DclSymbol fieldDcl : resolver.getUninheritedStaticFields()) {
                if (!fieldDcl.children.isEmpty()) {
                    final String fieldNameLbl = PkgClassResolver.getUniqueNameFor(fieldDcl);
                    final E size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));

                    instructions.add(platform.makeComment("Initializing static field " + fieldNameLbl
                            + " to 0, if forward referenced it must be 0."));
                    final ISymbol field = fieldDcl.children.get(0);
                    field.accept(new CodeGenVisitor<T, E>(platform));
                    instructions.addAll(platform.getBest(field));
                    if (fieldDcl.getType().value.equals(JoosNonTerminal.LONG)) {
                        platform.moveStaticLong(fieldNameLbl, instructions);
                    } else {
                        platform.moveStatic(fieldNameLbl, size, instructions);
                    }
                }
            }
        }

        instructions.add(platform.getRetStaticField());
    }
}
