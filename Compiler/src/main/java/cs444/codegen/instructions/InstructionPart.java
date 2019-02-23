package cs444.codegen.instructions;

import cs444.codegen.SizeHelper;

public interface InstructionPart <T extends Instruction<T>, U extends Enum<U>> {
    String getValue(final SizeHelper<T, U> sizeHelper);
    boolean uses(InstructionArg<T, ?> what);
}
