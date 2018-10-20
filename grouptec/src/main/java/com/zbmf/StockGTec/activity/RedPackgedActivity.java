package com.zbmf.StockGTec.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.RedBagAdapter;
import com.zbmf.StockGTec.api.RedPackgedDetailHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.RedBagUserMessage;
import com.zbmf.StockGTec.beans.RedPackgedBean;
import com.zbmf.StockGTec.utils.ImageLoaderOptions;
import com.zbmf.StockGTec.view.CustomProgressDialog;
import com.zbmf.StockGTec.view.ListViewForScrollView;
import com.zbmf.StockGTec.view.RoundedCornerImageView;
import com.zbmf.StockGTec.view.ScrollBottomScrollView;

import java.util.ArrayList;
import java.util.List;

public class RedPackgedActivity extends ExActivity {
	private ImageButton return_button;
	private Button go_to_get_money;
	private RoundedCornerImageView red_from_avatar;
	private RedPackgedBean rb;
	private TextView red_bag_from_name,red_bag_message,red_bag_message_count,red_bag_type;
	private LinearLayout have_red_bag;
	private ListViewForScrollView have_red_packged_user;
	private List<RedBagUserMessage> infolist;
	private RedBagAdapter adapter;
	private ScrollBottomScrollView red_bag_scrollview;
	private CustomProgressDialog dialog = null;
	private int index;
	private int page,pages;
	private TextView red_packe_number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.red_bag_detail_layout);
		rb=(RedPackgedBean) getIntent().getSerializableExtra("redpackged");
		page=1;
		init();
		setMessage();
	}
	private void setMessage() {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().displayImage(rb.getUser_avatar(), red_from_avatar, ImageLoaderOptions.AvatarOptions());
		red_bag_from_name.setText("来自"+rb.getUser_name()+"的红包");
		red_bag_message.setText(rb.getRed_message());
		if(rb.getRed_status()==4){
			have_red_bag.setVisibility(View.VISIBLE);
			red_bag_message_count.setText(rb.getRed_bag_money());
			red_bag_type.setText(rb.getRed_type());
			if(rb.getRed_type().equals("兑换券")){
				go_to_get_money.setVisibility(View.VISIBLE);
				go_to_get_money.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String url = rb.getCoupon_url(); // web address
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(url));
						startActivity(intent);
						finish();
					}
				});
			}else{
				go_to_get_money.setVisibility(View.GONE);
			}
		}else {
			have_red_bag.setVisibility(View.GONE);
		}
		String message=null;
		if(rb.getTotal_num()==rb.getReceive_num()){
			message="共"+rb.getTotal_num()+"个红包"+"10"+"秒被抢光";
		}else{
			message="已领取"+rb.getReceive_num()+"个，"+"共"+rb.getTotal_num()+"个";
		}
		red_packe_number.setText(message);
		return_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		infolist=new ArrayList<RedBagUserMessage>();
		infolist.addAll(rb.getInfolist());
		adapter=new RedBagAdapter(getApplicationContext(), infolist);
		have_red_packged_user.setAdapter(adapter);
		// 滑动加载
		red_bag_scrollview.setScrollBottomListener(new ScrollBottomScrollView.ScrollBottomListener() {
			@Override
			public void scrollBottom() {
				if(index==0){
					index++;
					if(page!=pages){
						if(dialog==null){
							dialog = CustomProgressDialog.createDialog(RedPackgedActivity.this);
						}
						dialog.setMessage("正在加载...");
						dialog.show();
						addUserMessage();
					}
				}
			}
			@Override
			public void scrollnoBottom() {

			}
			@Override
			public void scrollTop() {

			}
		});
		getUserMessage();
	}

	private void getUserMessage() {
		WebBase.getRedPackgedDetail(rb.getRed_id(), page, new RedPackgedDetailHandler() {
			@Override
			public void onSuccess(RedPackgedBean obj) {
				RedPackgedBean rbs=obj;
				String message=null;
				if(rbs.getTotal_num()==rbs.getReceive_num()){
					message="共"+rbs.getTotal_num()+"个红包，在"+getTime(rbs.getDuration())+"被抢光";
				}else{
					message="已领取"+rbs.getReceive_num()+"个，"+"共"+rbs.getTotal_num()+"个红包";
				}
				red_packe_number.setText(message);
				page=rbs.getPage();
				pages=rbs.getPages();
				infolist.clear();
				infolist.addAll(rbs.getInfolist());
				adapter.notifyDataSetChanged();
				index=0;
			}

			@Override
			public void onFailure(String err_msg) {

			}
		});
	}

	private void addUserMessage() {
		WebBase.getRedPackgedDetail(rb.getRed_id(), page+1, new RedPackgedDetailHandler() {
			@Override
			public void onSuccess(RedPackgedBean obj) {
				RedPackgedBean rb_lists=obj;
				page=rb_lists.getPage();
				pages=rb_lists.getPages();
				infolist.addAll(rb_lists.getInfolist());
				adapter.notifyDataSetChanged();
				index=0;
			}

			@Override
			public void onFailure(String err_msg) {

			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	private void init() {
		// TODO Auto-generated method stub
		red_from_avatar=(RoundedCornerImageView) findViewById(R.id.red_from_avatar);
		red_bag_from_name=(TextView) findViewById(R.id.red_bag_from_name);
		red_bag_message=(TextView) findViewById(R.id.red_bag_message);
		return_button=(ImageButton) findViewById(R.id.return_button);
		have_red_bag=(LinearLayout) findViewById(R.id.have_red_bag_layout);
		red_bag_message_count=(TextView) findViewById(R.id.red_bag_message_count);
		red_bag_type=(TextView) findViewById(R.id.red_bag_type);
		go_to_get_money=(Button) findViewById(R.id.go_to_get_money);
		have_red_packged_user=(ListViewForScrollView) findViewById(R.id.have_red_packged_user);
		red_bag_scrollview=(ScrollBottomScrollView) findViewById(R.id.red_bag_scrollview);
		red_packe_number=(TextView) findViewById(R.id.red_packe_number);
	}

	public String getTime(int duration){
		if(duration<60){
			return duration+"秒";
		}else if(duration<3600){
			if(duration%60>0){
				return (duration%3600/60)+1+"分钟";
			}else{
				return duration%3600/60+"分钟";
			}
		}else{
			if(duration%3600/60>0){
				return (duration/3600)+1+"小时";
			}else{
				return (duration/3600)+"小时";
			}
		}
	}
}
