package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.instructions.x86.factories.IDivMaker;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.BinUniOpTile;
import cs444.parser.symbols.ast.expressions.RemainderExprSymbol;

public class RemTile extends BinUniOpTile<RemainderExprSymbol>{
    private RemTile() {
        super(IDivMaker.maker, true);
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).rems.add(this);
    }

    public static void init(){
        new RemTile();
    }

    @Override
    public boolean fits(final RemainderExprSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final RemainderExprSymbol bin, final Platform<X86Instruction> platform){
        final InstructionsAndTiming<X86Instruction> instructions = super.generate(bin, platform);
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();
        instructions.add(new Mov(Register.ACCUMULATOR, Register.DATA, sizeHelper));
        return instructions;
    }
}
