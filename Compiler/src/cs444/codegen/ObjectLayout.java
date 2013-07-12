package cs444.codegen;

import java.util.List;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.x86.instructions.bases.X86Instruction;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;


public interface ObjectLayout<T extends Instruction> {
    public void initialize(final APkgClassResolver typeDclNode, final Addable<X86Instruction> instructions);
    public List<T> subtypeCheckCode(final TypeSymbol subType, final Platform<T, ?> platform);
    public long objSize();
}
