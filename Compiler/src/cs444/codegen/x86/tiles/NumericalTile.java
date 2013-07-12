package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.Register;
import cs444.parser.symbols.ast.INumericLiteral;

public class NumericalTile implements ITile<X86Instruction, Size, INumericLiteral>{

    public static void init(){
        new NumericalTile();
    }

    private NumericalTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).numbs.add(this);
    }

    @Override
    public boolean fits(final INumericLiteral symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final INumericLiteral num,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(num.getValue()), sizeHelper));
        return instructions;
    }
}
