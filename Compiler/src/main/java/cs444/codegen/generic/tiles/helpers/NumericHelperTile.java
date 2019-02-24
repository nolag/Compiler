package cs444.codegen.generic.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.Instruction;
import cs444.codegen.tiles.ITile;
import cs444.parser.symbols.ast.Typeable;

public abstract class NumericHelperTile<T extends Instruction<T>, E extends Enum<E>, S extends Typeable> implements ITile<T, E, S> {
    @Override
    public boolean fits(S symbol, Platform<T, E> platform) {
        SizeHelper<T, E> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize() >= sizeHelper.getByteSizeOfType(symbol.getType().getTypeDclNode().fullName);
    }
}
