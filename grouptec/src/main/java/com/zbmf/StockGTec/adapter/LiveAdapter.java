package com.zbmf.StockGTec.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.beans.LiveMessage;
import com.zbmf.StockGTec.utils.DateUtil;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;
import com.zbmf.StockGTec.utils.MessageType;
import com.zbmf.StockGTec.utils.SettingDefaultsManager;
import com.zbmf.StockGTec.view.MyTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by xuhao on 2016/12/16.
 */

public class LiveAdapter extends BaseAdapter {
    public LayoutInflater inflater;
    private List<LiveMessage> infolist;
    private boolean is_tf;
    private Context context;
    private MyTextView.OnTextClickListener listener;
    private View.OnClickListener onClickListener;
    public LiveAdapter(Activity context, List<LiveMessage>data,boolean is_tf_type,MyTextView.OnTextClickListener textlistener,View.OnClickListener onClickListener){
        this.inflater=LayoutInflater.from(context);
        this.infolist=data;
        this.context=context;
        this.is_tf=is_tf_type;
        this.listener=textlistener;
        this.onClickListener=onClickListener;
    }

    @Override
    public int getCount() {
        return infolist.size();
    }

    @Override
    public Object getItem(int i) {
        return infolist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LiveItem item=null;
        if(view==null){
            view=inflater.inflate(R.layout.live_item_layout,viewGroup,false);
            item=new LiveItem(view);
            view.setTag(item);
        }else{
            item= (LiveItem) view.getTag();
        }
        final LiveMessage cm=infolist.get(i);
        item.message_countent.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(SettingDefaultsManager.getInstance().getTextSize()));
        item.message_time.setText(DateUtil.getNewChatTime(cm.getMessage_time()));
        item.message_countent.getPaint().setFakeBoldText(false);
        item.message_countent.setTextColor(context.getResources().getColor(R.color.black));
        switch (cm.getMessage_type()){
            case MessageType.BOX:
                //宝盒消息 1
                String box_message=null;
                switch (cm.getAction()){
                    case 1://发布
                        box_message="圈主"+cm.getBox_user_name()+"发布了魔方宝盒";
                        break;
                    case 2://更新
                        box_message="圈主"+cm.getBox_user_name()+"更新了魔方宝盒";
                        break;
                    case 3://订阅
                        box_message=cm.getBox_user_name()+"订阅了圈主的魔方宝盒";
                        break;
                }
                red_bag_g(item);
                item.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_live_box));
                item.message_countent.setOnClickListener(null);
                item.message_countent.setTextClickListener(listener);
                item.message_name.setText("宝盒消息");
                item.message_countent.setBoxMessage(box_message,cm.getBox_name(),cm.getBox_id());
                item.live_img_id.setVisibility(View.GONE);
                break;
            case MessageType.BLOG:
                red_bag_g(item);
                item.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_live_notice));
                item.message_name.setText("系统消息");
                item.message_countent.setOnClickListener(null);
                item.message_countent.setTextClickListener(listener);
                item.message_countent.getPaint().setFakeBoldText(false);
                item.message_countent.setTextColor(context.getResources().getColor(R.color.black));
                String blog_message=cm.getMessage_countent();
                blog_message=blog_message.substring(0,cm.getMessage_countent().indexOf("["));
                item.message_countent.setBlogMessage(blog_message,cm.getBlog_name(),cm.getUser_id()+"-"+cm.getBlog_id());
                item.live_img_id.setVisibility(View.GONE);
                break;
            case MessageType.MEMBER:
                red_bag_g(item);
                item.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_live_notice));
                item.message_name.setText("系统消息");
                item.message_countent.setOnClickListener(null);
                item.message_countent.setTextClickListener(listener);
                item.message_countent.getPaint().setFakeBoldText(false);
                item.message_countent.setTextColor(context.getResources().getColor(R.color.black));
                item.message_countent.setMessageText(cm.getMessage_countent());
                item.live_img_id.setVisibility(View.GONE);
                break;
            case MessageType.FANS:
                red_bag_g(item);
                item.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_live_tf));
                item.message_countent.setOnClickListener(null);
                item.message_countent.setTextClickListener(listener);
                item.message_name.setText(cm.getMessage_name());
                if(!is_tf){
                    item.message_countent.setMessageText(context.getResources().getString(R.string.add_to_tf));
                    item.live_img_id.setVisibility(View.GONE);
                }else{
                    item.message_countent.setMessageText(cm.getMessage_countent());
                    if(cm.getMessage_or_img()!=null&&cm.getMessage_or_img().equals(MessageType.IMG)){
                        item.live_img_id.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(cm.getThumb(),item.live_img_id, ImageLoaderOptions.ProgressOptions());
                        item.live_img_id.setTag(cm.getImg_url());
                        item.live_img_id.setOnClickListener(onClickListener);
                    }else{
                        item.live_img_id.setVisibility(View.GONE);
                    }
                }

                break;
            case MessageType.SYSTEM:
                red_bag_g(item);
                item.message_name.setText("系统公告");
                item.message_countent.setOnClickListener(null);
                item.message_countent.setTextClickListener(listener);
                item.message_countent.setMessageText(cm.getMessage_countent());
                item.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_live_notice));
                if(cm.getMessage_or_img()!=null&&cm.getMessage_or_img().equals(MessageType.IMG)){
                    item.live_img_id.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(cm.getThumb(),item.live_img_id, ImageLoaderOptions.ProgressOptions());
                    item.live_img_id.setTag(cm.getImg_url());
                    item.live_img_id.setOnClickListener(onClickListener);
                }else{
                    item.live_img_id.setVisibility(View.GONE);
                }
                break;
            case MessageType.CHAT:
                red_bag_g(item);
                item.image.setImageDrawable(context.getResources().getDrawable(R.drawable.quan));
                item.message_name.setText(cm.getMessage_name());
                if(cm.getImportent()==0){
                    item.message_countent.getPaint().setFakeBoldText(false);
                    item.message_countent.setTextColor(context.getResources().getColor(R.color.black));
                }else{
                    item.message_countent.getPaint().setFakeBoldText(true);
                    item.message_countent.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }
                item.message_countent.setOnClickListener(null);
                item.message_countent.setTextClickListener(listener);
                item.message_countent.setMessageText(cm.getMessage_countent());
                if(cm.getMessage_or_img()!=null&&cm.getMessage_or_img().equals(MessageType.IMG)){
                    item.live_img_id.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(cm.getThumb(),item.live_img_id, ImageLoaderOptions.BigProgressOptions());
                    item.live_img_id.setTag(cm.getImg_url());
                    item.live_img_id.setOnClickListener(onClickListener);
                }else{
                    item.live_img_id.setVisibility(View.GONE);
                }
                break;
            case MessageType.GIFT:
                item.message_countent.setOnClickListener(null);
                item.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_live_gift));
                item.message_countent.setMessageText(cm.getMessage_countent());
                item.live_img_id.setVisibility(View.GONE);
                break;
            case MessageType.RED_PACKET:
                item.image.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_live_redbag));
                item.message_name.setText(cm.getMessage_name());
                red_bag_v(item);
                item.red_bag_layout.setTag(cm.getRed_id());
                item.red_bag_layout.setOnClickListener(onClickListener);
                item.red_bag_message.setText(cm.getRed_message());
                break;
        }
        return view;
    }
    public void red_bag_v(LiveItem item){
        item.message_countent.setVisibility(View.GONE);
        item.live_img_id.setVisibility(View.GONE);
        item.red_bag_layout.setVisibility(View.VISIBLE);
    }
    public void red_bag_g(LiveItem item){
        item.message_countent.setVisibility(View.VISIBLE);
        item.live_img_id.setVisibility(View.VISIBLE);
        item.red_bag_layout.setVisibility(View.GONE);
    }
    public void add_tf(boolean add_tf){
        is_tf=add_tf;
        notifyDataSetChanged();
    }
    public class LiveItem{
        TextView message_name,message_time,red_bag_message;
        private MyTextView message_countent;
        ImageView live_img_id,image;
        private RelativeLayout red_bag_layout;
        public LiveItem(View view){
            this.message_countent= (MyTextView) view.findViewById(R.id.message_countent);
            this.message_time= (TextView) view.findViewById(R.id.message_time);
            this.message_name= (TextView) view.findViewById(R.id.message_name);
            this.live_img_id= (ImageView) view.findViewById(R.id.live_img_id);
            this.red_bag_layout= (RelativeLayout) view.findViewById(R.id.red_bag_layout);
            this.red_bag_message= (TextView) view.findViewById(R.id.red_bag_message);
            this.image= (ImageView) view.findViewById(R.id.image);
        }
    }
}
