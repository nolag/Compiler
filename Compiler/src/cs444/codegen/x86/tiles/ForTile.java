package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.instructions.Add;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Jmp;
import cs444.codegen.x86.instructions.Label;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.expressions.ForExprSymbol;

public class ForTile implements ITile<X86Instruction, Size, ForExprSymbol> {
    private static ForTile tile;

    public static void init(final Class<? extends Platform<X86Instruction, Size>> klass) {
        if(tile == null) tile = new ForTile();
        TileSet.<X86Instruction, Size>getOrMake(klass).fors.add(tile);
    }

    private ForTile() { }

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

        platform.getTileHelper().setupJumpNe(loopEnd, sizeHelper, instructions);

        instructions.add(new Comment("for body" + mynum));

        instructions.addAll(platform.getBest(forExprSymbol.getBody()));


        instructions.add(new Comment("for update " + mynum));
        instructions.addAll(platform.getBest(forExprSymbol.getForUpdate()));

        instructions.add(new Jmp(new Immediate(loopStart), sizeHelper));
        instructions.add(new Label(loopEnd));

        //This takes care of the init if they dcl something there
        final long size = forExprSymbol.getStackSize(platform);
        if(0 != size){
            final Immediate by = new Immediate(String.valueOf(size));
            instructions.add(new Comment("for stack " + mynum));
            instructions.add(new Add(Register.STACK, by, sizeHelper));
        }

        instructions.add(new Comment("for end " + mynum));
        return instructions;
    }
}
