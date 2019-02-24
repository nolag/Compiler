package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;

import java.util.List;

public abstract class ObjectLayout<T extends Instruction<T>, E extends Enum<E>> {
    public final int suptypeOffset;

    protected ObjectLayout(SizeHelper<T, E> sizeHelper) {
        suptypeOffset = sizeHelper.getDefaultStackSize();
    }

    public abstract void initialize(APkgClassResolver typeDclNode, Addable<T> instructions);

    public abstract List<T> subtypeCheckCode(TypeSymbol subType, Platform<T, E> platform);

    public final long objSize() {
        return suptypeOffset * 2;
    }
}
