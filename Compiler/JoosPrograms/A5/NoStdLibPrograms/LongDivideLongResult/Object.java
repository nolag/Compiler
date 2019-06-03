package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long l1 =  586322586307896335L;
        long l2 = 5L;
        if(l1 / l2 != 117264517261579267L) i = i + 1;
        l1 = -l1;
        if(l1 / l2 != -117264517261579267L) i = i + 2;
        l2 = -l2;
        if(l1 / l2 != 117264517261579267L) i = i + 4;
        l1 = -l1;
        if(l1 / l2 != -117264517261579267L) i = i + 8;
        return i;
    }
}