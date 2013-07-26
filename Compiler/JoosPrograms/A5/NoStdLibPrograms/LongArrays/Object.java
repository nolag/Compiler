package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int i = 0;
        long [] ls = new long[10];
        long sum = 0;
        for(int i = 0; i < ls.size; i++) sum = sum + ls[i];
        if(sum != 0) i = 1;
        for(int i = 0; i < ls.size; i++) ls[i] = i;
        sum = 0;
        for(int i = 0; i < ls.size; i++) sum = sum + ls[i];
        if(sum != 45) i = i + 2;
        for(int i = 0; i < ls.size; i++) ls[i] = 2147483648L;
        sum = 0;
        for(int i = 0; i < ls.size; i++) sum = sum + ls[i];
        if(sum != 21474836480L) i = i + 4;
        for(int i = 0; i < ls.size; i++) ls[i] = -2147483649L;
        sum = 0;
        for(int i = 0; i < ls.size; i++) sum = sum + ls[i];
        if(sum != -21474836490L) i = i + 8;
        return i;
    }
}