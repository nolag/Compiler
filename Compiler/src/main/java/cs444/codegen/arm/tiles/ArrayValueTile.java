package cs444.codegen.arm.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Ldr;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.NumericArrayTile;
import cs444.codegen.tiles.InstructionsAndTiming;

import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public final class ArrayValueTile extends NumericArrayTile {
    private static ArrayValueTile tile;

    //private static MemoryFormat format = new AddMemoryFormat(Register.ACCUMULATOR, Register.BASE);

    public static ArrayValueTile getTile() {
        if (tile == null) tile = new ArrayValueTile();
        return tile;
    }

    private ArrayValueTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final ArrayAccessExprSymbol arrayAccess,
            final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = super.generate(arrayAccess, platform);
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        final long stackSize = arrayAccess.getType().getTypeDclNode().getRefStackSize(sizeHelper);
        Size elementSize;
        if (stackSize >= sizeHelper.getDefaultStackSize()) elementSize = sizeHelper.getDefaultSize();
        else elementSize = sizeHelper.getSize(stackSize);

        instructions.add(new Add(Register.R0, Register.R0, Register.R8, sizeHelper));
        instructions.add(new Ldr(elementSize, Register.R0, Register.R0, sizeHelper));

        instructions.add(new Pop(Register.R8));
        return instructions;
    }
}
