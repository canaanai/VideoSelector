package com.jaqen.videoselector;

import android.content.Context;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * VideoLoader
 * Created by Yancy on 2015/12/6.
 */
public interface VideoLoader extends Serializable {
    void displayImage(Context context, String path, ImageView imageView);
}