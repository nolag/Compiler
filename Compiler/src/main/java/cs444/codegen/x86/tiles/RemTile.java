package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.IDivMaker;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

public class RemTile extends BinUniOpTile<RemainderExprSymbol> {
    private static RemTile tile;

    private RemTile() {
        super(IDivMaker.maker, true);
    }

    public static RemTile getTile() {
        if (tile == null) {
            tile = new RemTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(RemainderExprSymbol bin,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = super.generate(bin, platform);
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA, sizeHelper));
        return instructions;
    }
}
