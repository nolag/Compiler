package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ImmediateStr;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Bl;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Extern;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.AModifiersOptSymbol.ImplementationLevel;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.APkgClassResolver;

public class SuperCallTile implements ITile<ArmInstruction, Size, SimpleMethodInvoke> {
    public static final String NATIVE_NAME = "NATIVE";
    private static SuperCallTile tile;

    public static SuperCallTile getTile() {
        if (tile == null) tile = new SuperCallTile();
        return tile;
    }

    private SuperCallTile() {}

    @Override
    public boolean fits(final SimpleMethodInvoke invoke, final Platform<ArmInstruction, Size> platform) {
        final MethodOrConstructorSymbol call = invoke.call;
        return call.getImplementationLevel() == ImplementationLevel.FINAL
                || CodeGenVisitor.<ArmInstruction, Size> getCurrentCodeGen(platform).isSuper;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final SimpleMethodInvoke invoke, final Platform<ArmInstruction, Size> platform) {

        final MethodOrConstructorSymbol call = invoke.call;
        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Backing up R8 because having this in R1 is bad"));
        instructions.add(new Push(Register.R8));
        instructions.add(new Comment("Preping this"));
        instructions.add(new Mov(Register.R8, Register.R0, sizeHelper));
        platform.getTileHelper().callStartHelper(invoke, instructions, platform);

        String name = APkgClassResolver.generateFullId(call);
        if (call.isNative()) name = NATIVE_NAME + name;
        final ImmediateStr arg = new ImmediateStr(name);
        instructions.add(new Push(Register.R8));

        if (call.dclInResolver != CodeGenVisitor.<ArmInstruction, Size> getCurrentCodeGen(platform).currentFile || call.isNative()) {
            instructions.add(new Extern(arg));
        }

        instructions.add(new Bl(name));
        instructions.add(new Pop(Register.R8));
        platform.getTileHelper().callEndHelper(call, instructions, platform);
        return instructions;
    }
}
