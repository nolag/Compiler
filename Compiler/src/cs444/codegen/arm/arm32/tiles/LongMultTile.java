package cs444.codegen.arm.arm32.tiles;

import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.Mov;
import cs444.codegen.arm.instructions.Pop;
import cs444.codegen.arm.instructions.Push;
import cs444.codegen.arm.instructions.Umull;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.generic.tiles.helpers.LongOnlyTile;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.Typeable;
import cs444.parser.symbols.ast.expressions.MultiplyExprSymbol;

public class LongMultTile extends LongOnlyTile<ArmInstruction, Size, MultiplyExprSymbol> {
    private static LongMultTile tile;

    public static LongMultTile getTile() {
        if (tile == null) tile = new LongMultTile();
        return tile;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(MultiplyExprSymbol mul, Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<ArmInstruction>();
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();
        final TileHelper<ArmInstruction, Size> tileHelper = platform.getTileHelper();

        instructions.add(new Comment("Start long multiply"));
        final Typeable lhs = (Typeable) mul.children.get(0);
        instructions.addAll(platform.getBest(lhs));
        tileHelper.makeLong(lhs, instructions, sizeHelper);

        instructions.add(new Push(Register.R0, Register.R2));

        final Typeable rhs = (Typeable) mul.children.get(1);
        instructions.addAll(platform.getBest(rhs));
        tileHelper.makeLong(rhs, instructions, sizeHelper);

        instructions.add(new Pop(Register.R1));
        instructions.add(new Umull(Register.R3, Register.R4, Register.R1, Register.R0));
        instructions.add(new Umull(Register.R5, Register.R6, Register.R1, Register.R2));
        instructions.add(new Pop(Register.R1));
        instructions.add(new Umull(Register.R6, Register.R7, Register.R0, Register.R1));
        instructions.add(new Add(Register.R2, Register.R5, Register.R6, sizeHelper));
        instructions.add(new Add(Register.R2, Register.R2, Register.R4, sizeHelper));
        instructions.add(new Mov(Register.R0, Register.R3, sizeHelper));

        instructions.add(new Comment("End long multiply"));
        return instructions;
    }
}
