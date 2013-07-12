package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Call;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.APkgClassResolver;

public class StaticCallTile implements ITile<X86Instruction, Size, SimpleMethodInvoke> {
    public static final String NATIVE_NAME = "NATIVE";

    public static void init(){
        new StaticCallTile();
    }

    private StaticCallTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).invokes.add(this);
    }

    @Override
    public boolean fits(final SimpleMethodInvoke invoke, final Platform<X86Instruction, Size> platform) {
        final MethodOrConstructorSymbol call = invoke.call;
        return call.isStatic();
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleMethodInvoke invoke,
            final Platform<X86Instruction, Size> platform) {

        final MethodOrConstructorSymbol call = invoke.call;
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        if(call.isNative()) instructions.add(new Push(Register.BASE, sizeHelper));
        platform.getTileHelper().callStartHelper(invoke, instructions, platform);
        String name = APkgClassResolver.generateFullId(call);
        if(call.isNative()) name = NATIVE_NAME + name;
        final Immediate arg = new Immediate(name);
        if(call.dclInResolver != CodeGenVisitor.<X86Instruction, Size>getCurrentCodeGen(platform).currentFile || call.isNative()) instructions.add(new Extern(arg));
        instructions.add(new Call(arg, sizeHelper));
        if(call.isNative())instructions.add(new Pop(Register.BASE, sizeHelper));
        platform.getTileHelper().callEndHelper(call, instructions, platform);

        return instructions;
    }

}
