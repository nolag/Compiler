package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.APkgClassResolver;

@SuppressWarnings("rawtypes")
public class StaticCallTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, SimpleMethodInvoke> {
    public static final String NATIVE_NAME = "NATIVE";

    private static StaticCallTile tile;

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> StaticCallTile<T, E> getTile() {
        if (tile == null) tile = new StaticCallTile();
        return tile;
    }

    private StaticCallTile() {}

    @Override
    public boolean fits(final SimpleMethodInvoke invoke, final Platform<T, E> platform) {
        final MethodOrConstructorSymbol call = invoke.call;
        return call.isStatic();
    }

    @Override
    public InstructionsAndTiming<T> generate(final SimpleMethodInvoke invoke, final Platform<T, E> platform) {

        final MethodOrConstructorSymbol call = invoke.call;
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();

        platform.getTileHelper().callStartHelper(invoke, instructions, platform);
        String name = APkgClassResolver.generateFullId(call);
        if (call.isNative()) name = NATIVE_NAME + name;

        if (call.dclInResolver != CodeGenVisitor.<T, E> getCurrentCodeGen(platform).currentFile || call.isNative()) instructions
                .add(platform.makeExtern(name));

        instructions.add(platform.makeCall(name));
        platform.getTileHelper().callEndHelper(call, instructions, platform);

        return instructions;
    }

}
