package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.Advert;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.utils.ShowActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends ExActivity {

    private static final String CACHE_URL = "advert";
    private ImageView iv_ad, iv_default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_launch);
        iv_ad = (ImageView) findViewById(R.id.iv_ad);
        iv_default = (ImageView) findViewById(R.id.iv_default);
        iv_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isJump = true;
                if(advert!=null){
                Bundle bundle = new Bundle();
                    bundle.putString("url",advert.getJump_url());
                ShowActivity.startActivity(StartActivity.this, bundle, HomeActivity.class.getName());
                finish();
                }
            }
        });
        getWelcomeAdverts();
    }

    private void goNext() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isJump)
                    if (!TextUtils.isEmpty(SettingDefaultsManager.getInstance().authToken())) {
                        ShowActivity.showActivity(StartActivity.this, HomeActivity.class);
                    } else {
                        ShowActivity.showActivity(StartActivity.this, LoginActivity.class);
                    }
                finish();
            }
        }, 3000);
    }

    Advert advert = null;
    boolean isJump = false;

    public void getWelcomeAdverts() {
        WebBase.getWelcomeAdverts(new JSONHandler(this) {
            @Override
            public void onSuccess(JSONObject obj) {

                JSONArray adverts = obj.optJSONArray("adverts");
                try {
                    JSONObject object = adverts.getJSONObject(0);
                    advert = new Advert();
                    advert.setAdvert_id(object.optInt("advert_id"));
                    advert.setImg_url(object.getString("img_url"));
                    advert.setJump_url(object.getString("jump_url"));
                    advert.setEnd_time(object.optLong("end_time"));
                    ImageLoader.getInstance().displayImage(advert.getImg_url(), iv_ad, ImageLoaderOptions.Options());
                    iv_ad.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                iv_ad.setVisibility(View.GONE);
            }
        });

        goNext();
    }

}
