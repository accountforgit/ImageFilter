package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/4/16.
 */
public class LightRadiusFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int centerX=width/2;
        int centerY=height/2;
        int radius=Math.min(centerX, centerY)+lposition;

        int pixR, pixG, pixB;
        int newR, newG, newB;

        float strength = 150+lposition-50;
        int pos = 0;

        for (int i = 1; i < height - 1; i++) {
            for (int k = 1; k < width - 1; k++) {
                pos = i * width + k;
                if (pos < width * height) {
                    int pixelColor = (pixels[pos]);
                    pixR = Color.red(pixelColor);
                    pixG = Color.green(pixelColor);;
                    pixB = Color.blue(pixelColor);;

                    newR = pixR;
                    newG = pixG;
                    newB = pixB;

                    int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
                    if (distance < radius * radius) {
                        int result = (int) (strength * (1.0 -Math.sqrt(distance) / radius));
                        newR = pixR + result;
                        newG = pixG + result;
                        newB = pixB + result;
                    }
                    newR = Math.min(255, Math.max(0, newR));
                    newG = Math.min(255, Math.max(0, newG));
                    newB = Math.min(255, Math.max(0, newB));

                    pixels[pos] = Color.argb(255, newR, newG, newB);
                }
            }
        }
        Bitmap returnBitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return returnBitmap;
    }

    @Override
    public Bitmap onProgress(Bitmap bitmap, int position) {
        lposition=position;
        return useFilter(bitmap);
    }
}
