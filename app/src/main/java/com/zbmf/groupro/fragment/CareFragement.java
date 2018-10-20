package com.zbmf.groupro.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.groupro.GroupActivity;
import com.zbmf.groupro.R;
import com.zbmf.groupro.activity.CareTeacherActivity;
import com.zbmf.groupro.activity.Chat1Activity;
import com.zbmf.groupro.activity.RecommendActivity;
import com.zbmf.groupro.activity.TieFActivity;
import com.zbmf.groupro.adapter.BoxItemAdapter;
import com.zbmf.groupro.adapter.DynamicAdapter;
import com.zbmf.groupro.adapter.FoucusAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BlogBean;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.beans.NewsFeed;
import com.zbmf.groupro.db.DBManager;
import com.zbmf.groupro.db.Database;
import com.zbmf.groupro.utils.Constants;
import com.zbmf.groupro.utils.DisplayUtil;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.SettingDefaultsManager;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.ListViewForScrollView;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshScrollView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao on 2017/2/13.
 */

public class CareFragement extends GroupBaseFragment implements View.OnClickListener {

    private FoucusAdapter mFoucusAdapter;
    private BoxItemAdapter adapter;
    private ListViewForScrollView mLvFocus, lv_tief, lv_dynamic;
    private List<BoxBean> infolist;
    private List<Group> mGroups = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<NewsFeed> mNewsFeeds = new ArrayList<NewsFeed>();
    private LinearLayout ll_none;
    private DynamicAdapter dynamicAdapter;
    private DBManager dbManager;
    private Dialog mDialog = null;
    private Group group = null;
    private int unread = 0, chat = 0;
    GroupActivity groupActivity;
    private PullToRefreshScrollView sc_focus;
    private Database db;
    private TextView mTv_title;

