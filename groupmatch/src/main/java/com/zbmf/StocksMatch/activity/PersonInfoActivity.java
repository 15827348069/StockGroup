package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.GenApiHashUrl;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.JSONFunctions;
import com.zbmf.StocksMatch.utils.PhotoUtil;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CircleImageView;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.http.AsyncHttpClient;
import com.zbmf.StocksMatch.widget.http.AsyncHttpResponseHandler;
import com.zbmf.StocksMatch.widget.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class PersonInfoActivity extends ExActivity implements View.OnClickListener {
    private TextView tv_title;
    private EditText ed_nickname;
    private PhotoUtil photoUtil;
    private String filename;
    private CircleImageView civ;
    private String nickname;
    private String path="";
    private User user = UiCommon.INSTANCE.getiUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        setupView();
    }

    private void setupView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.perinfo);
        ed_nickname = (EditText) findViewById(R.id.ed_nickname);
        civ = (CircleImageView) findViewById(R.id.civ);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.ll_sel).setOnClickListener(this);
        findViewById(R.id.ll_take).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        ed_nickname.setText(user.getNickname());
        ed_nickname.setSelection(user.getNickname().length());

        Log.e("tag",user.getAvatar()+"------");
        imageLoader.displayImage(user.getAvatar(),civ,options);
        photoUtil = new PhotoUtil(this);
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        filename = UiCommon.INSTANCE.getMD5String(format.format(new Date()));
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_sel:
                photoUtil.selectPhoto();
                break;
            case R.id.ll_take:

                photoUtil.takePhoto(filename);
                break;
            case R.id.btn_save:
                nickname = ed_nickname.getText().toString();
                if(nickname.equals(user.getNickname())&&TextUtils.isEmpty(path)){//未做修改
                    UiCommon.INSTANCE.showTip(getString(R.string.edit_tip));
                }else if(!nickname.equals(user.getNickname()) && TextUtils.isEmpty(path)){//只改名字
//                    new UpdateHead_IMGTask(this,R.string.submit_edit,R.string.load_fail,false).execute();
                    onUpLoad(0);
                }else if(nickname.equals(user.getNickname()) && !TextUtils.isEmpty(path)){
                    onUpLoad(0);
                }else
                    onUpLoad(1);//all
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.TAKEPHOTO) {
                try {
                    File file = new File(UiCommon.INSTANCE.DEFAULT_DATA_TEMP,filename +".jpg");
                    File file2 = new File(UiCommon.INSTANCE.DEFAULT_DATA_TEMP,"temp");
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }else {
                        file2.delete();
                        file2.createNewFile();
                    }
                    if (file != null && file.exists()) {
                        //开始剪裁
                        photoUtil.startPhotoZoom(Uri.fromFile(file),Uri.fromFile(file2));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode == Constants.PHOTO_REQUEST_CUT) {
                try {
                    File dir = new File(UiCommon.INSTANCE.DEFAULT_DATA_IMAGEPATH);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(UiCommon.INSTANCE.DEFAULT_DATA_IMAGEPATH, filename+".jpg");
                    path  = UiCommon.INSTANCE.DEFAULT_DATA_IMAGEPATH+File.separator+filename+".jpg";
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(UiCommon.INSTANCE.DEFAULT_DATA_TEMP + "/temp", options);
                    Log.e("tag", "temp1" + UiCommon.INSTANCE.DEFAULT_DATA_TEMP + "/temp");
                    int scale = 1;
                    if (options.outWidth > Constants.ICO_WIDTH && options.outWidth%Constants.ICO_SCALE != 0) {
                        scale = options.outWidth/Constants.ICO_SCALE + 1;
                    }else if(options.outWidth > Constants.ICO_WIDTH && options.outWidth%Constants.ICO_SCALE == 0){
                        scale = options.outWidth/Constants.ICO_SCALE;
                    }
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scale;

                    Bitmap photo = BitmapFactory.decodeFile(UiCommon.INSTANCE.DEFAULT_DATA_TEMP + "/temp");
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将图片压缩的流里面
                    bos.flush();// 刷新此缓冲区的输出流
                    bos.close();
                    //在此上传与设置
                    civ.setImageBitmap(photo);
                 } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (requestCode == Constants.PhotoGallery) {
                try {
                    Uri uri = data.getData();
                    if (uri != null) {

                        File dir = new File(UiCommon.INSTANCE.DEFAULT_DATA_TEMP);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file2 = new File(UiCommon.INSTANCE.DEFAULT_DATA_TEMP,"temp");
                        if (!file2.exists()) {
                            file2.createNewFile();
                        }else {
                            file2.delete();
                            file2.createNewFile();
                        }
                        photoUtil.startPhotoZoom(uri, Uri.fromFile(file2));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void onUpLoad(int type){
        showDialog(this, R.string.submit_edit);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("nickname", nickname);
//            File file = new File("/storage/emulated/0/zbmf/IMAGE/bef5401779f0a5ea8cd784435ec9fac5.jpg");
            File file = null;
            if(!TextUtils.isEmpty(path))
                file  = new File(path);
//            if(!TextUtils.isEmpty(path))
//                file  = new File(path);
//            else
//                file = new File(UiCommon.INSTANCE.DEFAULT_DATA_IMAGEPATH+File.separator+UiCommon.INSTANCE.getMD5String(UiCommon.INSTANCE.getiUser().getAvatar()));
            if(type==0)
                params.put("method","mcc.users.iconupload");
            else
                params.put("method","mcc.users.iconupload1");
            params.put("auth_token",  user.getAuth_token());
            params.put("api_key", "newiph7bafaidke9557430523e00802a");

            List<String> keyList = new ArrayList<String>();
            keyList.add("method");
            keyList.add("auth_token");
            keyList.add("api_key");
            keyList.add("nickname");
            String api_sig = "inewph";
            Collections.sort(keyList, new Comparator<String>() {
                public int compare(String str1, String str2) {
                    if (str1.compareTo(str2) > 0) {
                        return 1;
                    }
                    if (str1.compareTo(str2) < 0) {
                        return -1;
                    }
                    return 0;
                }
            });

            for (String key : keyList) {
                api_sig += key + params.get(key);
            }
            params.put("api_sig", UiCommon.INSTANCE.getMD5String(api_sig));//服务器端特殊加密参数
            if(!TextUtils.isEmpty(path))
                 params.put("avatar", file);

            asyncHttpClient.post(GenApiHashUrl.getInstance().apiUrlm, params ,new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String content) {
                    super.onSuccess(statusCode, headers, content);
                    Log.e("tag", content);
                    try {
                        JSONObject json = new JSONObject(content.toString());
                        User ret = JSONFunctions.iconupload(json,"");
                        if(ret!=null && ret.getStatus()==1){
                            ed_nickname.setText(ret.getNickname());
                            ed_nickname.setSelection(ret.getNickname().length());
                            if(!TextUtils.isEmpty(ret.getAvatar()))
                            user.setAvatar(ret.getAvatar());
                            user.setNickname(ret.getNickname());
                            new DatabaseImpl(PersonInfoActivity.this).addUser(user);
                            UiCommon.INSTANCE.setiUser(user);
                            UiCommon.INSTANCE.showTip(getString(R.string.eidt_succ));
                            finish();

                        }else{
                            UiCommon.INSTANCE.showTip(ret.msg);
                        }
                        DialogDismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable error, String content) {
                    super.onFailure(error, content);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }}

    private Get2Api server = null;

    /**
     * 只改名字
     */
    private class UpdateHead_IMGTask extends LoadingDialog<Void, User>{

        public UpdateHead_IMGTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public User doInBackground(Void... params) {
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                return server.iconupload(nickname,"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void doStuffWithResult(User result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    UiCommon.INSTANCE.showTip("保存成功");
                    finish();
                }else{
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }
}
