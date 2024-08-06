package main;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;

public class HexStone implements Writable {
    private int X;
    private int Y;
    private int hextype;
    private ArrayList<HexStone> AN = new ArrayList<>();
    private ArrayList<HexStone> VN = new ArrayList<>();

    public HexStone(int x, int y, int type) {

        if (isValid(x, y)) {

            this.X = x;
            this.Y = y;
            hextype = type;

            if (type != -1){
                this.AN = initAllNeighbors();
                this.VN = initValidNeighbors();
            }

        } else {

            this.X = -1;
            this.Y = -1;
            hextype = -1;

        }

    }

    public boolean isValid(int x, int y){

        return (x >= 0 && x < 9 && y >= 0 && y < this.Hash(x));

    }

    public boolean equal(HexStone pos) {

        return this.X == pos.X && this.Y == pos.Y;

    }

    public int Hash(int i) {
        if (i >= 0 && i < 5) {
            return i + 5;
        } else if (i >=5 && i < 9){
            return 13 - i;
        }
        else{
            return -1;
        }
    }

    public ArrayList<HexStone> initAllNeighbors() {

        ArrayList<HexStone> neighbor = new ArrayList<>();
        int i = X;
        int j = Y;
        if (i >= 0 && i < 4) {
            neighbor.add(new HexStone(i, j - 1, -1));
            neighbor.add(new HexStone(i - 1, j - 1, -1));
            neighbor.add(new HexStone(i - 1, j, -1));
            neighbor.add(new HexStone(i, j + 1, -1));
            neighbor.add(new HexStone(i + 1, j + 1, -1));
            neighbor.add(new HexStone(i + 1, j,-1));
        }

        else if (i == 4) {
            neighbor.add(new HexStone(i, j - 1, -1));
            neighbor.add(new HexStone(i - 1, j - 1, -1));
            neighbor.add(new HexStone(i - 1, j, -1));
            neighbor.add(new HexStone(i, j + 1, -1));
            neighbor.add(new HexStone(i + 1, j, -1));
            neighbor.add(new HexStone(i + 1, j - 1, -1));
        }

        else if (i >= 5 && i < 9) {
            neighbor.add(new HexStone(i, j - 1, -1));
            neighbor.add(new HexStone(i - 1, j, -1));
            neighbor.add(new HexStone(i - 1, j + 1, -1));
            neighbor.add(new HexStone(i, j + 1, -1));
            neighbor.add(new HexStone(i + 1, j, -1));
            neighbor.add(new HexStone(i + 1, j - 1, -1));
        }
        this.AN = neighbor;
        return neighbor;
    }

    public ArrayList<HexStone> initValidNeighbors() {

        ArrayList<HexStone> neighbor = new ArrayList();
        int i = X;
        int j = Y;
        if (i >= 0 && i < 4) {
            if ((isValid(i, j - 1))) {
                neighbor.add(new HexStone(i, j - 1, -1));
            }

            if (isValid(i - 1, j - 1)) {
                neighbor.add(new HexStone(i - 1, j - 1, -1));
            }

            if ((isValid(i - 1, j))) {
                neighbor.add(new HexStone(i - 1, j, -1));
            }

            if ((isValid(i, j + 1))) {
                neighbor.add(new HexStone(i, j + 1, -1));
            }

            if ((isValid(i + 1, j + 1))) {
                neighbor.add(new HexStone(i + 1, j + 1, -1));
            }

            if ((isValid(i + 1, j))) {
                neighbor.add(new HexStone(i + 1, j, -1));
            }
        }

        if (i == 4) {
            if ((isValid(i, j - 1))) {
                neighbor.add(new HexStone(i, j - 1,-1));
            }

            if ((isValid(i - 1, j - 1))) {
                neighbor.add(new HexStone(i - 1, j - 1, -1));
            }

            if ((isValid(i - 1, j))) {
                neighbor.add(new HexStone(i - 1, j, -1));
            }

            if ((isValid(i, j + 1))) {
                neighbor.add(new HexStone(i, j + 1, -1));
            }

            if ((isValid(i + 1, j))) {
                neighbor.add(new HexStone(i + 1, j, -1));
            }

            if ((isValid(i + 1, j - 1))) {
                neighbor.add(new HexStone(i + 1, j - 1, -1));
            }
        }

        if (i >= 5 && i < 9) {
            if ((isValid(i, j - 1))) {
                neighbor.add(new HexStone(i, j - 1, -1));
            }

            if ((isValid(i - 1, j))) {
                neighbor.add(new HexStone(i - 1, j, -1));
            }

            if ((isValid(i - 1, j + 1))) {
                neighbor.add(new HexStone(i - 1, j + 1, -1));
            }

            if ((isValid(i, j + 1))) {
                neighbor.add(new HexStone(i, j + 1, -1));
            }

            if ((isValid(i + 1, j))) {
                neighbor.add(new HexStone(i + 1, j, -1));
            }

            if ((isValid(i + 1, j - 1))) {
                neighbor.add(new HexStone(i + 1, j - 1, -1));
            }
        }
        this.VN = neighbor;
        return neighbor;
    }

    public int getX() {
        return this.X;
    }

    public int getY(){
        return this.Y;
    }

    public int getHextype(){
        return this.hextype;
    }

    public ArrayList<HexStone> getAN(){
        return this.AN;
    }

    public ArrayList<HexStone> getVN(){
        return this.VN;
    }

    public void setHextype(int hextype) {
        this.hextype = hextype;
    }

    @Override
    public JSONObject toJson() {

        JSONObject json = new JSONObject();
        json.put("X", X);
        json.put("Y", Y);
        json.put("hextype", hextype);
        if (hextype != -1) {
            json.put("AN", hexstonesToJson(AN));
            json.put("VN", hexstonesToJson(VN));
        }
        return json;
    }

    private JSONArray hexstonesToJson(ArrayList<HexStone> ahs){

        JSONArray jsonArray = new JSONArray();

        for (HexStone hs : ahs){
            jsonArray.put(hs.toJson());
        }

        return jsonArray;

    }
}
