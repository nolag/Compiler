package cs444.codegen.x86.tiles.helpers;

import cs444.codegen.Platform;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.CmpMaker;
import cs444.codegen.x86.instructions.factories.UniOpMaker;
import cs444.parser.symbols.ast.expressions.BinOpExpr;

public abstract class CompWithConstTile<T extends BinOpExpr> extends BinWithConstTile<T> {
    private final UniOpMaker uni;

    protected CompWithConstTile(final UniOpMaker uni) {
        super(CmpMaker.maker);
        this.uni = uni;
    }
    
    protected CompWithConstTile(final UniOpMaker uni, final Side side) {
        super(CmpMaker.maker, side);
        this.uni = uni;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final T bin, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = super.generate(bin, platform);
        X86TileHelper.addCompEnding(instructions, uni, platform);
        return instructions;
    }

    @Override
    public final boolean fits(final T op, final Platform<X86Instruction, Size> platform) {
        return super.fits(op, platform) && X86TileHelper.fitsSizedCompare(op, platform);
    }
}
