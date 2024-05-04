package tool.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class MapView extends JPanel {

    private List<RobotView> robots;

    public MapView(GridLayout var3, List<RobotView> robots) {
        super(var3);
        this.robots = robots;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int tileWidth = this.getWidth() / 8; // total width / number of columns
        int tileHeight = this.getHeight() / 5; // total height / number of rows

        g.setColor(Color.BLACK);
        for (int i = 0; i <= 8; i++) {
            int x = i * tileWidth;
            g.drawLine(x, 0, x, getHeight());
        }
        for (int i = 0; i <= 5; i++) {
            int y = i * tileHeight;
            g.drawLine(0, y, getWidth(), y);
        }

        for (RobotView robot : this.robots) {
            robot.draw(g);
        }
    }
}
