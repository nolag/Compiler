package cs444.codegen.x86;

import cs444.codegen.Addable;
import cs444.codegen.IRuntime;
import cs444.codegen.Platform;
import cs444.codegen.TileInit;
import cs444.codegen.peepholes.InstructionHolder;
import cs444.codegen.peepholes.InstructionPrinter;
import cs444.codegen.x86.instructions.*;
import cs444.codegen.x86.instructions.bases.ReserveInstruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.codegen.x86.instructions.factories.ReserveInstructionMaker;
import cs444.codegen.x86.peepholes.MovZeroRegRemover;
import cs444.codegen.x86.peepholes.PushPopRemover;

import java.util.Set;

public abstract class X86Platform extends Platform<X86Instruction, Size> {

    protected final X86SizeHelper sizeHelper;

    protected X86Platform(Set<String> options, TileInit<X86Instruction, Size> tiles,
                          IRuntime<X86Instruction> runtime,
                          X86SizeHelper sizeHelper, String name) {
        super(options, name, runtime, tiles, genInstructionHolder(options, sizeHelper), sizeHelper);
        this.sizeHelper = sizeHelper;
    }

    private static InstructionHolder<X86Instruction> genInstructionHolder(Set<String> options,
                                                                          X86SizeHelper sizeHelper) {
        InstructionHolder<X86Instruction> printer = new InstructionPrinter<>();
        if (options.contains(NO_PEEPHOLE)) {
            return printer;
        } else {
            InstructionHolder<X86Instruction> pushPop = new PushPopRemover(printer, sizeHelper);
            return new MovZeroRegRemover(pushPop);
        }
    }

    @Override
    public final void zeroDefaultLocation(Addable<X86Instruction> instructions) {
        instructions.add(new Xor(Register.ACCUMULATOR, Register.ACCUMULATOR, sizeHelper));
    }

    @Override
    public X86Instruction makeComment(String value) {
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
    public Extern makeExtern(String what) {
        return new Extern(what);
    }

    @Override
    public Label makeLabel(String what) {
        return new Label(what);
    }

    @Override
    public Global makeGlobal(String what) {
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
        return new ReserveInstruction[]{ReserveInstructionMaker.make(name, size)};
    }

    public interface X86PlatformFactory<P extends X86Platform> extends PlatformFactory<X86Instruction, Size, P> {
        @Override
        P getPlatform(Set<String> opts);
    }
}
