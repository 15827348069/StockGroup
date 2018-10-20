package com.zbmf.StockGroup.fragment.care;


import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;

import com.zbmf.StockGroup.adapter.CareBoxItemAdapter;

import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.beans.Group;

import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.fragment.BaseFragment;

import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.interfaces.LoadFinish;
import com.zbmf.StockGroup.interfaces.OnFansClick;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.CustomViewpager;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/8/16.
 */

public class CareFansFragment extends BaseFragment implements View.OnClickListener,OnFansClick{
    private List<BoxBean> infolist;
    private CareBoxItemAdapter adapter;
    private String groupId;
    String textStr = "抱歉，您暂未订阅铁粉";
    String textTilt="升级到包月铁粉即可查看私密宝盒";
    private TextDialog mDialog = null;
    private ListViewForScrollView lv_tief;
    private CustomViewpager careFragments;
    private boolean isFirst;

    public void setCustomViewPage(CustomViewpager careFragments) {
        this.careFragments = careFragments;
    }

    public static CareFansFragment newInstance() {
        CareFansFragment fragment = new CareFansFragment();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_care_fans_layout, null);
    }

    @Override
    protected void initView() {
        infolist = new ArrayList<>();
        adapter = new CareBoxItemAdapter(getActivity(), infolist);
        adapter.setOnFansClick(this);
        lv_tief = getView(R.id.lv_focus);
        lv_tief.setAdapter(adapter);
        lv_tief.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoxBean boxBean = (BoxBean) parent.getItemAtPosition(position);
                String box_level = boxBean.getBox_level();
                groupId = boxBean.getId();
                switch (box_level) {
                    case "20":
                        textTilt="抱歉，您暂未订阅年粉";
                        textStr = "升级到包年铁粉即可查看私密宝盒";
                        break;
                    case "10":
                        textTilt="抱歉，您暂未订阅铁粉";
                        textStr = "升级到包月铁粉即可查看私密宝盒";
                        break;
                    default:

                        break;
                }

                if (Integer.parseInt(box_level) <= Integer.parseInt(boxBean.getFans_level())) {
                    ShowActivity.showBoxDetailActivity(getActivity(), boxBean);
                } else {
                    if (mDialog == null){
                        mDialog = showDialog();
                    }
                    mDialog.setTitle(textTilt);
                    mDialog.setMessage(textStr);
                    mDialog.show();
                }
            }
        });
    }

    private TextDialog showDialog() {
        return  TextDialog.createDialog(getActivity())
                .setLeftButton("暂不")
                .setRightButton("升级")
                .setRightClick(new DialogYesClick() {
                    @Override
                    public void onYseClick() {
                        fansInfo(groupId);
                        mDialog.dismiss();
                    }
                });
    }

    @Override
    protected void initData() {
        isFirst = true;
        rushList();
    }

    public void rushList() {
        if (isFirst) {
            isFirst = false;
            dialogShow();
        }
        WebBase.box("1", new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                BoxBean newsBox = JSONParse.box(obj);
                if (newsBox != null && newsBox.getList() != null) {
                    infolist.clear();
                    infolist.addAll(newsBox.getList());
                    adapter.notifyDataSetChanged();
                    if (loadFinish != null) {
                        loadFinish.onFinish();
                    }
                    setViewPageHeight();
                    dialogDiss();
                }
            }

            @Override
            public void onFailure(String err_msg) {
                if (loadFinish != null) {
                    loadFinish.onFinish();
                }
                dialogDiss();
                setViewPageHeight();
            }
        });
    }

    public void setViewPageHeight() {
        careFragments.setObjectForPosition(getFragmentView(), 0);
        careFragments.resetHeight(0);
    }

    private void fansInfo(String group_id) {
        WebBase.fansInfo(group_id, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                Group group = JSONParse.getGroup(obj.optJSONObject("group"));
                ShowActivity.showFansActivity(getActivity(), group);
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yes:

                break;
            case R.id.tv_no:
                mDialog.dismiss();
                break;
        }
    }

    private LoadFinish loadFinish;

    public void setLoadFinish(LoadFinish loadFinish) {
        this.loadFinish = loadFinish;
    }

    @Override
    public void onBox(BoxBean boxBean) {
        ShowActivity.showBoxDetailActivity(getActivity(), boxBean);
    }

    @Override
    public void onFans(String groupId) {
        fansInfo(groupId);
    }

    @Override
    public void onGroup(String user) {
        ShowActivity.showGroupDetailActivity(getActivity(),user);
    }
}
