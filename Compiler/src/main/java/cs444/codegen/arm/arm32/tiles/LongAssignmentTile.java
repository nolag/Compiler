package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate8;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.Str;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;

public class LongAssignmentTile extends LongOnlyTile<ArmInstruction, Size, AssignmentExprSymbol> {
    private static LongAssignmentTile tile;

    public static LongAssignmentTile getTile() {
        if (tile == null) tile = new LongAssignmentTile();
        return tile;
    }

    private LongAssignmentTile() {}

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(final AssignmentExprSymbol op, final Platform<ArmInstruction, Size> platform) {

        final InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        final ISymbol leftHandSide = op.children.get(0);
        final Typeable rightHandSide = (Typeable) op.children.get(1);

        instructions.add(new Comment("Start Long Assignment " + leftHandSide.getName() + "=" + rightHandSide.getName()));
        instructions.addAll(platform.getBest(leftHandSide));
        instructions.add(new Push(Register.R0));
        instructions.addAll(platform.getBest(rightHandSide));
        platform.getTileHelper().makeLong(rightHandSide, instructions, sizeHelper);

        instructions.add(new Pop(Register.R4));
        instructions.add(new Str(Register.R0, Register.R4, sizeHelper));
        instructions.add(new Str(Register.R2, Register.R4, Immediate8.FOUR, sizeHelper));
        instructions.add(new Comment("End Long Assignment"));
        return instructions;
    }

}
