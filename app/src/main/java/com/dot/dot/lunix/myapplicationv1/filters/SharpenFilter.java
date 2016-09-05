package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/2/16.
 */
public class SharpenFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int pixR, pixG, pixB, newR, newG, newB, index;
        int laplacian[] = {0, -1, 0, -1,lposition/20+2, -1, 0, -1, 0}; // 3 * 3 laplacian
        int maskSize = 3;
        int halfMaskSize = maskSize / 2;

        int edgePixels[]=pixels.clone();
        for (int i = halfMaskSize; i < height - halfMaskSize; i++)  {
            for (int k = halfMaskSize; k < width - halfMaskSize; k++)  {
                index = 0;
                newR = newG = newB = 0;
                for (int m = -halfMaskSize; m <= halfMaskSize; m++) {
                    for (int n = -halfMaskSize; n <= halfMaskSize; n++) {
                        int pixel_index = (i + n) * width + k + m;
                        if (pixel_index < width * height) {
                            int color=pixels[pixel_index];
                            pixR = Color.red(color);
                            pixG = Color.green(color);
                            pixB = Color.blue(color);
                            newR += pixR * laplacian[index];
                            newG += pixG * laplacian[index];
                            newB += pixB * laplacian[index];
                            index++;
                        }
                    }
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                edgePixels[i * width + k] =Color.argb(255,newR, newG, newB);
            }
        }
        for (int i = 0; i < width * height; i++) {
            int color=edgePixels[i];
            int color1=pixels[i];
            int r = Math.min(255, Math.max(0, Color.red(color) + Color.red(color1)));
            int g = Math.min(255, Math.max(0, Color.green(color) + Color.green(color1)));
            int b = Math.min(255, Math.max(0, Color.blue(color) + Color.blue(color1)));
            pixels[i] = Color.argb(255, r, g, b);
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
