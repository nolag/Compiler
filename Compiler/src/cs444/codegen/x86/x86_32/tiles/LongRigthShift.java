package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.SizeHelper;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Cdq;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.RSOpMaker;
import cs444.codegen.x86.instructions.factories.ShrdMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongShiftTile;
import cs444.parser.symbols.ast.expressions.RSExprSymbol;

public class LongRigthShift extends LongShiftTile<RSExprSymbol> {
    private static LongRigthShift tile;

    public static LongRigthShift getTile() {
        if (tile == null) tile = new LongRigthShift();
        return tile;
    }

    private LongRigthShift() {
        super(RSOpMaker.maker, ShrdMaker.maker, true);
    }

    @Override
    protected X86Instruction bigFinish(final SizeHelper<X86Instruction, Size> sizeHelper) {
        return Cdq.cdq;
    }
}
