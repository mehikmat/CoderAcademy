public class TestClient {

  public static void main(String[] args) {

    HostingPlanManager manager = new HostingPlanManager();
    HostingPlanKit kit = manager.getHostingPlanKit("Win");
    HostingPlan plan = kit.getBasicPlan();
    System.out.println(plan.getFeatures());

    plan = kit.getPremiumPlan();
    System.out.println(plan.getFeatures());
  }
}
