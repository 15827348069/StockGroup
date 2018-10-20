package com.zbmf.StocksMatch.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.db.DatabaseImpl;
import com.zbmf.StocksMatch.utils.Constants;
import com.zbmf.StocksMatch.utils.SharedPreferencesUtils;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.CircleImageView;

/**
 * Created by lulu on 16/1/9.
 */
public class AccountAdapter extends ListAdapter<User>{

    private ImageLoader imageLoader;
    private SharedPreferencesUtils sp;
    private boolean edit = false;
    public AccountAdapter(Activity context) {
        super(context);
        imageLoader = ImageLoader.getInstance();
        sp = new SharedPreferencesUtils(mContext);
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else{
            convertView = LayoutInflater.from(mContext).inflate(R.layout.account_item, null);
            holder = new ViewHolder();
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_del = (TextView) convertView.findViewById(R.id.tv_del);
            holder.civ = (CircleImageView)convertView.findViewById(R.id.civ);
            holder.iv_choose = (ImageView)convertView.findViewById(R.id.iv_choose);
            convertView.setTag(holder);
        }
        final User user =  mList.get(position);
        holder.tv_username.setText(user.getNickname());

        if(user.isAccount())
            holder.iv_choose.setVisibility(View.VISIBLE);
        else
            holder.iv_choose.setVisibility(View.GONE);

        if(TextUtils.isEmpty(user.getAvatar())){

        }else{
            imageLoader.displayImage(user.getAvatar(),holder.civ);
        }

        if(edit){
            holder.tv_del.setVisibility(View.VISIBLE);
            holder.tv_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatabaseImpl(mContext).delUser(user.getUser_id());
                    if(user.isAccount()){
                        sp.setAccount("");
                        UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_LOGIN,null);
                    }

                    Intent intent = new Intent();
                    intent.setAction(Constants.ACCOUNT_DEL);
                    intent.putExtra("user_id",user.getUser_id());
                    mContext.sendBroadcast(intent);
                }
            });
        } else
            holder.tv_del.setVisibility(View.GONE);
        return convertView;
    }


    static class ViewHolder{
        TextView tv_username,tv_del;
        CircleImageView civ;
        ImageView iv_choose;
    }


}
