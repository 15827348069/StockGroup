package com.zbmf.groupro.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.Utils;
import com.zbmf.groupro.view.GridViewForScrollView;
import com.zbmf.groupro.view.RoundedCornerImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegInfoActivity extends AppCompatActivity {

    private static final String TAG = RegInfoActivity.class.getSimpleName();
    private EditText ed_name;
    private TextView tv_name_tip, tv_header_tip;
    private GridViewForScrollView gridview;
    private HeadAdapter adapter;
    private ImageLoader imageloader;
    private DisplayImageOptions options;

    private int selPos = 0;
    private String icon_key = "",url;
    private List<Head> heads = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_info);

        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        DEFAULT_DATA_TEMP = rootPath + DEFAULT_DATA_TEMP;
        DEFAULT_DATA_IMAGEPATH = rootPath + DEFAULT_DATA_IMAGEPATH;

        imageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.h01_1)
                .showImageForEmptyUri(R.drawable.h01_1)
                .showImageOnFail(R.drawable.h01_1)
                .cacheInMemory(true)
                .cacheOnDisc(false)
                .build();

        ed_name = (EditText) findViewById(R.id.ed_name);
        tv_name_tip = (TextView) findViewById(R.id.tv_name_tip);
        tv_header_tip = (TextView) findViewById(R.id.tv_header_tip);
        gridview = (GridViewForScrollView) findViewById(R.id.gridview);

        ed_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0)
                    tv_name_tip.setText("");
            }
        });


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selPos = position;
                Head head = (Head) parent.getItemAtPosition(position);
                icon_key = head.icon_key;
                adapter.notifyDataSetChanged();

                if (position == heads.size() - 1)
                    selectPhoto();
            }
        });

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_name.getText().toString())) {
                    tv_name_tip.setText("昵称不能为空");
                    return;
                }

                submitInfo();
            }
        });

        getDefaultAvatar();

    }

    /**
     * 提交注册信息
     */
    private void submitInfo() {
        WebBase.updateUser(icon_key, ed_name.getText().toString().trim(), new JSONHandler(true, RegInfoActivity.this, "正在提交信息...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(), "数据提交成功！", Toast.LENGTH_SHORT).show();
                SettingDefaultsManager.getInstance().setNickName(ed_name.getText().toString().trim());
                SettingDefaultsManager.getInstance().setUserAvatar(url);
                GroupActivity activity=(GroupActivity) ActivityUtil.getInstance().getActivity(ActivityUtil.GROUPACTIVITY);
                activity.bind_client_id();
                finish();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取默认头像列表
     */
    private void getDefaultAvatar() {
        WebBase.defaultAvatar(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                try {
                    JSONArray array = obj.getJSONArray("avatar");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String url = object.getString("url");
                        String icon_key = object.getString("icon_key");
                        heads.add(new Head(url, icon_key));
                    }
                    heads.add(new Head("", ""));
                    adapter = new HeadAdapter(RegInfoActivity.this);
                    gridview.setAdapter(adapter);
                    icon_key = heads.get(0).icon_key;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 选择图片
     */
    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.PhotoGallery);
    }

    private static final String DEFAULT_DATA_BASEPATH = "/Group+";
    public String DEFAULT_DATA_TEMP = DEFAULT_DATA_BASEPATH + "/TEMP";
    public String DEFAULT_DATA_IMAGEPATH = DEFAULT_DATA_BASEPATH + "/IMAGE";
    private String path = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PhotoGallery) {
                try {
                    Uri uri = data.getData();
                    if (uri != null) {
                        File dir = new File(DEFAULT_DATA_TEMP);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file2 = new File(DEFAULT_DATA_TEMP, "temp");
                        if (!file2.exists()) {
                            file2.createNewFile();
                        } else {
                            file2.delete();
                            file2.createNewFile();
                        }

                        cutPic(uri, file2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == Constants.PHOTO_REQUEST_CUT){
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String filename = Utils.getMD5String(format.format(new Date()));
                    File dir = new File(DEFAULT_DATA_IMAGEPATH);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(DEFAULT_DATA_IMAGEPATH, filename + ".jpg");
                    path = DEFAULT_DATA_IMAGEPATH + File.separator + filename + ".jpg";
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(DEFAULT_DATA_TEMP + "/temp", options);

                    int scale = 1;
                    if (options.outWidth > Constants.ICO_WIDTH && options.outWidth % Constants.ICO_SCALE != 0) {
                        scale = options.outWidth / Constants.ICO_SCALE + 1;
                    } else if (options.outWidth > Constants.ICO_WIDTH && options.outWidth % Constants.ICO_SCALE == 0) {
                        scale = options.outWidth / Constants.ICO_SCALE;
                    }
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = scale;

                    Bitmap photo = BitmapFactory.decodeFile(DEFAULT_DATA_TEMP + "/temp");
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                    photo.compress(Bitmap.CompressFormat.JPEG, 90, bos);//将图片压缩的流里面
                    bos.flush();
                    bos.close();

                    uploadAvatar();//上传头像

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 上传图片
     */
    private void uploadAvatar() {
        WebBase.uploadAvatar(path, new JSONHandler(true,RegInfoActivity.this,"正在上传头像...") {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(getBaseContext(), "头像上传成功！", Toast.LENGTH_SHORT).show();
                icon_key = obj.optString("icon_key");
                url = obj.optString("avatar");
                heads.get(8).url = url;
                heads.get(8).icon_key = icon_key;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getBaseContext(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 裁剪图片
     * @param uri
     * @param file2
     */
    private void cutPic(Uri uri, File file2) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file2));
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, Constants.PHOTO_REQUEST_CUT);
    }

    /**
     * 头像列表适配器
     */
    class HeadAdapter extends BaseAdapter {
        private Context mContext;

        public HeadAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return heads.size();
        }

        @Override
        public Object getItem(int i) {
            return heads.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            View v = View.inflate(mContext, R.layout.header, null);
            RoundedCornerImageView iv = (RoundedCornerImageView) v.findViewById(R.id.iv);
            RelativeLayout root = (RelativeLayout) v.findViewById(R.id.root);

            Head head = heads.get(position);
            if(TextUtils.isEmpty(head.url) && position == heads.size() - 1){
                iv.setImageResource(R.drawable.jiahao);
            }else{
                Log.e(TAG, "getView: " + head.url );
                Glide.with(RegInfoActivity.this).load(head.url).centerCrop().placeholder(R.drawable.h01_1).crossFade().into(iv);
            }

            if (position == selPos) {
                root.setBackgroundResource(R.drawable.white_ring);
            } else {
                root.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
            return v;
        }
    }

    /**
     * 头bean
     */
    class Head {
        public Head(String url, String key) {
            this.icon_key = key;
            this.url = url;
        }

        public String url;
        public String icon_key;
    }
}
