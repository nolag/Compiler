package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.CreationExpression;
import cs444.types.APkgClassResolver;

import java.util.List;

@SuppressWarnings("rawtypes")
public class NormalCreationTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E,
        CreationExpression> {

    private static NormalCreationTile tile;

    private NormalCreationTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> NormalCreationTile<T, E> getTile() {
        if (tile == null) {
            tile = new NormalCreationTile();
        }
        return tile;
    }

    @Override
    public boolean fits(CreationExpression creation, Platform<T, E> platform) {
        return !creation.getType().isArray;
    }

    @Override
    public InstructionsAndTiming<T> generate(CreationExpression creation, Platform<T, E> platform) {

        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        APkgClassResolver typeDclNode = creation.getType().getTypeDclNode();
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        TileHelper<T, E> tileHelper = platform.getTileHelper();
        long allocSize = typeDclNode.getStackSize(platform) + platform.getObjectLayout().objSize();

        platform.makeComment("Allocate " + allocSize + " bytes for " + typeDclNode.fullName);
        tileHelper.loadNumberToDefault((int) allocSize, instructions, sizeHelper);

        platform.getRunime().mallocClear(instructions);

        platform.getObjectLayout().initialize(typeDclNode, instructions);

        APkgClassResolver resolver = creation.getType().getTypeDclNode();

        List<ISymbol> children = creation.children;

        platform.makeComment("invoke Constructor");

        platform.getTileHelper().invokeConstructor(resolver, children, platform, instructions);
        platform.makeComment("Done creating object");
        return instructions;
    }
}
