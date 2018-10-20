package com.zbmf.StockGroup.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.MyTagAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.TagBean;
import com.zbmf.StockGroup.utils.JSONParse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagPopWindow extends PopupWindow implements View.OnClickListener {

    private RecyclerView tagRecyclerView;
    private List<TagBean> tagBeanList = new ArrayList<>();
    private MyTagAdapter tagAdapter;
    private Map<String,String>map=new HashMap<>();
    private View view;
    private Context context;
    public interface OnDismissClickLitener
    {
        void onDismissClick(Map<String,String>map);
    }
    private OnDismissClickLitener onDismissClickLitener;
    public void setOnDismissClickLitener(OnDismissClickLitener onDismissClickLitener){
        this.onDismissClickLitener=onDismissClickLitener;
    }
    public TagPopWindow(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_tag, null);
        this.context=context;
        initView();
        setRecyclerView();
    }

    public void initData() {
        WebBase.GetFilter(new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray tags=obj.optJSONArray("tag");
                tagBeanList.addAll(JSONParse.getTagBeanlist(tags));
                tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    private void setRecyclerView() {
        //按照类型
        LinearLayoutManager layoutManage = new LinearLayoutManager(context);
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        tagRecyclerView.setLayoutManager(layoutManage);
        tagAdapter = new MyTagAdapter(context,tagBeanList);
        tagAdapter.setOnItemClickLitener(new MyTagAdapter.OnItemClickLitener() {
            @Override
            public void onParenItemClick(TagBean.ChildrenTag childrenTag, int position,boolean isCheck) {
                if(isCheck){
                    map.put(tagBeanList.get(position).getTag_key(),childrenTag.getId());
                }else{
                    map.remove(tagBeanList.get(position).getTag_key());
                }
            }
        });
        tagRecyclerView.setAdapter(tagAdapter);
    }

    private void initView() {
        tagRecyclerView = (RecyclerView) view.findViewById(R.id.tag_rv);
        view.findViewById(R.id.btn_reset).setOnClickListener(this);
        view.findViewById(R.id.btn_submit).setOnClickListener(this);
        view.findViewById(R.id.popup_goods_noview).setOnClickListener(this);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        ColorDrawable dw = new ColorDrawable(00000000);
        this.setBackgroundDrawable(dw);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.AnimationFade);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if(onDismissClickLitener!=null){
                    onDismissClickLitener.onDismissClick(map);
                }

            }
        });
        this.update();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reset:
                map.clear();
                for(TagBean tagBean:tagBeanList){
                    List<TagBean.ChildrenTag>childrenTags=tagBean.getData();
                    for(TagBean.ChildrenTag tag:childrenTags){
                        tag.setSelect(false);
                    }
                }
                tagAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_submit:
                dismiss();
                break;
            case R.id.popup_goods_noview:
                dismiss();
                break;
        }
    }

    public boolean onKeyDown(Context context, int keyCode, KeyEvent event) {
        this.context = context;
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return true;
    }

    public void showFilterPopup(View parent) {
        if (!this.isShowing()) {
            if(tagBeanList!=null&&tagBeanList.size()==0){
                initData();
            }
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

}
