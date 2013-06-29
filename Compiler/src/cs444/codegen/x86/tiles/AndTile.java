package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Label;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ast.expressions.AndExprSymbol;

public class AndTile implements ITile<X86Instruction, AndExprSymbol>{
    public static void init(){
        new AndTile();
    }

    private AndTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).ands.add(this);
    }

    @Override
    public boolean fits(final AndExprSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final AndExprSymbol op, final Platform<X86Instruction> platform){
        final X86SizeHelper sizeHelper = (X86SizeHelper)platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final CodeGenVisitor codeGen = CodeGenVisitor.getCurrentCodeGen();

        final String andEnd = "and" + codeGen.getNewLblNum();
        instructions.addAll(platform.getBest(op.children.get(0)));
        TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, andEnd, sizeHelper, instructions);
        instructions.addAll(platform.getBest(op.children.get(1)));
        TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.TRUE, andEnd, sizeHelper, instructions);
        instructions.add(new Label(andEnd));

        return instructions;
    }
}
