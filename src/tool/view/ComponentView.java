package ija.ija2023.homework2.tool.view;

import ija.ija2023.homework2.tool.common.ToolRobot;
import java.awt.Graphics;

public interface ComponentView {
   void paintComponent(Graphics var1);

   int numberUpdates();

   ToolRobot getModel();

   void clearChanged();
}
