package cs444.codegen.generic.tiles;

import java.util.List;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.types.APkgClassResolver;

public class NormalCreationTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, CreationExpression> {
    private static NormalCreationTile<?, ?> tile;

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass) {
        if (tile == null) tile = new NormalCreationTile<T, E>();
        TileSet.<T, E> getOrMake(klass).creation.add((ITile<T, E, CreationExpression>) tile);
    }

    private NormalCreationTile() {}

    @Override
    public boolean fits(final CreationExpression creation, final Platform<T, E> platform) {
        return !creation.getType().isArray;
    }

    @Override
    public InstructionsAndTiming<T> generate(final CreationExpression creation, final Platform<T, E> platform) {

        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final APkgClassResolver typeDclNode = creation.getType().getTypeDclNode();
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final TileHelper<T, E> tileHelper = platform.getTileHelper();
        final long allocSize = typeDclNode.getStackSize(platform) + platform.getObjectLayout().objSize();

        platform.makeComment("Allocate " + allocSize + " bytes for " + typeDclNode.fullName);
        tileHelper.loadNumberToDefault((int) allocSize, instructions, sizeHelper);

        platform.getRunime().mallocClear(instructions);

        platform.getObjectLayout().initialize(typeDclNode, instructions);

        final APkgClassResolver resolver = creation.getType().getTypeDclNode();

        final List<ISymbol> children = creation.children;

        platform.makeComment("invoke Constructor");

        platform.getTileHelper().invokeConstructor(resolver, children, platform, instructions);
        platform.makeComment("Done creating object");
        return instructions;
    }
}
