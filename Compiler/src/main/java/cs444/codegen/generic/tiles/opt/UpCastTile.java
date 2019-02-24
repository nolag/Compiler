package cs444.codegen.generic.tiles.opt;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;
import cs444.types.APkgClassResolver.Castable;

@SuppressWarnings("rawtypes")
public class UpCastTile<T extends Instruction<T>, E extends Enum<E>> implements ITile<T, E, CastExpressionSymbol> {
    private static UpCastTile tile;

    private UpCastTile() {}

    @SuppressWarnings("unchecked")
    public static <T extends Instruction<T>, E extends Enum<E>> UpCastTile<T, E> getTile() {
        if (tile == null) {
            tile = new UpCastTile<>();
        }
        return tile;
    }

    @Override
    public boolean fits(CastExpressionSymbol cast, Platform<T, E> platform) {
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        TypeSymbol toType = cast.getType();
        Typeable from = (Typeable) cast.getOperandExpression();
        TypeSymbol fromType = from.getType();
        Castable castType = toType.getTypeDclNode().getCastablility(fromType.getTypeDclNode());
        boolean normalFrom = sizeHelper.getByteSizeOfType(fromType.value) <= sizeHelper.getDefaultStackSize();
        boolean normalTo = sizeHelper.getByteSizeOfType(toType.value) <= sizeHelper.getDefaultStackSize();
        return castType == Castable.UP_CAST & normalFrom & normalTo;
    }

    @Override
    public InstructionsAndTiming<T> generate(CastExpressionSymbol cast, Platform<T, E> platform) {
        InstructionsAndTiming<T> instructions = new InstructionsAndTiming<T>();
        String toType = cast.getType().value;
        Typeable from = (Typeable) cast.getOperandExpression();
        String fromType = from.getType().value;
        platform.getTileHelper().setupComment("Up cast to " + toType + " from " + fromType, instructions);
        instructions.addAll(platform.getBest(from));
        return instructions;
    }
}
