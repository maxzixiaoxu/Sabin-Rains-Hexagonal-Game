package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import persistence.JsonReader;
import persistence.JsonWriter;

public class SR implements Writable{
    public static final int STONE_WIDTH = (int)(50.0 * Math.sqrt(3.0)); //86
    public static final int SSIZE = 50;
    public static final int XSTART = (800 - STONE_WIDTH * 5) / 2 - 5; //180
    public HexStone[][] board;
    public ArrayList<HexStone> loadBoard = new ArrayList<>();
    public boolean[][] isChangeable;
    public boolean[] loadIC = new boolean [61];
    public ArrayList<Boolean> tempIC = new ArrayList<>();
    public boolean[][] isLocked;
    public boolean[] loadIL = new boolean [61];
    public ArrayList<Boolean> tempIL = new ArrayList<>();
    public boolean gameover = false;
    public boolean firstend = true;
    public int gamemode = 1;
    public int load = 0;
    public int result = 0;
    public int curr = 1;
    public int time_left = 30;
    public static final int TIME = 30;
    public int red_counter = 0;
    public int blue_counter = 0;
    public int placed_stones = 61;
    public ArrayList<HexStone> WillBeFlipped = new ArrayList();
    public ArrayList<HexStone> stone_array = new ArrayList();
    public ArrayList<ArrayList<HexStone>> hexrings = new ArrayList<>();
    public Timer timer;
    public Random rand = new Random();
    public JFrame frame = new JFrame("Sabin Rains");
    public GameMenu gamemenu;
    public StartMenu startmenu;
    private String name;
    public static SR play;

    static {
        play = new SR("New Game");
    }

    public SR(String name) {
        this.name = name;
    }

    public void init() {
        this.frame.setSize(new Dimension(800, 800));
        this.frame.setTitle("Sabin Rains");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.startmenu = new StartMenu();
        this.startmenu.addMouseListener(this.startmenu);
        this.gamemenu = new GameMenu();
        this.gamemenu.addMouseListener(this.gamemenu);
        this.gamemenu.addMouseMotionListener(this.gamemenu);
        this.frame.setContentPane(this.startmenu);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    public void initGame() {

        if (load == 0) {

            this.board = new HexStone[9][];
            this.isChangeable = new boolean[9][];
            this.isLocked = new boolean[9][];

            for (int i = 0; i < 9; i++) {

                this.board[i] = new HexStone[this.Hash(i)];
                this.isChangeable[i] = new boolean[this.Hash(i)];
                this.isLocked[i] = new boolean[this.Hash(i)];

                for (int j = 0; j < this.Hash(i); j++) {

                    this.board[i][j] = new HexStone(i, j, 0);
                    this.isChangeable[i][j] = true;
                    this.isLocked[i][j] = false;

                }

            }

            initHexrings();
            this.gameover = false;
            this.firstend = true;
            this.result = 0;
            this.curr = 1;
            this.StoneCount();
            this.time_left = TIME;
            this.WillBeFlipped.clear();
        }
        else{

            initHexrings();
            this.gameover = false;
            this.firstend = true;
            this.result = 0;
            this.StoneCount();
            this.time_left = TIME;
            this.WillBeFlipped.clear();

        }

        ActionListener TimeCount = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (SR.this.time_left > 0) {
                    SR.this.time_left--;
                    SR.this.gamemenu.repaint();
                }
                else if (SR.this.time_left == 0) {

                    int index = SR.this.rand.nextInt(SR.this.placed_stones);
                    int i = ( SR.this.stone_array.get(index)).getX();
                    int j = ( SR.this.stone_array.get(index)).getY();
                    SR.this.board[i][j].setHextype(SR.this.curr);
                    SR.this.isChangeable[i][j] = false;

                    HexStone placepos;
                    if (!SR.this.StoneCheck(i, j, SR.this.curr).isEmpty()) {
                        for(Iterator ni = SR.this.StoneCheck(i, j, SR.this.curr).iterator(); ni.hasNext(); SR.this.board[placepos.getX()][placepos.getY()].setHextype(SR.this.curr)) {
                            placepos = (HexStone)ni.next();
                        }
                    }


                    SR.this.PlayCheck(i, j);
                    SR.this.StoneCount();
                    SR.this.WillBeFlipped.clear();
                    SR.this.LockCheck();

                    if (SR.this.gamemode == 1) {

                        SR.this.curr = SR.this.change(SR.this.curr);

                    }

                    if (SR.this.gamemode == 2 && SR.this.placed_stones != 0) {
                        SR.this.curr = SR.this.change(SR.this.curr);
                        placepos = SR.this.Robot1Move();
                        SR.this.board[placepos.getX()][placepos.getY()].setHextype(SR.this.curr);
                        SR.this.isChangeable[placepos.getX()][placepos.getY()] = false;
                        HexStone pos;
                        if (!SR.this.StoneCheck(placepos.getX(), placepos.getY(), SR.this.curr).isEmpty()) {
                            for(Iterator si = SR.this.StoneCheck(placepos.getX(), placepos.getY(), SR.this.curr).iterator(); si.hasNext(); SR.this.board[pos.getX()][pos.getY()].setHextype(SR.this.curr)) {
                                pos = (HexStone) si.next();
                            }
                        }

                        SR.this.PlayCheck(placepos.getX(), placepos.getY());
                        SR.this.StoneCount();
                        SR.this.WillBeFlipped.clear();
                        SR.this.LockCheck();
                        SR.this.curr = SR.this.change(SR.this.curr);
                    }

                    if (SR.this.gamemode == 3 && SR.this.placed_stones != 0) {
                        SR.this.curr = SR.this.change(SR.this.curr);
                        placepos = SR.this.Robot2Move();
                        SR.this.board[placepos.getX()][placepos.getY()].setHextype(SR.this.curr);
                        SR.this.isChangeable[placepos.getX()][placepos.getY()] = false;
                        HexStone pos;
                        if (!SR.this.StoneCheck(placepos.getX(), placepos.getY(), SR.this.curr).isEmpty()) {
                            for(Iterator si = SR.this.StoneCheck(placepos.getX(), placepos.getY(), SR.this.curr).iterator(); si.hasNext(); SR.this.board[pos.getX()][pos.getY()].setHextype(SR.this.curr)) {
                                pos = (HexStone) si.next();
                            }
                        }

                        SR.this.PlayCheck(placepos.getX(), placepos.getY());
                        SR.this.StoneCount();
                        SR.this.WillBeFlipped.clear();
                        SR.this.LockCheck();
                        SR.this.curr = SR.this.change(SR.this.curr);
                    }

                    SR.this.time_left = TIME;
                    SR.this.gamemenu.repaint();
                }
            }
        };

