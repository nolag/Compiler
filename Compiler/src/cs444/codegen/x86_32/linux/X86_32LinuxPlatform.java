package cs444.codegen.x86_32.linux;

import java.util.Map;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.x86.*;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86_32.X86_32Platform;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public class X86_32LinuxPlatform extends X86_32Platform{
    public static X86_32LinuxPlatform platform;

    private static final Immediate EXIT = Immediate.ONE;
    private static final Immediate SOFTWARE_INTERUPT = new Immediate("80h");

    private X86_32LinuxPlatform(final Map<String, Boolean> opts){
        super(Runtime.instance, opts);
    }

    //NOTE this is for testing
    public static void reset(final Map<String, Boolean> opts){
        platform = new X86_32LinuxPlatform(opts);
    }

    @Override
    public final void genStartInstructions(final String methodName, final Addable<X86Instruction> instructions) {
        instructions.add(new Global("_start"));
        instructions.add(new Label("_start"));
        instructions.add(new Extern(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL)));
        instructions.add(new Call(new Immediate(StaticFieldInit.STATIC_FIELD_INIT_LBL), sizeHelper));
        instructions.add(new Call(new Immediate(methodName), sizeHelper));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(Register.ACCUMULATOR, EXIT, sizeHelper));
        instructions.add(new Int(SOFTWARE_INTERUPT, sizeHelper));
    }

    @Override
    public void genHeaderEnd(final APkgClassResolver resolver, final Addable<X86Instruction> instructions) {
        instructions.add(new Comment("Store pointer to object in edx"));
        instructions.add(new Mov(Register.DATA, Register.ACCUMULATOR, sizeHelper));

        for (final DclSymbol fieldDcl : resolver.getUninheritedNonStaticFields()) {
            final Size size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));

            final Memory fieldAddr = new Memory(Register.DATA, fieldDcl.getOffset());
            if(!fieldDcl.children.isEmpty()){
                instructions.add(new Comment("Initializing field " + fieldDcl.dclName + "."));
                // save pointer to object
                instructions.add(new Push(Register.DATA, sizeHelper));
                final CodeGenVisitor<X86Instruction, Size> visitor = new CodeGenVisitor<X86Instruction, Size>(
                        CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform).currentFile, platform);

                final ISymbol field = fieldDcl.children.get(0);
                field.accept(visitor);
                instructions.addAll(platform.getBest(field));
                instructions.add(new Comment("Pop the object address to edx"));
                instructions.add(new Pop(Register.DATA, sizeHelper));
                instructions.add(new Mov(fieldAddr, Register.ACCUMULATOR, size, sizeHelper));
            }
        }

        instructions.add(Ret.RET);
    }
}
