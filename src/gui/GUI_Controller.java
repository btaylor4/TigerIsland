package gui;
import javax.swing.*;

/**
 * Created by Rahul on 3/21/2017.
 */
public class GUI_Controller
{
    public GUI_Controller()
    {
        JFrame frame = new JFrame();
        frame.setTitle("Fucked Up shit");
        frame.setSize(500,500);
        JScrollPane scroll = new JScrollPane(new GUI(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scroll);
        frame.setVisible(true);
    }
}
