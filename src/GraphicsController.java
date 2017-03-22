import javax.swing.*;

/**
 * Created by Bryan on 3/22/17.
 */
public class GraphicsController extends JFrame
{
    public GraphicsController()
    {
        GraphicsWindow game = new GraphicsWindow();
        JFrame frame = new JFrame();
        JScrollPane scrollPane = new JScrollPane(game);
        frame.add(scrollPane);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

}
