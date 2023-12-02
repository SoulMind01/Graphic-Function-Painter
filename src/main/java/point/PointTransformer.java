package main.java.point;

public class PointTransformer {
    double Origin_x = 0.0, Origin_y = 0.0;
    double Scale_x = 1.0, Scale_y = 1.0;
    double Radium = 0.0;

    public void SetOrigin(double x, double y) {
        Origin_x = x;
        Origin_y = y;
    }

    public void SetScale(double x, double y) {
        Scale_x = x;
        Scale_y = y;
    }

    public void SetRadium(double r) {
        Radium = r;
    }

    public void InsertPoint(PointSet pointset, double x, double y) {
        x *= Scale_x;
        y *= Scale_y;
        double xx = x * Math.cos(Radium) + y * Math.sin(Radium);
        double yy = y * Math.cos(Radium) - x * Math.sin(Radium);
        x = xx + Origin_x;
        y = yy + Origin_y;
        pointset.InsertPoint(x, y);
    }
}
