package cs444.codegen;

public class MemoryAddress extends InstructionArg{
    public final String label;
    public final int offset;

    MemoryAddress(String label, int offset){
        this.label = label;
        this.offset = offset;
    }

    @Override
    public String getValue(Size size) {
        if(label == null) return label;
        return label + " + " + offset;
    }
}
