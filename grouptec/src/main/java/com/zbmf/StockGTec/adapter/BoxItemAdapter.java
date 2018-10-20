package com.zbmf.StockGTec.adapter;

import android.content.Context;
import android.location.GnssClock;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.activity.BoxActivity;
import com.zbmf.StockGTec.activity.BoxDetailActivity;
import com.zbmf.StockGTec.activity.BoxUpdateActivity;
import com.zbmf.StockGTec.activity.Chat1Activity;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.BoxBean;
import com.zbmf.StockGTec.utils.ShowActivity;

import org.json.JSONObject;

import java.util.List;

public class BoxItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<BoxBean> info;
    private Context context;

    public BoxItemAdapter(Context mcontext, List<BoxBean> infolist) {
        this.inflater = LayoutInflater.from(mcontext);
        this.info = infolist;
        this.context = mcontext;
    }

    @Override
    public int getCount() {
        return info.size();
    }

    @Override
    public Object getItem(int i) {
        return info.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BoxItem item = null;
        if (view == null) {
            view = inflater.inflate(R.layout.box_item_layout, null);
            item = new BoxItem(view);
            view.setTag(item);
        } else {
            item = (BoxItem) view.getTag();
        }
        final BoxBean bb = info.get(i);
        item.box_name.setText(bb.getSubject());
        item.box_desc.setText(bb.getDescription());
        item.upda_time.setText(bb.getBox_updated());
        int box_id = Integer.valueOf(bb.getBox_id());
        switch (bb.getBox_level()) {
            case "20":
                item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_year_fans_1));
                break;
            case "10":
                if (bb.getTags() != null && bb.getTags().size() > 0) {
                    if (bb.getTags().get(0).equals("投资策略")) {
                        item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fans_1));
                    } else if (bb.getTags().get(0).equals("操盘日志")) {
                        item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fans_2));
                    } else {
                        item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fans_3));
                    }
                } else {
                    item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fans_1));
                }

                break;
            default:
                item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_public_fans2));
                break;
        }

        if (bb.getTags() != null && bb.getTags().size() > 0) {
            item.tag_layout.removeAllViewsInLayout();
            item.tag_layout.setVisibility(View.VISIBLE);
            for (BoxBean.Tags tag : bb.getTags()) {
                item.tag_layout.addView(getTagView(tag));
            }

        } else {
            item.tag_layout.removeAllViewsInLayout();
            item.tag_layout.setVisibility(View.GONE);
        }



        item.rl_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visible = bb.visible;
                reset();
                bb.visible = visible == View.VISIBLE ? View.GONE : View.VISIBLE;
                notifyDataSetChanged();
            }
        });

        if ("1".equals(bb.getIs_stick())) {
            item.iv_zhiding.setImageResource(R.drawable.xiajia);
            item.tv_zhiding.setText("取消置顶");
        } else {
            item.iv_zhiding.setImageResource(R.drawable.zhiding);
            item.tv_zhiding.setText("置顶");
        }

        if (bb.visible == View.VISIBLE)
            item.ll_menu.setVisibility(View.VISIBLE);
        else
            item.ll_menu.setVisibility(View.GONE);

        item.ll_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("0".equals(bb.getIs_stick()))
                    WebBase.stickManageBox(bb.getBox_id(),"1", new JSONHandler(context) {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        bb.visible = View.GONE;
                        BoxBean temp = bb;
                        for (BoxBean b : info) {
                            if (b.getBox_id().equals(bb.getBox_id())) {
                                info.remove(b);
                                break;
                            }
                        }
                        temp.setIs_stick("1");
                        info.add(0, temp);
                        notifyDataSetChanged();
                        Toast.makeText(context, "置顶成功", 0).show();
                    }

                    @Override
                    public void onFailure(String err_msg) {

                        Toast.makeText(context, "置顶失败", 0).show();
                    }
                });

                else
                    WebBase.stickManageBox(bb.getBox_id(),"0", new JSONHandler(context) {
                        @Override
                        public void onSuccess(JSONObject obj) {
                            bb.visible = View.GONE;
                            for (BoxBean b : info) {
                                if (b.getBox_id().equals(bb.getBox_id())) {
                                    b.setIs_stick("0");
                                    break;
                                }
                            }

                            notifyDataSetChanged();
                            Toast.makeText(context, "取消成功", 0).show();
                        }

                        @Override
                        public void onFailure(String err_msg) {
                            Toast.makeText(context, "取消失败", 0).show();
                        }
                    });
            }
        });

        item.ll_shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WebBase.removeManageBox(bb.getBox_id(), new JSONHandler(context) {
                    @Override
                    public void onSuccess(JSONObject obj) {
                        Toast.makeText(context, "删除成功", 0).show();
                        for (BoxBean b : info) {
                            if (b.getBox_id().equals(bb.getBox_id())) {
                                info.remove(b);
                                notifyDataSetChanged();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(String err_msg) {

                    }
                });


            }
        });

        item.ll_gegnxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                Bundle bundle = new Bundle();
                bundle.putSerializable("box", bb);
                ShowActivity.startActivity((Chat1Activity) context, bundle, BoxUpdateActivity.class.getName());
            }
        });

        item.ll_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                notifyDataSetChanged();
                Bundle bundle = new Bundle();
                bundle.putSerializable("box", bb);
                ShowActivity.startActivity((Chat1Activity) context, bundle, BoxDetailActivity.class.getName());
            }
        });
        return view;
    }

    private void reset() {
        for (BoxBean bb : info) {
            bb.visible = View.GONE;
        }
    }

    public View getTagView(BoxBean.Tags tag) {
        View view = inflater.inflate(R.layout.tag_text_view, null);
        TextView tag_text = (TextView) view.findViewById(R.id.tag_layout_id);
        switch (tag.getTag_type()) {
            case 1:
                tag_text.setBackgroundResource(R.drawable.text_backound_blue);
                break;
            case 2:
                tag_text.setBackgroundResource(R.drawable.text_backound_orange);
                break;
            case 3:
                tag_text.setBackgroundResource(R.drawable.text_backound);
                break;
            default:
                tag_text.setBackgroundResource(R.drawable.text_backound_def);
                break;
        }
        tag_text.setText(tag.getName());
        return view;
    }

    public class BoxItem {
        TextView box_name, upda_time, box_desc, tv_zhiding;
        ImageView box_img, iv_zhiding;
        private LinearLayout tag_layout, ll_menu, ll_up, ll_shanchu, ll_look, ll_gegnxin;
        private LinearLayout rl_root;

        public BoxItem(View view) {
            box_name = (TextView) view.findViewById(R.id.box_name);
            upda_time = (TextView) view.findViewById(R.id.box_upde_time);
            box_desc = (TextView) view.findViewById(R.id.box_desc);
            tv_zhiding = (TextView) view.findViewById(R.id.tv_zhiding);
            this.tag_layout = (LinearLayout) view.findViewById(R.id.tag_layout);
            this.ll_menu = (LinearLayout) view.findViewById(R.id.ll_menu);
            this.ll_shanchu = (LinearLayout) view.findViewById(R.id.ll_shanchu);
            this.ll_up = (LinearLayout) view.findViewById(R.id.ll_up);
            this.ll_look = (LinearLayout) view.findViewById(R.id.ll_look);
            this.ll_gegnxin = (LinearLayout) view.findViewById(R.id.ll_gegnxin);
            this.rl_root = (LinearLayout) view.findViewById(R.id.rl_root);
            box_img = (ImageView) view.findViewById(R.id.box_img_id);
            iv_zhiding = (ImageView) view.findViewById(R.id.iv_zhiding);

        }
    }
}
