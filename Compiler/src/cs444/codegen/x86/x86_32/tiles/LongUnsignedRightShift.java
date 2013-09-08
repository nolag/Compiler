package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.ShrdMaker;
import cs444.codegen.x86.instructions.factories.URSOpMaker;
import cs444.codegen.x86.x86_32.X86_32Platform;
import cs444.codegen.x86.x86_32.tiles.helpers.LongShiftTile;
import cs444.parser.symbols.ast.expressions.URSExprSymbol;

public class LongUnsignedRightShift extends LongShiftTile<URSExprSymbol> {
    private static LongUnsignedRightShift tile;

    public static void init(){
        if(tile == null) tile = new LongUnsignedRightShift();
        TileSet.<X86Instruction, Size>getOrMake(X86_32Platform.class).urss.add(tile);
    }

    private LongUnsignedRightShift() {
        super(URSOpMaker.maker, ShrdMaker.maker, true);
    }

    @Override
    protected X86Instruction bigFinish(final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Xor(Register.DATA, Register.DATA, sizeHelper);
    }
}
