package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by lunix on 5/4/16.
 */
public class OilFilter extends CustomFilter {

    private int lposition=127;

    @Override
    public Bitmap useFilter(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int originPixels[] = pixels.clone();
        int rHis[];
        int gHis[];
        int bHis[];
        int OIL_FILTER_LEVEL=128;
        int oilRange=lposition/30+1;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rHis=new int[OIL_FILTER_LEVEL];
                gHis=new int[OIL_FILTER_LEVEL];
                bHis=new int[OIL_FILTER_LEVEL];
                int rowOffset, colOffset;
                for (int row = -oilRange; row <oilRange; row++) {
                    rowOffset = y + row;
                    if (rowOffset >= 0 && rowOffset < height) {
                        for (int col = -oilRange; col <oilRange; col++) {
                            colOffset = x + col;
                            if (colOffset >= 0 && colOffset < width) {
                                int color = originPixels[rowOffset * width + colOffset];
                                int r = Color.red(color)/2;
                                int g = Color.green(color)/2;
                                int b = Color.blue(color)/2;
                                rHis[r]++;
                                gHis[g]++;
                                bHis[b]++;
                            }
                        }
                    }
                }

                int maxR = 0, maxG = 0, maxB = 0;
                for (int i = 1; i < OIL_FILTER_LEVEL; i++) {
                    if (rHis[i] > rHis[maxR]) {
                        maxR = i;
                    }
                    if (gHis[i] > gHis[maxG]) {
                        maxG = i;
                    }
                    if (bHis[i] > bHis[maxB]) {
                        maxB = i;
                    }
                }
                if (rHis[maxR] != 0 && gHis[maxG] != 0 && bHis[maxB] != 0) {
                    int finalR = maxR;
                    int finalG = maxG;
                    int finalB = maxB;

                    finalR = Math.min(255, Math.max(0, finalR));
                    finalG = Math.min(255, Math.max(0, finalG));
                    finalB = Math.min(255, Math.max(0, finalB));

                    pixels[y * width + x] = Color.rgb(finalR, finalG, finalB);
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
