package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.types.APkgClassResolver;

@SuppressWarnings("rawtypes")
public class MethodTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, MethodSymbol> {

    private static MethodTile tile;

    private MethodTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> MethodTile<T, E> getTile() {
        if (tile == null) {
            tile = new MethodTile();
        }
        return tile;
    }

    @Override
    public boolean fits(MethodSymbol method, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(MethodSymbol method, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();

        if (!method.isNative()) {

            TileHelper<T, E> tileHelper = platform.getTileHelper();

            SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
            String methodName = APkgClassResolver.generateFullId(method);

            tileHelper.methProlog(method, methodName, sizeHelper, instructions);
            for (ISymbol child : method.children) {
                instructions.addAll(platform.getBest(child));
            }
            tileHelper.methEpilogue(platform.getSizeHelper(), instructions);
        }

        return instructions;
    }
}
