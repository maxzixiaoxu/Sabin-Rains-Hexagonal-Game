package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import persistence.JsonReader;
import persistence.JsonWriter;

public class GameMenu extends JPanel implements MouseListener, MouseMotionListener {
    Color menu;
    Color save;

    private static final String JSON_STORE = "./src/myFile.json";
    // json
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

        public GameMenu() {

        menu = Color.GREEN;
        save = Color.GREEN;

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

    }

    // MODIFIES: this
    // EFFECTS: If save function is successful, it saves the sr onto the JSON file
    // else, provides error message
    private void saveFunction() {
        try {
            jsonWriter.open();
            jsonWriter.write(SR.play);
            jsonWriter.close();
            this.revalidate();
            this.repaint();
        } catch (FileNotFoundException e) {
            this.add(new JLabel("Unable to write to file: " + JSON_STORE));
            this.revalidate();
            this.repaint();
        }
    }

    //utilizes raycasting algorithm
    public boolean withinHexagon(int x, int y, int[] hexagonX, int[] hexagonY) {

        int sides = 6;
        boolean inHexagon = false;

        for (int i = 0, j = sides - 1; i < sides; j = i++) {
            if (((hexagonY[i] > y) != (hexagonY[j] > y)) &&
                    (x < (hexagonX[j] - hexagonX[i]) * (y - hexagonY[i]) / (hexagonY[j] - hexagonY[i]) + hexagonX[i])) {
                inHexagon = !inHexagon;
            }
        }

        return inHexagon;

    }

    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        if (!SR.play.gameover) {

            for(int i = 0; i < 9; i++) {
                for(int j = 0; j < SR.play.Hash(i); j++) {

                    int cenX = (2*SR.XSTART + 2*SR.STONE_WIDTH*j + (SR.STONE_WIDTH*(12-2*SR.play.Hash(i))/2))/2;
                    int cenY = (150 + 150*i)/2;

                    if (withinHexagon(x, y, getXP(cenX, cenY, SR.SSIZE), getYP(cenX, cenY, SR.SSIZE)) && SR.play.isChangeable[i][j]) {

                        SR.play.board[i][j].setHextype(SR.play.curr);
                        SR.play.isChangeable[i][j] = false;
                        HexStone placepos;

                        if (!SR.play.StoneCheck(i, j, SR.play.curr).isEmpty()) {
                            for(Iterator ai = SR.play.StoneCheck(i, j, SR.play.curr).iterator(); ai.hasNext(); SR.play.board[placepos.getX()][placepos.getY()].setHextype(SR.play.curr)) {
                                placepos = (HexStone)ai.next();
                            }
                        }

                        SR.play.PlayCheck(i, j);
                        SR.play.StoneCount();
                        SR.play.WillBeFlipped.clear();
                        SR.play.LockCheck();
                        if (SR.play.gamemode == 1) {

                            SR.play.curr = SR.play.change(SR.play.curr);

                        }

                        if (SR.play.gamemode == 2 && SR.play.placed_stones != 0) {

                            SR.play.curr = SR.play.change(SR.play.curr);
                            placepos = SR.play.Robot1Move();
                            SR.play.board[placepos.getX()][placepos.getY()].setHextype(SR.play.curr);
                            SR.play.isChangeable[placepos.getX()][placepos.getY()] = false;
                            HexStone pos;
                            if (!SR.play.StoneCheck(placepos.getX(), placepos.getY(), SR.play.curr).isEmpty()) {
                                for(Iterator si = SR.play.StoneCheck(placepos.getX(), placepos.getY(), SR.play.curr).iterator(); si.hasNext(); SR.play.board[pos.getX()][pos.getY()].setHextype(SR.play.curr)) {
                                    pos = (HexStone)si.next();
                                }
                            }

                            SR.play.PlayCheck(placepos.getX(), placepos.getY());
                            SR.play.StoneCount();
                            SR.play.WillBeFlipped.clear();
                            SR.play.LockCheck();
                            SR.play.curr = SR.play.change(SR.play.curr);
                        }

                        if (SR.play.gamemode == 3 && SR.play.placed_stones != 0) {

                            SR.play.curr = SR.play.change(SR.play.curr);
                            placepos = SR.play.Robot2Move();
                            SR.play.board[placepos.getX()][placepos.getY()].setHextype(SR.play.curr);
                            SR.play.isChangeable[placepos.getX()][placepos.getY()] = false;
                            HexStone pos;
                            if (!SR.play.StoneCheck(placepos.getX(), placepos.getY(), SR.play.curr).isEmpty()) {
                                for(Iterator si = SR.play.StoneCheck(placepos.getX(), placepos.getY(), SR.play.curr).iterator(); si.hasNext(); SR.play.board[pos.getX()][pos.getY()].setHextype(SR.play.curr)) {
                                    pos = (HexStone)si.next();
                                }
                            }

                            SR.play.PlayCheck(placepos.getX(), placepos.getY());
                            SR.play.StoneCount();
                            SR.play.WillBeFlipped.clear();
                            SR.play.LockCheck();
                            SR.play.curr = SR.play.change(SR.play.curr);
                        }

                        SR.play.time_left = 30;
                        this.repaint();
                    }
                }
            }
        }

    }

    public void mouseReleased(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        if (x >= 640 && x <= 760 && y >= 670 && y <= 730) {
            SR.play.timer.stop();
            SR.play.load = 0;
            SR.play.loadBoard.clear();
            SR.play.tempIC.clear();
            SR.play.tempIL.clear();
            SR.play.frame.setContentPane(SR.play.startmenu);
            SR.play.frame.setVisible(true);
        }

        else if (x >= 40 && x <= 160 && y >= 670 && y <= 730) {
            saveFunction();
        }

    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {

        SR.play.WillBeFlipped.clear();

        int x = e.getX();
        int y = e.getY();

        if (x >= 640 && x <= 760 && y >= 670 && y <= 730) {
            menu = Color.MAGENTA;
        } else {
            menu = Color.BLACK;
        }
        if (x >= 40 && x <= 160 && y >= 670 && y <= 730){
            save = Color.MAGENTA;
        } else{
            save = Color.BLACK;
        }

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < SR.play.Hash(i); j++) {

                int cenX = (2*SR.XSTART + 2*SR.STONE_WIDTH*j + (SR.STONE_WIDTH*(12-2*SR.play.Hash(i))/2))/2;
                int cenY = (150 + 150*i)/2;

                if (withinHexagon(x, y, getXP(cenX, cenY, SR.SSIZE), getYP(cenX, cenY, SR.SSIZE)) && SR.play.isChangeable[i][j]) {
                    SR.play.board[i][j].setHextype(3);
                    SR.play.WillBeFlipped = SR.play.StoneCheck(i, j, SR.play.curr);
                }
                else if (!withinHexagon(x, y, getXP(cenX, cenY, SR.SSIZE), getYP(cenX, cenY, SR.SSIZE)) && SR.play.board[i][j].getHextype() == 3) {
                    SR.play.board[i][j].setHextype(0);
                }
            }
        }

        this.repaint();
    }

    public void paintComponent(Graphics g) {

        Font font = new Font(Font.MONOSPACED, 1, 30);
        g.setFont(font);
        g.setColor(new Color(128, 0, 128));
        g.fillRect(0, 0, 800, 800);
        g.setColor(Color.GREEN);
        g.fillRoundRect(640, 670, 120, 60, 10, 10);
        g.fillRoundRect(40, 670, 120, 60, 10, 10);
        g.setColor(menu);
        g.drawString("Home", 660, 710);
        g.setColor(save);
        g.drawString("Save", 60, 710);

        if (SR.play.gameover && SR.play.firstend) {
            SR.play.firstend = false;
            ResultMenu rm = new ResultMenu(SR.play.result);
            rm.display();
        }
        else {
            Font font2 = new Font(Font.MONOSPACED, 1, 25);
            g.setFont(font2);
            g.setColor(Color.BLACK);
            g.drawString("Timer: " + SR.play.time_left, 630, 60);
            Font font3 = new Font(Font.MONOSPACED, 1, 20);
            g.setFont(font3);
            g.setColor(Color.RED);
            g.drawString("Red Stones:" + SR.play.red_counter, 10, 50);
            g.setColor(Color.BLUE);
            g.drawString("Blue Stones:" + SR.play.blue_counter, 10, 80);

            int X = -1;
            int Y = -1;

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < SR.play.Hash(i); j++) {

                    int cenX = (2*SR.XSTART + 2*SR.STONE_WIDTH*j + (SR.STONE_WIDTH*(12-2*SR.play.Hash(i))/2))/2;
                    int cenY = (150 + 150*i)/2;

                    if (SR.play.board[i][j].getHextype() == 3) {
                        X = i;
                        Y = j;
                    }
                    else {

                        if (SR.play.isLocked[i][j]) {
                            g.setColor(Color.GRAY);
                            fillHexagon(g, cenX, cenY, SR.SSIZE);
                        }

                        g.setColor(Color.BLACK);
                        drawHexagon(g, cenX, cenY, SR.SSIZE);

                    }

                    if (SR.play.board[i][j].getHextype() == 1 || SR.play.board[i][j].getHextype() == 2) {
                        g.setColor(SR.play.GetColor(SR.play.board[i][j].getHextype()));
                        g.fillOval(SR.XSTART + SR.STONE_WIDTH * (5 - SR.play.Hash(i)) / 2 + SR.STONE_WIDTH * j + (SR.STONE_WIDTH - 50) / 2, 50 + 150 * i / 2, 50, 50);
                    }

                    if (SR.play.board[i][j].getHextype() == 3) {
                        g.setColor(SR.play.GetColor(SR.play.curr));
                        g.fillOval(SR.XSTART + SR.STONE_WIDTH * (5 - SR.play.Hash(i)) / 2 + SR.STONE_WIDTH * j + (SR.STONE_WIDTH * 2 - 50) / 4, 50 + 150 * i / 2 + 12, 25, 25);
                    }

                    if (SR.play.board[i][j].getHextype() == 0 && SR.play.isChangeable[i][j]) {
                        g.setColor(Color.GREEN);
                        g.fillOval(SR.XSTART + SR.STONE_WIDTH * (5 - SR.play.Hash(i)) / 2 + SR.STONE_WIDTH * j + (SR.STONE_WIDTH * 2 - 50) / 4, 50 + 150 * i / 2 + 12, 25, 25);
                    }

                    if (!SR.play.isChangeable[i][j] && SR.play.board[i][j].getHextype() != 1 && SR.play.board[i][j].getHextype() != 2 && !SR.play.isLocked[i][j]) {
                        g.setColor(Color.BLACK);
                        g.fillOval(SR.XSTART + SR.STONE_WIDTH * (5 - SR.play.Hash(i)) / 2 + SR.STONE_WIDTH * j + (SR.STONE_WIDTH * 2 - 50) / 4, 50 + 150 * i / 2 + 12, 25, 25);
                    }
                }
            }

            if (!SR.play.WillBeFlipped.isEmpty()) {

                Iterator fi = SR.play.WillBeFlipped.iterator();

                while (fi.hasNext()) {
                    HexStone s = (HexStone) fi.next();
                    int cenX = (2*SR.XSTART + 2*SR.STONE_WIDTH*s.getY() + SR.STONE_WIDTH*(12-2*SR.play.Hash(s.getX()))/2)/2;
                    int cenY = (150 + 150*s.getX())/2;
                    g.setColor(Color.GREEN);
                    drawHexagon(g, cenX, cenY, SR.SSIZE);
                }
            }

            if (X != -1) {
                int cenX = (2*SR.XSTART + 2*SR.STONE_WIDTH*Y + (SR.STONE_WIDTH*(12-2*SR.play.Hash(X))/2))/2;
                int cenY = (150 + 150*X)/2;
                g.setColor(SR.play.GetColor(SR.play.curr));
                drawHexagon(g, cenX, cenY, SR.SSIZE);
            }
        }
    }

    private int[] getXP(int centerX, int centerY, int size){
        int[] xPoints = new int[6];

        // Angle for each vertex of the hexagon (in radians)
        double[] angles = {Math.PI / 6, Math.PI / 2, 5 * Math.PI / 6, 7 * Math.PI / 6, 3 * Math.PI / 2, 11 * Math.PI / 6};

        for (int i = 0; i < 6; i++) {
            xPoints[i] = (int) (centerX + size * Math.cos(angles[i]));
        }
        return xPoints;
    }

    private int[] getYP(int centerX, int centerY, int size){
        int[] yPoints = new int[6];

        // Angle for each vertex of the hexagon (in radians)
        double[] angles = {Math.PI / 6, Math.PI / 2, 5 * Math.PI / 6, 7 * Math.PI / 6, 3 * Math.PI / 2, 11 * Math.PI / 6};

        for (int i = 0; i < 6; i++) {
            yPoints[i] = (int) (centerY + size * Math.sin(angles[i]));
        }
        return yPoints;
    }

    private void fillHexagon(Graphics g, int centerX, int centerY, int size){

        g.fillPolygon(getXP(centerX, centerY, size), getYP(centerX, centerY, size), 6);
    }

    private void drawHexagon(Graphics g, int centerX, int centerY, int size) {
        // Calculate coordinates of the six vertices of the hexagon

//        int[] xPoints = {
//                centerX - size,
//                centerX - size,
//                centerX,
//                centerX + size,
//                centerX + size,
//                centerX
//        };
//
//        int[] yPoints = {
//                centerY + size / 2,
//                centerY - size / 2,
//                centerY - size,
//                centerY - size / 2,
//                centerY + size / 2,
//                centerY + size
//        };

        g.drawPolygon(getXP(centerX, centerY, size), getYP(centerX, centerY, size), 6);
    }

        protected void topDownDraw(Graphics g) {
            super.paintComponent(g);
            Graphics g2d =  g;
            g.setColor(new Color(128, 0, 128));
            g.fillRect(0, 0, getWidth(), getHeight());

            // row 1
            int x = 100;
            int y = 100;
            for (int i = x; i <= (x+SR.STONE_WIDTH*4*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

            // row 2
            x -= SR.STONE_WIDTH;
            y = (int) (y+SR.STONE_WIDTH*1.5);
            for (int i = x; i <= (x+SR.STONE_WIDTH*5*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

            // row 3
            x -= SR.STONE_WIDTH;
            y = (int) (y+SR.STONE_WIDTH*1.5);
            for (int i = x; i <= (x+SR.STONE_WIDTH*6*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

            // row 4
            x -= SR.STONE_WIDTH;
            y = (int) (y+SR.STONE_WIDTH*1.5);
            for (int i = x; i <= (x+SR.STONE_WIDTH*7*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

            // row 5
            x -= SR.STONE_WIDTH;
            y = (int) (y+SR.STONE_WIDTH*1.5);
            for (int i = x; i <= (x+SR.STONE_WIDTH*8*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

            //row 6
            x += SR.STONE_WIDTH;
            y = (int) (y+SR.STONE_WIDTH*1.5);
            for (int i = x; i <= (x+SR.STONE_WIDTH*7*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

            // row 7
            x += SR.STONE_WIDTH;
            y = (int) (y+SR.STONE_WIDTH*1.5);
            for (int i = x; i <= (x+SR.STONE_WIDTH*6*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

            // row 8
            x += SR.STONE_WIDTH;
            y = (int) (y+SR.STONE_WIDTH*1.5);
            for (int i = x; i <= (x+SR.STONE_WIDTH*5*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

            // row 9
            x += SR.STONE_WIDTH;
            y = (int) (y+SR.STONE_WIDTH*1.5);
            for (int i = x; i <= (x+SR.STONE_WIDTH*4*2); i += SR.STONE_WIDTH*2){
                drawHexagon(g2d, i, y, SR.STONE_WIDTH);
            }

        }

}
