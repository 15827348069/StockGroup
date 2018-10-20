package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.activity.BanListActivity;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BanInfo;
import com.zbmf.StockGTec.view.SwipeLayout;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;

/**
 * Created by on 2017/1/12.
 * 禁言的适配器
 */

public class BanAdapter extends BaseAdapter {

    private Context cxt;
    private List<BanInfo> mList;
    private HashSet<SwipeLayout> mUnClosedLayouts = new HashSet<SwipeLayout>();
    private String groupId;

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public BanAdapter(Context cxt, List<BanInfo> list) {
        this.cxt = cxt;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(cxt, R.layout.unban_item, null);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_del = (TextView) convertView.findViewById(R.id.tv_del);
            holder.tv_unban = (TextView) convertView.findViewById(R.id.tv_unban);
            holder.sl = (SwipeLayout) convertView.findViewById(R.id.sl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.sl.setSwipeListener(mSwipeListener);
        final BanInfo info= mList.get(position);
        holder.tv_name.setText(info.getNickname());
        holder.tv_unban.setText("于"+info.getTime().substring(0,10)+"被禁言");

        holder.sl.getFrontView().setOnClickListener(new View.OnClickListener() {//条目删除
            @Override
            public void onClick(View v) {
                closeAllLayout();

            }
        });

        holder.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAllLayout();
                if (!TextUtils.isEmpty(groupId))
                    unBan(info.getUser_id());
                else
                    Toast.makeText(cxt, "圈号未知", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }

    /**
     * 解除言
     *
     * @param user_id
     */
    private void unBan(String user_id) {
        WebBase.unBan(groupId, user_id, new JSONHandler(cxt) {
            @Override
            public void onSuccess(JSONObject obj) {
                Toast.makeText(cxt, "已解除禁言", Toast.LENGTH_SHORT).show();
                ((BanListActivity) cxt).finish();
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(cxt, err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    SwipeLayout.SwipeListener mSwipeListener = new SwipeLayout.SwipeListener() {
        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            mUnClosedLayouts.add(swipeLayout);
        }

        @Override
        public void onClose(SwipeLayout swipeLayout) {
            mUnClosedLayouts.remove(swipeLayout);
        }

        @Override
        public void onStartClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onStartOpen(SwipeLayout swipeLayout) {
            closeAllLayout();
            mUnClosedLayouts.add(swipeLayout);
        }

    };

    public void closeAllLayout() {
        if (mUnClosedLayouts.size() == 0)
            return;

        for (SwipeLayout l : mUnClosedLayouts) {
            l.close(true, false);
        }
        mUnClosedLayouts.clear();
    }


    static class ViewHolder {
        TextView tv_name, tv_unban, tv_del;
        SwipeLayout sl;
    }
}
