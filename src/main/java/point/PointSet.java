package main.java.point;

import java.util.HashMap;
import java.util.Map;

public class PointSet {
    public Map<Integer, Pair> Set = new HashMap<>();
    int cnt = 0;

    public void InsertPoint(double x, double y) {
        Set.put(cnt, new Pair((int) Math.round(x), (int) Math.round(y)));
        cnt++;
    }
}
