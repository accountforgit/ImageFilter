package com.dot.dot.lunix.myapplicationv1.filters;

import android.graphics.Bitmap;

import com.dot.dot.lunix.myapplicationv1.R;

/**
 * Created by lunix on 4/19/16.
 */
public abstract class CustomFilter implements FilterAction{

}

interface FilterAction{

    public Bitmap useFilter(Bitmap bitmap);

    public Bitmap onProgress(Bitmap bitmap,int position);

}
