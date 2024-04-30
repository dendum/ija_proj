package ija.ija2023.homework2.tool.common;

public interface ToolRobot extends Observable {
   int angle();

   void turn(int var1);

   Position getPosition();
}
