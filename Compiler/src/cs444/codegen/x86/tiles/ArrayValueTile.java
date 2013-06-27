package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.ArrayBaseTile;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public final class ArrayValueTile extends ArrayBaseTile{

    public static void init(){
        new ArrayValueTile();
    }

    private ArrayValueTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).arrayValues.add(this);
    }

    @Override
    public boolean fits(final ArrayAccessExprSymbol name) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ArrayAccessExprSymbol arrayAccess, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = super.generate(arrayAccess, platform);
        final X86SizeHelper sizeHelper = (X86SizeHelper)platform.getSizeHelper();
        final long stackSize = arrayAccess.getType().getTypeDclNode().getRefStackSize(sizeHelper);
        Size elementSize;
        if(stackSize >= sizeHelper.defaultStackSize) elementSize = sizeHelper.defaultStack;
        else elementSize = X86SizeHelper.getSize(stackSize);
        final Memory mem = new Memory(Register.ACCUMULATOR, Register.BASE);
        instructions.addAll(TileHelper.genMov(elementSize, mem, "array", arrayAccess, sizeHelper));
        return instructions;
    }
}
