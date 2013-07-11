package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Jmp;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.tiles.helpers.X86TileHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.IfExprSymbol;

public class IfTile implements ITile<X86Instruction, Size, IfExprSymbol>{
    public static void init(){
        new IfTile();
    }

    private IfTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).ifs.add(this);
    }

    @Override
    public boolean fits(final IfExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final IfExprSymbol ifExprSymbol,
            final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();

        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        final long myid = CodeGenVisitor.getNewLblNum();
        instructions.add(new Comment("if start" + myid));
        final String falseLbl = "false" + myid;
        final String trueLbl = "true" + myid;
        instructions.addAll(platform.getBest(ifExprSymbol.getConditionSymbol()));

        X86TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, falseLbl, sizeHelper, instructions);

        instructions.addAll(platform.getBest(ifExprSymbol.getifBody()));

        instructions.add(new Jmp(new Immediate(trueLbl), sizeHelper));
        instructions.add(new Label(falseLbl));

        final ISymbol elseSymbol = ifExprSymbol.getElseBody();

        if(elseSymbol != null) instructions.addAll(platform.getBest(elseSymbol));

        instructions.add(new Label(trueLbl));
        instructions.add(new Comment("if end" + myid));
        return instructions;
    }
}
