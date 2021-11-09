package uk.co.bossdogsoftware.nes_collection.data;

/**
 * Created by Wildheart on 22/07/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PieChartView extends View {
    private Paint slicePaint;
    private Paint sliceBorder;
    private int[] sliceClrs;
    private RectF rectf; // Our box
    private float[] datapoints; // Our values
    private String[] names;
    private int alphaIndex = -1;
    private Bitmap canvasBitmap;

    // The data and color set object to be returned after the drawing is done
    private DataColorSet[] dataColorSet;

    // Our Callback object
    private Callback callback;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        slicePaint = new Paint();
        slicePaint.setAntiAlias(true);
        slicePaint.setDither(true);
        slicePaint.setStyle(Style.FILL);

        sliceBorder = new Paint();
        sliceBorder.setAntiAlias(true);
        sliceBorder.setDither(true);
        sliceBorder.setStyle(Style.STROKE);
        sliceBorder.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.datapoints != null) {
            int startTop = 0;
            int startLeft = 0;
            int endBottom = getWidth();
            int endRight = endBottom; // To make this an equal square
            // Create the box

            rectf = new RectF(startLeft, startTop, endRight, endBottom); // Creating
            // the
            // box

            float[] scaledValues = scale(); // Get the scaled values
            String names[] = names();
            float sliceStartPoint = 0;

            // Initialize colors used
            dataColorSet = new DataColorSet[scaledValues.length];

            for (int i = 0; i < scaledValues.length; i++) {
                slicePaint.setColor(sliceClrs[i]); // Remember to set the paint
                // color first

                // Let's check the alphaIndex if it is greater than or equal to
                // 0 first
                if (alphaIndex > -1 && alphaIndex == i)
                    slicePaint.setAlpha(150); // Then slice at i was pressed
                else
                    slicePaint.setAlpha(255);

                canvas.drawArc(rectf, sliceStartPoint, scaledValues[i], true, slicePaint); // Draw slice
                canvas.drawArc(rectf, sliceStartPoint, scaledValues[i], true, sliceBorder);
                sliceStartPoint += scaledValues[i]; // Update starting point of
                // the next slice

                // Add DataColorSet object to return after draw
                dataColorSet[i] = new DataColorSet( Integer.toHexString(sliceClrs[i]), datapoints[i], names[i] );
            }

            // Build and get what's drawn on the canvas as a bitmap
            buildDrawingCache(true);
            canvasBitmap = getDrawingCache(true);

            // After the drawing has been done use your Callback's
            // onDrawFinished() method
            callback.onDrawFinished(dataColorSet);
        }
    }

    public void setCallback(Callback callback) {
        this.callback = callback; // To initialize the callback object on your
        // activity
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If the finger is on the screen
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Get x and y coordinates of where the finger touched
            int pixelX = (int) (Math.floor(event.getX()));
            int pixelY = (int) (Math.floor(event.getY()));

            if (canvasBitmap != null) {
                // Get the pixel color
                int pixel = canvasBitmap.getPixel(pixelX, pixelY);

                // Convert the pixel into a hex color string
                String pixelHex = Integer.toHexString(pixel); // This will
                // return a
                // hexa-decimal
                // of the bitmap

                // Set the alpha index so that we know which slice to change the
                // opacity of
                setAlphaIndex(pixelHex);
            }

            invalidate();
        }

        // If the finger is off the screen
        if (event.getAction() == MotionEvent.ACTION_UP) {
            alphaIndex = -1; // This is a flag to reset the view alphas to
            // normal
            invalidate();
        }

        return true;
    }

    public void setDataPoints(float[] datapoints, String[] names, int[] sliceClrs) {
        this.datapoints = datapoints;
        this.names = names;
        this.sliceClrs = sliceClrs;
        invalidate(); // Tells the chart to redraw itself
    }

    private void setAlphaIndex(String pixelHex) {
        for (int i = 0; i < sliceClrs.length; i++) {
            String tempHex = Integer.toHexString(sliceClrs[i]);
            if (pixelHex.equals(tempHex)) {
                alphaIndex = i; // This is the slice index to tweak the opacity
                // of

                // Call our slice click here because the slice has been found
                callback.onSliceClick(dataColorSet[i]);

                break;
            }
        }
    }

    private float[] scale() {
        float[] scaledValues = new float[this.datapoints.length];

        float total = getTotal(); // Total all values supplied to the chart
        for (int i = 0; i < this.datapoints.length; i++) {
            scaledValues[i] = (this.datapoints[i] / total) * 360; // Scale each
            // value
        }
        return scaledValues;
    }

    private float getTotal() {
        float total = 0;
        for (float val : this.datapoints)
            total += val;
        return total;
    }

    private String[] names() {
        String[] names = new String[this.names.length];
        for (int i = 0; i < this.names.length; i++) {
            names[i] = (this.names[i]);
        }
        return names;

    }

    // Our Callback interface
    public interface Callback {
        public void onDrawFinished(DataColorSet[] data);
        public void onSliceClick(DataColorSet data);
    }

}