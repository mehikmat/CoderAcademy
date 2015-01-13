public abstract class HostingPlan implements Cloneable {
  String name;

  public Object clone() {
    //shallow copy
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      return null;
    }
  }
  public abstract String getFeatures();
}
