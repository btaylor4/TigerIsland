import javax.swing.*;
import java.awt.*;

/**
 * Created by Bryan on 3/22/17.
 */
public class GraphicsWindow extends JPanel
{
    private int[][] board = new int[210][210];

    public void paintComponent(Graphics g)
    {
        initBoard();
        super.paintComponent(g);
        drawBoard(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(3000, 3000);
    }

    public void initBoard()
    {
        for(int i = 0; i < 210; i++)
        {
            for(int j = 0; j < 210; j++)
            {
                board[i][j] = 0;
            }
        }

        board[0][0] = 1;

        board[104][104] = 1;
        board[104][105] = 2;
        board[103][105] = 3;

        board[104][106] = 1;
        board[103][107] = 2;
        board[104][107] = 3;
    }


    public void drawBoard(Graphics g)
    {
        double sideLength = 10;
        double a = Math.sqrt(Math.pow(sideLength, 2) - Math.pow((sideLength/2), 2));

        int[][] Xcenters = new int[210][210];
        int[][] Ycenters = new int[210][210];

        for(int i = 0; i < 210; i++) {
            for (int j = 0; j < 210; j++) {
                Xcenters[i][j] = 35 + j * (int)((3 * sideLength) / 2);
                Ycenters[i][j] = 40 + (int)((j % 2) * a + 2 * i * a);
            }
        }

        for(int i = 0; i < 210; i++)
        {
            for (int j = 0; j < 210; j++)
            {
                if (board[i][j] != 0)
                {
                    Graphics2D color = (Graphics2D) g;

                    switch(board[i][j])
                    {
                        case 1:
                            color.setColor(Color.red);
                            break;

                        case 2:
                            color.setColor(Color.BLUE);
                            break;

                        case 3:
                            color.setColor(Color.GRAY);
                            break;
                    }

                    int[] X = { Xcenters[i][j]+(int)(sideLength/2), Xcenters[i][j]+(int)sideLength,
                            Xcenters[i][j]+(int)(sideLength/2), Xcenters[i][j]-(int)(sideLength/2),
                            Xcenters[i][j]-(int)sideLength, Xcenters[i][j]-(int)(sideLength/2) };

                    int[]Y = {Ycenters[i][j]-(int)a, Ycenters[i][j], Ycenters[i][j] +(int)a,
                            Ycenters[i][j]+(int)a, Ycenters[i][j], Ycenters[i][j]-(int)a};

                    Polygon p = new Polygon(X, Y, 6);
                    g.drawPolygon(p);
                    color.fill(p);
                }
            }
        }
    }
}
