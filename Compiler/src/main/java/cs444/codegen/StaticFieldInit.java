package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassResolver;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class StaticFieldInit<E> {
    public static final String STATIC_FIELD_INIT_LBL = "__init_static_fields";
    private static final String STATIC_FIELD_INIT_FILE = "_static_init.s";

    public static <T extends Instruction<T>, E extends Enum<E>> void generateCode(List<APkgClassResolver> resolvers,
                                                                                  Platform<T, E> platform,
                                                                                  boolean outputFile,
                                                                                  String directory) throws IOException {

        genCode(resolvers, platform);
        if (outputFile) {
            File file = new File(directory, STATIC_FIELD_INIT_FILE);
            file.createNewFile();
            PrintStream printer = new PrintStream(file);
            platform.getInstructionHolder().flush(platform, printer);
            printer.close();
        }
    }

    private static <T extends Instruction<T>, E extends Enum<E>> void genCode(List<APkgClassResolver> resolvers,
                                                                              Platform<T, E> platform) {

        InstructionHolder<T> instructions = platform.getInstructionHolder();
        platform.getEnterStaticField(instructions);

        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();

        for (APkgClassResolver aPkgClassResolver : resolvers) {
            if (!(aPkgClassResolver instanceof PkgClassResolver)) {
                continue;
            }
            PkgClassResolver resolver = (PkgClassResolver) aPkgClassResolver;
            if (!resolver.shouldGenCode()) {
                continue;
            }

            for (DclSymbol fieldDcl : resolver.getUninheritedStaticFields()) {
                String fieldNameLbl = PkgClassResolver.getUniqueNameFor(fieldDcl);
                instructions.add(platform.makeExtern(fieldNameLbl));

                if (fieldDcl.children.isEmpty()) {
                    E size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));
                    if (fieldDcl.getType().value.equals(JoosNonTerminal.LONG)) {
                        platform.zeroStaticLong(fieldNameLbl, instructions);
                    } else {
                        platform.zeroStatic(fieldNameLbl, size, instructions);
                    }
                }
            }
        }

        for (APkgClassResolver aPkgClassResolver : resolvers) {
            if (!(aPkgClassResolver instanceof PkgClassResolver)) {
                continue;
            }
            PkgClassResolver resolver = (PkgClassResolver) aPkgClassResolver;
            if (!resolver.shouldGenCode()) {
                continue;
            }

            for (DclSymbol fieldDcl : resolver.getUninheritedStaticFields()) {
                if (!fieldDcl.children.isEmpty()) {
                    String fieldNameLbl = PkgClassResolver.getUniqueNameFor(fieldDcl);
                    E size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));

                    instructions.add(platform.makeComment("Initializing static field " + fieldNameLbl
                            + " to 0, if forward referenced it must be 0."));
                    ISymbol field = fieldDcl.children.get(0);
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
