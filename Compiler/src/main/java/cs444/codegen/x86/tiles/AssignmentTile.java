package cs444.codegen.x86.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.BasicMemoryFormat;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;

public class AssignmentTile extends NumericHelperTile<X86Instruction, Size, AssignmentExprSymbol> {
    private static AssignmentTile tile;

    private AssignmentTile() {}

    public static AssignmentTile getTile() {
        if (tile == null) {
            tile = new AssignmentTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(AssignmentExprSymbol op,
                                                          Platform<X86Instruction, Size> platform) {

        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();
        TileHelper<X86Instruction, Size> tileHelper = platform.getTileHelper();

        Typeable leftHandSide = (Typeable) op.children.get(0);
        Typeable rightHandSide = (Typeable) op.children.get(1);

        instructions.add(new Comment("Start Assignment " + leftHandSide.getName() + "=" + rightHandSide.getName()));
        instructions.addAll(platform.getBest(leftHandSide));
        instructions.add(new Push(Register.ACCUMULATOR, sizeHelper));
        instructions.addAll(platform.getBest(rightHandSide));

        instructions.add(new Pop(Register.DATA, sizeHelper));
        Memory to = new Memory(BasicMemoryFormat.getBasicMemoryFormat(Register.DATA));

        Size size = sizeHelper.getSizeOfType(op.getType().getTypeDclNode().fullName);

        if (leftHandSide.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)) {
            tileHelper.makeLong(rightHandSide, instructions, sizeHelper);
        }

        instructions.add(new Mov(to, Register.ACCUMULATOR, size, sizeHelper));
        instructions.add(new Comment("End Assignment"));
        return instructions;
    }
}
