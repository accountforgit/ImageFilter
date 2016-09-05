package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 4/23/16.
 */
public class RedFilter extends CustomFilter {

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
                r=r-lposition;
                if(r<0){
                    r=0;
                }
                if(r>255){
                    r=255;
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
