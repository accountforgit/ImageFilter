package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 4/21/16.
 */
public class LightFilter extends CustomFilter{

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height*width; i++){
            int color=pixels[i];
            int a= Color.alpha(color);
            int r=Color.red(color)+lposition;
            int b=Color.blue(color)+lposition;
            int g=Color.green(color)+lposition;
            int newColor = Color.argb(a,r<255 ? r : 255,g<255 ? g : 255,b<255 ? b : 255);
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
