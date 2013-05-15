package cs444.codegen.instructions.x86;

import cs444.codegen.x86.Register;

public class Neg implements X86Instruction{
    //May be allowed to use other things than register, not sure
    public final Register what;

    public Neg(Register what){
        this.what = what;
    }

    @Override
    public String generate() {
        return "neg " + what.get32();
    }

}
