package java.lang;

public class Object{
    public Object() { }
    
    public String toString() {
        return "Some random object";
    }

    public static int test(){
        char [] cs = new char[3];
        cs[0] = 'l';
        cs[1] = '0';
        cs[2] = 'l';
        String s1 = new String(cs);
        String s2 = new String(cs);
        String s3 = s1 + s2;
        int i = 0;
        
        //using binary bits so it's easy to see what is wrong.
        if(!s3.equals((Object)"lollol")) i = i + 1;
        
        /*s3 = "lol" + 7;
        if(!s3.equals((Object)"lol7")) i = i + 2;
        
        s3 = 7 + "rofl";
        if(!s3.equals((Object)"7rofl")) i = i + 4;
        
        s3 = "lol" + 7 + "rofl";
        if(!s3.equals((Object)"lol7rofl")) i = i + 8;*/
        
        return i;
    }
}