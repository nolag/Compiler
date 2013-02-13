public class PrimitiveReferenceCast {
    public PrimitiveReferenceCast() {}
    public int m1(byte x) {
        return (int)x;
    }

    public PrimitiveReferenceCast m2(Object x) {
        return (PrimitiveReferenceCast)x;
    }
}
