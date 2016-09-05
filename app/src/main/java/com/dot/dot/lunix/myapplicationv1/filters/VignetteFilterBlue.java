package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by lunix on 5/2/16.
 */
public class VignetteFilterBlue extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int centerX=width/2;
        int centerY=height/2;
        double scale=1-1.011*(lposition)/100.0;
        double maxD=Math.sqrt(centerX*centerX+centerY*centerY);
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                double dis=Math.sqrt((i-centerY)*(i-centerY)+(j-centerX)*(j-centerX));
                int color=pixels[i*width+j];
                int a=Color.alpha(color);
                int r= Color.red(color);
                int g= Color.green(color);
                int b= Color.blue(color);
                int r1=(int)(r*(1-(1-scale)*(dis/maxD)));
                int g1=(int)(g*(1-(1-scale)*(dis/maxD)));
                int b1=(int)(b*(1-(1-scale)*(dis/maxD)));
                pixels[i*width+j]=Color.argb(a,Math.max(0,Math.min(255,r1)),Math.max(0,Math.min(255,g1)),Math.max(0,Math.min(255,b1)));
            }
        }
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                double dis=Math.sqrt(Math.abs(i-centerY)*Math.abs(i-centerY)+Math.abs(j-centerX)*Math.abs(j-centerX));
                int color=pixels[i*width+j];
                int a=Color.alpha(color);
                int r= Color.red(color);
                int g= Color.green(color);
                int b= Color.blue(color);
                int r1=(int)(r*(1-(1-scale)*(dis/maxD)));
                int g1=(int)(g*(1-(1-scale)*(dis/maxD)));
                int b1=(int)(b*(1-(1-scale)*(dis/maxD)))+lposition/5;
                pixels[i*width+j]=Color.argb(a,Math.max(0,Math.min(255,r1)),Math.max(0,Math.min(255,g1)),Math.max(0,Math.min(255,b1)));
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
