package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 4/19/16.
 */
public class GrayScaleFilter extends CustomFilter {

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
            int r=Color.red(color);
            int b=Color.blue(color);
            int g=Color.green(color);
            int k=(int)(r*(0.299+lposition/300.0-lposition/400.0)+g*(0.587-lposition/300.0-lposition/400.0)+b*(0.114+2*lposition/400.0));
            int newColor = Color.argb(a,k,k,k);
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
