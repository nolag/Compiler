package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.NumericArrayTile;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public final class ArrayValueTile extends NumericArrayTile {
    private static ArrayValueTile tile;
    private static MemoryFormat format = new AddMemoryFormat(Register.ACCUMULATOR, Register.BASE);

    private ArrayValueTile() {}

    public static ArrayValueTile getTile() {
        if (tile == null) {
            tile = new ArrayValueTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(ArrayAccessExprSymbol arrayAccess,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = super.generate(arrayAccess, platform);
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        long stackSize = arrayAccess.getType().getTypeDclNode().getRefStackSize(sizeHelper);
        Size elementSize;
        if (stackSize >= sizeHelper.getDefaultStackSize()) {
            elementSize = sizeHelper.getDefaultSize();
        } else {
            elementSize = sizeHelper.getSize(stackSize);
        }
        Memory mem = new Memory(format);
        X86TileHelper.genMov(elementSize, mem, "array", arrayAccess, sizeHelper, instructions);
        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }
}
