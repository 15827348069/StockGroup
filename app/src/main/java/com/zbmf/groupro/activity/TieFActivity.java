package com.zbmf.groupro.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.BoxItemAdapter;
import com.zbmf.groupro.api.JSONHandler;
import com.zbmf.groupro.api.WebBase;
import com.zbmf.groupro.beans.BoxBean;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.DisplayUtil;
import com.zbmf.groupro.utils.JSONParse;
import com.zbmf.groupro.utils.ShowActivity;
import com.zbmf.groupro.view.PullToRefreshBase;
import com.zbmf.groupro.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.zbmf.groupro.R.id.listview;

//铁粉更多
public class TieFActivity extends AppCompatActivity implements View.OnClickListener {

    private PullToRefreshListView content_view;
    private BoxItemAdapter mBoxItemAdapter;
    private List<BoxBean> infos = new ArrayList<>();
    private int PAGE_INDEX, PAGGS;
    public static final int Refresh = 1;
    private Group group = null;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_teacher);

        final TextView tv_title = ((TextView) findViewById(R.id.group_title_name));
        tv_title.setText("铁粉");
        tv_title.setVisibility(View.VISIBLE);
        findViewById(R.id.group_title_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        content_view = (PullToRefreshListView) findViewById(listview);
        content_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mBoxItemAdapter = new BoxItemAdapter(this, infos);
        content_view.setAdapter(mBoxItemAdapter);

        content_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                userGroups(Refresh);
            }
        });

        content_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    ShowActivity.showBoxDetailActivity(TieFActivity.this, boxBean);
                } else {
                    if (mDialog == null)
                        mDialog = showDialog();
                    mTv_title.setText(textStr);
                    mDialog.show();
                }
            }
        });
        userGroups(Refresh);

    }

    String textStr = "您还不是该老师的铁粉\n加入才能查看哦";

    private void fansInfo() {
        WebBase.fansInfo(groupId, new JSONHandler(true, TieFActivity.this, "正在加载数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                group = parse(obj);
                ShowActivity.showAddFansActivity(TieFActivity.this, group);
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


    private Dialog mDialog = null;
    private TextView mTv_title;

    private Dialog showDialog() {
        final Dialog dialog = new Dialog(this, R.style.myDialogTheme);
        final LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_tip, null);
        layout.findViewById(R.id.tv_yes).setOnClickListener(this);
        layout.findViewById(R.id.tv_no).setOnClickListener(this);
        mTv_title = (TextView) layout.findViewById(R.id.tv_title);
        mTv_title.setText(textStr);
        dialog.setContentView(layout);
        Window win = dialog.getWindow();
        win.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (DisplayUtil.getScreenWidthPixels(this) * 0.75);
        win.setAttributes(lp);
        dialog.setCancelable(false);

        return dialog;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_yes:
                    fansInfo();
                mDialog.dismiss();
                break;
            case R.id.tv_no:
                mDialog.dismiss();
                break;
        }
    }

    private void userGroups(final int direction) {
        if (direction == Refresh)
            PAGE_INDEX = 1;

        WebBase.box("1", new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                content_view.onRefreshComplete();
                BoxBean newsBox = JSONParse.box(obj);
                if (newsBox != null)
                    if (newsBox.getList() != null && newsBox.getList().size() > 0) {
                        if (direction == Refresh) {
                            infos.clear();
                        }

                        infos.addAll(newsBox.getList());
                        mBoxItemAdapter.notifyDataSetChanged();
                    }
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

}
