package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastNonPrimTile implements ITile<X86Instruction, Size, CastExpressionSymbol> {
    private static CastNonPrimTile tile;

    private CastNonPrimTile() {}

    public static CastNonPrimTile getTile() {
        if (tile == null) {
            tile = new CastNonPrimTile();
        }
        return tile;
    }

    @Override
    public boolean fits(CastExpressionSymbol symbol, Platform<X86Instruction, Size> platform) {
        return X86TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(CastExpressionSymbol symbol,
                                                          Platform<X86Instruction, Size> platform) {

        TypeSymbol type = symbol.getType();
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        String castExprEnd = "CastExprEnd" + CodeGenVisitor.getNewLblNum();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        platform.getTileHelper().setupJmpNull(castExprEnd, sizeHelper, instructions);
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getObjectLayout().subtypeCheckCode(type, platform));
        platform.getTileHelper().setupJumpNe(IRuntime.EXCEPTION_LBL, sizeHelper, instructions);
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));
        instructions.add(platform.makeLabel(castExprEnd));

        return instructions;
    }
}
