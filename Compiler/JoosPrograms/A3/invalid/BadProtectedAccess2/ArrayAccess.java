public class ArrayAccess {
    public ArrayAccess() { }
    public int val(Object o){
        //illegal, ArrayAccess does not implement o
        return o.a;
    }
    
    public int test(){
        return val;
    }
}