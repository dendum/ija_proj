package ija.ija2023.homework2.tool.view;

import ija.ija2023.homework2.tool.EnvPresenter;
import ija.ija2023.homework2.tool.common.Observable;
import ija.ija2023.homework2.tool.common.ToolRobot;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D.Double;

public class RobotView implements ComponentView, Observable.Observer {
   private final ToolRobot model;
   private final EnvPresenter parent;
   private FieldView current;
   private int changedModel = 0;

   public RobotView(EnvPresenter var1, ToolRobot var2) {
      this.model = var2;
      this.parent = var1;
      var2.addObserver(this);
      this.privUpdate();
   }

   private void privUpdate() {
      FieldView var1 = this.parent.fieldAt(this.model.getPosition());
      if (this.current != null) {
         this.current.removeComponent();
         this.current.repaint();
      }

      this.current = var1;
      var1.addComponent(this);
      this.current.repaint();
   }

   public final void update(Observable var1) {
      ++this.changedModel;
      this.privUpdate();
   }

   public void paintComponent(Graphics var1) {
      Graphics2D var2 = (Graphics2D)var1;
      Rectangle var3 = this.current.getBounds();
      double var4 = var3.getWidth();
      double var6 = var3.getHeight();
      Math.max(var6, var4);
      double var10 = Math.min(var6, var4) - 10.0D;
      double var12 = (var4 - var10) / 2.0D;
      double var14 = (var6 - var10) / 2.0D;
      Double var16 = new Double(var12, var14, var10, var10);
      var2.setColor(Color.cyan);
      var2.fill(var16);
      double var17 = var12 + var10 / 2.0D;
      double var19 = var14 + var10 / 2.0D;
      int var21 = this.model.angle();
      double var22 = 0.0D;
      double var24 = 0.0D;
      double var26 = var10 / 4.0D;
      switch(var21) {
      case 0:
         var22 = var17;
         var24 = var14;
         break;
      case 45:
         var22 = var17 + var26;
         var24 = var14 + var26;
         break;
      case 90:
         var22 = var17 + var26 * 2.0D;
         var24 = var14 + var26 * 2.0D;
         break;
      case 135:
         var22 = var17 + var26;
         var24 = var14 + var26 * 3.0D;
         break;
      case 180:
         var22 = var17;
         var24 = var19 + var26 * 2.0D;
         break;
      case 225:
         var22 = var17 - var26;
         var24 = var19 + var26;
         break;
      case 270:
         var22 = var12;
         var24 = var19;
         break;
      case 315:
         var22 = var17 - var26;
         var24 = var19 - var26;
      }

      Double var28 = new Double(var22 - 3.0D, var24 - 3.0D, 6.0D, 6.0D);
      var2.setColor(Color.black);
      var2.fill(var28);
   }

   public int numberUpdates() {
      return this.changedModel;
   }

   public void clearChanged() {
      this.changedModel = 0;
   }

   public ToolRobot getModel() {
      return this.model;
   }
}
