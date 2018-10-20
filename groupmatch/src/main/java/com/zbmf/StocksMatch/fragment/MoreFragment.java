package com.zbmf.StocksMatch.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CircleImageView;
import com.zbmf.StocksMatch.widget.LoadingDialog;

import org.json.JSONException;


public class MoreFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DisplayImageOptions options;
    private CircleImageView civ;
    private TextView tv_num,tv_mum,tv_name;
    private Get2Api server;
    ImageLoader imageLoader;
    private View rootView;
    public MoreFragment() {
    }

    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_avatar)
                .showImageForEmptyUri(R.drawable.default_avatar)
                .showImageOnFail(R.drawable.default_avatar)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888) // 设置图片的解码类型
                .displayer(new RoundedBitmapDisplayer(20)).build();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null){
            rootView=inflater.inflate(R.layout.activity_more, null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }




    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ll_activity).setOnClickListener(this);
        view.findViewById(R.id.ll_store).setOnClickListener(this);
        view.findViewById(R.id.ll_about).setOnClickListener(this);
        view. findViewById(R.id.ll_clear).setOnClickListener(this);
        view.findViewById(R.id.ll_change).setOnClickListener(this);
        view. findViewById(R.id.ll_focus).setOnClickListener(this);

        tv_mum = (TextView)view.findViewById(R.id.tv_mum);
        tv_num = (TextView)view.findViewById(R.id.tv_num);
        tv_name = (TextView)view.findViewById(R.id.tv_name);
        civ = (CircleImageView)view.findViewById(R.id.civ);

        if (!TextUtils.isEmpty(UiCommon.INSTANCE.getiUser().getAvatar())){
            imageLoader.displayImage(UiCommon.INSTANCE.getiUser().getAvatar(), civ, options);
        }else {
            civ.setImageResource(R.drawable.default_avatar);
        }
        tv_name.setText(UiCommon.INSTANCE.getiUser().getNickname());

        civ.setOnClickListener(this);

    }


    @Override
    public void onResume() {
        super.onResume();
//        new downloadPic().execute();
        new UserMoreTask(getActivity(),false,true).execute(UiCommon.INSTANCE.getiUser().getAuth_token());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_activity:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACTIVES,null);
                break;
            case R.id.ll_store:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_STORE, null);
                break;

            case R.id.ll_about:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ABOUT,null);
                break;
            case R.id.ll_clear:
                new ClearTask(getActivity(),R.string.clearing,R.string.clear_tip1,true).execute();
                break;
            case R.id.ll_change:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT,null);
                break;
            case R.id.ll_focus:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_FOCUS,null);
                break;
            case R.id.civ:
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_PERINFO,null);
                break;

        }
    }

    private class UserMoreTask extends LoadingDialog<String,User> {
        public UserMoreTask(Context activity, boolean Enddismiss, boolean isNoTip) {
            super(activity, Enddismiss, isNoTip);
        }

        @Override
        public User doInBackground(String... params) {
            User user = null;
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                user =  server.UserMore();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return user;
        }

        @Override
        public void doStuffWithResult(User user) {
            if (user != null && user.code != -1) {
                if (user.getStatus() == 1) {
                    tv_mum.setText(user.getMpay());
                    tv_num.setText(Integer.parseInt(user.getCount_fens())-1+"");
                    tv_name.setText(user.getNickname());
                    imageLoader.displayImage(user.getAvatar(), civ, options);
                    User u = UiCommon.INSTANCE.getiUser();
                    u.setNickname(user.getNickname());
                    new DatabaseImpl(getActivity()).addUser(u);
                    UiCommon.INSTANCE.setiUser(u);
                } else {
                    UiCommon.INSTANCE.showTip(user.msg);
                }
            } else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }

    private class ClearTask extends LoadingDialog<Void,Boolean>{

        public ClearTask(Context activity, int loadingMsg, int failMsg, boolean Enddismiss) {
            super(activity, loadingMsg, failMsg, Enddismiss);
        }

        @Override
        public Boolean doInBackground(Void... params) {
            SystemClock.sleep(2000);
            return UiCommon.INSTANCE.deletAllImgFiles();
        }

        @Override
        public void doStuffWithResult(Boolean b) {
            if(b)
                UiCommon.INSTANCE.showTip(mActivity.getString(R.string.clear_tip));
            else
                UiCommon.INSTANCE.showTip(mActivity.getString(R.string.clear_tip1));
            
        }
    }

    /**
     * 下载头像
     *
     * @author atan
     */
    private class downloadPic extends AsyncTask<Void, Void, Bitmap> {

        @Override
        public Bitmap doInBackground(Void... arg0) {
            return UiCommon.INSTANCE.downloadPic(UiCommon.INSTANCE.getiUser()
                    .getAvatar());
        }

        @Override
        protected void onPostExecute(Bitmap ret) {
            super.onPostExecute(ret);
            if (ret != null) {
                civ.setImageBitmap(ret);
            }
        }

    }
}
