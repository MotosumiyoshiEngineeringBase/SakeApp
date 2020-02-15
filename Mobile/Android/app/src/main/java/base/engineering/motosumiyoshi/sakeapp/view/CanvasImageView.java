package base.engineering.motosumiyoshi.sakeapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import base.engineering.motosumiyoshi.sakeapp.model.OCRBox;
import base.engineering.motosumiyoshi.sakeapp.model.LinePoint;

public class CanvasImageView extends View {

    private Paint paint;

    private Bitmap image;

    private List<OCRBox> lineBoxList;

    public CanvasImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public void setImageBitmap(byte[] imageByte){
        this.image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
    }

    public void setLineBoxList(List<OCRBox> lineBoxList) {
        this.lineBoxList = lineBoxList;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画像を描画
        canvas.drawBitmap(image, 0, 0, paint);

        // 描画
        paint.setColor(Color.argb(255, 255, 0, 255));
        paint.setStrokeWidth(5);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        if (lineBoxList != null) {
            for (OCRBox box : lineBoxList) {
                List<LinePoint> points = box.getPoints();
                for (int index = 0; index < points.size() - 1; index++) {
                    canvas.drawLine(
                            points.get(index).getX(),
                            points.get(index).getY(),
                            points.get(index + 1).getX(),
                            points.get(index + 1).getY(),
                            paint);
                }
                if (points.size() > 1) {
                    canvas.drawLine(
                            points.get(points.size() - 1).getX(),
                            points.get(points.size() - 1).getY(),
                            points.get(0).getX(),
                            points.get(0).getY(),
                            paint);
                }
            }
        }
    }
}
