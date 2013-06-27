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
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;

public class IfTile implements ITile<X86Instruction, IfExprSymbol>{
    public static void init(){
        new IfTile();
    }

    private IfTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).ifs.add(this);
    }

    @Override
    public boolean fits(final IfExprSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final IfExprSymbol ifExprSymbol, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();

        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();

        final long myid = CodeGenVisitor.getCurrentCodeGen().getNewLblNum();
        instructions.add(new Comment("if start" + myid));
        final String falseLbl = "false" + myid;
        final String trueLbl = "true" + myid;
        platform.getBest(ifExprSymbol.getConditionSymbol());

        TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, falseLbl, sizeHelper);

        platform.getBest(ifExprSymbol.getifBody());

        instructions.add(new Jmp(new Immediate(trueLbl), sizeHelper));
        instructions.add(new Label(falseLbl));

        final ISymbol elseSymbol = ifExprSymbol.getElseBody();

        if(elseSymbol != null) platform.getBest(elseSymbol);

        instructions.add(new Label(trueLbl));
        instructions.add(new Comment("if end" + myid));
        return instructions;
    }
}