    public static CareFragement newInstance() {
        CareFragement fragment = new CareFragement();
        return fragment;
    }

    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.care_fragment_layout, null);
    }

    public void setData(boolean updata) {
        setinitData(updata);
    }

    @Override
    protected void initView() {
        groupActivity = (GroupActivity) getActivity();
        ((TextView) getView(R.id.group_title_name)).setText("我的关注");
        getView(R.id.group_title_return).setVisibility(View.GONE);
        getView(R.id.tv_mfoucus).setOnClickListener(this);
        getView(R.id.tv_mtief).setOnClickListener(this);
        getView(R.id.btn_focus).setOnClickListener(this);
        ll_none = getView(R.id.ll_none);
        sc_focus = getView(R.id.sc_focus);
        mLvFocus = getView(R.id.lv_focus);
        lv_tief = getView(R.id.lv_tief);
        lv_dynamic = getView(R.id.lv_dynamic);

        dbManager = new DBManager(getContext());
        db = new Database(getContext());
        mFoucusAdapter = new FoucusAdapter(getActivity(), mGroups, FoucusAdapter.FOCUS_LIST);
        mLvFocus.setAdapter(mFoucusAdapter);
        infolist = new ArrayList<>();
        mLvFocus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getGroupInfo((Group) parent.getItemAtPosition(position));
                isFirstIn = true;
            }
        });
        adapter = new BoxItemAdapter(getActivity(), infolist);
        lv_tief.setAdapter(adapter);

        dynamicAdapter = new DynamicAdapter(getActivity(), mNewsFeeds);
        lv_dynamic.setAdapter(dynamicAdapter);
        lv_dynamic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsFeed newsFeed = mNewsFeeds.get(position);
                BlogBean blog = new BlogBean();
                blog.setBlog_id(newsFeed.getBlog_id());
                blog.setApp_link(newsFeed.getLink().getApp());
                blog.setWap_link(newsFeed.getLink().getWap());
                blog.setImg(newsFeed.getCover());
                blog.setTitle(newsFeed.getSubject());
                blog.setLook_number(newsFeed.getStat().getViews());
                blog.setPinglun(newsFeed.getStat().getReplys());
                blog.setDate(newsFeed.getChanged_at());
                ShowActivity.showBlogDetailActivity(getActivity(), blog);
            }
        });

        lv_tief.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoxBean boxBean = (BoxBean) parent.getItemAtPosition(position);
                String box_level = boxBean.getBox_level();
                groupId = boxBean.getId();
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


                if (Integer.parseInt(box_level) <= Integer.parseInt(boxBean.getFans_level())) {
                    ShowActivity.showBoxDetailActivity(getActivity(), boxBean);
                } else {
                    if(mDialog == null)
                        mDialog = showDialog();
                    mTv_title.setText(textStr);
                    mDialog.show();
                }
            }
        });

        sc_focus.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                initData();
            }
        });
    }


    private String groupId;

    private void fansInfo() {
        WebBase.fansInfo(groupId, new JSONHandler(true, getActivity(), "正在加载数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                group = parse(obj);
                ShowActivity.showAddFansActivity(getActivity(), group);
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @NonNull
    private Group parse(JSONObject obj) {
        JSONObject group = obj.optJSONObject("group");
        Group groupbean = new Group();
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
        return groupbean;
    }

    public void rushData() {
        if (!sc_focus.isRefreshing()) {
            sc_focus.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
            sc_focus.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            sc_focus.setRefreshing();
            initData();
//            sc_focus.setMode(PullToRefreshBase.Mode.BOTH);
        }
    }

    @Override
    protected void initData() {
        WebBase.blog(0, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                mNewsFeeds.clear();
                NewsFeed blog = JSONParse.blog(obj);
                if (blog != null) {
                    mNewsFeeds.addAll(blog.getList());
                }
                dynamicAdapter.notifyDataSetChanged();
                sc_focus.onRefreshComplete();
            }

            @Override
            public void onFailure(String err_msg) {
                sc_focus.onRefreshComplete();
//                if(checkVa(err_msg)){
//                    SettingDefaultsManager.getInstance().setAuthtoken("");
//                    ((GroupActivity)getActivity()).checked();
//                }else{
//                    Log.e("TAG", "onFailure: " );
//                    Toast.makeText(getActivity(),err_msg,Toast.LENGTH_SHORT).show();
//                }
            }
        });

        userGroups();

        WebBase.box("0", new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                BoxBean newsBox = JSONParse.box(obj);
                if (newsBox != null && newsBox.getList() != null) {
                    infolist.clear();
                    infolist.addAll(newsBox.getList());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String err_msg) {
//                if(checkVa(err_msg)){
//                    SettingDefaultsManager.getInstance().setAuthtoken("");
//                    ((GroupActivity)getActivity()).checked();
//                }else{
//                    Log.e("TAG", "onFailure: " );
//                    Toast.makeText(getActivity(),err_msg,Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private boolean checkVa(String err_msg){
        if(err_msg.equals("用户登录失败或已过期") || err_msg.equals(Constants.NEED_LOGIN)){
            return true;
        }
        return false;
    }

    private void userGroups() {
        chat = 0;
        WebBase.userGroups(1, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                isFirstIn = false;
                group = JSONParse.userGroups(obj);

                if (group != null && group.getList() != null) {
                    mGroups.clear();
                    groups = group.getList();
                    for (Group group1 : groups) {
                        int liveNo = dbManager.getUnredCount(group1.getId());
                        group1.setUnredcount(liveNo);
                        unread += liveNo;
                    }
                    for (Group group1 : groups) {
                        int chatNo = db.getChatUnReadNum(group1.getId())/*+dbManager.getOfflineMsgConunt(Constants.ROOM,group1.getId())*/;
                        group1.setChat(chatNo);
                        chat += chatNo;
                    }

//                    if((unread + chat) > 99)
//                        groupActivity.setCare_menu_point(99);
//                    else
//                        groupActivity.setCare_menu_point(unread + chat);


                    if (group.getList().size() == 0) {
                        ll_none.setVisibility(View.VISIBLE);
                        sc_focus.setVisibility(View.GONE);
                    } else if (group.getList().size() > 3) {
                        ll_none.setVisibility(View.GONE);
                        sc_focus.setVisibility(View.VISIBLE);
                        for (int i = 0; i < 3; i++) {
                            mGroups.add(group.getList().get(i));
                        }
                    } else {
                        ll_none.setVisibility(View.GONE);
                        sc_focus.setVisibility(View.VISIBLE);
                        mGroups.addAll(group.getList());
                    }

                    mFoucusAdapter.notifyDataSetChanged();
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


    public void updateDate(boolean is_live) {
        if (groups != null && mFoucusAdapter != null) {
            for (Group group:groups) {
                if (is_live) {
                    int liveNo = dbManager.getUnredCount(group.getId());
                    group.setUnredcount(liveNo);
                } else {
                    int chatNo = db.getChatUnReadNum(group.getId());
                    group.setChat(chatNo);
                }
            }
            mFoucusAdapter.notifyDataSetChanged();
        }
    }

    private boolean isFirstIn = true;

    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < groups.size(); i++) {
            unread += groups.get(i).getUnredcount();
        }
        groupActivity.setCare_menu_point();
        if (isFirstIn)
            initData();
    }

//    public void getFansInfo(final BoxBean boxBean){
//        final String group_id = boxBean.getUser().getId();
//        final String box_level = boxBean.getBox_level();
//        WebBase.fansInfo(group_id, new JSONHandler(true,getActivity(),"正在加载...") {
//            @Override
//            public void onSuccess(JSONObject obj) {
//                group = JSONParse.fansInfo(obj);
////                ShowActivity.showFansActivity(getActivity(),group1);
//                boxBean.setId(group_id);
//                if(Integer.parseInt(box_level) <= group.getFans_level()){
//                    ShowActivity.showBoxDetailActivity(getActivity(),boxBean);
//                }else{
//                    if(mDialog == null)
//                        mDialog = showDialog();
//                    mDialog.show();
//                }
//            }
//            @Override
//            public void onFailure(String err_msg) {
//
//            }
//        });
//
//
//    }

    String textStr = "您还不是该老师的铁粉\n加入才能查看哦";

    private Dialog showDialog() {
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

    private void getGroupInfo(Group group) {
        final int role = group.getRoles();
        WebBase.getGroupInfo(group.getId(), new JSONHandler(true, getActivity(), "正在获取数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject group = obj.optJSONObject("group");
                Group groupbean = new Group();
                groupbean.setId(group.optString("id"));
                groupbean.setName(group.optString("name"));
                groupbean.setNick_name(group.optString("nickname"));
                groupbean.setAvatar(group.optString("avatar"));
                groupbean.setIs_close(group.optInt("is_close"));
                groupbean.setIs_private(group.optInt("is_private"));
                groupbean.setRoles(role);
                groupbean.setFans_level(group.optInt("fans_level"));
                groupbean.setNotice(group.optString("notice"));
                Bundle bundle = new Bundle();
                bundle.putSerializable("GROUP", groupbean);
                ShowActivity.showActivity(getActivity(), bundle, Chat1Activity.class.getName());
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mfoucus://更多关注
                ShowActivity.showActivity(getActivity(), CareTeacherActivity.class);
                break;
            case R.id.tv_mtief://更多铁粉
                ShowActivity.showActivity(getActivity(), TieFActivity.class);
                break;
            case R.id.tv_yes:
                fansInfo();
                mDialog.dismiss();
                break;
            case R.id.tv_no:
                mDialog.dismiss();
                break;
            case R.id.btn_focus:
                isFirstIn = true;
                ShowActivity.showActivity(getActivity(), RecommendActivity.class);
                break;
        }
    }

    public void setFirstIn(){
        isFirstIn = true;
    }
}
