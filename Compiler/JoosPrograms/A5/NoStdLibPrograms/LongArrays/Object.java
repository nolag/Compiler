package java.lang;

public class Object{
    public Object() { }
    public static int test(){
        int j = 0;
        long [] ls = new long[10];
        long sum = 0;
        for(int i = 0; i < ls.length; i = i + 1) sum = sum + ls[i];
        if(sum != 0) j = 1;
        for(int i = 0; i < ls.length; i = i + 1) ls[i] = i;
        sum = 0;
        for(int i = 0; i < ls.length; i = i + 1) sum = sum + ls[i];
        if(sum != 45) j = j + 2;
        for(int i = 0; i < ls.length; i = i + 1) ls[i] = 2147483648L;
        sum = 0;
        for(int i = 0; i < ls.length; i = i + 1) sum = sum + ls[i];
        if(sum != 21474836480L) j = j + 4;
        for(int i = 0; i < ls.length; i = i + 1) ls[i] = -2147483649L;
        sum = 0;
        for(int i = 0; i < ls.length; i = i + 1) sum = sum + ls[i];
        if(sum != -21474836490L) j = j + 8;
        return j;
    }
}