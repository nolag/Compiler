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
import cs444.parser.symbols.ast.expressions.OrExprSymbol;

public class OrTile implements ITile<X86Instruction, X86SizeHelper, OrExprSymbol>{
    public static void init(){
        new OrTile();
    }

    private OrTile(){
        TileSet.<X86Instruction, X86SizeHelper>getOrMake(X86Instruction.class).ors.add(this);
    }

    @Override
    public boolean fits(final OrExprSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final OrExprSymbol op, final Platform<X86Instruction, X86SizeHelper> platform){
        final X86SizeHelper sizeHelper = platform.getSizeHelper();
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();

        final String orEnd = "or" + CodeGenVisitor.getNewLblNum();
        instructions.addAll(platform.getBest(op.children.get(0)));
        TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.FALSE, orEnd, sizeHelper, instructions);
        instructions.addAll(platform.getBest(op.children.get(1)));
        TileHelper.setupJumpNe(Register.ACCUMULATOR, Immediate.FALSE, orEnd, sizeHelper, instructions);
        instructions.add(new Label(orEnd));

        return instructions;
    }
}
