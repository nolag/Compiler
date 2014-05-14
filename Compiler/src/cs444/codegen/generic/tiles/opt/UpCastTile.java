package cs444.codegen.generic.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.types.APkgClassResolver.Castable;

public class UpCastTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, CastExpressionSymbol>{
    public static <T extends Instruction<T>, E extends Enum<E>> void init(final Class<? extends Platform<T, E>> klass){
        new UpCastTile<T, E>(klass);
    }

    private UpCastTile(final Class<? extends Platform<T, E>> klass) {
        TileSet.<T, E>getOrMake(klass).casts.add(this);
    }

    @Override
    public boolean fits(final CastExpressionSymbol cast, final Platform<T, E> platform) {
        final SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        final TypeSymbol toType = cast.getType();
        final Typeable from = (Typeable)cast.getOperandExpression();
        final TypeSymbol fromType = from.getType();
        final Castable castType = toType.getTypeDclNode().getCastablility(fromType.getTypeDclNode());
        final boolean normalFrom = sizeHelper.getByteSizeOfType(fromType.value) <= sizeHelper.getDefaultStackSize();
        final boolean normalTo = sizeHelper.getByteSizeOfType(toType.value) <= sizeHelper.getDefaultStackSize();
        return castType == Castable.UP_CAST & normalFrom & normalTo;
    }

    @Override
    public InstructionsAndTiming<T> generate(final CastExpressionSymbol cast, final Platform<T, E> platform){
        final InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        final String toType = cast.getType().value;
        final Typeable from = (Typeable)cast.getOperandExpression();
        final String fromType = from.getType().value;
        platform.getTileHelper().setupComment("Up cast to " + toType + " from " + fromType, instructions);
        instructions.addAll(platform.getBest(from));
        return instructions;
    }
}
