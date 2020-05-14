package com.release.PitchView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

// Liam Bailey
/* This is a custom canvas class that extends view. It will allow the user to draw
*  on the screen handled by the path and paint values.*/
public class CustomCanvas extends View {

    // Path handles user touch and paint handles the style to draw the path.
    private Paint paint;
    private Path path;

    // Constructor
    public CustomCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        path = new Path();
        paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8f);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
            super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float xPos = event.getX();
        float yPos = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(xPos,yPos);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(xPos, yPos);
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                return false;
        }

        invalidate();
        return true;
    }

    // Method to clear the canvas by reseting the path.
    public void clearCanvas()
    {
        path.reset();
        invalidate();
    }

    // Method to return a bitmap from a CustomCanvas.
    public Bitmap getBitmap()
    {
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);


        return bmp;
    }
}
