package com.zbmf.groupro.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.UserCouponsAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.CouponsBean;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 已使用、过期的优惠券
 */

public class LoseTimeConponsFragment extends BaseFragment {
    private UserCouponsAdapter adapter;
    private PullToRefreshListView louse_use_coupons;
    private List<CouponsBean> couponslist;
    private int page,pages;
    public static LoseTimeConponsFragment newInstance() {
        LoseTimeConponsFragment fragment = new LoseTimeConponsFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.losttime_fragment_layout,null);
    }

    @Override
    protected void initView() {
        louse_use_coupons=getView(R.id.louse_use_coupons);
        louse_use_coupons.getLoadingLayoutProxy().setPullLabel("加载更多数据");
        louse_use_coupons.getLoadingLayoutProxy().setRefreshingLabel("正在加载新数据...");
        louse_use_coupons.getLoadingLayoutProxy().setReleaseLabel("松开加载数据");
        louse_use_coupons.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //执行刷新函数
                page+=1;
                getUserCoupons(false);
            }
        });
    }

    @Override
    protected void initData() {
        page=1;
        couponslist=new ArrayList<>();
        adapter=new UserCouponsAdapter(getActivity(),couponslist);
        louse_use_coupons.setAdapter(adapter);
        getUserCoupons(true);
    }
    public void getUserCoupons(boolean show_dialog) {
        WebBase.getHistCoupons(page, new JSONHandler(show_dialog,getActivity(),"正在加载...") {
            @Override
            public void onSuccess(JSONObject obj) {
                page=obj.optJSONObject("result").optInt("page");
                pages=obj.optJSONObject("result").optInt("pages");
                JSONArray coupons = obj.optJSONObject("result").optJSONArray("coupons");
                int size = coupons.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject coupon = coupons.optJSONObject(i);
                        if (coupon != null) {
                            CouponsBean cb = new CouponsBean();
                            cb.setCoupon_id(coupon.optString("coupon_id"));
                            cb.setSubject(coupon.optString("subject"));
                            cb.setSumary(coupon.optString("summary"));
                            cb.setKind(coupon.optInt("kind"));
                            cb.setStart_at(coupon.optString("start_at"));
                            cb.setEnd_at(coupon.optString("end_at"));
                            cb.setIs_take(false);
                            cb.setCan_use(false);
                            couponslist.add(cb);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    louse_use_coupons.onRefreshComplete();
                    if(page==pages){
                        Toast.makeText(getActivity(),"已加载全部数据",Toast.LENGTH_SHORT).show();
                        louse_use_coupons.setMode(PullToRefreshBase.Mode.DISABLED);
                    }
            }
            @Override
            public void onFailure(String err_msg) {

            }
        });
    }
}
