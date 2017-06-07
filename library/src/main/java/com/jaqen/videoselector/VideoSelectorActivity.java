package com.jaqen.videoselector;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaqen.videoselector.utils.Utils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * VideoSelectorActivity
 * Created by Yancy on 2015/12/2.
 */
public class VideoSelectorActivity extends FragmentActivity implements VideoSelectorFragment.Callback {


    public static final String EXTRA_RESULT = "select_result";

    private ArrayList<String> pathList = new ArrayList<>();

    private VideoConfig videoConfig;

    private TextView title_text;
    //private TextView submitButton;
    private RelativeLayout imageselector_title_bar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoselector_activity);

        videoConfig = VideoSelector.getImageConfig();

        Utils.hideTitleBar(this, R.id.imageselector_activity_layout, videoConfig.getSteepToolBarColor());

        /*getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, VideoSelectorFragment.class.getName(), null))
                .commit();*/

        //submitButton = (TextView) super.findViewById(R.id.title_right);
        title_text = (TextView) super.findViewById(R.id.title_text);
        imageselector_title_bar_layout = (RelativeLayout) super.findViewById(R.id.imageselector_title_bar_layout);

        init();

    }

    private void init() {

        //submitButton.setTextColor(videoConfig.getTitleSubmitTextColor());
        title_text.setTextColor(videoConfig.getTitleTextColor());
        imageselector_title_bar_layout.setBackgroundColor(videoConfig.getTitleBgColor());

        pathList = videoConfig.getPathList();


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                exit();
            }
        });


        if (pathList == null || pathList.size() <= 0) {
            //submitButton.setText(R.string.finish);
            //submitButton.setEnabled(false);
        } else {
            //submitButton.setText((getResources().getText(R.string.finish)) + "(" + pathList.size() + "/" + videoConfig.getMaxSize() + ")");
            //submitButton.setEnabled(true);
        }

        AndPermission.with(this)
                .requestCode(10001)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .send();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, 10001, permissions, grantResults);
    }

    @PermissionYes(10001)
    protected void onStorageYes(List<String> permissions){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, VideoSelectorFragment.class.getName(), null))
                .commit();
    }

    @PermissionNo(10001)
    protected void onStorageNo(List<String> permissions){
        Toast.makeText(this, "未取得权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VideoSelector.IMAGE_CROP_CODE && resultCode == RESULT_OK) {
            Intent intent = new Intent();
            pathList.add(cropImagePath);
            intent.putStringArrayListExtra(EXTRA_RESULT, pathList);
            setResult(RESULT_OK, intent);
            exit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void exit() {
        finish();
    }

    private String cropImagePath;

    private void crop(String imagePath, int aspectX, int aspectY, int outputX, int outputY) {
        File file;
        if (Utils.existSDCard()) {
            file = new File(Environment.getExternalStorageDirectory() + videoConfig.getFilePath(), Utils.getImageName());
        } else {
            file = new File(getCacheDir(), Utils.getImageName());
        }


        cropImagePath = file.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(imagePath)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, VideoSelector.IMAGE_CROP_CODE);
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        pathList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, pathList);
        setResult(RESULT_OK, data);
        exit();
    }

    @Override
    public void onImageSelected(String path) {
        if (!pathList.contains(path)) {
            pathList.add(path);
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (pathList.contains(path)) {
            pathList.remove(path);
            //submitButton.setText((getResources().getText(R.string.finish)) + "(" + pathList.size() + "/" + videoConfig.getMaxSize() + ")");
        } else {
            ////submitButton.setText((getResources().getText(R.string.finish)) + "(" + pathList.size() + "/" + videoConfig.getMaxSize() + ")");
        }
        if (pathList.size() == 0) {
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            Intent data = new Intent();
            pathList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, pathList);
            setResult(RESULT_OK, data);
            exit();
        }
    }
}