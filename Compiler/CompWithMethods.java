package my.pkg.lol.simple;

import my.pkg.lol.X;

public abstract class SimpleCompUnit { 
    double a = 10;
    private int b = 100;
    public boolean d = false;
    X x = new X();
    
    
    double getValue(){
        return a;
    }
    
    boolean isSame(boolean value){
        return d == value;
    }
    
    public abstract void doStuff();
}
