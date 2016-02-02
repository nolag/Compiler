package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ConstantShift;
import cs444.codegen.arm.Operand2.Shift;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastFromLongTile implements ITile<ArmInstruction, Size, CastExpressionSymbol> {
    private static CastFromLongTile tile;

    public static CastFromLongTile getTile() {
        if (tile == null) tile = new CastFromLongTile();
        return tile;
    }

    private CastFromLongTile() {}

    @Override
    public boolean fits(final CastExpressionSymbol symbol, final Platform<ArmInstruction, Size> platform) {
        final Typeable typeable = (Typeable) symbol.getOperandExpression();
        return typeable.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG);
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final CastExpressionSymbol symbol, final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        instructions.add(platform.makeComment("making long"));
        instructions.add(new Mov(Register.R1, new ConstantShift(Register.R0, (byte) 31, Shift.LSL), sizeHelper));
        instructions.add(new Mov(Register.R1, new ConstantShift(Register.R1, (byte) 32, Shift.ASR), sizeHelper));
        return instructions;
    }
}
