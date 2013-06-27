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
import cs444.parser.symbols.ast.NullSymbol;


public class NullTile implements ITile<X86Instruction, NullSymbol>{
    public static void init(){
        new NullTile();
    }

    private NullTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).nulls.add(this);
    }

    @Override
    public boolean fits(final NullSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final NullSymbol symbol, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();
        instructions.add(new Mov(Register.ACCUMULATOR, Immediate.NULL, sizeHelper));
        return instructions;
    }
}
