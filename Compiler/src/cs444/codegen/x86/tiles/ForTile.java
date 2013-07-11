package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.instructions.x86.Add;
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
import cs444.parser.symbols.ast.expressions.ForExprSymbol;

public class ForTile implements ITile<X86Instruction, Size, ForExprSymbol>{
    public static void init(){
        new ForTile();
    }

    private ForTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).fors.add(this);
    }

    @Override
    public boolean fits(final ForExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final ForExprSymbol forExprSymbol, final Platform<X86Instruction, Size> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final long mynum = CodeGenVisitor.getNewLblNum();
        instructions.add(new Comment("for start " + mynum));
        final String loopStart = "loopStart" + mynum;
        final String loopEnd = "loopEnd" + mynum;

        instructions.add(new Comment("Init for " + mynum));
        instructions.addAll(platform.getBest(forExprSymbol.getForInit()));
        instructions.add(new Label(loopStart));
        instructions.add(new Comment("Compare for " + mynum));
        instructions.addAll(platform.getBest(forExprSymbol.getConditionExpr()));

        X86TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, loopEnd, sizeHelper, instructions);

        instructions.add(new Comment("for body" + mynum));

        instructions.addAll(platform.getBest(forExprSymbol.getBody()));


        instructions.add(new Comment("for update " + mynum));
        instructions.addAll(platform.getBest(forExprSymbol.getForUpdate()));

        instructions.add(new Jmp(new Immediate(loopStart), sizeHelper));
        instructions.add(new Label(loopEnd));

        //This takes care of the init if they dcl something there
        final long size = forExprSymbol.getStackSize();
        if(0 != size){
            final Immediate by = new Immediate(String.valueOf(size));
            instructions.add(new Comment("for stack " + mynum));
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }

        instructions.add(new Comment("for end " + mynum));
        return instructions;
    }
}
