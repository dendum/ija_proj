//package tool;
//
//import tool.common.ToolEnvironment;
//import tool.common.ToolRobot;
//import tool.view.ComponentView;
//import tool.view.FieldView;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class EnvTester {
//   private final ToolEnvironment maze;
//   private EnvPresenter presenter;
//
//   public EnvTester(ToolEnvironment var1) {
//      this.presenter = new EnvPresenter(var1);
//      this.presenter.init();
//      this.maze = var1;
//   }
//
//   public List<ToolRobot> checkEmptyNotification() {
//      return (List)this.check().stream().map((var0) -> {
//         return var0.getComponent().getModel();
//      }).collect(Collectors.toList());
//   }
//
//   public boolean checkNotification(StringBuilder var1, ToolRobot var2) {
//      boolean var3 = this.privCheckNotification(var1, var2);
//      this.presenter.fields().forEach((var0) -> {
//         var0.clearChanged();
//      });
//      return var3;
//   }
//
//   private boolean privCheckNotification(StringBuilder var1, ToolRobot var2) {
//      List var3 = this.check();
//      int var5 = var3.size();
//      if (var5 != 1) {
//         var1.append("Chyba - nespravny pocet notifikovanych objektu!").append(" Ocekava se 1, je ").append(var5);
//         return false;
//      } else {
//         FieldView var4 = (FieldView)var3.get(0);
//         ComponentView var6 = var4.getComponent();
//         if (!var6.getModel().equals(var2)) {
//            var1.append("Chyba - notifikovan nespravny objekt ").append(" Ocekava se ").append(var2).append(", je ").append(var5);
//         }
//
//         var5 = var4.numberUpdates();
//         if (var5 != 1) {
//            var1.append("Chyba - nespravny pocet notifikovani objektu ").append(" Ocekava se 1, je ").append(var5);
//            return false;
//         } else {
//            return true;
//         }
//      }
//   }
//
//   private List<FieldView> check() {
//      List var1 = (List)this.presenter.fields().stream().filter((var0) -> {
//         return var0.numberUpdates() > 0;
//      }).collect(Collectors.toList());
//      return var1;
//   }
//}
