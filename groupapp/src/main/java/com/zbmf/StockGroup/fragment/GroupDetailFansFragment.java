package com.zbmf.StockGroup.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.activity.GroupDetailActivity;
import com.zbmf.StockGroup.adapter.BoxItemAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.dialog.TextDialog;
import com.zbmf.StockGroup.interfaces.DialogYesClick;
import com.zbmf.StockGroup.utils.DisplayUtil;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.LogUtil;
import com.zbmf.StockGroup.utils.ShowActivity;
import com.zbmf.StockGroup.view.AddMoreLayout;
import com.zbmf.StockGroup.view.ListViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/25.
 */

public class GroupDetailFansFragment extends BaseFragment implements AdapterView.OnItemClickListener, AddMoreLayout.OnSendClickListener{
    private ListViewForScrollView lv;
    private BoxItemAdapter adapter;
    private List<BoxBean> infolist = new ArrayList<>();
    private Group group;
    private int page,pages;

    public static GroupDetailFansFragment newInstance(Group group) {
        GroupDetailFansFragment fragment = new GroupDetailFansFragment();
        Bundle args = new Bundle();
        args.putSerializable("GROUP", group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            group = (Group) getArguments().getSerializable("GROUP");
        }
    }
    public void setGroup(Group group){
        this.group=group;
    }
    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.group_detail_fragment,null);
    }

    @Override
    protected void initView() {
        lv = getView(R.id.group_detail_list);
        adapter=new BoxItemAdapter(getActivity(),infolist);
        lv.addFootView(this);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        page=1;
        pages =0;
        infolist.clear();
        getBox_Message();
    }

    public void getBox_Message(){
        lv.onLoad();
        WebBase.getGroupBoxs(group.getId(),"0", page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject object) {
                BoxBean newsBox = JSONParse.getGroupBoxs(object);
                if(newsBox!=null){
                    page=newsBox.getPage();
                    pages=newsBox.getPages();
                    if(page==pages){
                        lv.addAllData();
                    }else{
                        lv.onStop();
                    }
                    if(newsBox.getList()!=null && newsBox.getList().size()>0){
                        infolist.addAll(newsBox.getList());
                        adapter.notifyDataSetChanged();
                        GroupDetailActivity activity= (GroupDetailActivity) getActivity();
                        activity.viewPager.setObjectForPosition(getFragmentView(),1);
                    }
                }
            }

            @Override
            public void onFailure(String err_msg) {
                lv.onStop();
            }
        });

    }
    public void addList(final List<BoxBean>info){

    }

    String textStr = "抱歉，您暂未订阅铁粉";
    String textTilt="升级到包月铁粉即可查看私密宝盒";
    private TextView mTv_title;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(ShowActivity.isLogin(getActivity())){
            BoxBean boxBean = (BoxBean) parent.getItemAtPosition(position);
            boxBean.setId(group.getId());
            String box_level = boxBean.getBox_level();

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
            LogUtil.e("box_level"+box_level);
            LogUtil.e(" group.getFans_level()"+ group.getRoles());
            if(Integer.parseInt(box_level) <= group.getRoles()){
                ShowActivity.showBoxDetailActivity(getActivity(),boxBean);
            }else{
                if(mDialog == null)
                    mDialog = showDialog();
                mDialog.setTitle(textTilt);
                mDialog.setMessage(textStr);
                mDialog.show();
            }
        }
    }
    private TextDialog mDialog = null;
    @Override
    public void OnSendClickListener(View view) {
        pages+=1;
        getBox_Message();
    }
    private TextDialog showDialog(){
        return  TextDialog.createDialog(getActivity())
                .setLeftButton("暂不")
                .setRightButton("升级")
                .setRightClick(new DialogYesClick() {
                    @Override
                    public void onYseClick() {
                        WebBase.fansInfo(group.getId(), new JSONHandler() {
                            @Override
                            public void onSuccess(JSONObject obj) {
                                if (!obj.isNull("group")) {
                                    Group group = JSONParse.getGroup(obj.optJSONObject("group"));
                                    ShowActivity.showFansActivity(getActivity(), group);
                                    mDialog.dismiss();
                                }
                            }
                            @Override
                            public void onFailure(String err_msg) {
                                showToast(err_msg);
                                mDialog.dismiss();
                            }
                        });
                    }
                });
    }

}
