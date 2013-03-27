package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 10;
        int k = 20;
        if(i < k){
            i = i + k;
        }
        return i;
    }
}