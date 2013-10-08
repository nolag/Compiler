package cs444.codegen.x86;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.Section.SectionType;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.Runtime;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassResolver;


public class StaticFieldInit {
    public static final String STATIC_FIELD_INIT_LBL = "__init_static_fields";
    private static final String STATIC_FIELD_INIT_FILE = "_static_init.s";
    private final InstructionHolder<X86Instruction> instructions;
    private final Platform<X86Instruction, Size> platform;

    public static void generateCode(final List<APkgClassResolver> resolvers, final Platform<X86Instruction, Size> platform,
            final boolean outputFile, final String directory) throws IOException{

        final StaticFieldInit fInit = new StaticFieldInit(platform);

        fInit.genCode(resolvers);
        if(outputFile){
            final File file = new File(directory + STATIC_FIELD_INIT_FILE);
            file.createNewFile();
            final PrintStream printer = new PrintStream(file);
            fInit.print(printer);
            printer.close();
        }
    }

    private StaticFieldInit(final Platform<X86Instruction, Size> platform){
        instructions = platform.getInstructionHolder();
        this.platform = platform;
    }

    private void print(final PrintStream printer) {
        instructions.flush(printer);
    }

    private void genCode(final List<APkgClassResolver> resolvers) {
        instructions.add(new Section(SectionType.TEXT));
        instructions.add(new Global(STATIC_FIELD_INIT_LBL));
        Runtime.instance.externAll(instructions);
        instructions.add(new Label(STATIC_FIELD_INIT_LBL));
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        for (final APkgClassResolver aPkgClassResolver : resolvers) {
            if (!(aPkgClassResolver instanceof PkgClassResolver)) continue;
            final PkgClassResolver resolver = (PkgClassResolver) aPkgClassResolver;
            if (!resolver.shouldGenCode()) continue;

            for (final DclSymbol fieldDcl : resolver.getUninheritedStaticFields()){
                final String fieldNameLbl = PkgClassResolver.getUniqueNameFor(fieldDcl);
                instructions.add(new Extern(fieldNameLbl));

                if(fieldDcl.children.isEmpty()){
                    final Size size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));
                    final Memory toAddr = new Memory(new Immediate(fieldNameLbl));
                    if(fieldDcl.getType().value.equals(JoosNonTerminal.LONG)){
                        platform.zeroStaticLong(fieldNameLbl, instructions);
                    }else{
                        //NOTE that this assumes that 0 is null otherwise numbers need to be done differently
                        instructions.add(new Mov(toAddr, Immediate.NULL, size, platform.getSizeHelper()));
                    }
                }
            }
        }

        for (final APkgClassResolver aPkgClassResolver : resolvers) {
            if (!(aPkgClassResolver instanceof PkgClassResolver)) continue;
            final PkgClassResolver resolver = (PkgClassResolver) aPkgClassResolver;
            if (!resolver.shouldGenCode()) continue;

            for (final DclSymbol fieldDcl : resolver.getUninheritedStaticFields()){
                if(!fieldDcl.children.isEmpty()){
                    final String fieldNameLbl = PkgClassResolver.getUniqueNameFor(fieldDcl);
                    final Size size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));
                    final Memory toAddr = new Memory(new Immediate(fieldNameLbl));
                    instructions.add(new Comment("Initializing static field " + fieldNameLbl + " to 0, if forward referenced it must be 0."));
                    final ISymbol field = fieldDcl.children.get(0);
                    field.accept(new CodeGenVisitor<X86Instruction, Size>(platform));
                    instructions.addAll(platform.getBest(field));
                    if(fieldDcl.getType().value.equals(JoosNonTerminal.LONG)){
                        platform.moveStaticLong(fieldNameLbl, instructions);
                    }else{
                        instructions.add(new Mov(toAddr, Register.ACCUMULATOR, size, platform.getSizeHelper()));
                    }
                }
            }
        }

        instructions.add(Ret.RET);
    }
}
