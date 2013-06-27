package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.Pop;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.codegen.x86_32.linux.Runtime;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.parser.symbols.ast.expressions.CastExpressionSymbol;

public class CastNonPrimTile implements ITile<X86Instruction, CastExpressionSymbol> {
    public static void init(){
        new CastNonPrimTile();
    }

    private CastNonPrimTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).casts.add(this);
    }

    @Override
    public boolean fits(final CastExpressionSymbol symbol) {
        return TileHelper.isReferenceType(symbol);
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final CastExpressionSymbol symbol, final Platform<X86Instruction> platform) {
        final TypeSymbol type = symbol.getType();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final String castExprEnd = "CastExprEnd" + CodeGenVisitor.getCurrentCodeGen().getNewLblNum();
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();

        instructions.addAll(platform.getBest(symbol.getOperandExpression()));
        TileHelper.ifNullJmpCode(Register.ACCUMULATOR, castExprEnd, sizeHelper);
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getObjectLayout().subtypeCheckCode(type, platform));
        TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, Runtime.EXCEPTION_LBL, sizeHelper);
        instructions.add(new Pop(Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Label(castExprEnd));

        return instructions;
    }
}
