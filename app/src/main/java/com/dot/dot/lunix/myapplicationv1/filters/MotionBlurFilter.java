package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/4/16.
 */
public class MotionBlurFilter extends CustomFilter {

    private int lposition=50;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int xSpeed=3;
        int ySpeed=1;
        int absXSpeed = Math.abs(xSpeed);
        int absYSpeed = Math.abs(ySpeed);
        int originPixels[] = pixels.clone();
        int DELAY=lposition/20;
        for (int y = 0; y < height - DELAY; y++) {
            for (int x = 0; x < width - DELAY; x++) {
                for (int t = 0; t < DELAY; t++) {
                    int color=(originPixels[(y + t) * width + x + t]);
                    int sumA=Color.alpha(color);
                    float sumR = Color.red(color);
                    float sumG = Color.green(color);
                    float sumB = Color.blue(color);
                    for (int xOffset = 1; xOffset <= absXSpeed; xOffset++) {
                        for (int yOffset = 1; yOffset <= absYSpeed; yOffset++) {
                            int xOff, yOff;
                            if (xOffset <= x) {
                                xOff = xOffset;
                            } else {
                                xOff = xOffset + x;
                            }
                            if (yOffset <= y) {
                                yOff = yOffset;
                            } else {
                                yOff = yOffset + y;
                            }
                            int finalXCoord = xSpeed > 0 ? x - xOff * DELAY + t : x + xOff * DELAY + t;
                            int finalYCoord = ySpeed > 0 ? y - yOff * DELAY + t : y + yOff * DELAY + t;
                            finalXCoord = Math.min(width - 1, Math.max(finalXCoord, 0));
                            finalYCoord = Math.min(height - 1, Math.max(finalYCoord, 0));
                            int color1=(originPixels[finalYCoord * width + finalXCoord]);
                            sumR += Color.red(color1);
                            sumG += Color.green(color1);
                            sumB += Color.blue(color1);
                        }
                    }
                    sumR /= (float)(absXSpeed * absYSpeed);
                    sumG /= (float)(absXSpeed * absYSpeed);
                    sumB /= (float)(absXSpeed * absYSpeed);
                    sumR = Math.min(255, Math.max((int) (sumR), 0));
                    sumG = Math.min(255, Math.max((int) (sumG), 0));
                    sumB = Math.min(255, Math.max((int)(sumB), 0));
                    pixels[(y + t) * width + x + t] = Color.argb(sumA, (int) sumR, (int) sumG, (int) sumB);
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
