package java.lang;
public class Make {
    public Make() { }
    public Make(int i){ }
    public Make(boolean [] b1, boolean b2, int i) { }
    
    public static Make getMake(){
        return new Make();
    }
    
    public static Make getMake2(){
        return new Make(10);
    }
    
    public static Make getMake3(boolean [] bs){
        return new Make(bs, true, 100);
    }
}