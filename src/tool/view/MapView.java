package tool.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * The MapView class represents a panel that displays a map with robots.
 * @author Artem Verninov
 * @author Denys Dumych
 */
public class MapView extends JPanel {

    private List<RobotView> robots;

    private String image1;

    public MapView(GridLayout var3, List<RobotView> robots) {
        super(var3);
        this.robots = robots;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        try {
            ImageIcon imageIcon = new ImageIcon(image1);
            Image image = imageIcon.getImage();
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(),null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (RobotView robot : this.robots) {
            robot.draw(g);
        }
    }

    public void setImage(String image){
        image1 = image;
    }
}
