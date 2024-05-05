package tool.view;

import tool.EnvPresenter;
import tool.common.Observable;
import tool.common.ToolRobot;
import tool.common.Position;

import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class RobotView implements ComponentView, Observable.Observer {
    private final ToolRobot model;
    private final EnvPresenter parent;
    private FieldView current;
    private FieldView next;
    private int changedModel = 0;
    public Position previousPosition;
    public Position currentPosition;
    private double position_X;
    private double position_Y;
    private double angle_offset_X;
    private double angle_offset_Y;
    double robotSize;
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
        this.currentPosition = this.model.getPosition();
        this.previousPosition = new Position(-1,-1);

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
        calculateAngle();
        SwingUtilities.invokeLater(() -> parent.var4.repaint());
    }

    public void needDraw() {
        this.current = this.parent.fieldAt(this.model.getPosition());
        this.next = this.parent.fieldAt(this.model.getPosition());
        this.previousPosition = currentPosition;
        this.currentPosition = this.model.getPosition();
        Rectangle cur =  this.current.getBounds();

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
        calculateAngle();
        SwingUtilities.invokeLater(() -> parent.var4.repaint());
    }

    private void calculateAngle() {
        double offset_x = 0.0D;
        double offset_y = 0.0D;
        switch (this.model.angle()) {
            case 0:
                offset_x = robotSize / 2.0D;
                offset_y = 0;
                break;
            case 45:
                offset_x = robotSize * 3.0D / 4.0D;
                offset_y = robotSize / 4.0D;
                break;
            case 90:
                offset_x = robotSize;
                offset_y = robotSize / 2.0D;
                break;
            case 135:
                offset_x = robotSize * 3.0D / 4.0D;
                offset_y = robotSize * 3.0D / 4.0D;
                break;
            case 180:
                offset_x = robotSize / 2.0D;
                offset_y = robotSize;
                break;
            case 225:
                offset_x = robotSize / 4.0D;
                offset_y = robotSize * 3.0D / 4.0D;
                break;
            case 270:
                offset_x = 0;
                offset_y = robotSize / 2.0D;
                break;
            case 315:
                offset_x = robotSize / 4.0D;
                offset_y = robotSize / 4.0D;
        }

        angle_offset_X = offset_x - 3.0D;
        angle_offset_Y = offset_y - 3.0D;
    }

    private void privUpdate() {
        this.next = this.parent.fieldAt(this.model.getPosition());
        previousPosition = currentPosition;
        currentPosition = this.model.getPosition();
        actionGo();
        this.current = this.next;
    }

    private void actionGo() {
        if (current == next) {
            calculateAngle();
            SwingUtilities.invokeLater(() -> parent.var4.repaint());
            return;
        }

        Rectangle cur = this.current.getBounds();
        Rectangle nxt = this.next.getBounds();

        double width = cur.getWidth();
        double height = cur.getHeight();
        double curposX = cur.getX();
        double curposY = cur.getY();

        double nxtposX = nxt.getX();
        double nxtposY = nxt.getY();

        double var10 = Math.min(height, width) - 10.0D;
        double posX = (width - var10) / 2.0D;
        double posY = (height - var10) / 2.0D;
        position_X = curposX + posX;
        position_Y = curposY + posY;
        double dest_position_X = nxtposX + posX;
        double dest_position_Y = nxtposY + posY;
        robotSize = var10;

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

            if (parent.changed[0]) {
                parent.changed[0] = false;
                break;
            }

            position_X += stepX;
            position_Y += stepY;

            SwingUtilities.invokeLater(() -> parent.var4.repaint());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public final void update(Observable var1) {
        ++this.changedModel;
        this.privUpdate();
    }

    public void paintComponent(Graphics var1) {}

    public void draw(Graphics g) {
        g.setColor(Color.cyan);
        g.fillOval((int) position_X, (int) position_Y, (int) robotSize, (int) robotSize);
        g.setColor(Color.black);
        g.fillOval((int) (position_X + angle_offset_X), (int) (position_Y + angle_offset_Y), 6, 6);
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