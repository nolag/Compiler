package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.ArrayBaseTile;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public class ArrayRefTile extends ArrayBaseTile {
    private static ArrayRefTile tile;

    private ArrayRefTile() {}

    public static ArrayRefTile getTile() {
        if (tile == null) {
            tile = new ArrayRefTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(ArrayAccessExprSymbol arrayAccess,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = super.generate(arrayAccess, platform);
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Add(Register.ACCUMULATOR, Register.BASE, sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }

    @Override
    public boolean fits(ArrayAccessExprSymbol symbol, Platform<X86Instruction, Size> platform) {
        return true;
    }
}
