package base.engineering.motosumiyoshi.sakeapp.model;

import java.util.List;

public class OCRBox {

    private final String text;

    private final List<LinePoint> points;

    public OCRBox(String text, List<LinePoint> points) {
        this.text = text;
        this.points = points;
    }

    public String getText() {
        return text;
    }

    public List<LinePoint> getPoints() {
        return points;
    }
}
