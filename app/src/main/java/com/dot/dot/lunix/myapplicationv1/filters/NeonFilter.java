package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/4/16.
 */
public class NeonFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        int xSobel[] = { 1, 2, 1, 0, 0, 0, -1, -2, -1 };
        int ySobel[] = { 1, 0, -1, 2, 0, -2, 1, 0, -1 };

        int index = 0;
        int originGray = 0;
        int afterGray = 0;
        int maskSize = 3;
        int halfMaskSize = maskSize / 2;

        int xVal = 0;
        int yVal = 0;
        float threshold =110-lposition;

        int originPixels[] = pixels.clone();
        for (int i = halfMaskSize; i < height - halfMaskSize; i++) {
            for (int j = halfMaskSize; j < width - halfMaskSize; j++) {
                index = 0;
                afterGray = 0;
                xVal = yVal = 0;
                for (int m = -halfMaskSize; m <= halfMaskSize; m++) {
                    for (int n = -halfMaskSize; n <= halfMaskSize; n++) {
                        int color = (originPixels[(i + m) * width + j + n]);
                        int r=Color.red(color);
                        int b=Color.blue(color);
                        int g=Color.green(color);
                        int k=(int)(r*(0.299)+g*(0.587)+b*(0.114));
                        originGray =k;

                        xVal += originGray * xSobel[index];
                        yVal += originGray * ySobel[index];
                        index++;
                    }
                }
                afterGray = Math.abs(xVal) +Math.abs(yVal);
                afterGray = Math.min(255, Math.max(0, afterGray));
                int pixel_index = i * width + j;
                if (pixel_index < width * height) {
                    if (afterGray > threshold) {
                        pixels[i * width + j] = Color.rgb(127,127,127);
                    } else
                        pixels[i * width + j] = Color.rgb(1, 1, 1);
                }

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
