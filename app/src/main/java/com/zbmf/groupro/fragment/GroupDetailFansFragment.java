package com.zbmf.groupro.fragment;

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

import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.GroupDetailActivity;
import com.zbmf.groupro.adapter.BoxItemAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.DisplayUtil;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.AddMoreLayout;
import com.zbmf.groupro.view.ListViewForScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/25.
 */

public class GroupDetailFansFragment extends BaseFragment implements AdapterView.OnItemClickListener, AddMoreLayout.OnSendClickListener, View.OnClickListener {
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

    String textStr = "您还不是该老师的铁粉\n加入才能查看哦";

    private TextView mTv_title;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(ShowActivity.isLogin(getActivity())){
            BoxBean boxBean = (BoxBean) parent.getItemAtPosition(position);
            boxBean.setId(group.getId());
            String box_level = boxBean.getBox_level();

            switch (box_level) {
                case "20":
                    textStr = "您还不是该老师的年粉\n加入才能查看哦";
                    break;
                case "10":
                    textStr = "您还不是该老师的铁粉\n加入才能查看哦";
                    break;
                default:

                    break;
            }

            if(Integer.parseInt(box_level) <= group.getFans_level()){
                ShowActivity.showBoxDetailActivity(getActivity(),boxBean);
            }else{
                if(mDialog == null)
                    mDialog = showDialog();
                mTv_title.setText(textStr);
                mDialog.show();
            }
        }
    }
    private Dialog mDialog = null;
    @Override
    public void OnSendClickListener(View view) {
        pages+=1;
        getBox_Message();
    }
    private Dialog showDialog(){
        final Dialog dialog = new Dialog(getActivity(), R.style.myDialogTheme);
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_tip, null);
        layout.findViewById(R.id.tv_yes).setOnClickListener(this);
        layout.findViewById(R.id.tv_no).setOnClickListener(this);

        mTv_title = (TextView) layout.findViewById(R.id.tv_title);
        mTv_title.setText(textStr);
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (DisplayUtil.getScreenWidthPixels(getActivity()) * 0.75);
        win.setAttributes(lp);
        dialog.setCancelable(false);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_yes:
                ShowActivity.showAddFansActivity(getActivity(),group);
                mDialog.dismiss();
                break;
            case R.id.tv_no:
                mDialog.dismiss();
                break;
        }
    }
}
