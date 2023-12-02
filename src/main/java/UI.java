package main.java;

import main.java.point.PointSet;
import javax.swing.*;
import java.awt.*;

public class UI {
    JFrame Frame = new JFrame("Function explainer");
    PointDrawer Area = new PointDrawer();
    Graphics g;
    PointSet pointset;

    public void entry(PointSet pointset) {
        init();
        this.pointset = pointset;
        Area.repaint();
    }

    void init() {
        Frame.setSize(1000, 800);
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setResizable(false);
        Frame.setVisible(true);
        Area.setPreferredSize(new Dimension(1000, 800));
        Frame.add(Area);
    }

    class PointDrawer extends Canvas {
        public void paint(Graphics g) {
            g.setColor(Color.RED);
            for (int i = 0; i < pointset.Set.size(); i++) {
                try {
                    g.drawOval(pointset.Set.get(i).getKey(), pointset.Set.get(i).getValue(), 1, 1);
                } catch (NullPointerException e) {
                    continue;
                }
            }
        }
    }
}
