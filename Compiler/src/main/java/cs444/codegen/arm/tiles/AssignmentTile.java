package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.Str;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.NumericHelperTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.AssignmentExprSymbol;

public class AssignmentTile extends NumericHelperTile<ArmInstruction, Size, AssignmentExprSymbol> {
    private static AssignmentTile tile;

    private AssignmentTile() {}

    public static AssignmentTile getTile() {
        if (tile == null) {
            tile = new AssignmentTile();
        }
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(AssignmentExprSymbol op,
                                                          Platform<ArmInstruction, Size> platform) {

        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        Typeable leftHandSide = (Typeable) op.children.get(0);
        Typeable rightHandSide = (Typeable) op.children.get(1);

        instructions.add(new Comment("Start Assignment " + leftHandSide.getName() + "=" + rightHandSide.getName()));
        instructions.addAll(platform.getBest(leftHandSide));
        instructions.add(new Push(Register.R0));
        instructions.addAll(platform.getBest(rightHandSide));

        instructions.add(new Pop(Register.R1));

        Size size = CodeGenVisitor.getCurrentCodeGen(platform).lhsSize;

        if (leftHandSide.getType().getTypeDclNode().fullName.equals(JoosNonTerminal.LONG)) {
            tileHelper.makeLong(rightHandSide, instructions, sizeHelper);
        }

        instructions.add(new Str(size, Register.R0, Register.R1, sizeHelper));
        instructions.add(new Comment("End Assignment"));
        return instructions;
    }
}
