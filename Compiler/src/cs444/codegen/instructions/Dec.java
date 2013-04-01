package cs444.codegen.instructions;

import cs444.codegen.Register;

public class Dec implements Instruction {
    public final Register register;
    public Dec(Register register){
        this.register = register;
    }


    @Override
    public String generate() {
        return "dec " + register.getValue();
    }
}
