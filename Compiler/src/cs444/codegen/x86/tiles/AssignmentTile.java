package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Pop;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;

public class AssignmentTile implements ITile<X86Instruction, AssignmentExprSymbol> {
    public static void init(){
        new AssignmentTile();
    }

    private AssignmentTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).assigns.add(this);
    }

    @Override
    public boolean fits(final AssignmentExprSymbol symbol) {
        return true;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final AssignmentExprSymbol op, final Platform<X86Instruction> platform) {
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();


        final ISymbol leftHandSide = op.children.get(0);
        final ISymbol rightHandSide = op.children.get(1);

        instructions.add(new Comment("Start Assignment " + leftHandSide.getName() + "=" + rightHandSide.getName()));
        instructions.addAll(platform.getBest(leftHandSide));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(rightHandSide));

        instructions.add(new Pop(Register.DATA, sizeHelper));
        final Memory to = new Memory(Register.DATA);
        instructions.add(new Mov(to, Register.ACCUMULATOR, CodeGenVisitor.getCurrentCodeGen().lhsSize, sizeHelper));
        instructions.add(new Comment("End Assignment"));
        return instructions;
    }

}
