package utils;

public class GenericMaker {
    @SafeVarargs
    public static <T> T[] makeArray(T... ts) {
        return ts;
    }
}
