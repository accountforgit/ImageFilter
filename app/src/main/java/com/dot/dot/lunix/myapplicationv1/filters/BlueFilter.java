package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 4/26/16.
 */
public class BlueFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        for(int i=0;i<bitmap.getWidth();i++){
            for(int j=0;j<bitmap.getHeight();j++){
                int color=bitmap.getPixel(i,j);
                int a= Color.alpha(color);
                int r=Color.red(color);
                int b=Color.blue(color);
                int g=Color.green(color);
                b=b-lposition;
                if(b<0){
                    b=0;
                }
                if(b>255){
                    b=255;
                }
                bitmap.setPixel(i,j,Color.argb(a,r,g,b));
            }
        }
        return bitmap;
    }

    @Override
    public Bitmap onProgress(Bitmap bitmap, int position) {
        lposition=position;
        return useFilter(bitmap);
    }
}
