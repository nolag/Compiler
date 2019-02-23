package utils;

public class GenericMaker {
    @SafeVarargs
    public static <T> T[] makeArray(final T ... ts) {
        return ts;
    }
}
