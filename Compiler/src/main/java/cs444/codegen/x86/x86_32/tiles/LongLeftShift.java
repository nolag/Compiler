package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.LSOpMaker;
import cs444.codegen.x86.instructions.factories.ShldMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongShiftTile;
import cs444.parser.symbols.ast.expressions.LSExprSymbol;

public class LongLeftShift extends LongShiftTile<LSExprSymbol> {
    private static LongLeftShift tile;

    private LongLeftShift() {
        super(LSOpMaker.maker, ShldMaker.maker, false);
    }

    public static LongLeftShift getTile() {
        if (tile == null) {
            tile = new LongLeftShift();
        }
        return tile;
    }

    @Override
    protected X86Instruction bigFinish(SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper);
    }
}
