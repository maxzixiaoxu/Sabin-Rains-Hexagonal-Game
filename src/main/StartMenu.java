package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import persistence.JsonReader;
import persistence.JsonWriter;

public class StartMenu extends JPanel implements MouseListener {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;

    public Color player;
    public Color load;
    public Color robot1;
    public Color robot2;

    private static final String JSON_STORE = "./src/myFile.json";
    // json
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;


    // creates a generic menu
    public StartMenu() {

        player = Color.GREEN;
        load = Color.GREEN;
        robot1 = Color.GREEN;
        robot2 = Color.GREEN;

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

    }

    // MODIFIES: this
    // EFFECTS: if load function is successful, it loads the sr from the json file to be viewed
    // else, provides error message
    private void loadFunction() {

        try {
            SR.play.frame.setVisible(false);
            SR.play = jsonReader.read();
            this.revalidate();
            this.repaint();
        } catch (IOException e) {
            this.add(new JLabel("Unable to read from file: " + JSON_STORE));
            this.revalidate();
            this.repaint();
        }
    }

    public void paintComponent(Graphics g) {

        try {
            ImageIcon image = new ImageIcon(this.getClass().getResource("hexagonMenu.jpg"));
            g.drawImage(image.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Font tfont = new Font (Font.MONOSPACED, 1, 100);
        Font font = new Font(Font.MONOSPACED, 1, 28);
        g.setColor(player);
        g.fillRoundRect(this.WIDTH * 3 / 8, this.HEIGHT * 7 / 20, this.WIDTH / 4, this.HEIGHT / 10, this.WIDTH / 30, this.HEIGHT / 30);
        g.setColor(load);
        g.fillRoundRect(this.WIDTH * 3 / 8, this.HEIGHT * 10 / 20, this.WIDTH / 4, this.HEIGHT / 10, this.WIDTH / 30, this.HEIGHT / 30);
        g.setColor(robot1);
        g.fillRoundRect(this.WIDTH * 3 / 8, this.HEIGHT * 13 / 20, this.WIDTH / 4, this.HEIGHT / 10, this.WIDTH / 30, this.HEIGHT / 30);
        g.setColor(robot2);
        g.fillRoundRect(this.WIDTH * 3 / 8, this.HEIGHT * 16 / 20, this.WIDTH / 4, this.HEIGHT / 10, this.WIDTH / 30, this.HEIGHT / 30);
        g.setColor(Color.GREEN);
        g.setFont(tfont);
        g.drawString("SABIN RAINS", this.WIDTH * 60 / 800, this.HEIGHT * 20/80);
        g.setColor(Color.BLACK);
        g.setFont(font);
        g.drawString("Two-Player", this.WIDTH * 310 / 800, this.HEIGHT * 33/80);
        g.drawString("Load", this.WIDTH * 360 / 800, this.HEIGHT * 45/80);
        g.drawString("Robot 1", this.WIDTH * 320 / 800, this.HEIGHT * 57/80);
        g.drawString("Robot 2", this.WIDTH * 315 / 800, this.HEIGHT * 69/80);
    }

    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        if (x >= this.WIDTH * 3 / 8 && x <= this.WIDTH * 5 / 8 && y >= this.HEIGHT * 7 / 20 && y <= this.HEIGHT * 9 / 20) {
            player = Color.MAGENTA;
            this.repaint();
        } else if (x >= this.WIDTH * 3 / 8 && x <= this.WIDTH * 5 / 8 && y >= this.HEIGHT * 10 / 20 && y <= this.HEIGHT * 12 / 20) {
            load = Color.MAGENTA;
            this.repaint();
        } else if (x >= this.WIDTH * 3 / 8 && x <= this.WIDTH * 5 / 8 && y >= this.HEIGHT * 13 / 20 && y <= this.HEIGHT * 15 / 20) {
            robot1 = Color.MAGENTA;
            this.repaint();
        } else if (x >= this.WIDTH * 3 / 8 && x <= this.WIDTH * 5 / 8 && y >= this.HEIGHT * 16 / 20 && y <= this.HEIGHT * 18 / 20) {
            robot2 = Color.MAGENTA;
            this.repaint();
        }

    }

    public void mouseReleased(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        player = Color.GREEN;
        load = Color.GREEN;
        robot1 = Color.GREEN;
        robot2 = Color.GREEN;

        if (x >= this.WIDTH * 3 / 8 && x <= this.WIDTH * 5 / 8 && y >= this.HEIGHT * 7 / 20 && y <= this.HEIGHT * 9/ 20) {
            SR.play.gamemode = 1;
            SR.play.initGame();
            SR.play.frame.setContentPane(SR.play.gamemenu);
            SR.play.frame.setVisible(true);
        } else if (x >= this.WIDTH * 3 / 8 && x <= this.WIDTH * 5 / 8 && y >= this.HEIGHT * 10 / 20 && y <= this.HEIGHT * 12 / 20) {

            loadFunction();
            SR.play.gamemode = 1;
            SR.play.load = 1;
            SR.play.init();
            SR.play.initGame();
            SR.play.frame.setContentPane(SR.play.gamemenu);
            SR.play.frame.setVisible(true);
        } else if (x >= this.WIDTH * 3 / 8 && x <= this.WIDTH * 5 / 8 && y >= this.HEIGHT * 13 / 20 && y <= this.HEIGHT * 15 / 20) {
            SR.play.gamemode = 2;
            SR.play.initGame();
            SR.play.frame.setContentPane(SR.play.gamemenu);
            SR.play.frame.setVisible(true);
        } else if (x >= this.WIDTH * 3 / 8 && x <= this.WIDTH * 5 / 8 && y >= this.HEIGHT * 16 / 20 && y <= this.HEIGHT * 18 / 20) {
            SR.play.gamemode = 3;
            SR.play.initGame();
            SR.play.frame.setContentPane(SR.play.gamemenu);
            SR.play.frame.setVisible(true);
        }

        this.repaint();
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

}
