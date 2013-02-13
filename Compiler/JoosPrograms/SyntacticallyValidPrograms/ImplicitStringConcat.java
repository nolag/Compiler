public class ImplicitStringConcat {
  public ImplicitStringConcat() {}
  public String m(int x) {
    return "foo" + x + true + null;
  }
}
