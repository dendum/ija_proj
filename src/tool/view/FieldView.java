package tool.view;

import tool.common.Position;
import tool.common.ToolEnvironment;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * FieldView class extends JPanel and represents a view for a single field in the environment.
 * @author Vereninov Artem
 * @author Denys Dumych
 */
public class FieldView extends JPanel {
   private final ToolEnvironment model;
   private final Position position;
   private ComponentView obj;
   public FieldView(ToolEnvironment var1, Position var2) {
      this.model = var1;
      this.position = var2;
      this.privUpdate();
   }
   protected void paintComponent(Graphics var1) {
      super.paintComponent(var1);
   }
   private void privUpdate() {
      if (this.model.obstacleAt(this.position)) {
         this.setBackground(Color.lightGray);
         this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      } else {
         this.setVisible(false);
      }

   }
   public void createObstacle() {
      this.setBackground(Color.lightGray);
      this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      this.setVisible(true);
      this.repaint();
   }

   public void addComponent(ComponentView var1) {
      this.obj = var1;
   }

   public void removeComponent() {
      this.obj = null;
   }

   public ComponentView getComponent() {
      return this.obj;
   }

   public int numberUpdates() {
      return this.obj == null ? 0 : this.obj.numberUpdates();
   }

   public void clearChanged() {
      if (this.obj != null) {
         this.obj.clearChanged();
      }

   }
}
