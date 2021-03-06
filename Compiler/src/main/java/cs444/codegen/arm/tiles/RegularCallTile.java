package cs444.codegen.arm.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.arm.Immediate12;
import cs444.codegen.arm.Register;
import cs444.codegen.arm.Size;
import cs444.codegen.arm.instructions.*;
import cs444.codegen.arm.instructions.bases.ArmInstruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.PkgClassResolver;
import cs444.types.exceptions.UndeclaredException;

public class RegularCallTile implements ITile<ArmInstruction, Size, SimpleMethodInvoke> {
    private static RegularCallTile tile;

    private RegularCallTile() {}

    public static RegularCallTile getTile() {
        if (tile == null) {
            tile = new RegularCallTile();
        }
        return tile;
    }

    @Override
    public boolean fits(SimpleMethodInvoke invoke, Platform<ArmInstruction, Size> platform) {
        MethodOrConstructorSymbol call = invoke.call;
        return !call.isStatic() && !CodeGenVisitor.getCurrentCodeGen(platform).isSuper;
    }

    @Override
    public InstructionsAndTiming<ArmInstruction> generate(SimpleMethodInvoke invoke,
                                                          Platform<ArmInstruction, Size> platform) {

        MethodOrConstructorSymbol call = invoke.call;
        InstructionsAndTiming<ArmInstruction> instructions = new InstructionsAndTiming<>();
        SizeHelper<ArmInstruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Backing up R8 because having this in R1 is bad"));
        instructions.add(new Push(Register.R8));
        instructions.add(new Comment("Preping this"));
        instructions.add(new Mov(Register.R8, Register.R0, sizeHelper));
        platform.getTileHelper().callStartHelper(invoke, instructions, platform);

        instructions.add(new Push(Register.R8));
        instructions.add(new Comment("get SIT column of " + call.dclName));
        instructions.add(new Ldr(Register.R8, Register.R8, sizeHelper));

        try {
            long by = platform.getSelectorIndex().getOffset(PkgClassResolver.generateUniqueName(call,
                    call.dclName));
            instructions.add(new Ldr(Register.R8, Register.R8, new Immediate12((short) by), sizeHelper));
        } catch (UndeclaredException e) {
            // shouldn't get here
            e.printStackTrace();
        }
        instructions.add(new Mov(Register.LINK, Register.PC, sizeHelper));
        instructions.add(new Mov(Register.PC, Register.R8, sizeHelper));

        platform.getTileHelper().callEndHelper(call, instructions, platform);
        instructions.add(new Pop(Register.R8));
        return instructions;
    }
}
