package tool.view;

import tool.EnvPresenter;
import tool.common.Observable;
import tool.common.ToolRobot;
import tool.common.Position;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D.Double;

// TODO
// SwingUtilities.invokeLater(() -> mapPanel.repaint());

public class RobotView implements ComponentView, Observable.Observer {
    private final ToolRobot model;
    private final EnvPresenter parent;
    private FieldView current;
    private FieldView next;
    private int changedModel = 0;

    private Position currentPosition;
    private Position nextPosition;
    private double position_X;
    private double position_Y;
    double robotSize;
    private Timer movementTimer;
    boolean[] stop;
    final Object lock;

    public RobotView(EnvPresenter var1, ToolRobot var2, boolean[] s, Object l) {
        this.model = var2;
        this.parent = var1;
        var2.addObserver(this);
        this.current = this.parent.fieldAt(this.model.getPosition());
        firstDraw();
        stop = s;
        lock = l;
    }

    private void firstDraw() {
        Rectangle cur = this.current.getBounds();

        double width = cur.getWidth();
        double height = cur.getHeight();
        double curposX = cur.getX();
        double curposY = cur.getY();

        double var10 = Math.min(height, width) - 10.0D;
        double posX = (width - var10) / 2.0D;
        double posY = (height - var10) / 2.0D;
        position_X = curposX + posX;
        position_Y = curposY + posY;
        robotSize = var10;
        // parent.var4.repaint();
        SwingUtilities.invokeLater(() -> parent.var4.repaint());
    }
    private void privUpdate() {
        this.next = this.parent.fieldAt(this.model.getPosition());
        actionGo();
        this.current = this.next;
    }

    private void actionGo() {
        if (current == next)
            return;
        Rectangle cur = this.current.getBounds();
        Rectangle nxt = this.next.getBounds();

        double width = cur.getWidth();
        double height = cur.getHeight();
        double curposX = cur.getX();
        double curposY = cur.getY();

        double nxtposX = nxt.getX();
        double nxtposY = nxt.getY();

//        Math.max(height, width);
        double var10 = Math.min(height, width) - 10.0D;
        double posX = (width - var10) / 2.0D;
        double posY = (height - var10) / 2.0D;
        position_X = curposX + posX;
        position_Y = curposY + posY;
        double dest_position_X = nxtposX + posX;
        double dest_position_Y = nxtposY + posY;
        robotSize = var10;

        // Calculate difference in x and y direction
        double dx = nxtposX - curposX;
        double dy = nxtposY - curposY;

        double stepX = dx / 100;
        double stepY = dy / 100;

        while (!(Math.abs(position_X - dest_position_X) < 0.1 && Math.abs(position_Y - dest_position_Y) < 0.1)) {
            synchronized (lock) {
                while (stop[0]) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }
            }
            position_X += stepX;
            position_Y += stepY;

//                parent.var4.repaint();
            SwingUtilities.invokeLater(() -> parent.var4.repaint());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//        this.movementTimer = new Timer(10, new ActionListener() {
//            public void actionPerformed(ActionEvent arg0) {
//                position_X += stepX;
//                position_Y += stepY;
//
//                parent.var4.repaint();
//                SwingUtilities.invokeLater(() -> parent.var4.repaint());
//
//                if (Math.abs(position_X - dest_position_X) < 0.1 && Math.abs(position_Y - dest_position_Y) < 0.1) {
//                    ((Timer) arg0.getSource()).stop();
//                }
//            }
//        });
//        movementTimer.start();
    }

    public final void update(Observable var1) {
        ++this.changedModel;
        this.privUpdate();
    }

    public void paintComponent(Graphics var1) {
        System.out.println("FAFA");
        Graphics2D var2 = (Graphics2D) var1;
        Rectangle var3 = this.current.getBounds();
        double var4 = var3.getWidth();
        double var6 = var3.getHeight();
        double posX = var3.getX();
        double posY = var3.getY();

//      System.out.println(changedModel + ": x=" + posX + ", y=" + posY);

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
        switch (var21) {
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

    public void draw(Graphics g) {
        g.setColor(Color.cyan);
        g.fillOval((int) position_X, (int) position_Y, (int) robotSize, (int) robotSize);
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
