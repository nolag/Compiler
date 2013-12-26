package cs444.codegen.arm;

public abstract class SimpleInstructionArg extends InstructionArg {
    @Override
    public final boolean uses(InstructionArg what) {
        return this == what;
    }
}
