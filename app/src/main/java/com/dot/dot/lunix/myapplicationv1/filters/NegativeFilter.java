package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 4/19/16.
 */
public class NegativeFilter extends CustomFilter{

    public int lposition=50;

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
            int r1=255-r+(lposition-50);
            int b1=255-b+(lposition-50);
            int g1=255-g+(lposition-50);
            if(r1<0){
                r1=0;
            }
            if(r1>255){
                r1=255;
            }
            if(b1<0){
                b1=0;
            }
            if(b1>255){
                b1=255;
            }
            if(g1<0){
                g1=0;
            }
            if(g1>255){
                g1=255;
            }
            int newColor = Color.argb(a,r1,g1,b1);
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
