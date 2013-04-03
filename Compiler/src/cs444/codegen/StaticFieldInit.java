package cs444.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import cs444.codegen.InstructionArg.Size;
import cs444.codegen.instructions.Comment;
import cs444.codegen.instructions.Extern;
import cs444.codegen.instructions.Global;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.instructions.Label;
import cs444.codegen.instructions.Mov;
import cs444.codegen.instructions.Ret;
import cs444.codegen.instructions.Section;
import cs444.codegen.instructions.Section.SectionType;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;
import cs444.types.PkgClassResolver;

public class StaticFieldInit {
    public static final String STATIC_FIELD_INIT_LBL = "__init_static_fields";
    private static final String STATIC_FIELD_INIT_FILE = "_static_init.s";
    private final List<Instruction> instructions = new LinkedList<Instruction>();

    public static void generateCode(List<APkgClassResolver> resolvers,
            SelectorIndexedTable sit, SubtypeIndexedTable subit,
            boolean outputFile, String directory) throws IOException {
        StaticFieldInit fInit = new StaticFieldInit();

        fInit.genCode(resolvers, sit, subit);

        if(outputFile){
            File file = new File(directory + STATIC_FIELD_INIT_FILE);
            file.createNewFile();
            PrintStream printer = new PrintStream(file);
            fInit.print(printer);
            printer.close();
        }
    }

    private void print(PrintStream printer) {
        for(Instruction instruction : instructions) printer.println(instruction.generate());
    }

    private void genCode(List<APkgClassResolver> resolvers, SelectorIndexedTable sit, SubtypeIndexedTable subit) {
        instructions.add(new Section(SectionType.TEXT));
        instructions.add(new Global(STATIC_FIELD_INIT_LBL));
        Runtime.externAll(instructions);
        instructions.add(new Label(STATIC_FIELD_INIT_LBL));

        for (APkgClassResolver aPkgClassResolver : resolvers) {
            if (!(aPkgClassResolver instanceof PkgClassResolver)) continue;
            PkgClassResolver resolver = (PkgClassResolver) aPkgClassResolver;
            if (!resolver.shouldGenCode()) continue;

            for (DclSymbol fieldDcl : resolver.getUninheritedStaticFields()){
                String fieldNameLbl = PkgClassResolver.getUniqueNameFor(fieldDcl);
                Size size = SizeHelper.getSize(fieldDcl.getType().getTypeDclNode().realSize);
                PointerRegister toAddr = new PointerRegister(new Immediate(fieldNameLbl));

                instructions.add(new Extern(fieldNameLbl));
                if(fieldDcl.children.isEmpty()){
                    instructions.add(new Mov(toAddr, Immediate.NULL, size));
                }else{
                    instructions.add(new Comment("Initializing static field " + fieldNameLbl + "."));
                    fieldDcl.children.get(0).accept(new CodeGenVisitor(sit, subit, instructions));
                    instructions.add(new Mov(toAddr, Register.ACCUMULATOR, size));
                }
            }
        }

        instructions.add(Ret.RET);
    }
}
