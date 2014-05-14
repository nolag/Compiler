package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.ArrayBaseTile;
import cs444.parser.symbols.ast.expressions.ArrayAccessExprSymbol;

public class ArrayRefTile extends ArrayBaseTile {
    private static ArrayRefTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new ArrayRefTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).arrayRefs.add(tile);
    }

    private ArrayRefTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ArrayAccessExprSymbol arrayAccess,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = super.generate(arrayAccess, platform);
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Add(Register.ACCUMULATOR, Register.BASE, sizeHelper));
        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }

    @Override
    public boolean fits(final ArrayAccessExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }
}
