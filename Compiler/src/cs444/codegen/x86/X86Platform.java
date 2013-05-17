package cs444.codegen.x86;

import cs444.codegen.IPlatform;
import cs444.codegen.instructions.x86.bases.X86Instruction;

public interface X86Platform extends IPlatform<X86Instruction>{
    @Override
    public X86SizeHelper getSizeHelper();
}
