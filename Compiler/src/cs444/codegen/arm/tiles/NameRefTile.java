package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.ArmSizeHelper;
import cs444.codegen.arm.Operand2;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.Add;
import cs444.codegen.arm.instructions.Comment;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.arm.tiles.helpers.ArmTileHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleNameSymbol;
import cs444.types.PkgClassResolver;

public class NameRefTile implements ITile<ArmInstruction, Size, SimpleNameSymbol> {
    private static NameRefTile tile;

    public static NameRefTile getTile() {
        if (tile == null) tile = new NameRefTile();
        return tile;
    }

    private NameRefTile() {}

    @Override
    public boolean fits(SimpleNameSymbol symbol, Platform<ArmInstruction, Size> platform) {
        return true;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(SimpleNameSymbol name, Platform<ArmInstruction, Size> platform) {
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        final DclSymbol dcl = name.dcl;
        final String staticFieldLbl = dcl.isStatic() ? PkgClassResolver.getUniqueNameFor(dcl) : null;
        final SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Move reference of " + dcl.dclName + " in to R0"));
        if (dcl.isStatic()) {
            if (dcl.dclInResolver != CodeGenVisitor.<ArmInstruction, Size> getCurrentCodeGen(platform).currentFile) {
                platform.makeExtern(staticFieldLbl);
            }
            instructions.addAll(ArmSizeHelper.putInReg(Register.R0, staticFieldLbl, sizeHelper));
        } else if (dcl.isLocal) {
            instructions.add(new Comment("mov frame to r0 because it is local"));
            final Operand2 op2 = ArmTileHelper.setupOp2(Register.R1, (int) dcl.getOffset(platform), instructions, sizeHelper);
            instructions.add(new Add(Register.R0, Register.INTRA_PROCEDURE, op2, sizeHelper));
        } else {
            final Operand2 op2 = ArmTileHelper.setupOp2(Register.R1, (int) dcl.getOffset(platform), instructions, sizeHelper);
            instructions.add(new Add(Register.R0, Register.R0, op2, sizeHelper));
        }

        return instructions;
    }
}
