package cs444.codegen.x86_32linux;

import cs444.codegen.IPlatform;
import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.X86ObjectLayout;
import cs444.codegen.x86.X86SelectorIndexedTable;
import cs444.codegen.x86.X86SizeHelper;

public class X86_32LinuxPlatform implements IPlatform<X86Instruction>{
    final X86SelectorIndexedTable sit = new X86SelectorIndexedTable(X86SizeHelper.sizeHelper32);

    @Override
    public X86SizeHelper getSizeHelper() {
        return X86SizeHelper.sizeHelper32;
    }

    @Override
    public X86ObjectLayout getObjectLayout() {
        return X86ObjectLayout.object32;
    }

    @Override
    public X86SelectorIndexedTable getSelectorIndex() {
        return sit;
    }
}
