package my.pkg.lol.simple;

import my.pkg.lol.X;
import your.pkg.lol.*;

public abstract class CompWithMethods { 
    public static int a = 10;
    public int b = 100;
    public boolean d = false;
    
    protected my.pkg.lol.X X; 
    
    final double getValue(){
        return 120;
    }
    
    static boolean valueReturn(){
        return true;
    }
    
    public boolean hasVal(){
        return true;
    }
    
    public abstract void doStuff(int x, int y);
}
