package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ImplementationLevel;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.APkgClassResolver;

public class SuperCallTile implements ITile<X86Instruction, Size, SimpleMethodInvoke> {
    public static final String NATIVE_NAME = "NATIVE";
    private static SuperCallTile tile;

    private SuperCallTile() { }

    public static SuperCallTile getTile() {
        if (tile == null) {
            tile = new SuperCallTile();
        }
        return tile;
    }

    @Override
    public boolean fits(SimpleMethodInvoke invoke, Platform<X86Instruction, Size> platform) {
        MethodOrConstructorSymbol call = invoke.call;
        return call.getImplementationLevel() == ImplementationLevel.FINAL || CodeGenVisitor.getCurrentCodeGen(platform).isSuper;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(SimpleMethodInvoke invoke,
                                                          Platform<X86Instruction, Size> platform) {

        MethodOrConstructorSymbol call = invoke.call;
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Backing up ebx because having this in ecx is bad"));
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Comment("Preping this"));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        platform.getTileHelper().callStartHelper(invoke, instructions, platform);

        String name = APkgClassResolver.generateFullId(call);
        if (call.isNative()) {
            name = NATIVE_NAME + name;
        }
        Immediate arg = new Immediate(name);
        instructions.add(new Push(Register.BASE, sizeHelper));

        if (call.dclInResolver != CodeGenVisitor.getCurrentCodeGen(platform).currentFile || call.isNative()) {
            instructions.add(new Extern(arg));
        }

        instructions.add(new Call(arg, sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        platform.getTileHelper().callEndHelper(call, instructions, platform);
        return instructions;
    }
}
