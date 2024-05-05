package tool;
import java.awt.event.*;
import javax.swing.*;

/**
 * The ClickListener class is a class that extends MouseAdapter and implements ActionListener.
 * Class is used to identify double and single clicks
 * @author Vereninov Artem
 * @author Denys Dumych
 */
public class ClickListener extends MouseAdapter implements ActionListener{
    private final static int clickInterval = 300;
    MouseEvent lastEvent;
    Timer timer;

    public ClickListener()
    {
        this(clickInterval);
    }

    public ClickListener(int delay)
    {
        timer = new Timer( delay, this);
    }

    public void mouseClicked (MouseEvent e)
    {
        if (e.getClickCount() > 2) return;

        lastEvent = e;

        if (timer.isRunning())
        {
            timer.stop();
            doubleClick( lastEvent );
        }
        else
        {
            timer.restart();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        timer.stop();
        singleClick( lastEvent );
    }

    public void singleClick(MouseEvent e) {}
    public void doubleClick(MouseEvent e) {}
}
