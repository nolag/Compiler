package cs444.codegen;

import java.util.List;

import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;

public abstract class ObjectLayout<T extends Instruction<T>, E extends Enum<E>> {
    public final int suptypeOffset;

    protected ObjectLayout(SizeHelper<T, E> sizeHelper) {
        suptypeOffset = sizeHelper.getDefaultStackSize();
    }

    public abstract void initialize(final APkgClassResolver typeDclNode, final Addable<T> instructions);

    public abstract List<T> subtypeCheckCode(final TypeSymbol subType, final Platform<T, E> platform);

    public final long objSize() {
        return suptypeOffset * 2;
    }
}
