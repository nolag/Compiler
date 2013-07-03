package cs444.codegen.instructions.x86;

import cs444.codegen.instructions.x86.bases.BinInstruction;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Or extends BinInstruction {
    public Or(final Register addTo, final NotMemory addWith, final X86SizeHelper sizeHelper){
        super("or", addTo, addWith, sizeHelper, 1);
    }

    public Or(final Register addTo, final Memory addWith, final X86SizeHelper sizeHelper){
        super("or", addTo, addWith, sizeHelper, 2);
    }

    public Or(final Memory addTo, final NotMemory addWith, final X86SizeHelper sizeHelper){
        super("or", addTo, addWith, sizeHelper, 3);
    }
}
