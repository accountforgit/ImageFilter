package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/1/16.
 */
public class OldFilter extends CustomFilter{

    private int lposition=50;
    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++)
        {
            for (int k = 0; k < width; k++)
            {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB)+(lposition)/5;
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB)+(lposition)/5;
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB)+(lposition)/5;
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255 : newB);
                pixels[width * i + k] = newColor;
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
