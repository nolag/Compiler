package cs444.codegen.instructions;

import cs444.codegen.Register;

public class Neg implements Instruction{
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
