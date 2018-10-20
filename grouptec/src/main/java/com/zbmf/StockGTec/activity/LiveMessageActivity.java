package com.zbmf.StockGTec.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.utils.BitmapUtil;
import com.zbmf.StockGTec.utils.EditTextUtil;
import com.zbmf.StockGTec.utils.EmojiUtil;
import com.zbmf.StockGTec.utils.MessageType;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.Util;

import org.json.JSONObject;

public class LiveMessageActivity extends ExActivity implements View.OnClickListener {

    private TextView tv_right, tv_important, tv_qqh;
    private ImageView iv_qqh, iv_important;
    private boolean isImportant, isQQH;
    private EditText ed_msg;
    private LinearLayout ll_expand;
    private InputMethodManager imm;
    private ImageView upload_img_view;
    private ProgressBar upload_progress_bar;
    private TextView upload_img_view_err;
    private RelativeLayout parogress_bar_layout;
    private String image_url;
    private int width;
    private int height, important, fans, creator = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_message);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        setupView();
    }

    private void setupView() {
        findViewById(R.id.group_title_return).setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.group_title_name);
        tv_title.setText("解盘直播");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_important = (TextView) findViewById(R.id.tv_important);
        tv_qqh = (TextView) findViewById(R.id.tv_qqh);
        ed_msg = (EditText) findViewById(R.id.ed_msg);
        tv_title.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);

        findViewById(R.id.ll_important).setOnClickListener(this);
        findViewById(R.id.ll_qqh).setOnClickListener(this);
        findViewById(R.id.ll_biaoqing).setOnClickListener(this);
        ll_expand = (LinearLayout) findViewById(R.id.ll_expand);
        iv_qqh = (ImageView) findViewById(R.id.iv_qqh);
        iv_important = (ImageView) findViewById(R.id.iv_important);
        upload_img_view = (ImageView) findViewById(R.id.upload_img_view);
        upload_progress_bar = (ProgressBar) findViewById(R.id.upload_progress_bar);
        upload_img_view_err = (TextView) findViewById(R.id.upload_img_view_err);
        parogress_bar_layout = (RelativeLayout) findViewById(R.id.parogress_bar_layout);
        findViewById(R.id.ll_tupian).setOnClickListener(this);
        EmojiUtil emoji = new EmojiUtil(this);
        emoji.setOnItemClickListener(new EmojiUtil.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == parent.getAdapter().getCount() - 1) {
                    ed_msg.dispatchKeyEvent(new KeyEvent(
                            KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
                } else {
                    String content = ed_msg.getText().toString();
                    String emoji = (String) parent.getItemAtPosition(position);
                    int selection = ed_msg.getSelectionStart();
                    StringBuilder sb = new StringBuilder();
                    sb.append(content.substring(0, selection)).append(emoji).append(content.substring(selection, content.length()));
                    ed_msg.setText(EditTextUtil.getContent(LiveMessageActivity.this, ed_msg, sb.toString()));
                    ed_msg.setSelection(selection + emoji.length());
                }
            }
        });
        ll_expand.addView(emoji.getEmojiView());

        ed_msg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ll_expand.setVisibility(View.GONE);
                return false;
            }
        });

        if (SettingDefaultsManager.getInstance().isGroupManager()) {
            creator = 1;
        }
    }

    private int IMAGE_CODE = 1001;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_title_return:
                finish();
                break;
            case R.id.tv_right:
                String message_type;
                if (image_url != null && !TextUtils.isEmpty(image_url)) {
                    message_type = MessageType.IMG;
                } else {
                    image_url = "";
                    message_type = MessageType.TXT;
                }
                WebBase.sendToLive(SettingDefaultsManager.getInstance().getGroupId(), message_type,
                        ed_msg.getText().toString(), image_url, width, height, fans, important, creator, new JSONHandler(LiveMessageActivity.this) {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                finish();
                            }

                            @Override
                            public void onFailure(String err_msg) {
                                Toast.makeText(LiveMessageActivity.this, err_msg, Toast.LENGTH_SHORT).show();
                            }

                        });
                break;
            case R.id.ll_biaoqing:
                if (ll_expand.getVisibility() == View.VISIBLE) {
                    ll_expand.setVisibility(View.GONE);
                } else {
                    ll_expand.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(ed_msg.getWindowToken(), 0);
                }
                break;
            case R.id.ll_important:
                chQQH(false);
                fans = 0;
                isQQH = false;
                if (isImportant) {
                    important = 0;
                    isImportant = !isImportant;
                    chImportant(isImportant);
                } else {
                    important = 1;
                    isImportant = !isImportant;
                    chImportant(isImportant);
                }
                break;
            case R.id.ll_qqh:
                if (SettingDefaultsManager.getInstance().isManager() && !SettingDefaultsManager.getInstance().isSayFans()) {
                    Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                    return;
                }
                chImportant(false);
                important = 0;
                isImportant = false;
                if (isQQH) {
                    fans = 0;
                    isQQH = !isQQH;
                    chQQH(isQQH);
                    ed_msg.setHint("请输入直播室公开内容");
                } else {
                    chImportant(false);
                    important = 0;
                    fans = 1;
                    isQQH = !isQQH;
                    chQQH(isQQH);
                    ed_msg.setHint("请输入铁粉悄悄话");
                }
                break;
            case R.id.ll_tupian:
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, IMAGE_CODE);
                break;
        }
    }

    private void chQQH(boolean selected) {
        tv_qqh.setSelected(selected);
        iv_qqh.setSelected(selected);
    }

    private void chImportant(boolean selected) {
        tv_important.setSelected(selected);
        iv_important.setSelected(selected);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CODE) {
                String imagePath = null;
                Uri originalUri = data.getData();
                if (!TextUtils.isEmpty(originalUri.getAuthority())) {
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                    if (cursor == null) {
                        originalUri = Util.getUri(this, data);
                    }
                    imagePath = Util.getRealPathFromURI(this, originalUri);
                } else {
                    imagePath = originalUri.getPath();
                }
                upload_img_view.setVisibility(View.VISIBLE);
                upload_img_view.setImageBitmap(BitmapUtil.getBitmap(imagePath));
                upload_image(imagePath);
            }
        }
    }

    public void upload_image(final String imagePath) {
        WebBase.uploadImg(SettingDefaultsManager.getInstance().UserId(), imagePath, new JSONHandler(LiveMessageActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                parogress_bar_layout.setVisibility(View.GONE);
                upload_img_view_err.setVisibility(View.GONE);
                upload_progress_bar.setVisibility(View.GONE);
                image_url = obj.optString("url");
                width = obj.optInt("width");
                height = obj.optInt("height");
            }

            @Override
            public void onFailure(String err_msg) {
                upload_progress_bar.setVisibility(View.GONE);
                upload_img_view_err.setVisibility(View.VISIBLE);
                upload_img_view_err.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        upload_image(imagePath);
                    }
                });
            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                // 上传进度显示
                if (upload_img_view_err.getVisibility() == View.VISIBLE) {
                    upload_img_view_err.setVisibility(View.GONE);
                }
                if (parogress_bar_layout.getVisibility() == View.GONE) {
                    parogress_bar_layout.setVisibility(View.VISIBLE);
                }
                if (upload_progress_bar.getVisibility() == View.GONE) {
                    upload_progress_bar.setVisibility(View.VISIBLE);
                }
                upload_progress_bar.setProgress(count);
            }
        });
    }
}
