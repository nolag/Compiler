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

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> ConstructorTile<T, E> getTile() {
        if (tile == null) tile = new ConstructorTile();
        return tile;
    }

    private ConstructorTile() {}

    @Override
    public boolean fits(final ConstructorSymbol symbol, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final ConstructorSymbol constructor, final Platform<T, E> platform) {

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();

        final String constrName = APkgClassResolver.generateFullId(constructor);
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final TileHelper<T, E> tileHelper = platform.getTileHelper();

        tileHelper.methProlog(constructor, constrName, sizeHelper, instructions);
        tileHelper.loadThisToDefault(instructions, sizeHelper);
        tileHelper.makeCall(CodeGenVisitor.INIT_OBJECT_FUNC, instructions, sizeHelper);

        for (final ISymbol child : constructor.children)
            instructions.addAll(platform.getBest(child));

        tileHelper.methEpilogue(sizeHelper, instructions);
        return instructions;
    }

}
