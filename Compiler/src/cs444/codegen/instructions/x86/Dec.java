package cs444.codegen.instructions.x86;

import cs444.codegen.x86.Register;

public class Dec implements X86Instruction {
    public final Register register;
    public Dec(Register register){
        this.register = register;
    }


    @Override
    public String generate() {
        return "dec " + register.getValue();
    }
}
