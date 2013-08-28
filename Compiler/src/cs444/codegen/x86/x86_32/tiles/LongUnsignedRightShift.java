package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.ShrdMaker;
import cs444.codegen.x86.instructions.factories.URSOpMaker;
import cs444.codegen.x86.x86_32.tiles.helpers.LongShiftTile;
import cs444.parser.symbols.ast.expressions.URSExprSymbol;

public class LongUnsignedRightShift extends LongShiftTile<URSExprSymbol> {

    public static void init(){
        new LongUnsignedRightShift();
    }

    private LongUnsignedRightShift() {
        super(URSOpMaker.maker, ShrdMaker.maker, true);
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).urss.add(this);
    }

    @Override
    protected X86Instruction bigFinish(final SizeHelper<X86Instruction, Size> sizeHelper) {
        return new Xor(Register.DATA, Register.DATA, sizeHelper);
    }
}
