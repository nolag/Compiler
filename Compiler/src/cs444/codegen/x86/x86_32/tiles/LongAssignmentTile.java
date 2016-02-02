package cs444.codegen.x86.x86_32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.AddMemoryFormat;
import cs444.codegen.x86.BasicMemoryFormat;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;

public class LongAssignmentTile extends LongOnlyTile<X86Instruction, Size, AssignmentExprSymbol> {
    private static LongAssignmentTile tile;

    public static LongAssignmentTile getTile() {
        if (tile == null) tile = new LongAssignmentTile();
        return tile;
    }

    private LongAssignmentTile() {}

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final AssignmentExprSymbol op, final Platform<X86Instruction, Size> platform) {

        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        final ISymbol leftHandSide = op.children.get(0);
        final Typeable rightHandSide = (Typeable) op.children.get(1);

        instructions.add(new Comment("Start Long Assignment " + leftHandSide.getName() + "=" + rightHandSide.getName()));
        instructions.addAll(platform.getBest(leftHandSide));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(rightHandSide));
        platform.getTileHelper().makeLong(rightHandSide, instructions, sizeHelper);

        instructions.add(new Pop(Register.COUNTER, sizeHelper));
        final Memory toh = new Memory(new AddMemoryFormat(Register.COUNTER, Immediate.FOUR));
        final Memory tol = new Memory(BasicMemoryFormat.getBasicMemoryFormat(Register.COUNTER));
        instructions.add(new Mov(tol, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(toh, Register.DATA, sizeHelper));
        instructions.add(new Comment("End Long Assignment"));
        return instructions;
    }

}
