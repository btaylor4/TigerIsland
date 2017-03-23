import javax.swing.*;

/**
 * Created by Bryan on 3/22/17.
 */
public class GraphicsController extends JFrame
{
    public GraphicsController(GameBoard game)
    {
        GraphicsWindow window = new GraphicsWindow(game);
        JFrame frame = new JFrame();
        JScrollPane scrollPane = new JScrollPane(window);
        frame.add(scrollPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}
