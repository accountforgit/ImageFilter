package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

/**
 * Created by lunix on 5/4/16.
 */
public class LomoFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap returnBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        float scaleValue = (95) * 1.0F / (127);
        double roundRadius=Math.sqrt(Math.pow(width,2)+Math.pow(height,2))/2-lposition*2;

        ColorMatrix scaleMatrix = new ColorMatrix();
        scaleMatrix.reset();
        scaleMatrix.setScale((float) (scaleValue + 0.2), (float) (scaleValue + 0.4), (float) (scaleValue + 0.2), 1);

        ColorMatrix satMatrix = new ColorMatrix();
        satMatrix.reset();
        satMatrix.setSaturation(0.85f);

        ColorMatrix allMatrix = new ColorMatrix();
        allMatrix.reset();
        allMatrix.postConcat(scaleMatrix);
        allMatrix.postConcat(satMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(allMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        int[] pixels = new int[width * height];
        returnBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        double centerX = width / 2;
        double centerY = height / 2;

        int currentPos;
        double pixelsFalloff = 10;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dis = Math.sqrt(Math.pow((centerX - x), 2) + Math.pow(centerY - y, 2));
                currentPos = y * width + x;

                if (dis > roundRadius && currentPos < width * height) {
                    int color=(pixels[currentPos]);
                    int pixA=Color.alpha(color);
                    int pixR = Color.red(color);
                    int pixG = Color.green(color);
                    int pixB = Color.blue(color);

                    double scaler = scaleFunc(dis,roundRadius,pixelsFalloff);

                    scaler = Math.abs(scaler);

                    int newR = (int)(pixR - scaler);
                    int newG = (int)(pixG - scaler);
                    int newB = (int)(pixB - scaler);

                    newR = Math.min(255, Math.max(0, newR));
                    newG = Math.min(255, Math.max(0, newG));
                    newB = Math.min(255, Math.max(0, newB));

                    pixels[currentPos] = Color.argb(pixA,newR, newG, newB);

                }
            }
        }
        returnBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return returnBitmap;
    }

    private double scaleFunc(double dis,double roundRadius,double pixelsFalloff) {
        return 1 - Math.pow(((dis - roundRadius) / pixelsFalloff), 2);
    }

    @Override
    public Bitmap onProgress(Bitmap bitmap, int position) {
        lposition=position;
        return useFilter(bitmap);
    }
}
