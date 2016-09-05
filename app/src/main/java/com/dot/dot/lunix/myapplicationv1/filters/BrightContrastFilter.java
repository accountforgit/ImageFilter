package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/1/16.
 */
public class BrightContrastFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap bitmap1=new LightFilter().onProgress(bitmap,(lposition)/3);
        bitmap1.getPixels(pixels, 0, width, 0, 0, width, height);
        double contrast=lposition/20.0+1;
        for (int i = 0; i < width * height; i++) {
            int color=pixels[i];
            int a=Color.alpha(color);
            int r = 128 +(int)((Color.red(color) - 128) * contrast);
            int g = 128 +(int)((Color.green(color) - 128) * contrast);
            int b = 128 +(int) ((Color.blue(color) - 128) * contrast);

            r = Math.min(255, Math.max(0, r));
            g = Math.min(255, Math.max(0, g));
            b = Math.min(255, Math.max(0, b));

            pixels[i] =Color.argb(a,r,g,b);
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
