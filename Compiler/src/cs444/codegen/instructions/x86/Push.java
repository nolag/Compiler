package cs444.codegen.instructions.x86;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.instructions.x86.bases.UniInstruction;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Memory;
import cs444.codegen.x86.NotMemory;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Push extends UniInstruction{
    public static final Map<X86SizeHelper, Push> map = new HashMap<X86SizeHelper, Push>();

    public Push(final NotMemory what, final Size size, final X86SizeHelper sizeHelper){
        super("push", what, size, sizeHelper, 1);
    }

    public Push(final NotMemory what, final X86SizeHelper sizeHelper){
        super("push", what, sizeHelper, 1);
    }

    public Push(final Memory what, final Size size, final X86SizeHelper sizeHelper){
        super("push", what, size, sizeHelper, 4);
    }

    public Push(final Memory what, final X86SizeHelper sizeHelper){
        super("push", what, sizeHelper, 4);
    }

    public static Push getStackPush(final X86SizeHelper sizeHelper){
        Push push = map.get(sizeHelper);
        if(push == null){
            push = new Push(Register.FRAME, sizeHelper);
            map.put(sizeHelper, push);
        }
        return push;
    }
}
