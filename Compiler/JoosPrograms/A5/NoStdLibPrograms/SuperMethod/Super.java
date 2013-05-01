package java.lang;

public class Super{
    public Super(){ }
    public int getValue(){
        return 11;
    }
    public static int test(){
        return super.getValue();
    }
}