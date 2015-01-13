public class HostingPlanKit {

  private HostingPlan basicPlan;
  private HostingPlan premiumPlan;
  private HostingPlan premPlusPlan;

  public HostingPlanKit(HostingPlan basic, HostingPlan premium,
      HostingPlan premPlus) {

    basicPlan = basic;
    premiumPlan = premium;
    premPlusPlan = premPlus;
  }

  public HostingPlan getBasicPlan() {
    return (HostingPlan) basicPlan.clone();
  }
  public HostingPlan getPremiumPlan() {
    return (HostingPlan) premiumPlan.clone();
  }
  public HostingPlan getPremPlusPlan() {
    return (HostingPlan) premPlusPlan.clone();
  }
}