        this.timer = new Timer(1000, TimeCount);
        this.timer.start();

    }

    public void addHsArrays(HexStone hs, String arr){

        if (arr.equals("WBF")){
            WillBeFlipped.add(hs);
        }
        else if(arr.equals("stone")){
            stone_array.add(hs);
        }
        else if (arr.equals("board")){

            loadBoard.add(hs);
            if (loadBoard.size() == 61){
                int c = 0;
                this.board = new HexStone[9][];
                for (int i = 0; i < 9; i++) {

                    this.board[i] = new HexStone[this.Hash(i)];

                    for (int j = 0; j < this.Hash(i); j++) {

                        this.board[i][j] = loadBoard.get(c);
                        c++;
                    }
                }
            }
        }

    }

    public void addBooleanArrays(boolean b, int t){
        if (t == 1){
            tempIC.add(b);
            if (tempIC.size() == 61){
                int c = 0;
                this.isChangeable = new boolean[9][];
                for (int i = 0; i < 9; i++) {

                    this.isChangeable[i] = new boolean[this.Hash(i)];

                    for (int j = 0; j < this.Hash(i); j++) {

                        this.isChangeable[i][j] = tempIC.get(c);
                        c++;
                    }
                }
            }
        }
        else if (t == 2){
            tempIL.add(b);
            if (tempIL.size() == 61){
                int c = 0;
                this.isLocked = new boolean[9][];
                for (int i = 0; i < 9; i++) {

                    this.isLocked[i] = new boolean[this.Hash(i)];

                    for (int j = 0; j < this.Hash(i); j++) {

                        this.isLocked[i][j] = tempIL.get(c);
                        c++;
                    }
                }
            }

        }
    }

    public void PlayCheck(int x, int y) {

        HexStone temp = new HexStone(x, y, 0);
        Iterator<HexStone> ni = temp.getVN().iterator();

        while (ni.hasNext()) {
            HexStone neighbor = ni.next();
            int i = neighbor.getX();
            int j = neighbor.getY();

            if (this.board[i][j].getHextype() == 0) {

                HexStone temp2 = new HexStone(i, j, 0);
                ArrayList<HexStone> secondLevelNeighbors = temp2.getVN();

                this.isChangeable[i][j] = false;

                for (HexStone secondNeighbor : secondLevelNeighbors) {
                    int x2 = secondNeighbor.getX();
                    int y2 = secondNeighbor.getY();

                    if (this.board[x2][y2].getHextype() == 0) {
                        this.isChangeable[i][j] = true;
                        break;
                    }
                }
            }
        }
    }

    // checks and outputs the state of stone after move
    public ArrayList<HexStone> StoneCheck(int x, int y, int color) {

        ArrayList<HexStone> ultimate = new ArrayList<>();
        HexStone temp = new HexStone(x, y, 0);
        Iterator<HexStone> ni = temp.getVN().iterator();

        loop1:
        while (ni.hasNext()) {
            HexStone pos1 = ni.next();
            pos1 = board[pos1.getX()][pos1.getY()];
            ArrayList<HexStone> ring = pos1.getAN();

            for (int i = 0; i < ring.size(); ++i) {
                if (ring.get(i).equal(temp)) {
                    ArrayList<HexStone> clockwise = new ArrayList<>();
                    ArrayList<HexStone> anticlockwise = new ArrayList<>();

                    // Traverse clockwise
                    for (int j = i + 1; j <= i + ring.size(); j++) {
                        int m = ring.get(j % ring.size()).getX();
                        int n = ring.get(j % ring.size()).getY();
                        if (m == -1 || this.board[m][n].getHextype() == 0) {
                            break;
                        }

                        if (this.board[m][n].getHextype() == this.change(color)) {
                            clockwise.add(new HexStone(m, n, 0));
                        }
                        else if (this.board[m][n].getHextype() == color || this.board[m][n].getHextype() == 3) {
                            for (HexStone pos : clockwise) {
                                ultimate.add(pos);
                            }
                            break;
                        }
                    }

                    // Traverse anticlockwise
                    for (int j = i - 1; j >= i - ring.size(); --j) {
                        int m = ring.get((j + ring.size()) % ring.size()).getX();
                        int n = ring.get((j + ring.size()) % ring.size()).getY();
                        if (m == -1 || this.board[m][n].getHextype() == 0) {
                            continue loop1;
                        }

                        if (this.board[m][n].getHextype() == this.change(color)) {
                            anticlockwise.add(new HexStone(m, n, 0));
                        }
                        else if (this.board[m][n].getHextype() == color || this.board[m][n].getHextype() == 3) {
                            for (HexStone pos : anticlockwise) {
                                ultimate.add(pos);
                            }
                            continue loop1;
                        }
                    }
                }
            }
        }

        return ultimate;

    }

    public void LockCheck() {
        
        for(int i = 0; i < 9; i++) {

            for(int j = 0; j < this.Hash(i); j++) {
                this.isLocked[i][j] = true;
            }

        }

        Iterator si = this.stone_array.iterator();

        while(si.hasNext()) {
            HexStone pos = (HexStone) si.next();
            Iterator ni = pos.getVN().iterator();

            while(ni.hasNext()) {
                HexStone pos1 = (HexStone) ni.next();
                pos1 = board[pos1.getX()][pos1.getY()];
                HexStone pos2;
                for(Iterator ni2 = pos1.getVN().iterator(); ni2.hasNext(); this.isLocked[pos2.getX()][pos2.getY()] = false) {
                    pos2 = (HexStone) ni2.next();
                    pos2 = board[pos2.getX()][pos2.getY()];
                }
            }
        }

    }

    public void StoneCount() {

        this.red_counter = 0;
        this.blue_counter = 0;
        this.placed_stones = 0;
        this.stone_array.clear();

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < this.Hash(i); j++) {
                if (this.board[i][j].getHextype() == 1) {
                    this.red_counter++;
                }

                if (this.board[i][j].getHextype() == 2) {
                    this.blue_counter++;
                }

                if (this.isChangeable[i][j]) {
                    this.stone_array.add(new HexStone(i, j, 0));
                    this.placed_stones++;
                }
            }
        }

        if (this.placed_stones == 0) {

            this.gameover = true;
            this.timer.stop();

            if (this.red_counter > this.blue_counter) {
                this.result = 1;
            }

            if (this.blue_counter > this.red_counter) {
                this.result = 2;
            }
        }

    }

    public HexStone Robot1Move() {

        int bestindex = -1;
        HexStone placepos = new HexStone(-1, -1, 0);
        Iterator si = this.stone_array.iterator();

        while(si.hasNext()) {
            HexStone pos = (HexStone)si.next();
            int reversedcount = this.StoneCheck(pos.getX(), pos.getY(), this.curr).size();
            if (reversedcount > bestindex) {
                bestindex = reversedcount;
                placepos = pos;
            }
        }

        return placepos;
    }

    //initialize arraylist of arraylist of hexstones depicting all the hexrings
    public void initHexrings(){

        ArrayList<HexStone> all_hexagon =  new ArrayList<>();
        for(int i = 0; i < 9; i++) {

            for (int j = 0; j < this.Hash(i); j++) {

               all_hexagon.add(new HexStone(i, j, 0));

            }
        }
        Iterator ah = all_hexagon.iterator();
        while (ah.hasNext()){
            HexStone h = (HexStone) (ah.next());
            ArrayList<HexStone> hexring = h.getVN();
            hexrings.add(hexring);
//            for(HexStone hs : hexring){
//                System.out.println(hs.getX() + ", " + hs.getY());
//            }
//            System.out.println("next");
        }

    }
    public HexStone Robot2Move() {

        int bestindex = -1;
        HexStone placepos = new HexStone(-1, -1, 0);
        Iterator si = this.stone_array.iterator();

        while(si.hasNext()) {
            boolean dstate = false;
            HexStone pos = (HexStone)si.next();
            int reversedcount = this.StoneCheck(pos.getX(), pos.getY(), this.curr).size();
            if (reversedcount > bestindex) {

                Iterator hrsi = hexrings.iterator();
                while (hrsi.hasNext()){

                    int count = 0;
                    ArrayList<HexStone> hsal = (ArrayList<HexStone>) hrsi.next();
                    if (hsal.contains(pos)){
                        for (int a = 0; a < hsal.size(); a++){

                            if (this.stone_array.contains(hsal.get(a))){
                                count++;
                            }

                        }
                        // case where stone completes hexring
                        if (count == hsal.size()-1){

                        }
                        // case where stone puts hexring one away from completion
                        else if (count == hsal.size()-2){
                            dstate = true;
                        }
                    }

                }
                if (!dstate) {
                    bestindex = reversedcount;
                    placepos = pos;
                }
            }
        }

        return placepos;
    }

    public int Hash(int i) {
        if (i >= 0 && i < 5) {
            return i + 5;
        } else if (i >= 5 && i < 9){
            return 13 - i;
        }
        else {
            return -1;
        }
    }

    public int change(int curr) {
        
        if (curr == 1) {
            return 2;
        } 
        else if (curr == 2){
            return 1;
        }
        else{
            return -1;
        }
    }

    public Color GetColor(int i) {
        if (i == 1) {
            return Color.RED;
        } 
        else if (i == 2){
            return Color.BLUE;
        }
        else{
            return Color.WHITE;
        }
    }

    public static void main(String[] args) {

        play.init();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", "SRSave");
        json.put("red_counter", red_counter);
        json.put("blue_counter", blue_counter);
        json.put("placed_stones", placed_stones);
        json.put("curr", curr);
        json.put("WillBeFlipped", hexstonesToJson(WillBeFlipped));
        json.put("stone_array", hexstonesToJson(stone_array));

        int counter = 0;
        for (int i = 0; i < 9; i++) {

            for (int j = 0; j < this.Hash(i); j++) {

                loadBoard.add(this.board[i][j]);
                loadIC[counter] = this.isChangeable[i][j];
                loadIL[counter] = this.isLocked[i][j];

                counter++;
            }

        }

        json.put("loadBoard", hexstonesToJson(loadBoard));
        json.put("loadIC", booleanToJson(loadIC));
        json.put("loadIL", booleanToJson(loadIL));

        return json;
    }

    private JSONArray hexstonesToJson(ArrayList<HexStone> ahs){

        JSONArray jsonArray = new JSONArray();

        for (HexStone hs : ahs){
            jsonArray.put(hs.toJson());
        }

        return jsonArray;

    }

    private JSONArray booleanToJson(boolean[] ab){

        JSONArray jsonArray = new JSONArray();

        for (boolean b : ab){
            jsonArray.put(b);
        }

        return jsonArray;

    }
}
