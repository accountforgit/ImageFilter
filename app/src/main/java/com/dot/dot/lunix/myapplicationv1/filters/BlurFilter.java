package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by lunix on 5/1/16.
 */
public class BlurFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maskSize=(lposition/20)+1+((lposition/20)%2)*1;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int sumR = 0;
        int sumG = 0;
        int sumB = 0;
        int div = maskSize * maskSize;
        int halfMaskSize = maskSize / 2;

        for (int row = halfMaskSize; row < height - halfMaskSize; row++) {
            for (int col = halfMaskSize; col < width - halfMaskSize; col++) {
                sumR = sumG = sumB = 0;
                for (int m = -halfMaskSize; m <= halfMaskSize; m++) {
                    for (int n = -halfMaskSize; n <= halfMaskSize; n++) {
                        int index = (row + m) * width + col + n;
                        if (index < width * height) {
                            int r=Color.red(pixels[(row + m) * width + col + n]);
                            int g=Color.green(pixels[(row + m) * width + col + n]);
                            int b=Color.blue(pixels[(row + m) * width + col + n]);
                            sumR=sumR+r;
                            sumG=sumG+g;
                            sumB=sumB+b;
                        }
                    }
                }
                pixels[row * width + col] = Color.argb(255, sumR / div, sumG / div, sumB / div);
            }
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }

    @Override
    public Bitmap onProgress(Bitmap bitmap, int position) {
        lposition=position;
        return useFilter(bitmap);
    }
}
