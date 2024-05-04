package tool;

import ija.ija2023.homework2.common.Robot;
import tool.common.Position;
import tool.common.ToolEnvironment;
import tool.common.ToolRobot;
import tool.view.FieldView;
import tool.view.RobotView;
import tool.view.MapView;

import java.awt.*;
import java.awt.event.*;
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
    private Robot active_robot;

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
        active_robot = null;
        GridLayout var3 = new GridLayout(var1, var2);
        var4 = new MapView(var3, this.robots);

//        var4.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("Mouse clicked");
//                if (e.getClickCount() == 2) {
//                    System.out.println("Double click");
//                    queue.add(new int[]{(int) (e.getPoint().x / fields.get(new Position(0, 0)).getWidth()),
//                            (int) (e.getPoint().y / fields.get(new Position(0, 0)).getHeight()), 0});
//                } else {
//
//                    if (e.getClickCount() == 1) {
//                        System.out.println("Single click");
//                        queue.add(new int[]{(int) (e.getPoint().x / fields.get(new Position(0, 0)).getWidth()),
//                                (int) (e.getPoint().y / fields.get(new Position(0, 0)).getHeight()), 1});
//                    }
//                }
//            }
//        });

        var4.addMouseListener(new ClickListener(){
            public void singleClick(MouseEvent e){
                System.out.println("single");
                queue.add(new int[]{(int) (e.getPoint().x / fields.get(new Position(0, 0)).getWidth()),
                                (int) (e.getPoint().y / fields.get(new Position(0, 0)).getHeight()), 1});
            }
            public void doubleClick(MouseEvent e)
            {
                System.out.println("double");
                queue.add(new int[]{(int) (e.getPoint().x / fields.get(new Position(0, 0)).getWidth()),
                            (int) (e.getPoint().y / fields.get(new Position(0, 0)).getHeight()), 0});
            }
        });

//        var4.setFocusable(true);
//        var4.requestFocusInWindow();
//
//        var4.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                int keyCode = e.getKeyCode();
//                char keyChar = e.getKeyChar();
//                System.out.println("Key code: " + keyCode + ", Key character: " + keyChar);
//                if (keyCode == 38) {
//                    System.out.println("up key pressed");
//                    System.out.println("Angle is" + active_robot.angle());
//
////                    active_robot.move();
//                    new SwingWorker<Void, Void>() {
//                        @Override
//                        protected Void doInBackground() {
//                            active_robot.move();
//                            return null;
//                        }
//                    }.execute();
//                }
//            }
//        });

        //Arrows keys listener
        var4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up pressed");
        var4.getActionMap().put("up pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active_robot != null) {
                    System.out.println("up key pressed");
                    System.out.println("Angle is" + active_robot.angle());
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            active_robot.move();
                            return null;
                        }
                    }.execute();
                }
            }
        });

        var4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "down pressed");
        var4.getActionMap().put("down pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active_robot != null) {
                    System.out.println("down key pressed");
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            active_robot.turn(4);
                            System.out.println("Angle is" + active_robot.angle());
                            active_robot.move();
                            return null;
                        }
                    }.execute();
                }
            }
        });

        var4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left pressed");
        var4.getActionMap().put("left pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active_robot != null) {
                    System.out.println("left key pressed");
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            active_robot.turn(-1);
                            System.out.println("Angle is" + active_robot.angle());
                            return null;
                        }
                    }.execute();

                }
            }
        });

        var4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right pressed");
        var4.getActionMap().put("right pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active_robot != null) {
                    System.out.println("right key pressed");
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            active_robot.turn();
                            return null;
                        }
                    }.execute();
                    System.out.println("Angle is" + active_robot.angle());
                }
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

    public void setActiveRobot(Robot r) {
        active_robot = r;
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
