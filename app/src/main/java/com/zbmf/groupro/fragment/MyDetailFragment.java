package com.zbmf.groupro.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.CareTeacherActivity;
import com.zbmf.groupro.activity.MfbActivity;
import com.zbmf.groupro.activity.MyBlogListActivity;
import com.zbmf.groupro.activity.MyMessageActivity;
import com.zbmf.groupro.activity.MyQuestionActivity;
import com.zbmf.groupro.activity.MySubscripActivity;
import com.zbmf.groupro.activity.PointActivity;
import com.zbmf.groupro.activity.UserCollectsActivity;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.CouPonsRules;
import com.zbmf.groupro.beans.CouponsBean;
import com.zbmf.groupro.utils.ActivityUtil;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.ImageLoaderOptions;
import com.zbmf.groupro.utils.MessageType;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshScrollView;
import com.zbmf.groupro.view.RoundedCornerImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/13.
 */

public class MyDetailFragment extends GroupBaseFragment implements View.OnClickListener{
    private TextView my_detail_mfb,my_detail_yhq,my_detail_jf,my_detail_name;
    private Dialog kf_dialog;
    private RoundedCornerImageView my_detail_avatar;
    private CouponsBean couponsBean;
    private CheckBox qiandao;
    private TextView message_count_text;
    private ImageView my_dingyue_point,my_blog_point,my_care_point,my_ask_point;
    private PullToRefreshScrollView home_scrollview;
    public static MyDetailFragment newInstance() {
        MyDetailFragment fragment = new MyDetailFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.my_detail_fragment,null);
    }
    public void setData(boolean updata){
        setinitData(updata);
    }
    @Override
    protected void initView() {
        my_detail_name= getView(R.id.my_detail_names);
        my_detail_mfb= getView(R.id.my_detail_mfb);
        my_detail_yhq= getView(R.id.my_detail_yhq);
        my_detail_jf= getView(R.id.my_detail_jf);
        my_detail_avatar= getView(R.id.my_detail_avatar_id);

        my_dingyue_point=getView(R.id.my_dingyue_point);
        my_blog_point=getView(R.id.my_blog_point);
        my_care_point=getView(R.id.my_care_point);
        my_ask_point=getView(R.id.my_ask_point);

        message_count_text=getView(R.id.message_count_text);

        home_scrollview=getView(R.id.home_scrollview);
        home_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        home_scrollview.getLoadingLayoutProxy().setPullLabel("下拉刷新数据");
        home_scrollview.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        home_scrollview.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");

        home_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                ActivityUtil.getInstance().UpdaUserMessage();
                initData();
            }
        });
        getView(R.id.my_message_layout).setOnClickListener(this);
        getView(R.id.my_detail_dingyue_button).setOnClickListener(this);
        getView(R.id.my_detail_blog_button).setOnClickListener(this);
        getView(R.id.my_detail_shouc_button).setOnClickListener(this);
        getView(R.id.my_detail_care_button).setOnClickListener(this);
        getView(R.id.my_detail_question_button).setOnClickListener(this);
        getView(R.id.my_detail_setting_button).setOnClickListener(this);
        getView(R.id.my_detail_kf_button).setOnClickListener(this);
        getView(R.id.my_detail_mfb_button).setOnClickListener(this);
        getView(R.id.my_detail_yhq_button).setOnClickListener(this);
        getView(R.id.my_detail_jf_button).setOnClickListener(this);
        getView(R.id.my_detail_message).setOnClickListener(this);
        qiandao=getView(R.id.qiandao_button);
    }
    public void setPointVisi(String type){
        switch (type){
            case Constants.PUSH_BOX_TYPE:
                //宝盒
                if(my_dingyue_point!=null&&my_dingyue_point.getVisibility()==View.GONE){
                    my_dingyue_point.setVisibility(View.VISIBLE);
                }
                break;
            case MessageType.ANSWER:
                //问答
                if(my_ask_point!=null&&my_ask_point.getVisibility()==View.GONE){
                    my_ask_point.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    public void setPointGone(String type){
        switch (type){
            case Constants.PUSH_BOX_TYPE:
                //宝盒
                if(my_dingyue_point!=null&&my_dingyue_point.getVisibility()==View.VISIBLE){
                    my_dingyue_point.setVisibility(View.GONE);
                }
                break;
            case MessageType.ANSWER:
                //问答
                if(my_ask_point!=null&&my_ask_point.getVisibility()==View.VISIBLE){
                    my_ask_point.setVisibility(View.GONE);
                }
                break;
        }
    }
    public void runshData(){
        if(!home_scrollview.isRefreshing()){
            home_scrollview.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
            home_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            home_scrollview.setRefreshing();
            initData();
        }
    }
    @Override
    protected void initData() {
        updataUserMessage();
        userSigns();
        getUserCoupons(false);
    }
    public boolean getPoint(){
        if(my_ask_point!=null&&my_ask_point.getVisibility()==View.VISIBLE){
            return true;
        }
        if(my_dingyue_point!=null&&my_dingyue_point.getVisibility()==View.VISIBLE){
            return true;
        }
        return false;
    }
    public void updataUserMessage(){
        if(my_detail_name!=null){
            my_detail_name.setText(SettingDefaultsManager.getInstance().NickName());
        }
        if(my_detail_mfb!=null){
            my_detail_mfb.setText(SettingDefaultsManager.getInstance().getPays());
        }
       if(my_detail_jf!=null){
           my_detail_jf.setText(String.format("%d",SettingDefaultsManager.getInstance().getPoint()));
       }
        if(my_detail_avatar!=null){
            ImageLoader.getInstance().displayImage(SettingDefaultsManager.getInstance().UserAvatar(),my_detail_avatar, ImageLoaderOptions.AvatarOptions());
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_detail_message:
                //消息
                ShowActivity.showActivity(getActivity(), MyMessageActivity.class);
                break;
            case R.id.qiandao_button:
                //签到
                signIn();
                break;
            case R.id.my_detail_mfb_button:
                //魔方宝
                ShowActivity.showActivity(getActivity(), MfbActivity.class);
                break;
            case R.id.my_detail_yhq_button:
                //优惠券
                if(couponsBean!=null){
                    ShowActivity.showConponsActivity(getActivity(),couponsBean);
                }else{
                    getUserCoupons(true);
                }
                break;
            case R.id.my_detail_jf_button:
                //积分
                ShowActivity.showActivity(getActivity(), PointActivity.class);
                break;
            case R.id.my_detail_dingyue_button:
                //订阅
                my_dingyue_point.setVisibility(View.GONE);
                ShowActivity.showActivity(getActivity(), MySubscripActivity.class);
                break;
            case R.id.my_detail_blog_button:
                //文章
                ShowActivity.showActivity(getActivity(), MyBlogListActivity.class);
                break;
            case R.id.my_detail_shouc_button:
                //收藏
                ShowActivity.showActivity(getActivity(),UserCollectsActivity.class);
                break;
            case R.id.my_detail_care_button:
                //关注
                Bundle bundle = new Bundle();
                bundle.putSerializable("type",0);
                ShowActivity.showActivity(getActivity(),bundle, CareTeacherActivity.class.getName());
                break;
            case R.id.my_detail_question_button:
                //提问
                my_ask_point.setVisibility(View.GONE);
                ShowActivity.showActivity(getActivity(), MyQuestionActivity.class);
                break;
            case R.id.my_detail_setting_button:
                //设置
                ShowActivity.showSettingActivity(getActivity());
                break;
            case R.id.my_detail_kf_button:
                //客服
                if(kf_dialog==null){
                   kf_dialog= dialog1();
                }
                kf_dialog.show();
                break;
            case R.id.my_message_layout:
                ShowActivity.showMyDetail(getActivity());
                break;
        }
    }
    private Dialog dialog1(){
        final Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.kefu_layout, null);
        Button qq_button=(Button) layout.findViewById(R.id.qq_kefu_button);
        Button phone_button=(Button) layout.findViewById(R.id.phone_id_button);
        phone_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:4000607878"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss(); //
            }
        });
        qq_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url="mqqwpa://im/chat?chat_type=crm&uin=2852273339&version=1&src_type=web&web_src=http:://wpa.b.qq.com";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                dialog.dismiss(); //关闭dialog
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

    public void getUserCoupons(final boolean is_to){
        WebBase.getUserCoupons(null, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                couponsBean=new CouponsBean();
                List<CouponsBean>couponslist=new ArrayList<>();
                if(!obj.isNull("coupons")){
                    JSONArray coupons=obj.optJSONArray("coupons");
                    int size=coupons.length();
                    my_detail_yhq.setText(size+"");
                    for(int i=0;i<size;i++){
                        JSONObject coupon=coupons.optJSONObject(i);
                        if(coupon!=null){
                            CouponsBean cb=new CouponsBean();
                            cb.setCoupon_id(coupon.optString("coupon_id"));
                            cb.setSubject(coupon.optString("subject"));
                            cb.setSumary(coupon.optString("summary"));
                            cb.setKind(coupon.optInt("kind"));
                            cb.setId(coupon.optString("id"));
                            cb.setCouponsName(coupon.optString("name"));
                            cb.setType("coupons");
                            cb.setIs_take(false);
                            cb.setCan_use(true);
                            cb.setStart_at(coupon.optString("start_at"));
                            cb.setEnd_at(coupon.optString("end_at"));
                            cb.setMinimum(coupon.optDouble("minimum"));
                            cb.setMaximum(coupon.optDouble("maximum"));
                            cb.setCateogry(coupon.optInt("category"));
                            JSONArray rules=coupon.optJSONArray("rules");
                            List<CouPonsRules> riles=new ArrayList<CouPonsRules>();
                            int rules_size=rules.length();
                            for(int k=0;k<rules_size;k++){
                                CouPonsRules cr=new CouPonsRules();
                                JSONObject rule=rules.optJSONObject(k);
                                if(!rule.isNull("maximum")){
                                    cr.setAmount(rule.optDouble("amount"));
                                    cr.setCateogry(rule.optDouble("category"));
                                    cr.setIs_ratio(rule.optInt("is_ratio"));
                                    cr.setIs_incr(rule.optInt("is_incr"));
                                    cr.setMaximum(rule.optDouble("maximum"));
                                    riles.add(cr);
                                }
                            }
                            cb.setRiles(riles);
                            couponslist.add(cb);
                        }
                    }
                    couponsBean.setInfolist(couponslist);
                    if(is_to){
                        ShowActivity.showConponsActivity(getActivity(),couponsBean);
                    }
                }
                home_scrollview.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                home_scrollview.onRefreshComplete();if(checkVa(err_msg)){
                    SettingDefaultsManager.getInstance().setAuthtoken("");
                    ((GroupActivity)getActivity()).checked();
                }else{
                    Log.e("TAG", "onFailure: " );
                    Toast.makeText(getActivity(),err_msg,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean checkVa(String err_msg){
        if(err_msg.equals("用户登录失败或已过期") || err_msg.equals(Constants.NEED_LOGIN)){
            return true;
        }
        return false;
    }
    /**
     * 签到
     */
    public void signIn(){
        WebBase.signIn(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                showToast("签到成功,积分+"+obj.optString("gains"));
                SettingDefaultsManager.getInstance().setPoint(obj.optLong("point"));
                my_detail_jf.setText(String.format("%d",SettingDefaultsManager.getInstance().getPoint()));
                qiandao.setChecked(false);
                qiandao.setEnabled(false);

            }
            @Override
            public void onFailure(String err_msg) {
                showToast("签到失败");
                qiandao.setChecked(true);
                qiandao.setEnabled(true);
            }
        });
    }

    /**
     * 签到状态
     */
    public void userSigns(){
        WebBase.userSigns(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
               if(obj.optInt("today_sign")==0){
                   qiandao.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if(!qiandao.isChecked()){
                               signIn();
                           }else{
                               showToast("今日已签到");
                           }
                       }
                   });
                   qiandao.setChecked(true);
                   qiandao.setEnabled(true);
               }else{
                   qiandao.setChecked(false);
                   qiandao.setEnabled(false);
               }
            }

            @Override
            public void onFailure(String err_msg) {
                if(checkVa(err_msg)){
                    SettingDefaultsManager.getInstance().setAuthtoken("");
                    ((GroupActivity)getActivity()).checked();
                }else{
                    Log.e("TAG", "onFailure: " );
                    Toast.makeText(getActivity(),err_msg,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
