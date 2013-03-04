public class ConstructorMultParams {
    public ConstructorMultParams(){}
    public ConstructorMultParams(int a){}
    public ConstructorMultParams(int a, String b) {}

    public void method(){
        ConstructorMultParams a = new ConstructorMultParams();
        ConstructorMultParams b = new ConstructorMultParams(1);
        ConstructorMultParams c = new ConstructorMultParams(1, "A");
    }
}