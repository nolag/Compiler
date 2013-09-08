package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.NumericArrayTile;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public final class ArrayValueTile extends NumericArrayTile{
    private static ArrayValueTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new ArrayValueTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).arrayValues.add(tile);
    }

    private ArrayValueTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ArrayAccessExprSymbol arrayAccess,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = super.generate(arrayAccess, platform);
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        final long stackSize = arrayAccess.getType().getTypeDclNode().getRefStackSize(sizeHelper);
        Size elementSize;
        if(stackSize >= sizeHelper.getDefaultStackSize()) elementSize = sizeHelper.getDefaultSize();
        else elementSize = sizeHelper.getSize(stackSize);
        final Memory mem = new Memory(Register.ACCUMULATOR, Register.BASE);
        X86TileHelper.genMov(elementSize, mem, "array", arrayAccess, sizeHelper, instructions);
        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }
}
