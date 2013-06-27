package cs444.codegen.x86.tiles;

import cs444.codegen.CodeGenVisitor;
import cs444.codegen.Platform;
import cs444.codegen.instructions.x86.Call;
import cs444.codegen.instructions.x86.Comment;
import cs444.codegen.instructions.x86.Mov;
import cs444.codegen.instructions.x86.Push;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.tiles.ITile;
import cs444.codegen.tiles.InstructionsAndTiming;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.tiles.helpers.TileHelper;
import cs444.parser.symbols.ast.MethodOrConstructorSymbol;
import cs444.parser.symbols.ast.cleanup.SimpleMethodInvoke;
import cs444.types.PkgClassResolver;
import cs444.types.exceptions.UndeclaredException;

public class RegularCallTile implements ITile<X86Instruction, SimpleMethodInvoke> {
    public static final String NATIVE_NAME = "NATIVE";

    public static void init(){
        new RegularCallTile();
    }

    private RegularCallTile(){
        TileSet.<X86Instruction>getOrMake(X86Instruction.class).invokes.add(this);
    }

    @Override
    public boolean fits(final SimpleMethodInvoke invoke) {
        final MethodOrConstructorSymbol call = invoke.call;
        return !call.isStatic() && !CodeGenVisitor.getCurrentCodeGen().isSuper;
    }

    @Override
    public InstructionsAndTiming<X86Instruction> generate(final SimpleMethodInvoke invoke, final Platform<X86Instruction> platform) {
        final MethodOrConstructorSymbol call = invoke.call;
        final InstructionsAndTiming<X86Instruction> instructions = new InstructionsAndTiming<X86Instruction>();
        final X86SizeHelper sizeHelper = (X86SizeHelper) platform.getSizeHelper();

        instructions.add(new Comment("Backing up ebx because having this in ecx is bad"));
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Comment("Preping this"));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));
        TileHelper.callStartHelper(invoke, instructions, platform);

        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Comment("get SIT column of " + call.dclName));
        instructions.add(new Mov(Register.BASE, new Memory(Register.BASE), sizeHelper));

        Memory methodAddr = null;
        try {
            final long by = platform.getSelectorIndex().getOffset(PkgClassResolver.generateUniqueName(call, call.dclName));
            methodAddr = new Memory(Register.BASE, by);
        } catch (final UndeclaredException e) {
            // shouldn't get here
            e.printStackTrace();
        }
        instructions.add(new Mov(Register.BASE,  methodAddr, sizeHelper));
        instructions.add(new Call(Register.BASE, sizeHelper));

        TileHelper.callEndHelper(call, instructions, platform);
        return instructions;
    }
}
