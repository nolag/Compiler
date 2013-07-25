package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.Typeable;

public abstract class NumericHelperTile<T extends Typeable> implements ITile<X86Instruction, Size, T>{
    @Override
    public boolean fits(final T symbol, final Platform<X86Instruction, Size> platform) {
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        return sizeHelper.getDefaultStackSize()  >= sizeHelper.getByteSizeOfType(symbol.getType().getTypeDclNode().fullName);
    }
}
