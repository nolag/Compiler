package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l1 =  1172645172615792670L;
        long l2 = 586322586307896336L;
        if(l1 % l2 != 586322586307896334L) i = i + 1;
        l1 = -l1;
        if(l1 % l2 != -586322586307896334L) i = i + 2;
        l2 = -l2;
        if(l1 % l2 != -586322586307896334L) i = i + 4;
        return i;
    }
}