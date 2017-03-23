import javax.swing.*;
import java.awt.*;

/**
 * Created by Bryan on 3/22/17.
 */
public class GraphicsController extends JFrame
{
    private GraphicsWindow window;
    private JFrame frame;

    public GraphicsController(GameBoard game)
    {
        window = new GraphicsWindow(game);
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(700, 700));
        JScrollPane scrollPane = new JScrollPane(window);
        frame.add(scrollPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public GraphicsWindow getWindow()
    {
        return window;
    }

    public JFrame getFrame()
    {
        return frame;
    }
}
