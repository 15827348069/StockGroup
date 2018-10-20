package com.zbmf.StockGroup.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.MyTopicData;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.IntentKey;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.dialog.EmojiViewDialog;
import com.zbmf.StockGroup.utils.EditTextUtil;
import com.zbmf.StockGroup.utils.FileUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.view.View.VISIBLE;

public class FB_GD_Activity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mIv_photo;
    private String mTopic_id;
    private String mTitle;
    private EditText mShareGD;
    private String mFile;
    private Dialog dialog;
    private String mFilePath = "";
    private String mGdStr;
    private TextView mTopicName;
    private boolean isFirst=true;
    private MyTopicData mMyTopicData;
    private Bitmap mBitmap=null;
    private String mImgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb__gd_);
        iCheckPermission();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mTopic_id = extras.getString("topic_id");
            mTitle = extras.getString("title");
            getHtDetail();
        }

        TextView title = findViewById(R.id.group_title_name);
        title.setText("话题");
        ImageButton back = findViewById(R.id.group_title_return);
        FrameLayout rightTipR = findViewById(R.id.rightTipR);
        Button group_title_right_button = findViewById(R.id.group_title_right_button);
        group_title_right_button.setText("发布");
        group_title_right_button.setVisibility(VISIBLE);
        rightTipR.setVisibility(VISIBLE);

        mShareGD = findViewById(R.id.shareGD);
        mTopicName = findViewById(R.id.topicName);
        if (!TextUtils.isEmpty(mTitle)) {
            mTopicName.setText(mTitle);
        }
        mIv_photo = findViewById(R.id.iv_photo);
        ImageView select_emoj_btn = findViewById(R.id.select_emoj_btn);
        ImageView select_pic_btn = findViewById(R.id.select_pic_btn);
        ImageView take_photo_btn = findViewById(R.id.take_photo_btn);

        group_title_right_button.setOnClickListener(this);
        back.setOnClickListener(this);
        select_emoj_btn.setOnClickListener(this);
        select_pic_btn.setOnClickListener(this);
        take_photo_btn.setOnClickListener(this);

        mShareGD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mGdStr = mShareGD.getText().toString().trim();
                }
            }
        });
    }

    //获取话题详情
    private void getHtDetail() {
        //获取话题详情
        WebBase.getHtDetail(mTopic_id, new JSONHandler(isFirst, this, getString(R.string.loading)) {

            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")) {
                    JSONObject data = obj.optJSONObject("data");
                    int topic_id = data.optInt("topic_id");
                    int type_id = data.optInt("type_id");
                    int vp_number = data.optInt("vp_number");
                    int is_hot = data.optInt("is_hot");
                    int users = data.optInt("users");
                    int status = data.optInt("status");
                    String img = data.optString("img");
                    String name = data.optString("name");
                    String title = data.optString("title");
                    String body = data.optString("body");
                    String created_at = data.optString("created_at");
                    mMyTopicData = new MyTopicData(topic_id, type_id, vp_number, is_hot, users,
                            status, img, name, title, body, created_at);
                    isFirst = false;
                    mTopicName.setText(String.format("#%s",title));
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Log.e("--TAG", err_msg);
            }
        });
    }

    private void publishPoint(String content, String img_keys,String width,String height) {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "观点内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(mTopic_id)) {
            WebBase.publishPoint(content, mTopic_id, img_keys,height,width, new JSONHandler(true,this,"正在发布中...") {
                @Override
                public void onSuccess(JSONObject obj) {
                    if (obj.optString("status").equals("ok")) {
                        Toast.makeText(FB_GD_Activity.this, "观点发布成功", Toast.LENGTH_SHORT).show();
                        mShareGD.getText().clear();
                        mIv_photo.setWillNotDraw(true);
                        //发广播  通知话题列表更新数据
                        sendBroadcast(new Intent(IntentKey.FLUSH_TOPIC_LIST));
                        FB_GD_Activity.this.finish();
                    }
                }

                @Override
                public void onFailure(String err_msg) {
                    Log.e("--TAG", "观点发布失败" + "--- 错误信息:" + err_msg);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_emoj_btn:
                if (editdialog1 == null) {
                    editdialog1 = Editdialog1();
                }
                editdialog1.show();
                break;
            case R.id.select_pic_btn:
                //选择图片
                skipSelectPhotos();


                /*mIv_photo.setWillNotDraw(false);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(FB_GD_Activity.this,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCode.WRITE_EXTERNAL_STORAGE);
                } else {
                    if (dialog == null) {
                        dialog = dialog1();
                    }
                    dialog.show();
                }*/
                break;
            case R.id.take_photo_btn:
                //照相
                skipTakePhoto();


               /* mIv_photo.setWillNotDraw(false);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(FB_GD_Activity.this,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCode.CARMERA_PERMISS);
                } else {
                    takeCameraOnly(true);
                }*/
                break;
            case R.id.group_title_right_button:
                //发布观点
                if (!TextUtils.isEmpty(mImgPath)){
                    upFile();
                }else {
                    String content = mShareGD.getText().toString();
                    if (!TextUtils.isEmpty(content)){
                        publishPoint(content, "","","");
                    }else {
                        Toast.makeText(this, "发布的观点内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.group_title_return:
                finish();
                break;
        }
    }

    private void iCheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Constants.permissions.length > 0) {
                for (int i = 0; i < Constants.permissions.length; i++) {
                    if (checkSelfPermission(Constants.permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        //需要授权
                        ActivityCompat.requestPermissions(this, Constants.permissions,
                                IntentKey.REQUEST_PERMISSION_CODE);
                    }
                }
            }
        }
    }
    //选择图片
    public void skipSelectPhotos() {
        PictureSelector.create(FB_GD_Activity.this)
                .openGallery(PictureMimeType.ofImage())
                /*.maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .forResult(PictureConfig.CHOOSE_REQUEST);*/
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
//                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(16,9)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
//                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality()// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }
    //拍照
    public void skipTakePhoto() {
//        PictureSelector.create(FB_GD_Activity.this)
//                .openCamera(PictureMimeType.ofImage())
//                .enableCrop(true)// 是否裁剪 true or false
//                .compress(true)// 是否压缩 true or false
//                .forResult(PictureConfig.CHOOSE_REQUEST);

        PictureSelector.create(FB_GD_Activity.this)
                .openCamera(PictureMimeType.ofImage())
//                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
                .minSelectNum(1)// 最小选择数量 int
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .previewVideo(true)// 是否可预览视频 true or false
                .enablePreviewAudio(true) // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
//                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(16,9)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(true)// 是否显示gif图片 true or false
//                .compressSavePath(getPath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .cropCompressQuality()// 裁剪压缩质量 默认90 int
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .videoQuality(1)// 视频录制质量 0 or 1 int
                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                .recordVideoSecond(60)//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private EmojiViewDialog editdialog1;

    private EmojiViewDialog Editdialog1() {
        return EmojiViewDialog.createDialog(this, R.style.myDialogTheme1)
                .setDiss(new EmojiViewDialog.OnDiss() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDiss(SpannableString content, int length) {
                        String str = mGdStr + content;
                        mShareGD.setText(str);
                        mShareGD.setText(EditTextUtil.getContent(FB_GD_Activity.this, mShareGD, str));
                        mShareGD.setSelection(str.length());
                        mShareGD.requestFocus();//先设置et的内容再设置et的焦点,让光标处于内容最后
                    }
                });
    }

    private Dialog dialog1() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.avatar_setting_layout, null);
        Button graph = (Button) layout.findViewById(R.id.photograph_button);
        Button album = (Button) layout.findViewById(R.id.photo_album_button);
        graph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takeCameraOnly(true);
            }
        });
        album.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takeCameraOnly(false);
            }
        });
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
        win.setWindowAnimations(R.style.dialoganimstyle);
        dialog.setCancelable(true);
        return dialog;
    }

    private void takeCameraOnly(boolean takeCamera) {
        String filename = getPhotoFileName();
        mFile = FileUtils.getIntence().isCacheFileIsExit(getBaseContext()) + "/" + filename;
        if (takeCamera) {
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (currentapiVersion < 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FileUtils.getIntence()
                        .isCacheFileIsExit(getBaseContext()), filename)));
                startActivityForResult(intent, 2);
            } else {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, mFile);
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 2);
            }
        } else {
            Intent intentFromGallery = new Intent();
            intentFromGallery.setType("image/*"); // 设置文件类型
            intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentFromGallery, 1);
        }
    }

    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RequestCode.WRITE_EXTERNAL_STORAGE:
                if (dialog == null) {
                    dialog = dialog1();
                }
                dialog.show();
                break;
            case RequestCode.CARMERA_PERMISS:
                takeCameraOnly(true);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      /*  switch (requestCode) {
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            case 2:
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion < 24) {
                    File tempFile = new File(mFile);
                    startPhotoZoom(Uri.fromFile(tempFile));
                } else {
                    File tempFile = new File(mFile);
                    startPhotoZoom(getImageContentUri(tempFile));
                }
                break;
            case 3:
                if (data != null) {
                    try {
                        setPicToView(data);
                    } catch (Exception e) {
                        LogUtil.e(e.getMessage());
                    }
                }
                break;
        }*/
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    //优先选择上传压缩后的图片
                    boolean compressed = selectList.get(0).isCompressed();
                    Log.i("--TAG","---    是否为压缩的图片   "+compressed);
                    if (selectList.get(0).isCompressed()) {
                        mImgPath = selectList.get(0).getCompressPath();
                        mBitmap = BitmapFactory.decodeFile(mImgPath);
                        mIv_photo.setImageBitmap(mBitmap);
                    } else {
                        mImgPath = selectList.get(0).getPath();
                        mBitmap = BitmapFactory.decodeFile(mImgPath);
                        mIv_photo.setImageBitmap(mBitmap);
                    }
