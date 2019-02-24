package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.SizeHelper;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.PkgClassResolver;
import cs444.types.exceptions.UndeclaredException;

public class RegularCallTile implements ITile<X86Instruction, Size, SimpleMethodInvoke> {
    private static RegularCallTile tile;

    private RegularCallTile() { }

    public static RegularCallTile getTile() {
        if (tile == null) {
            tile = new RegularCallTile();
        }
        return tile;
    }

    @Override
    public boolean fits(SimpleMethodInvoke invoke, Platform<X86Instruction, Size> platform) {
        MethodOrConstructorSymbol call = invoke.call;
        return !call.isStatic() && !CodeGenVisitor.getCurrentCodeGen(platform).isSuper;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(SimpleMethodInvoke invoke,
                                                          Platform<X86Instruction, Size> platform) {

        MethodOrConstructorSymbol call = invoke.call;
        InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        SizeHelper<X86Instruction, Size> sizeHelper = platform.getSizeHelper();

        instructions.add(new Comment("Backing up ebx because having this in ecx is bad"));
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Comment("Preping this"));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        platform.getTileHelper().callStartHelper(invoke, instructions, platform);

        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Comment("get SIT column of " + call.dclName));
        instructions.add(new Mov(Register.BASE, new Memory(BasicMemoryFormat.getBasicMemoryFormat(Register.BASE)),
                sizeHelper));

        Memory methodAddr = null;
        try {
            long by = platform.getSelectorIndex().getOffset(PkgClassResolver.generateUniqueName(call,
                    call.dclName));
            methodAddr = new Memory(new AddMemoryFormat(Register.BASE, new Immediate(by)));
        } catch (UndeclaredException e) {
            // shouldn't get here
            e.printStackTrace();
        }
        instructions.add(new Mov(Register.BASE, methodAddr, sizeHelper));
        instructions.add(new Call(Register.BASE, sizeHelper));

        platform.getTileHelper().callEndHelper(call, instructions, platform);
        instructions.add(new Pop(Register.BASE, sizeHelper));
        return instructions;
    }
}
