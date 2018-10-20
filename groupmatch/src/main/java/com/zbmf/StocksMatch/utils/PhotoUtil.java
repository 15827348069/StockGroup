package com.zbmf.StocksMatch.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;

/**
 * Created by lulu on 16/1/12.
 */
public class PhotoUtil {
    private static PhotoUtil instance;
    private Activity activity;

    public PhotoUtil(Activity activity) {
        this.activity = activity;
    }

//    public static synchronized PhotoUtil getInstance() {
//        if (instance == null) {
//            instance = new PhotoUtil();
//        }
//        return instance;
//    }

    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
		/* 使用Intent.ACTION_GET_CONTENT这个Action */
//		intent.setAction(Intent.ACTION_GET_CONTENT);
		/* 取得相片后返回本画面 */
        activity.startActivityForResult(intent, Constants.PhotoGallery);
    }

    public void takePhoto(String filename) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(UiCommon.INSTANCE.DEFAULT_DATA_TEMP);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(UiCommon.INSTANCE.DEFAULT_DATA_TEMP,filename+".jpg");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            activity.startActivityForResult(intent, Constants.TAKEPHOTO);
        }

    }


    public void startPhotoZoom(Uri uri, Uri uri2) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
        // outputX,outputY 是剪裁图片的宽高
        // intent.putExtra("outputX", 600);
        // intent.putExtra("outputY", 600);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, Constants.PHOTO_REQUEST_CUT);
    }

}