//                    mBitmap.recycle();
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
           /*
            * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
	        * yourself_sdk_path/docs/reference/android/content/Intent.html
	        */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w = 500;
        int h = 1080;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 0.5);
        intent.putExtra("aspectY", 0.5);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", h);
        intent.putExtra("outputY", h);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    public Uri getImageContentUri(File imageFile) {
        mFilePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{mFilePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, mFilePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public void upFile() {
        WebBase.upImgFile(mImgPath, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                if (obj.optString("status").equals("ok")){
                    JSONObject image = obj.optJSONObject("image");
                     /*String file_key = image.optString("file_key");
                    String object = image.optString("object");*/
                    String url = image.optString("url");
                    int width = image.optInt("width");
                    int height = image.optInt("height");
                    Log.i("--TAG","--- 上传图片     返回的图片的宽高  wid:"+width+"----  hei:"+height);
                    //发布观点
                    String content = mShareGD.getText().toString();
                    if (!TextUtils.isEmpty(content)){
                        publishPoint(content, url,String.valueOf(width),String.valueOf(height));
                    }else {
                        Toast.makeText(FB_GD_Activity.this, "发布内容不能为空", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
               Log.e("--TAG","  返回的图片   "+err_msg);
            }
        });
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    @SuppressWarnings("deprecation")
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            mIv_photo.setImageBitmap(photo);
            try {
                saveBitmap(mFile, photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将位图保存到指定的路径
     *
     * @param path
     * @param bitmap
     * @throws IOException
     */
    public void saveBitmap(final String path, Bitmap bitmap) throws IOException {
        if (path != null && bitmap != null) {
            File _file = new File(path);
            // 如果文件夹不存在则创建一个新的文件
            if (!_file.exists()) {
                _file.getParentFile().mkdirs();
                _file.createNewFile();
            }
            // 创建输出流
            OutputStream write = new FileOutputStream(_file);
            // 获取文件名
            String fileName = _file.getName();
            // 取出文件的格式名
            String endName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if ("png".equalsIgnoreCase(endName)) {
                // bitmap的压缩格式
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, write);
                write.close();
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, write);
                write.close();
            }
        }
    }
}
