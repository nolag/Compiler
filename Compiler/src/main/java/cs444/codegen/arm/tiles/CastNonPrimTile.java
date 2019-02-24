package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastNonPrimTile implements ITile<ArmInstruction, Size, CastExpressionSymbol> {
    private static CastNonPrimTile tile;

    private CastNonPrimTile() {}

    public static CastNonPrimTile getTile() {
        if (tile == null) {
            tile = new CastNonPrimTile();
        }
        return tile;
    }

    @Override
    public boolean fits(CastExpressionSymbol symbol, Platform<ArmInstruction, Size> platform) {
        return X86TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(CastExpressionSymbol symbol,
                                                          Platform<ArmInstruction, Size> platform) {

        TypeSymbol type = symbol.getType();
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        String castExprEnd = "CastExprEnd" + CodeGenVisitor.getNewLblNum();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        platform.getTileHelper().setupJmpNull(castExprEnd, sizeHelper, instructions);
        instructions.add(new Push(Register.R0));
        instructions.addAll(platform.getObjectLayout().subtypeCheckCode(type, platform));
        platform.getTileHelper().setupJumpNe(IRuntime.EXCEPTION_LBL, sizeHelper, instructions);
        instructions.add(new Pop(Register.R0));
        instructions.add(platform.makeLabel(castExprEnd));

        return instructions;
    }
}
