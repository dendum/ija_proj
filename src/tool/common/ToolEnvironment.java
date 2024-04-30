package ija.ija2023.homework2.tool.common;

import java.util.List;

public interface ToolEnvironment {
   boolean obstacleAt(Position var1);

   int rows();

   int cols();

   List<ToolRobot> robots();
}
