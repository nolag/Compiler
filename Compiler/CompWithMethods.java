package my.pkg.lol.simple;

import my.pkg.lol.X;
import your.pkg.lol.*;

public abstract class CompWithMethods { 
    public static int a = - -10;
    public int b = 100;
    public boolean c = false;
    
    protected my.pkg.lol.X d; 
    
    public final int getValue(){
        return 120;
    }
    
    protected static boolean valueReturn(){
        return true;
    }
    
    protected boolean hasVal(){
        return true;
    }
    
    public abstract void doStuff(int x, int y);
}
