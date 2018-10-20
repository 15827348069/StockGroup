package com.zbmf.StockGTec.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
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
import com.zbmf.StockGTec.beans.BoxBean;
import com.zbmf.StockGTec.utils.BitmapUtil;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;
import com.zbmf.StockGTec.utils.Util;

import org.json.JSONObject;

import static com.igexin.sdk.GTServiceManager.context;

//宝盒更新
public class BoxUpdateActivity extends ExActivity implements View.OnClickListener {

    private TextView tv_right;
    private BoxBean box;
    private TextView tv_box_name, tv_box_upde_time, tv_desc;
    private LinearLayout tag_layout;
    private ImageView iv;
    private EditText ed_msg;
    private ImageView upload_img_view;
    private ProgressBar upload_progress_bar;
    private TextView upload_img_view_err;
    private RelativeLayout parogress_bar_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_update);

        initData();
        setupView();
    }

    private void initData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            box = (BoxBean) bundle.getSerializable("box");
        }
    }

    private void setupView() {
        iv = (ImageView) findViewById(R.id.box_img_id);
        findViewById(R.id.group_title_return).setOnClickListener(this);
        TextView tv_title = (TextView) findViewById(R.id.group_title_name);
        tv_title.setText("宝盒更新");
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setOnClickListener(this);
        tv_title.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_box_name = (TextView) findViewById(R.id.box_name);
        tv_box_upde_time = (TextView) findViewById(R.id.box_upde_time);
        tag_layout = (LinearLayout) findViewById(R.id.tag_layout);
        ed_msg = (EditText) findViewById(R.id.ed_msg);
        findViewById(R.id.ll_tupian).setOnClickListener(this);
        upload_img_view = (ImageView) findViewById(R.id.upload_img_view);
        upload_progress_bar = (ProgressBar) findViewById(R.id.upload_progress_bar);
        upload_img_view_err = (TextView) findViewById(R.id.upload_img_view_err);
        parogress_bar_layout = (RelativeLayout) findViewById(R.id.parogress_bar_layout);

        if (box != null) {
            initBoxData(box);
        }
    }

    private void initBoxData(BoxBean bb) {
        tv_box_upde_time.setText(bb.getBox_updated());
        tv_box_name.setText(bb.getSubject());
        if (bb.getTags() != null && bb.getTags().size() > 0) {
            tag_layout.removeAllViewsInLayout();
            tag_layout.setVisibility(View.VISIBLE);
            for (BoxBean.Tags tag : bb.getTags()) {
                tag_layout.addView(getTagView(tag));
            }

        } else {
            tag_layout.removeAllViewsInLayout();
            tag_layout.setVisibility(View.GONE);
        }

        switch (bb.getBox_level()) {
            case "20":
                iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_year_fans_1));
                break;
            case "10":
                if (bb.getTags() != null && bb.getTags().size() > 0) {
                    if (bb.getTags().get(0).equals("投资策略")) {
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_fans_1));
                    } else if (bb.getTags().get(0).equals("操盘日志")) {
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_fans_2));
                    } else {
                        iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_fans_3));
                    }
                } else {
                    iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_fans_1));
                }

                break;
            default:
                iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_public_fans2));
                break;
        }
    }

    public View getTagView(BoxBean.Tags tag) {
        View view = View.inflate(this, R.layout.tag_text_view, null);
        TextView tag_text = (TextView) view.findViewById(R.id.tag_layout_id);
        switch (tag.getTag_type()) {
            case 1:
                tag_text.setBackgroundResource(R.drawable.text_backound_blue);
                break;
            case 2:
                tag_text.setBackgroundResource(R.drawable.text_backound_orange);
                break;
            case 3:
                tag_text.setBackgroundResource(R.drawable.text_backound);
                break;
            default:
                tag_text.setBackgroundResource(R.drawable.text_backound_def);
                break;
        }
        tag_text.setText(tag.getName());
        return view;
    }

    private int IMAGE_CODE = 1001;
    private String image_url = "";
    private String content = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.group_title_return:
                finish();
                break;
            case R.id.tv_right:
                content = ed_msg.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    Toast.makeText(this,"请输入宝盒内容!",0).show();
                    return;
                }
                WebBase.createManageBoxItem(box.getBox_id(),content,image_url, new JSONHandler(true, this, "加载中...") {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        Toast.makeText(BoxUpdateActivity.this,"宝盒发送成功!",0).show();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("box", box);
                        ShowActivity.startActivity(BoxUpdateActivity.this, bundle, BoxDetailActivity.class.getName());
                        finish();
                    }

                    @Override
                    public void onFailure(String err_msg) {
                        Toast.makeText(BoxUpdateActivity.this,err_msg,0).show();
                    }
                });
                break;
            case R.id.ll_tupian:
                Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
                albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(albumIntent, IMAGE_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
        WebBase.uploadImg(SettingDefaultsManager.getInstance().UserId(), imagePath, new JSONHandler(BoxUpdateActivity.this) {
            @Override
            public void onSuccess(JSONObject obj) {
                parogress_bar_layout.setVisibility(View.GONE);
                upload_img_view_err.setVisibility(View.GONE);
                upload_progress_bar.setVisibility(View.GONE);
                image_url = obj.optString("url");
//                width=obj.optInt("width");
//                height=obj.optInt("height");
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
