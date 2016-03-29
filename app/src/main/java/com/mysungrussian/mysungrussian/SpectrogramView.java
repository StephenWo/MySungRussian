package com.mysungrussian.mysungrussian;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

/**
 * Created by chengchenliu on 2016-03-28.
 */
public class SpectrogramView extends View {
    private Paint paint = new Paint();
    private Bitmap bmp;

    public SpectrogramView(Context context, double [][] data) {
        super(context);

        if (data != null) {
            paint.setStrokeWidth(1);
            int width = data.length;
            int height = data[0].length;    // Higher part of the frequency is always empty, so discard.

            int[] arrayCol = new int[width*height];
            int counter = 0;
            for(int i = height-1; i >=0 ; i--) { //0 is top, want to start from bottom
                for(int j = 0; j < width; j++) {
                    int value;
                    int color;

                    float hsv[] = new float[3];
                    hsv[0] = (float)(1 - data[j][i]*data[j][i]) * 300;
                    hsv[1] = (float) 1.0;   //saturation
                    hsv[2] = (float) 0.5;   //value (brightness)

                    //value = 255 - (int) (data[j][i] * 255);
                    //color = (value << 16 | value << 8 | value | 255 << 24);

                    color = Color.HSVToColor(hsv);
                    //Log.d("!!!", "data="+data[j][i]+" hsv[0]="+hsv[0]+" color="+color);

                    arrayCol[counter] = color;
                    counter ++;
                }
            }

            //Calling createBitmap(int[] colors, int width, int height, Bitmap.Config config)
            bmp = Bitmap.createBitmap(arrayCol, width, height, Bitmap.Config.ARGB_8888);
            bmp = Bitmap.createScaledBitmap(bmp, 200, 200, true);
            Log.d("Che", "width " + width + ", height " + height);


        } else {
            System.err.println("Data Corrupt");
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bmp, 0, 100, paint);
    }

    public Bitmap getBmp (){
        return bmp;
    }
}
