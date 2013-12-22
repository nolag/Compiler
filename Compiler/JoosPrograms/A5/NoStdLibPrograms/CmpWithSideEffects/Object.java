package java.lang;

public class Object{
    public int value = 0;
    
    public int getAndInc() {
        value = value + 1;
        return value;
    }
    
    public int getAndDec() {
        value = value - 1;
        return value;
    }
    
    public int get(){
        return value;
    }
    
    public long lvalue  = 0;
    
    public long getAndIncl() {
        lvalue = lvalue + 1;
        return lvalue;
    }
    
    public long getAndDecl() {
        lvalue = lvalue - 1;
        return lvalue;
    }
    
    public long getl(){
        return lvalue;
    }
    
    public Object() { }
    public static int test(){
        int i = 0;
        Object o = new Object();
        if(o.getAndInc() > o.get()) i = 1;
        if(o.getAndDec() < o.get()) i = i + 2;
        
        if(o.get() >= o.getAndInc()) i = i + 4;
        if(o.get() <= o.getAndDec()) i = i + 8;
        
        if(o.getAndIncl() > o.getl()) i = i + 16;
        if(o.getAndDecl() < o.getl()) i = i + 32;
        
        if(o.getl() >= o.getAndIncl()) i = i + 64;
        if(o.getl() <= o.getAndDecl()) i = i + 128;
        
        
        
        return i;
    }
}