public class AccessStaticFields {
  public AccessStaticFields() {}

  protected static int f;

  public int m() {
    return AccessStaticFields.f;
  }
}
