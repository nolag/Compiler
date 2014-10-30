package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.IDivMaker;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

public class RemTile extends BinUniOpTile<RemainderExprSymbol>{
    private static RemTile tile;

    public static RemTile getTile(){
        if(tile == null) tile = new RemTile();
        return tile;
    }

    private RemTile() {
        super(IDivMaker.maker, true);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final RemainderExprSymbol bin,
            final Platform<X86Instruction, Size> platform){

        final InstructionsAndTiming<X86Instruction> instructions = super.generate(bin, platform);
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA, sizeHelper));
        return instructions;
    }
}
