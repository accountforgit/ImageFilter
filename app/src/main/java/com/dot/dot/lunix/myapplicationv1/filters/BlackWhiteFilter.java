package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 4/19/16.
 */
public class BlackWhiteFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height*width; i++){
            pixColor = pixels[i];
            pixR = Color.red(pixColor);
            pixG = Color.green(pixColor);
            pixB = Color.blue(pixColor);
            int t=(int)(pixR*0.299+pixG*0.587+pixB*0.114)/(77+lposition);
            int newColor = Color.argb(255, t>0 ? 255:0,t>0 ? 255:0,t>0 ? 255:0);
            pixels[i] = newColor;
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
