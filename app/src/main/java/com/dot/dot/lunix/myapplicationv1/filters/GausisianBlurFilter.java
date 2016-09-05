package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/1/16.
 */
public class GausisianBlurFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int tempPixels[]=pixels.clone();
        double sigma=((lposition-50)/20.0+1);
        int ksize = (int)Math.ceil((sigma * 3 + 1));
        int maskSize = ksize;
        double kernel[] = new double[ksize * ksize];
        double scale = -0.5 / (sigma * sigma);
        double cons = -scale / Math.PI;
        double sum = 0;
        for (int i = 0; i < ksize; i++) {
            for (int j = 0; j < ksize; j++) {
                int x = i - (ksize - 1) / 2;
                int y = j - (ksize - 1) / 2;
                kernel[i * ksize + j] = cons * Math.exp(scale * (x * x + y * y));

                sum += kernel[i * ksize + j];
            }
        }
        for (int i = 0; i < ksize; i++) {
            for (int j = 0; j < ksize; j++) {
                kernel[i * ksize + j] /= sum;
            }
        }
        double sumR = 0, sumG = 0, sumB = 0;
        int index = 0;
        int bound = maskSize / 2;

        for (int row = bound; row < height - bound; row++) {
            for (int col = bound; col < width - bound; col++) {
                index = 0;
                sumR = sumG = sumB = 0;
                for (int i = -bound; i <= bound; i++) {
                    for (int j = -bound; j <= bound; j++) {
                        int pixel_index = (row + i) * width + col + j;
                        if (pixel_index < width * height) {
                            int color=tempPixels[pixel_index];
                            sumR +=Color.red(color)* kernel[index];
                            sumG += Color.green(color)* kernel[index];
                            sumB += Color.blue(color) * kernel[index];
                            index++;
                        }

                    }
                }

                pixels[row * width + col]=Color.argb(255,(int)sumR,(int)sumG,(int)sumB);
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
