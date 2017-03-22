/**
 * Created by Rahul and Bryan on 3/20/2017.
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

public class GUI extends JPanel
{
    private static final int ARRAY_DIMENSION = 210;
    int [][] board = new int[ARRAY_DIMENSION][ARRAY_DIMENSION];

    public static void main(String [] args)
    {
        JFrame frame = new JFrame();
        frame.setTitle("Fucked Up shit");
        frame.setSize(500,500);
        JScrollPane scroll = new JScrollPane(new GUI(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scroll);
        frame.setVisible(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(3000, 3000);
    }

    public void paintComponent(Graphics g)
    {
        board();
        super.paintComponent(g);
        drawBoard(g);
        /*Graphics2D g2d = (Graphics2D) g;
        Polygon hexagon = new Polygon();
        int x = 104;
        int y = 104;
        int h = 60;

        for(int i = 0; i < 7; i++)
        {
            double a = Math.PI / 3.0 * i;
            hexagon.addPoint((int)(Math.round(x + Math.sin(a) * h)), (int)(Math.round(y + Math.cos(a) * h)));
        }
        g2d.setColor(Color.red);
        g2d.drawPolygon(hexagon);
        g2d.fill(hexagon);*/
    }

    public void board()
    {
        for(int i = 0; i < ARRAY_DIMENSION; i++)
        {
            for(int j = 0; j < ARRAY_DIMENSION; j++)
            {
                board[i][j] = 0;
            }
        }

        board[0][0] = 1;

        board[10][10] = 1;
        board[10][11] = 1;
        board[9][10] = 1;

        board[11][11] = 1;
        board[11][12] = 1;
        board[10][12] = 1;

        board[104][104] = 1;
    }

    public void drawBoard(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        int hexSide = 10;
        int dist = (int)Math.sqrt(Math.pow(hexSide, 2) - Math.pow((hexSide / 2), 2));

        int [][] xCenter = new int [ARRAY_DIMENSION][ARRAY_DIMENSION];
        int [][] yCenter = new int [ARRAY_DIMENSION][ARRAY_DIMENSION];

        for(int i = 0; i < ARRAY_DIMENSION; i++)
        {
            for(int j = 0; j < ARRAY_DIMENSION; j++)
            {
                xCenter[i][j] = 40 + (i%2) *dist + 2*j*dist;
                yCenter[i][j] = 35 + (i*((3*hexSide)/2));
            }
        }

        for(int i = 0; i < ARRAY_DIMENSION; i++)
        {
            for(int j = 0; j < ARRAY_DIMENSION; j++)
            {
                //System.out.print(board[i][j]);
                if(board[i][j] == 1)
                {
                    int [] x = {xCenter[i][j], xCenter[i][j] + dist, xCenter[i][j] + dist, xCenter[i][j], xCenter[i][j] - dist, xCenter[i][j]- dist};
                    int [] y = {yCenter[i][j]-hexSide, yCenter[i][j]-hexSide/2, yCenter[i][j]+hexSide/2,yCenter[i][j]+hexSide, yCenter[i][j]+hexSide/2, yCenter[i][j]-hexSide/2};
                    g2d.drawPolygon(x,y,6);
                }
            }
            //System.out.println();
        }
    }
}
