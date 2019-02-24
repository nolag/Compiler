package cs444.codegen.x86.x86_32;

import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.OperatingSystem;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.*;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_32.tiles.helpers.X86_32TileHelper;
import cs444.codegen.x86.x86_32.tiles.helpers.X86_32TileInit;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.JoosNonTerminal;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;
import utils.GenericMaker;

import java.util.Collections;
import java.util.Set;

public class X86_32Platform extends X86Platform {
    private final OperatingSystem<X86_32Platform>[] oses =
            GenericMaker.<OperatingSystem<X86_32Platform>>makeArray(new Linux(this),
                    new Windows(this), new OSX(this));

    private X86_32Platform(Set<String> opts) {
        super(opts, X86_32TileInit.instance, Runtime.instance, X86SizeHelper.sizeHelper32, "x86");
    }

    @Override
    public final X86ObjectLayout getObjectLayout() {
        return X86_32ObjectLayout.layout;
    }

    @Override
    public final void genInstructorInvoke(APkgClassResolver resolver,
                                          Addable<X86Instruction> instructions) {
        X86_32TileHelper.instance.invokeConstructor(resolver, Collections.emptyList(), this, instructions);
    }

    @Override
    public final void moveStatic(String staticLbl, Size size, Addable<X86Instruction> instructions) {
        Memory toAddr = new Memory(new BasicMemoryFormat(new Immediate(staticLbl)));
        instructions.add(new Mov(toAddr, Register.ACCUMULATOR, size, sizeHelper));
    }

    @Override
    public final void zeroStatic(String staticLbl, Size size, Addable<X86Instruction> instructions) {
        Memory toAddr = new Memory(new BasicMemoryFormat(new Immediate(staticLbl)));
        instructions.add(new Mov(toAddr, Immediate.ZERO, size, sizeHelper));
    }

    @Override
    public final void moveStaticLong(String staticLbl, Addable<X86Instruction> instructions) {
        Immediate lbl = new Immediate(staticLbl);
        Memory toAddrL = new Memory(new BasicMemoryFormat(lbl));
        Memory toAddrH = new Memory(new AddMemoryFormat(lbl, Immediate.FOUR));
        instructions.add(new Mov(toAddrL, Register.ACCUMULATOR, sizeHelper));
        instructions.add(new Mov(toAddrH, Register.DATA, sizeHelper));
    }

    @Override
    public final void zeroStaticLong(String staticLbl, Addable<X86Instruction> instructions) {
        Immediate lbl = new Immediate(staticLbl);
        Memory toAddrL = new Memory(new BasicMemoryFormat(lbl));
        Memory toAddrH = new Memory(new AddMemoryFormat(lbl, Immediate.FOUR));
        instructions.add(new Mov(toAddrL, Immediate.ZERO, sizeHelper));
        instructions.add(new Mov(toAddrH, Immediate.ZERO, sizeHelper));
    }

    @Override
    public void genHeaderEnd(APkgClassResolver resolver, Addable<X86Instruction> instructions) {
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Comment("Store pointer to object in ebx"));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

        for (DclSymbol fieldDcl : resolver.getUninheritedNonStaticFields()) {
            Size size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));

            long offset = fieldDcl.getOffset(this);
            Memory fieldAddr = new Memory(new AddMemoryFormat(Register.BASE, new Immediate(offset)));

            if (!fieldDcl.children.isEmpty()) {
                instructions.add(new Comment("Initializing field " + fieldDcl.dclName + "."));

                CodeGenVisitor<X86Instruction, Size> visitor = new CodeGenVisitor<X86Instruction, Size>(
                        CodeGenVisitor.getCurrentCodeGen(this).currentFile, this);

                ISymbol field = fieldDcl.children.get(0);
                field.accept(visitor);
                instructions.addAll(getBest(field));

                if (fieldDcl.getType().value.equals(JoosNonTerminal.LONG)) {
                    Memory fieldAddrH = new Memory(new AddMemoryFormat(Register.BASE, new Immediate(offset + 4)));
                    instructions.add(new Mov(fieldAddr, Register.ACCUMULATOR, Size.DWORD, sizeHelper));
                    instructions.add(new Mov(fieldAddrH, Register.DATA, Size.DWORD, sizeHelper));
                } else {
                    instructions.add(new Mov(fieldAddr, Register.ACCUMULATOR, size, sizeHelper));
                }
            }
        }
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(Ret.RET);
    }

    @Override
    public final TileSet<X86Instruction, Size> getTiles() {
        return TileSet.getOrMake(X86_32Platform.class);
    }

    @Override
    public TileHelper<X86Instruction, Size> getTileHelper() {
        return X86_32TileHelper.instance;
    }

    @Override
    public OperatingSystem<X86_32Platform>[] getOperatingSystems() {
        return oses;
    }

    public static class Factory implements X86PlatformFactory<X86_32Platform> {
        public static final Factory FACTORY = new Factory();

        private Factory() {}

        @Override
        public X86_32Platform getPlatform(Set<String> opts) {
            return new X86_32Platform(opts);
        }
    }
}