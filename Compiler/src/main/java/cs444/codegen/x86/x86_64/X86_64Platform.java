package cs444.codegen.x86.x86_64;

import java.util.Collections;
import java.util.Set;

import utils.GenericMaker;
import cs444.codegen.Addable;
import cs444.codegen.CodeGenVisitor;
import cs444.codegen.OperatingSystem;
import cs444.codegen.generic.tiles.helpers.TileHelper;
import cs444.codegen.tiles.TileSet;
import cs444.codegen.x86.AddMemoryFormat;
import cs444.codegen.x86.BasicMemoryFormat;
import cs444.codegen.x86.Immediate;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.Size;
import cs444.codegen.x86.X86ObjectLayout;
import cs444.codegen.x86.X86Platform;
import cs444.codegen.x86.X86SizeHelper;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Mov;
import cs444.codegen.x86.instructions.Pop;
import cs444.codegen.x86.instructions.Push;
import cs444.codegen.x86.instructions.Ret;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.x86_64.tiles.helpers.X86_64TileHelper;
import cs444.codegen.x86.x86_64.tiles.helpers.X86_64TileInit;
import cs444.parser.symbols.ISymbol;
import cs444.parser.symbols.ast.DclSymbol;
import cs444.types.APkgClassResolver;

public class X86_64Platform extends X86Platform {
    private final OperatingSystem<X86_64Platform>[] oses = GenericMaker.<OperatingSystem<X86_64Platform>> makeArray(new Linux(this),
            new Windows(this), new OSX(this));

    public static class Factory implements X86PlatformFactory<X86_64Platform> {
        public static final Factory FACTORY = new Factory();

        private Factory() {}

        @Override
        public X86_64Platform getPlatform(final Set<String> opts) {
            return new X86_64Platform(opts);
        }
    }

    private X86_64Platform(final Set<String> opts) {
        super(opts, X86_64TileInit.instance, Runtime.instance, X86SizeHelper.sizeHelper64, "x64");
    }

    @Override
    public final X86ObjectLayout getObjectLayout() {
        return X86_64ObjectLayout.layout;
    }

    @Override
    public final void genInstructorInvoke(final APkgClassResolver resolver, final Addable<X86Instruction> instructions) {
        X86_64TileHelper.instance.invokeConstructor(resolver, Collections.<ISymbol> emptyList(), this, instructions);
    }

    @Override
    public final void moveStatic(final String staticLbl, final Size size, final Addable<X86Instruction> instructions) {
        final Immediate toAddr = new Immediate(staticLbl);
        instructions.add(new Mov(Register.DATA, toAddr, sizeHelper));
        instructions
                .add(new Mov(new Memory(BasicMemoryFormat.getBasicMemoryFormat(Register.DATA)), Register.ACCUMULATOR, size, sizeHelper));
    }

    @Override
    public final void zeroStatic(final String staticLbl, final Size size, final Addable<X86Instruction> instructions) {
        final Immediate toAddr = new Immediate(staticLbl);
        instructions.add(new Mov(Register.DATA, toAddr, sizeHelper));
        instructions.add(new Mov(new Memory(BasicMemoryFormat.getBasicMemoryFormat(Register.DATA)), Immediate.ZERO, size, sizeHelper));
    }

    @Override
    public final void moveStaticLong(final String staticLbl, final Addable<X86Instruction> instructions) {
        moveStatic(staticLbl, Size.QWORD, instructions);
    }

    @Override
    public final void zeroStaticLong(final String staticLbl, final Addable<X86Instruction> instructions) {
        zeroStatic(staticLbl, Size.QWORD, instructions);
    }

    @Override
    public void genHeaderEnd(final APkgClassResolver resolver, final Addable<X86Instruction> instructions) {
        instructions.add(new Push(Register.BASE, sizeHelper));
        instructions.add(new Comment("Store pointer to object in rbx"));
        instructions.add(new Mov(Register.BASE, Register.ACCUMULATOR, sizeHelper));

        for (final DclSymbol fieldDcl : resolver.getUninheritedNonStaticFields()) {
            final Size size = sizeHelper.getSize(fieldDcl.getType().getTypeDclNode().getRealSize(sizeHelper));

            final Memory fieldAddr = new Memory(new AddMemoryFormat(Register.BASE, new Immediate(fieldDcl.getOffset(this))));

            if (!fieldDcl.children.isEmpty()) {
                instructions.add(new Comment("Initializing field " + fieldDcl.dclName + "."));

                final CodeGenVisitor<X86Instruction, Size> visitor = new CodeGenVisitor<X86Instruction, Size>(
                        CodeGenVisitor.<X86Instruction, Size> getCurrentCodeGen(this).currentFile, this);

                final ISymbol field = fieldDcl.children.get(0);
                field.accept(visitor);
                instructions.addAll(getBest(field));

                instructions.add(new Mov(fieldAddr, Register.ACCUMULATOR, size, sizeHelper));
            }
        }
        instructions.add(new Pop(Register.BASE, sizeHelper));
        instructions.add(Ret.RET);
    }

    @Override
    public final TileSet<X86Instruction, Size> getTiles() {
        return TileSet.<X86Instruction, Size> getOrMake(X86_64Platform.class);
    }

    @Override
    public TileHelper<X86Instruction, Size> getTileHelper() {
        return X86_64TileHelper.instance;
    }

    @Override
    public OperatingSystem<X86_64Platform>[] getOperatingSystems() {
        return oses;
    }
}