package com.zbmf.StocksMatch.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zbmf.StocksMatch.R;
import com.zbmf.worklibrary.baseview.BaseView;
import com.zbmf.worklibrary.dialog.CustomProgressDialog;
import com.zbmf.worklibrary.presenter.BasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by xuhao
 * on 2017/11/21.
 */
public abstract class BaseActivity<T extends BasePresenter> extends FragmentActivity implements BaseView, View.OnClickListener {
    protected T presenter;
    private CustomProgressDialog progressDialog;
    private Unbinder mUnBinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen(true);
        setContentView(getLayout());
        mUnBinder = ButterKnife.bind(this);
        initData(getIntent().getExtras());
        initTitle(initTitle());
        presenter = initPresent();
        if(presenter!=null){
            presenter.onStart(this);
            //只要加载子activity并且presenter不为null就初始化该activity的数据
            presenter.getDatas();
            // TODO: 2018/4/4 初始加载数据的时候显示加载对话框
        }
        if(findViewById(R.id.imb_title_return)!=null){
            findViewById(R.id.imb_title_return).setOnClickListener(this);
        }
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setFullscreen(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |=  bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    protected abstract int getLayout();
    protected abstract String initTitle();
    protected abstract void initData(Bundle bundle);
    protected abstract T initPresent();
    protected BasePresenter getPresenter(){
        return  presenter;
    }


    @Override
    public void dissLoading() {
        DissLoading();
    }

    @Override
    public void showToastMsg(String msg) {
        showToast(msg);
    }

    public void ShowLoading() {
        if(progressDialog!=null&&!progressDialog.isShowing()){
            progressDialog.show();
        }
    }
    private void initTitle(String title){
        TextView textView=findViewById(R.id.tv_title);
        if(textView!=null&&title!=null){
            textView.setText(title);
        }
    }

    public void DissLoading() {
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
    public void initProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(this);
        }
    }
    public void setProgressDialogMsg(String dialogMessage){
        if(progressDialog!=null&&!progressDialog.isShowing()&&!TextUtils.isEmpty(dialogMessage)){
            progressDialog.setMessage(dialogMessage);
        }
    }
    public void setCancelDialog(boolean b){
        if(progressDialog!=null&&!progressDialog.isShowing()){
            progressDialog.setCanceledOnTouchOutside(b);
            progressDialog.setCancelable(b);
        }
    }
    public void showToast(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 提示信息
     *
     * @param aFormatMsg
     * @param aMsgArgs
     */
    public void showTip(String aFormatMsg, Object... aMsgArgs) {
        String outString = String.format(aFormatMsg, aMsgArgs);
        int duration = (outString.length() > 10) ? Toast.LENGTH_LONG
                : Toast.LENGTH_SHORT;
        Toast.makeText(BaseActivity.this,
                outString, duration).show();
    }

    @Override
    public void onRefreshComplete() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);//百度统计
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);//百度统计
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder=null;
        }
        if(presenter!=null){
            presenter.onDestroy();
            presenter=null;
        }
        if(progressDialog!=null){
            progressDialog=null;
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}