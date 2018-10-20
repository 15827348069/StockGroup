package com.zbmf.groupro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.UserCouponsAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.CouponsBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.ShowActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 未使用优惠券
 */

public class CanUseConponsFragment extends BaseFragment {
    private UserCouponsAdapter adapter;
    private ListView can_use_conpous;
    public static final String KEY="CouponsBean";
    private CouponsBean couponsBean;
    private List<CouponsBean>infolist;
    private LinearLayout ll_none;
    private TextView no_message_text;
    public static CanUseConponsFragment newInstance(CouponsBean couponsBean) {
        CanUseConponsFragment fragment = new CanUseConponsFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY, couponsBean);
        fragment.setArguments(args);
        return fragment;
    }

    protected View setContentView(LayoutInflater inflater) {
        if (getArguments() != null) {
            couponsBean = (CouponsBean) getArguments().getSerializable(KEY);
        }
        return inflater.inflate(R.layout.canuse_fragment_layout,null);
    }

    @Override
    protected void initView() {
        can_use_conpous= getView(R.id.can_use_coupons);
        ll_none=getView(R.id.ll_none);
        no_message_text=getView(R.id.no_message_text);
        can_use_conpous.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String use_id=infolist.get(position).getId();
                switch (use_id){
                    case "0":
                        showToast("该优惠券可以在所有圈子使用");
                        break;
                    default:
                        into_fansActivity(infolist.get(position).getId());
                        break;
                }
            }
        });
    }
    @Override
    protected void initData() {
        infolist=new ArrayList<>();
        if(couponsBean.getInfolist()!=null){
            infolist.addAll(couponsBean.getInfolist());
        }
        adapter=new UserCouponsAdapter(getActivity(),infolist);
        can_use_conpous.setAdapter(adapter);
        setLayoutMessage(infolist.size());
    }
    public void setLayoutMessage(int size){
        if(size==0){
            if(can_use_conpous.getVisibility()==View.VISIBLE){
                can_use_conpous.setVisibility(View.GONE);
            }
            if(ll_none.getVisibility()==View.GONE){
                ll_none.setVisibility(View.VISIBLE);
            }
            no_message_text.setText("还没有未使用的优惠券哦");
        }else{
            if(can_use_conpous.getVisibility()==View.GONE){
                can_use_conpous.setVisibility(View.VISIBLE);
            }
            if(ll_none.getVisibility()==View.VISIBLE){
                ll_none.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
    public void into_fansActivity(String group_id){
        WebBase.fansInfo(group_id, new JSONHandler(true,getActivity(),"正在加载数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject group=obj.optJSONObject("group");
                Group groupbean=new Group();
                groupbean.setId(group.optString("id"));
                groupbean.setName(group.optString("name"));
                groupbean.setNick_name(group.optString("nickname"));
                groupbean.setAvatar(group.optString("avatar"));
                groupbean.setIs_close(group.optInt("is_close"));
                groupbean.setIs_private(group.optInt("is_private"));
                groupbean.setRoles(group.optInt("roles"));
                groupbean.setFans_level(group.optInt("fans_level"));
                groupbean.setDay_mapy(group.optLong("day_mpay"));
                groupbean.setMonth_mapy(group.optLong("month_mpay"));
                groupbean.setEnable_day(group.optInt("enable_day"));
                groupbean.setEnable_point(group.optInt("enable_point"));
                groupbean.setMax_point(group.optInt("max_point"));
                groupbean.setDescription(group.optString("fans_profile"));
                groupbean.setFans_activity(group.optString("fans_activity"));
                groupbean.setFans_countent(group.optString("fans_content"));
                groupbean.setPoint_desc(group.optString("point_desc"));
                groupbean.setMax_mpay(group.optLong("max_mpay"));
                ShowActivity.showAddFansActivity(getActivity(),groupbean);
            }
            @Override
            public void onFailure(String err_msg) {
                showToast(err_msg);
            }
        });
    }
}
