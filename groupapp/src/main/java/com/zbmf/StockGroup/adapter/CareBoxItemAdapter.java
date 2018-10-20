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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.beans.BoxBean;
import com.zbmf.StockGroup.interfaces.OnFansClick;
import com.zbmf.StockGroup.utils.ImageLoaderOptions;
import com.zbmf.StockGroup.view.RoundedCornerImageView;

import java.util.List;

/**
 * Created by xuhao on 2017/1/4.
 */

public class CareBoxItemAdapter extends BaseAdapter
{
    private LayoutInflater inflater;
    private List<BoxBean>info;
    private Context context;
    private OnFansClick onFansClick;
    private int white,gray;
    public void setOnFansClick(OnFansClick onFansClick) {
        this.onFansClick = onFansClick;
    }

    public CareBoxItemAdapter(Context mcontext, List<BoxBean> infolist){
        this.inflater=LayoutInflater.from(mcontext);
        this.info=infolist;
        this.context=mcontext;
        white=context.getResources().getColor(R.color.white);
        gray=context.getResources().getColor(R.color.black_99);
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
            view=inflater.inflate(R.layout.care_box_item_layout,null);
            item=new BoxItem(view);
            view.setTag(item);
        }else{
            item= (BoxItem) view.getTag();
        }
        final  BoxBean bb=info.get(i);
        item.box_name.setText(bb.getSubject());
        item.box_desc.setText(bb.getDescription());
        item.teacher_name.setText(bb.getUser()!=null && !TextUtils.isEmpty(bb.getUser().getNickname())?bb.getUser().getNickname():"");
        ImageLoader.getInstance().displayImage(bb.getUser()!=null&&!TextUtils.isEmpty(bb.getUser().getAvatar())?bb.getUser().getAvatar():"",
                item.imv_box_avatar, ImageLoaderOptions.AvatarOptions());
//        ViewFactory.imgCircleView(viewGroup.getContext(),bb.getUser()!=null&&!TextUtils.
//                isEmpty(bb.getUser().getAvatar())?bb.getUser().getAvatar():"",item.imv_box_avatar);
        item.imv_box_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFansClick!=null&&bb.getUser()!=null){
                    onFansClick.onGroup(bb.getUser().getId());
                }

            }
        });
        item.upda_time.setText(bb.getBox_updated());
        String fans_message="";
        switch (bb.getBox_level()){
            case "20":
                item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_year_fans_1));
                fans_message="订阅年粉";
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
                fans_message="订阅铁粉";
                break;
            default:
                item.box_img.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_public_fans2));
                break;
        }
        if (Integer.parseInt(bb.getBox_level()) <= Integer.parseInt(bb.getFans_level())) {
            String msg="";
            switch (bb.getFans_level()){
                case "20":
                    msg="您是年粉，可免费查看";
                    break;
                case "10":
                    msg="您是铁粉，可免费查看";
                    break;
            }
            item.box_fans_message.setText(msg);
            item.box_fans_message.setTextColor(gray);
            item.box_fans_message.setSelected(false);
            item.box_fans_message.setTag(false);
        } else {
            item.box_fans_message.setText(fans_message);
            item.box_fans_message.setTextColor(white);
            item.box_fans_message.setSelected(true);
            item.box_fans_message.setTag(true);
        }
        item.box_fans_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFansClick!=null){
                    if((boolean)v.getTag()){
                        onFansClick.onFans(bb.getId());
                    }else{
                        onFansClick.onBox(bb);
                    }
                }
            }
        });
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
        TextView box_name,upda_time,box_desc,box_fans_message,teacher_name;
        ImageView box_img;
        RoundedCornerImageView imv_box_avatar;
        private LinearLayout tag_layout;
        public BoxItem(View view){
            box_name= (TextView) view.findViewById(R.id.box_name);
            upda_time= (TextView) view.findViewById(R.id.box_upde_time);
            box_desc= (TextView) view.findViewById(R.id.box_desc);
            this.tag_layout= (LinearLayout) view.findViewById(R.id.tag_layout);
            box_img= (ImageView) view.findViewById(R.id.box_img_id);
            box_fans_message= (TextView) view.findViewById(R.id.box_fans_message);
            teacher_name= (TextView) view.findViewById(R.id.box_item_name);
            imv_box_avatar= (RoundedCornerImageView) view.findViewById(R.id.imv_box_avatar);
        }
    }
}
