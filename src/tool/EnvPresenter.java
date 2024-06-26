package tool;

import basic.common.Environment;
import basic.common.Robot;
import basic.control.runAutonomous;
import tool.common.Position;
import tool.common.ToolEnvironment;
import tool.common.ToolRobot;
import tool.view.FieldView;
import tool.view.RobotView;
import tool.view.MapView;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * The EnvPresenter class is responsible for presenting and interacting with the environment.
 * It provides methods for initializing the environment view, handling user input, and updating the environment state.
 * @author Denys Dumych
 * @author Vereninov Artem
 */
public class EnvPresenter {
    private final ToolEnvironment env;
    private Map<Position, FieldView> fields;
    private List<RobotView> robots;
    private JFrame frame;
    public MapView var4;
    private BlockingQueue<int[]> queue;
    ArrayList<Thread> threads;
    private Robot active_robot;
    private boolean in_process;
    public Recorder recorder;
    final Object lock;
    boolean[] stop;
    boolean[] program_run;
    public boolean[] changed;
    File[] files;
    int backgound_counter;

    public EnvPresenter(ToolEnvironment var1, BlockingQueue<int[]> q, ArrayList<Thread> t, boolean[] p_r) {
        this.env = var1;
        this.queue = q;
        this.fields = new HashMap();
        this.robots = new ArrayList();
        in_process = false;
        threads = t;
        stop = new boolean[1];
        changed = new boolean[1];
        program_run = p_r;
        lock = new Object();
        File folder = new File("data/img/");
        files = folder.listFiles();
        backgound_counter = 0;
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
        this.frame = new JFrame("Robot");
        JButton button = new JButton("Stop");
        button.setPreferredSize(new Dimension(130, 30));
        button.setBackground(Color.RED);
        button.addActionListener(actionEvent -> {
            synchronized (lock) {
                stop[0] = true;
                lock.notifyAll();
            }
        });

        JButton button2 = new JButton("Continue");
        button2.setPreferredSize(new Dimension(130, 30));
        button2.setBackground(Color.GREEN);
        button2.addActionListener(actionEvent2 -> {
            synchronized (lock) {
                stop[0] = false;
                lock.notifyAll();
            }
            recorder.clearActions();
        });

        JButton button3 = new JButton("Forward");
        button3.setPreferredSize(new Dimension(130, 30));
        button3.setBackground(Color.GRAY);
        button3.addActionListener(actionEvent3 -> {
            if (stop[0]) {
                changed[0] = true;
                recorder.forward();
            }
        });

        JButton button4 = new JButton("Rewind");
        button4.setPreferredSize(new Dimension(130, 30));
        button4.setBackground(Color.GRAY);
        button4.addActionListener(actionEvent4 -> {
            if (stop[0]) {
                changed[0] = true;
                recorder.rewind();
            }
        });

        JPanel button_panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        button_panel.add(button4);
        button_panel.add(button);
        button_panel.add(button2);
        button_panel.add(button3);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.DEFAULT_OPTION){
                    program_run[0] = false;

                    for (Thread thread : threads) {
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    frame.dispose();
                }
            }
        });

        frame.add(button_panel, BorderLayout.NORTH);

        this.frame.setDefaultCloseOperation(3);
        this.frame.setSize(800, 600);
        this.frame.setPreferredSize(new Dimension(800, 600));
        this.frame.setResizable(true);
        int var1 = this.env.rows();
        int var2 = this.env.cols();
        active_robot = null;
        GridLayout var3 = new GridLayout(var1, var2);
        var4 = new MapView(var3, this.robots);
        var4.setImage("data/img/default.jpg");

        var4.addMouseListener(new ClickListener() {
            public void singleClick(MouseEvent e) {
                if (!stop[0]) {
                    queue.add(new int[]{(int) (e.getPoint().x / fields.get(new Position(0, 0)).getWidth()),
                            (int) (e.getPoint().y / fields.get(new Position(0, 0)).getHeight()), SwingUtilities.isRightMouseButton(e) ? 2 : 1});
                }
            }
            public void doubleClick(MouseEvent e) {
                if (!stop[0]) {
                    queue.add(new int[]{(int) (e.getPoint().x / fields.get(new Position(0, 0)).getWidth()),
                            (int) (e.getPoint().y / fields.get(new Position(0, 0)).getHeight()), 0});
                }
            }
        });

        var4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false), "q pressed");
        var4.getActionMap().put("q pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (files != null && files.length > 0) {
                    File picture = files[backgound_counter];
                    var4.setImage("data/img/"+picture.getName());
                    backgound_counter++;
                    if(backgound_counter == files.length)
                        backgound_counter = 0;
                } else {
                    System.out.println("The specified directory is empty or does not exist.");
                }
            }
        });

        //Arrows keys listener
        var4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up pressed");
        var4.getActionMap().put("up pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (active_robot != null && !in_process && !stop[0]) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            in_process = true;
                            active_robot.move();
                            in_process = false;
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
                if (active_robot != null && !in_process && !stop[0]) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            in_process = true;
                            active_robot.turn(4);
                            active_robot.move();
                            in_process = false;
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
                if (active_robot != null && !in_process && !stop[0]) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            in_process = true;
                            active_robot.turn(-1);
                            in_process = false;
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
                if (active_robot != null && !in_process && !stop[0]) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() {
                            in_process = true;
                            active_robot.turn();
                            in_process = false;
                            return null;
                        }
                    }.execute();
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

        this.frame.getContentPane().add(var4, "Center");
        this.frame.pack();

        recorder = new Recorder();

        this.env.robots().forEach((var1x) -> {
            RobotView var2_1 = new RobotView(this, var1x, stop, lock);
            var1x.addObserver(recorder);
            recorder.addRobot(var1x, var2_1);
            recorder.update(var1x);
            this.robots.add(var2_1);
        });

    }

    public void setActiveRobot(Robot r, Environment room) {
        if (active_robot != null) {
            Runnable run = new runAutonomous(active_robot, room, 100, program_run);
            threads.add(new Thread(run));
            threads.get(threads.size() - 1).start();
        }
        active_robot = r;
    }

    public void add_thread_Robot(ToolRobot robot) {
        RobotView robotView = new RobotView(EnvPresenter.this, robot, stop, lock);
        robots.add(robotView);
        robot.addObserver(recorder);
        recorder.addRobot(robot, robotView);
        recorder.update(robot);
    }

    public void add_Obstacle(Position pos) {
        fields.get(pos).createObstacle();
    }

    protected List<FieldView> fields() {
        return new ArrayList(this.fields.values());
    }

    public void paintRobot() {
        this.var4.repaint();
    }
}
