package tool;

import ija.ija2023.homework2.common.Environment;
import ija.ija2023.homework2.control.runAutonomous;
import ija.ija2023.homework2.room.ControlledRobot;
import ija.ija2023.homework2.common.Robot;
import tool.common.Position;
import tool.common.ToolEnvironment;
import tool.common.ToolRobot;
import tool.view.FieldView;
import tool.view.RobotView;
import tool.view.MapView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class EnvPresenter {
    private final ToolEnvironment env;
    private Map<Position, FieldView> fields;
    private List<RobotView> robots;
    private JFrame frame;
    public MapView var4;
    private BlockingQueue<int[]> queue;

    public EnvPresenter(ToolEnvironment var1, BlockingQueue<int[]> q) {
        this.env = var1;
        this.queue = q;
        this.fields = new HashMap();
        this.robots = new ArrayList();
    }

    public void open() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                this.initialize();
                this.frame.setVisible(true);
            });
        } catch (InvocationTargetException | InterruptedException var2) {
            Logger.getLogger(EnvPresenter.class.getName()).log(Level.SEVERE, (String) null, var2);
        }

    }

    protected void init() {
        try {
            SwingUtilities.invokeAndWait(this::initialize);
        } catch (InvocationTargetException | InterruptedException var2) {
            Logger.getLogger(EnvPresenter.class.getName()).log(Level.SEVERE, (String) null, var2);
        }

    }

    public FieldView fieldAt(Position var1) {
        return (FieldView) this.fields.get(var1);
    }

    private void initialize() {
        this.frame = new JFrame("Robot Environment Demo");
        this.frame.setDefaultCloseOperation(3);
        this.frame.setSize(350, 400);
        this.frame.setPreferredSize(new Dimension(350, 400));
        this.frame.setResizable(false);
        int var1 = this.env.rows();
        int var2 = this.env.cols();
        GridLayout var3 = new GridLayout(var1, var2);
        var4 = new MapView(var3, this.robots);

        var4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                queue.add(new int[]{(int) (e.getPoint().x / fields.get(new Position(0, 0)).getWidth()),
                        (int) (e.getPoint().y / fields.get(new Position(0, 0)).getHeight())});
            }
        });

        for (int var5 = 0; var5 < var1; ++var5) {
            for (int var6 = 0; var6 < var2; ++var6) {
                Position var7 = new Position(var5, var6);
                FieldView var8 = new FieldView(this.env, var7);
                var4.add(var8);
                this.fields.put(var7, var8);
            }
        }


        this.env.robots().forEach((var1x) -> {
            RobotView var2_1 = new RobotView(this, var1x);
            this.robots.add(var2_1);
        });
        this.frame.getContentPane().add(var4, "Center");
        this.frame.pack();
    }

    public void add_thread_Robot(ToolRobot robot) {
        RobotView robotView = new RobotView(EnvPresenter.this, robot);
        robots.add(robotView);
    }

    protected List<FieldView> fields() {
        return new ArrayList(this.fields.values());
    }

    public void paintRobot() {
        this.var4.repaint();
    }
}
