package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.instructions.Label;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;

public class AndTile implements ITile<X86Instruction, Size, AndExprSymbol>{
    public static void init(){
        new AndTile();
    }

    private AndTile(){
        TileSet.<X86Instruction, Size>getOrMake(X86Instruction.class).ands.add(this);
    }

    @Override
    public boolean fits(final AndExprSymbol symbol, final Platform<X86Instruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final AndExprSymbol op, final Platform<X86Instruction, Size> platform){
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();

        final String andEnd = "and" + CodeGenVisitor.getNewLblNum();
        instructions.addAll(platform.getBest(op.children.get(0)));
        platform.getTileHelper().setupJumpNe(andEnd, sizeHelper, instructions);
        instructions.addAll(platform.getBest(op.children.get(1)));
        platform.getTileHelper().setupJumpNe(andEnd, sizeHelper, instructions);
        instructions.add(new Label(andEnd));

        return instructions;
    }
}
