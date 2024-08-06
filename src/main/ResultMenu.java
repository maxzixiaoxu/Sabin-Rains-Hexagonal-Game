package main;

import java.awt.*;
import javax.swing.*;

public class ResultMenu extends JPanel {
    public int RESULT;
    public static final int WIDTH = 400;
    public static final int HEIGHT = 400;

    public ResultMenu(int result) {

        this.RESULT = result;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.MAGENTA);
        setForeground(Color.GREEN);
        setFont(new Font(Font.MONOSPACED, Font.BOLD, 50 * WIDTH / 800));
    }

    public void display(){

        JFrame frame = new JFrame("Result");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g.drawString("GAME RESULT", 215 * WIDTH / 800, 300 * HEIGHT / 800);

        switch (RESULT) {
            case 1:
                g.setColor(Color.RED);
                g.drawString("WINNER: RED", 225 * WIDTH / 800, 380 * HEIGHT / 800);
                break;
            case 2:
                g.setColor(Color.BLUE);
                g.drawString("WINNER: BLUE", 215 * WIDTH / 800, 380 * HEIGHT / 800);
                break;
            default:
                g.setColor(Color.BLACK);
                g.drawString("DRAW", 315 * WIDTH / 800, 380 * HEIGHT / 800);
                break;
        }
    }

}
