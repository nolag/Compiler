public class ImplicitThisForMethod {
  public ImplicitThisForMethod() {}
  public int m1() {
    return 42;
  }
  public int m2() {
    return m1();
  }
}
