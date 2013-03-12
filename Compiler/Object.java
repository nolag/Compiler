package java.lang;

public class Object{
    public Object(){}

    public Object f;
    

    public Object B(int a, int b){
        return f.B(a, b).f.B(a + b, b);
    }
}