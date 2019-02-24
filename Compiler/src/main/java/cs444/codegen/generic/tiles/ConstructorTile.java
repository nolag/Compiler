package cs444.codegen.generic.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.ConstructorSymbol;
import cs444.types.APkgClassResolver;

@SuppressWarnings("rawtypes")
public class ConstructorTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, ConstructorSymbol> {

    private static ConstructorTile tile;

    private ConstructorTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> ConstructorTile<T, E> getTile() {
        if (tile == null) {
            tile = new ConstructorTile();
        }
        return tile;
    }

    @Override
    public boolean fits(ConstructorSymbol symbol, Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(ConstructorSymbol constructor, Platform<T, E> platform) {

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();

        String constrName = APkgClassResolver.generateFullId(constructor);
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        TileHelper<T, E> tileHelper = platform.getTileHelper();

        tileHelper.methProlog(constructor, constrName, sizeHelper, instructions);
        tileHelper.loadThisToDefault(instructions, sizeHelper);
        tileHelper.makeCall(CodeGenVisitor.INIT_OBJECT_FUNC, instructions, sizeHelper);

        for (ISymbol child : constructor.children) {
            instructions.addAll(platform.getBest(child));
        }

        tileHelper.methEpilogue(sizeHelper, instructions);
        return instructions;
    }
}
