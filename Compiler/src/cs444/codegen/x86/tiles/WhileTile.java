package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Jmp;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ast.expressions.WhileExprSymbol;

public class WhileTile implements ITile<X86Instruction, X86SizeHelper, WhileExprSymbol>{
    public static void init(){
        new WhileTile();
    }

    private WhileTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).whiles.add(this);
    }

    @Override
    public boolean fits(final WhileExprSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final WhileExprSymbol whileExprSymbol,
            final Platform<X86Instruction, X86SizeHelper> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final long mynum = CodeGenVisitor.getNewLblNum();
        instructions.add(new Comment("while start " + mynum));
        final String loopStart = "loopStart" + mynum;
        final String loopEnd = "loopEnd" + mynum;

        instructions.add(new Label(loopStart));
        instructions.addAll(platform.getBest(whileExprSymbol.getConditionSymbol()));

        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, loopEnd, sizeHelper, instructions);

        instructions.addAll(platform.getBest((whileExprSymbol.getBody())));

        instructions.add(new Jmp(new Immediate(loopStart), sizeHelper));
        instructions.add(new Label(loopEnd));
        instructions.add(new Comment("while end " + mynum));
        return instructions;
    }
}
