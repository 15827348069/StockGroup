package com.zbmf.StocksMatch.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StocksMatch.R;
import com.zbmf.worklibrary.util.ActivityUtil;

/**
 * Created by pq
 * on 2018/3/29.
 */

public abstract class WebViewBaseActivity extends AppCompatActivity {

    private TextView title_text;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.addActivity(this,getClass());
        init();
    }
    private void init(){
        setContentView(getLayoutResId());
        View view=getView(R.id.action_bar_layout);
        if(view!=null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                view.setVisibility(View.VISIBLE);
                int statusBarHeight1 = -1;
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    //根据资源ID获取响应的尺寸值
                    statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
                    ViewGroup.LayoutParams layoutParams= view.getLayoutParams();
                    layoutParams.height=statusBarHeight1;
                    view.setLayoutParams(layoutParams);
                }

            }else{
                view.setVisibility(View.GONE);
            }
        }
        initView();
        initData();
        addListener();
    }
    public void initTitle(){
//        title_text=getView(R.id.group_title_name);
//        getView(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
    }
    public void setAction_bar_layout(int v){
        View view=getView(R.id.action_bar_layout);
        if(view!=null){
            getView(R.id.action_bar_layout).setVisibility(v);
        }
    }
    public void initTitle(String message){
        initTitle();
        title_text.setText(message);
    }
    @SuppressWarnings("unchecked")
    protected <T extends View>T getView(int resourcesId){
        try {
            return (T) findViewById(resourcesId);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return null;
    }
    public abstract int getLayoutResId();
    public abstract void initView();
    public abstract void initData();
    public abstract void addListener();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setTitleBackGround(Drawable drawable){
        RelativeLayout title=getView(R.id.title_layout);
        if(title!=null){
            title.setBackground(drawable);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setActionBackGround(Drawable drawable){
        View view=getView(R.id.action_bar_layout);
        if(view!=null){
            view.setBackground(drawable);
        }
    }
    public RelativeLayout getTitleLayout(){
        return getView(R.id.title_layout);
    }
    public ImageButton getSearchButton(){
        if(getView(R.id.search_button)!=null){
            getView(R.id.search_button).setVisibility(View.VISIBLE);
        }
        return getView(R.id.search_button);
    }
    public void showToast(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WebViewBaseActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void dialogShow(){
        if(getView(R.id.dialog_layout)!=null){
            getView(R.id.dialog_layout).setVisibility(View.VISIBLE);
        }
    }
    public void dialogDiss(){
        if(getView(R.id.dialog_layout)!=null){
            getView(R.id.dialog_layout).setVisibility(View.GONE);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityUtil.removeActivity(this);
    }
}
