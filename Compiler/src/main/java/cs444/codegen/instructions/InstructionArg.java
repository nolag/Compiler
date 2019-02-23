package cs444.codegen.instructions;

import cs444.codegen.SizeHelper;

public abstract class InstructionArg<T extends Instruction<T>, U extends Enum<U>> implements InstructionPart<T, U> {
    public abstract String getValue(final U size, final SizeHelper<T, U> sizeHelper);

    @Override
    public final String getValue(final SizeHelper<T, U> sizeHelper) {
        return getValue(sizeHelper.getDefaultSize(), sizeHelper);
    }
}
