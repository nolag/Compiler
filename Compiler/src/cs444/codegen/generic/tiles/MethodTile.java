package cs444.codegen.generic.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.MethodSymbol;
import cs444.types.APkgClassResolver;

public class MethodTile<T extends Instruction, E extends Enum<E>> implements ITile<T, E, MethodSymbol>{
    public static <T extends Instruction, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass){
        new MethodTile<T, E>(klass);
    }

    private MethodTile(final Class<? extends Platform<T, E>> klass){
        TileSet.<T, E>getOrMake(klass).methods.add(this);
    }

    @Override
    public boolean fits(final MethodSymbol method, final Platform<T, E> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<T> generate(final MethodSymbol method, final Platform<T, E> platform) {
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();

        if(!method.isNative()) {

            final TileHelper<T, E> tileHelper = platform.getTileHelper();

            final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
            final String methodName = APkgClassResolver.generateFullId(method);

            tileHelper.methProlog(method, methodName, sizeHelper, instructions);
            for(final ISymbol child : method.children) instructions.addAll(platform.getBest(child));
            tileHelper.methEpilogue(method, instructions);
        }

        return instructions;
    }
}
