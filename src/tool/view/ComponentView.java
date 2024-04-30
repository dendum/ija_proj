package tool.view;

import tool.common.ToolRobot;
import java.awt.Graphics;

public interface ComponentView {
   void paintComponent(Graphics var1);

   int numberUpdates();

   ToolRobot getModel();

   void clearChanged();
}
