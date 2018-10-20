package com.zbmf.StockGroup.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.BoxBean;

import java.util.List;

/**
 * Created by xuhao on 2017/1/4.
 */

public class BoxItemAdapter extends BaseAdapter
{
    private LayoutInflater inflater;
    private List<BoxBean>info;
    private Context context;
    public BoxItemAdapter(Context mcontext, List<BoxBean> infolist){
        this.inflater=LayoutInflater.from(mcontext);
        this.info=infolist;
        this.context=mcontext;
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
        BoxItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.box_item_layout,null);
            item=new BoxItem(view);
            view.setTag(item);
        }else{
            item= (BoxItem) view.getTag();
        }
        BoxBean bb=info.get(i);
        item.box_name.setText(bb.getSubject());
        item.box_desc.setText(bb.getDescription());
        String name = "";
        if(bb.getUser()!=null && !TextUtils.isEmpty(bb.getUser().getNickname()))
            name = bb.getUser().getNickname()+"   ";

        item.upda_time.setText(name+bb.getBox_updated());
        int box_id=Integer.valueOf(bb.getBox_id());
        switch (bb.getBox_level()){
            case "20":
                item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_year_fans_1));
                break;
            case "10":
                if(bb.getTags()!=null&&bb.getTags().size()>0){
                    if(bb.getTags().get(0).equals("投资策略")){
                        item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fans_1));
                    }else if(bb.getTags().get(0).equals("操盘日志")){
                        item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fans_2));
                    }else{
                        item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fans_3));
                    }
                }else{
                    item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fans_1));
                }

                break;
            default:
                item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_public_fans2));
                break;
        }
        if(bb.getTags()!=null&&bb.getTags().size()>0){
            item.tag_layout.removeAllViewsInLayout();
            item.tag_layout.setVisibility(View.VISIBLE);
            for(BoxBean.Tags tag:bb.getTags()){
                item.tag_layout.addView(getTagView(tag));
            }
        }else{
            item.tag_layout.removeAllViewsInLayout();
            item.tag_layout.setVisibility(View.GONE);
        }
        return view;
    }
    public View getTagView(BoxBean.Tags tag){
        View view= inflater.inflate(R.layout.tag_text_view,null);
        TextView tag_text= (TextView) view.findViewById(R.id.tag_layout_id);
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
    public class BoxItem{
        TextView box_name,upda_time,box_desc;
        ImageView box_img;
        private LinearLayout tag_layout;
        public BoxItem(View view){
            box_name= (TextView) view.findViewById(R.id.box_name);
            upda_time= (TextView) view.findViewById(R.id.box_upde_time);
            box_desc= (TextView) view.findViewById(R.id.box_desc);
            this.tag_layout= (LinearLayout) view.findViewById(R.id.tag_layout);
            box_img= (ImageView) view.findViewById(R.id.box_img_id);
        }
    }
}
