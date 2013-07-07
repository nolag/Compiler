package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ast.BooleanLiteralSymbol;


public class BoolTile implements ITile<X86Instruction, X86SizeHelper, BooleanLiteralSymbol>{
    public static void init(){
        new BoolTile();
    }

    private BoolTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).bools.add(this);
    }

    @Override
    public boolean fits(final BooleanLiteralSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final BooleanLiteralSymbol boolSymbol,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        instructions.add(new Mov(Register.ACCUMULATOR, boolSymbol.boolValue ? Immediate.TRUE : Immediate.FALSE, sizeHelper));
        return instructions;
    }
}
