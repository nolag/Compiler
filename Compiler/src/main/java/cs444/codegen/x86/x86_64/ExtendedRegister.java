package cs444.codegen.x86.x86_64;

import cs444.codegen.x86.Register;

public class ExtendedRegister extends Register {
    public static final ExtendedRegister R8 = new ExtendedRegister("r8");
    public static final ExtendedRegister R9 = new ExtendedRegister("r9");
    public static final ExtendedRegister R10 = new ExtendedRegister("r10");
    public static final ExtendedRegister R11 = new ExtendedRegister("r11");
    public static final ExtendedRegister R12 = new ExtendedRegister("r12");
    public static final ExtendedRegister R13 = new ExtendedRegister("r13");
    public static final ExtendedRegister R14 = new ExtendedRegister("r14");
    public static final ExtendedRegister R15 = new ExtendedRegister("r15");

    protected ExtendedRegister(String name) {
        super(name, '\0');
    }

    @Override
    public String get8High() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String get8Low() {
        return name + 'b';
    }

    @Override
    public String get16() {
        return name + 'w';
    }

    @Override
    public String get32() {
        return name + 'd';
    }

    @Override
    public String get64() {
        return name;
    }
}
