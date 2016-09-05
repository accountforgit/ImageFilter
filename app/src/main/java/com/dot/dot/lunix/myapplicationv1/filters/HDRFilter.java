package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/4/16.
 */
public class HDRFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap=new BlurFilter().onProgress(bitmap,lposition);
        int smoothPixels[]=new int[width*height];
        bitmap.getPixels(smoothPixels, 0, width, 0, 0, width, height);

        double newR = 0, newG = 0, newB = 0;
        double blurA = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int index = row * width + col;
                if (index < width * height) {
                    int color = (pixels[row * width + col]);
                    int smoothColor = (smoothPixels[row * width + col]);
                    newR = newG = newB = blurA = 0;
                    if (Color.red(smoothColor) / 255.0 <= 0.5) {
                        newR = 2 * (Color.red(smoothColor) / 255.0)
                                * (Color.red(color) / 255.0);
                    } else {
                        newR = 1 - 2 * (1 - Color.red(color) / 255.0) * (1 - Color.red(smoothColor) / 255.0);
                    }
                    if (Color.green(smoothColor) / 255.0 <= 0.5) {
                        newG = 2 * (Color.green(smoothColor) / 255.0) * (Color.green(color) / 255.0);
                    } else {
                        newG = 1 - 2 * (1 - Color.green(color) / 255.0) * (1 - Color.green(smoothColor) / 255.0);
                    }
                    if (Color.blue(smoothColor) / 255.0 <= 0.5) {
                        newB = 2 * (Color.blue(smoothColor) / 255.0) * (Color.blue(color) / 255.0);
                    } else {
                        newB = 1 - 2 * (1 - Color.blue(color) / 255.0) * (1 - Color.blue(smoothColor) / 255.0);
                    }

                    blurA = Color.alpha(smoothColor);
                    pixels[index] = Color.argb((int) blurA, (int) (newR * 255), (int) (newG * 255), (int)( newB * 255));
                }
            }
        }
        bitmap= Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        bitmap=new SharpenFilter().onProgress(bitmap,lposition);
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        Bitmap returnBitmap = Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        return returnBitmap;
    }

    @Override
    public Bitmap onProgress(Bitmap bitmap, int position) {
        lposition=position;
        return useFilter(bitmap);
    }
}
