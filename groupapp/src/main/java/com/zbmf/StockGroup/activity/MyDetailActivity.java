package com.zbmf.StockGroup.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.constans.Constants;
import com.zbmf.StockGroup.constans.RequestCode;
import com.zbmf.StockGroup.utils.FileUtils;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.SendBrodacast;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 个人信息页面
 */
public class MyDetailActivity extends BaseActivity implements View.OnClickListener {
    private RoundedCornerImageView my_detail_avatar;
    private TextView my_detail_nickname;
    private Button group_title_right_button;
    private String filename;
    private String mFile;
    private Dialog dialog,edit_dialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_my_detail;
    }

    @Override
    public void initView() {
        initTitle("我的资料");
        my_detail_avatar= (RoundedCornerImageView) findViewById(R.id.my_detail_avatar_id);
        my_detail_nickname= (TextView) findViewById(R.id.my_detail_nickname);
        FrameLayout rightTipR = getView(R.id.rightTipR);
        rightTipR.setVisibility(View.VISIBLE);
        group_title_right_button= (Button) findViewById(R.id.group_title_right_button);
        group_title_right_button.setVisibility(View.VISIBLE);
        group_title_right_button.setText("保存");
    }

    @Override
    public void initData() {
        my_detail_nickname.setText(SettingDefaultsManager.getInstance().NickName());
//        ViewFactory.imgCircleView(this,SettingDefaultsManager.getInstance().UserAvatar(),my_detail_avatar);
        ImageLoader.getInstance().displayImage(SettingDefaultsManager.getInstance().UserAvatar(),my_detail_avatar,
                ImageLoaderOptions.AvatarOptions());
    }

    @Override
    public void addListener() {
        findViewById(R.id.change_user_avatar_id).setOnClickListener(this);
        findViewById(R.id.my_detail_nickname_layout).setOnClickListener(this);
        group_title_right_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.group_title_right_button:
                updateUser();
                break;
            case R.id.my_detail_nickname_layout:
                if(edit_dialog==null){
                    edit_dialog=Editdialog1();
                }
                edit_dialog.show();
                break;
            case R.id.change_user_avatar_id:
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(MyDetailActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            RequestCode.CARMERA_PERMISS);
                } else {
                    if (dialog==null){
                        dialog=dialog1();
                    }
                    dialog.show();
                }

                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RequestCode.WRITE_EXTERNAL_STORAGE:
                if (dialog==null){
                    dialog=dialog1();
                }
                dialog.show();
                break;
        }
    }

    private Dialog dialog1(){
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.avatar_setting_layout, null);
        Button graph=(Button) layout.findViewById(R.id.photograph_button);
        Button album=(Button) layout.findViewById(R.id.photo_album_button);
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
        return  dialog;
    }
    private Dialog Editdialog1(){
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.eidt_layout, null);
        final EditText my_detail_nickname_edit=(EditText) layout.findViewById(R.id.my_detail_nickname_edit);
        my_detail_nickname_edit.setHint(getString(R.string.please_write_nick));
        my_detail_nickname_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int acition_id, KeyEvent keyEvent) {
                if(acition_id== EditorInfo.IME_ACTION_DONE){
                    edit_dialog.dismiss();
                    if(!TextUtils.isEmpty(my_detail_nickname_edit.getText())){
                        my_detail_nickname.setText(my_detail_nickname_edit.getText());
                    }
                    return true;
                }
                return false;
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
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                showSoftInputFromWindow(my_detail_nickname_edit);
            }
        });
        return  dialog;
    }
    public String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date) + ".jpg";
    }
    private void takeCameraOnly(boolean takeCamera){
        filename = getPhotoFileName();
        mFile = FileUtils.getIntence().isCacheFileIsExit(getBaseContext())+"/"+filename;
        if(takeCamera){
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (currentapiVersion<24){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(FileUtils.getIntence().isCacheFileIsExit(getBaseContext()),filename)));
                startActivityForResult(intent, 2);
            }else {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, mFile);
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, 2);
            }
        }else{
            Intent intentFromGallery = new Intent();
            intentFromGallery.setType("image/*"); // 设置文件类型
            intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intentFromGallery,1);
        }
    }
    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        edit_dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(data!=null){
                    startPhotoZoom(data.getData());
                }
                break;
            case 2:
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion<24){
                    File tempFile = new File(mFile);
                    startPhotoZoom(Uri.fromFile(tempFile));
                }else{
                    File tempFile = new File(mFile);
                    startPhotoZoom(getImageContentUri(tempFile));
                }
                break;
            case 3:
                if(data != null){
                    try{
                        setPicToView(data);
                    }catch(Exception e){
                        LogUtil.e(e.getMessage());
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public void startPhotoZoom(Uri uri) {
	       /*
	        * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
	        * yourself_sdk_path/docs/reference/android/content/Intent.html
	        */
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int w=180;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 0.5);
        intent.putExtra("aspectY", 0.5);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", w);
        intent.putExtra("outputY", w);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }
    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    @SuppressWarnings("deprecation")
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            my_detail_avatar.setImageBitmap(photo);
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
    public  void saveBitmap(final String path, Bitmap bitmap)throws IOException {
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
    public void updateUser(){
        WebBase.uploadAvatar(mFile,my_detail_nickname.getText().toString(), new JSONHandler(true,this,"正在保存...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(),"保存成功",Toast.LENGTH_SHORT).show();
                String avatar_url=obj.optString("avatar");
                String nickname=obj.optString("nickname");
                SettingDefaultsManager.getInstance().setNickName(nickname);
                SettingDefaultsManager.getInstance().setUserAvatar(avatar_url);
                SendBrodacast.send(getBaseContext(),Constants.UP_DATA_MESSAGE);
            }
            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(),err_msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
