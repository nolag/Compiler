package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.NumericHelperTile;
import cs444.parser.symbols.ast.INumericLiteral;

public class NumericalTile extends NumericHelperTile<INumericLiteral>{
    private static NumericalTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new NumericalTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).numbs.add(tile);
    }

    private NumericalTile() { }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final INumericLiteral num,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        instructions.add(new Mov(Register.ACCUMULATOR, new Immediate(num.getAsLongValue()), sizeHelper));
        return instructions;
    }
}
