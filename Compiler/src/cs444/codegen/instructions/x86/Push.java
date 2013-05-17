package cs444.codegen.instructions.x86;

import java.util.HashMap;
import java.util.Map;

import cs444.codegen.instructions.x86.bases.X86Instruction;
import cs444.codegen.x86.InstructionArg;
import cs444.codegen.x86.InstructionArg.Size;
import cs444.codegen.x86.Register;
import cs444.codegen.x86.X86SizeHelper;

public class Push implements X86Instruction {
    public final InstructionArg what;
    public final Size size;
    public final X86SizeHelper sizeHelper;

    public static final Map<X86SizeHelper, Push> map = new HashMap<X86SizeHelper, Push>();

    public Push(final InstructionArg what, final Size size, final X86SizeHelper sizeHelper){
        this.what = what;
        this.size = size;
        this.sizeHelper = sizeHelper;
    }

    public Push(final InstructionArg what, final X86SizeHelper sizeHelper){
        this(what, sizeHelper.defaultStack, sizeHelper);
    }

    public static Push getStackPush(final X86SizeHelper sizeHelper){
        Push push = map.get(sizeHelper);
        if(push == null){
            push = new Push(Register.FRAME, sizeHelper);
            map.put(sizeHelper, push);
        }
        return push;
    }

    @Override
    public String generate() {
        return "push " + InstructionArg.getSizeStr(size) + " " + what.getValue(size, sizeHelper);
    }

}
