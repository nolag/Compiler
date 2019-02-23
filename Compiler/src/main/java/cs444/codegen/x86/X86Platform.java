package cs444.codegen.x86;

import java.util.Set;

import cs444.codegen.Addable;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;
import cs444.codegen.x86.instructions.Call;
import cs444.codegen.x86.instructions.Comment;
import cs444.codegen.x86.instructions.Extern;
import cs444.codegen.x86.instructions.Global;
import cs444.codegen.x86.instructions.Label;
import cs444.codegen.x86.instructions.Ret;
import cs444.codegen.x86.instructions.Section;
import cs444.codegen.x86.instructions.Xor;
import cs444.codegen.x86.instructions.bases.ReserveInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.ReserveInstructionMaker;
import cs444.codegen.x86.peepholes.MovZeroRegRemover;
import cs444.codegen.x86.peepholes.PushPopRemover;

public abstract class X86Platform extends Platform<X86Instruction, Size> {

    public interface X86PlatformFactory<P extends X86Platform> extends PlatformFactory<X86Instruction, Size, P> {
        @Override
        P getPlatform(Set<String> opts);
    }

    protected final X86SizeHelper sizeHelper;

    private static InstructionHolder<X86Instruction> genInstructionHolder(final Set<String> options, final X86SizeHelper sizeHelper) {
        final InstructionHolder<X86Instruction> printer = new InstructionPrinter<>();
        if (options.contains(NO_PEEPHOLE)) {
            return printer;
        } else {
            final InstructionHolder<X86Instruction> pushPop = new PushPopRemover(printer, sizeHelper);
            return new MovZeroRegRemover(pushPop);
        }
    }

    protected X86Platform(final Set<String> options, final TileInit<X86Instruction, Size> tiles, final IRuntime<X86Instruction> runtime,
            final X86SizeHelper sizeHelper, final String name) {
        super(options, name, runtime, tiles, genInstructionHolder(options, sizeHelper), sizeHelper);
        this.sizeHelper = sizeHelper;
    }

    @Override
    public final void zeroDefaultLocation(final Addable<X86Instruction> instructions) {
        instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
    }

    @Override
    public X86Instruction makeComment(final String value) {
        return new Comment(value);
    }

    @Override
    public String getNullStr() {
        return Immediate.NULL.toString();
    }

    @Override
    public String getFalseStr() {
        return Immediate.FALSE.toString();
    }

    @Override
    public String getTrueStr() {
        return Immediate.TRUE.toString();
    }

    @Override
    public Extern makeExtern(final String what) {
        return new Extern(what);
    }

    @Override
    public Label makeLabel(final String what) {
        return new Label(what);
    }

    @Override
    public Global makeGlobal(final String what) {
        return new Global(what);
    }

    @Override
    public Section getDataSection() {
        return Section.DATA;
    }

    @Override
    public Section getTextSection() {
        return Section.TEXT;
    }

    @Override
    public Section getBSSSection() {
        return Section.BSS;
    }

    @Override
    public Call makeCall(String what) {
        return new Call(new Immediate(what), sizeHelper);
    }

    @Override
    public Ret getRetStaticField() {
        return Ret.RET;
    }

    @Override
    public ReserveInstruction[] makeSpace(String name, Size size) {
        return new ReserveInstruction[] { ReserveInstructionMaker.make(name, size) };
    }
}
