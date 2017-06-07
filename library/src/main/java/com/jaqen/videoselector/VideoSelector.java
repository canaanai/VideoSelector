package com.jaqen.videoselector;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.jaqen.videoselector.utils.Utils;


/**
 * VideoSelector
 * Created by Yancy on 2015/12/6.
 */
public class VideoSelector {


    public static final int VIDEO_REQUEST_CODE = 1002;
    public static final int IMAGE_CROP_CODE = 1003;

    private static VideoConfig mVideoConfig;

    public static VideoConfig getImageConfig() {
        return mVideoConfig;
    }

    public static void open(Activity activity, VideoConfig config) {
        if (config == null) {
            return;
        }
        mVideoConfig = config;

        if (config.getVideoLoader() == null) {
            Toast.makeText(activity, R.string.open_camera_fail_video, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utils.existSDCard()) {
            Toast.makeText(activity, R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(activity, VideoSelectorActivity.class);
        activity.startActivityForResult(intent, mVideoConfig.getRequestCode());
    }

    public static void open(Fragment fragment, VideoConfig config) {
        if (config == null) {
            return;
        }
        mVideoConfig = config;

        if (config.getVideoLoader() == null) {
            Toast.makeText(fragment.getActivity(), R.string.open_camera_fail_video, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utils.existSDCard()) {
            Toast.makeText(fragment.getActivity(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(fragment.getActivity(), VideoSelectorActivity.class);
        fragment.startActivityForResult(intent, mVideoConfig.getRequestCode());
    }

    /*public static void open(Fragment fragment, VideoConfig config, int requestCode) {
        if (config == null) {
            return;
        }
        mVideoConfig = config;

        if (config.getVideoLoader() == null) {
            Toast.makeText(fragment.getActivity(), R.string.open_camera_fail_video, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Utils.existSDCard()) {
            Toast.makeText(fragment.getActivity(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(fragment.getActivity(), VideoSelectorActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }*/
}
