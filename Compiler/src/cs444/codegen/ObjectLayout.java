package cs444.codegen;

import cs444.codegen.instructions.Instruction;
import cs444.codegen.peephole.InstructionHolder;
import cs444.parser.symbols.ast.TypeSymbol;
import cs444.types.APkgClassResolver;


public interface ObjectLayout<T extends Instruction> {
    public void initialize(final APkgClassResolver typeDclNode, final InstructionHolder<T> instructions);
    public void subtypeCheckCode(TypeSymbol subType, InstructionHolder<T> instructions, IPlatform<T> platform);
    public long objSize();
}
