package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.codegen.x86.SubtypeIndexedTable;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;

public abstract class ObjectLayout<T extends Instruction> {

    public abstract void initialize(final APkgClassResolver typeDclNode, final InstructionHolder<T> instructions);

    public abstract void subtypeCheckCode(final TypeSymbol subType,
            final SubtypeIndexedTable subtypeITable, final InstructionHolder<T> instructions);
}
