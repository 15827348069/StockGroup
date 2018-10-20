package com.zbmf.groupro.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by xuhao on 2017/1/12.
 */

public class StretchTextUtil {
    private int arrowUpId;
    private int arrowDownId;
    private TextView textView;
    private int lines;
    private ImageView arrowImage;
    private LinearLayout look_all_desc;
    private TextView look_all_text;
    public StretchTextUtil(TextView textView ,TextView look_all_text, int lines ,LinearLayout look_all_desc,ImageView arrowImage , int arrowUpId ,int arrowDownId){
        this.textView = textView;
        this.lines = lines;
        this.arrowImage = arrowImage;
        this.arrowUpId = arrowUpId;
        this.arrowDownId = arrowDownId;
        this.look_all_desc=look_all_desc;
        this.look_all_text=look_all_text;
    }
    public static StretchTextUtil getInstance(TextView textView ,TextView look_all_text, int lines ,LinearLayout look_all_desc,ImageView arrowImage , int arrowUpId ,int arrowDownId){
        return new StretchTextUtil(textView ,look_all_text,lines ,look_all_desc ,arrowImage,arrowUpId , arrowDownId);
    }

    /**
     * @Title: initStretch
     * @Description:初始化文本收缩
     */
    public void initStretch(){
        //设置默认状态
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            boolean flag = false;
            @Override
            public void onGlobalLayout(){
                if(!flag){
                    flag = true;
                    initStretchStatus();
                }
            }
        });
        //当文本内容改变时初始化状态
        textView.addTextChangedListener(new TextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
                initStretchStatus();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            @Override
            public void afterTextChanged(Editable s){}
        });
        look_all_desc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Object o = v.getTag();
                if(o != null){
                    int status = (Integer)o;
                    if(status == 0){
                        setStretch(1);
                    } else if(status == 1){
                        setStretch(0);
                    }
                }
            }
        });
    }

    /**
     * @Title: initStretchStatus
     * @Description:初始化文本的状态
     */
    public void initStretchStatus(){
        if(isMoreLines()){
            setStretch(1);
        }else{
            setStretch(-1);
        }
    }

    /**
     * @Title: isMoreLines
     * @Description:判断文本的内容是否超过指定的行数
     * @return boolean
     */
    public boolean isMoreLines(){
        float allTextPx = textView.getPaint().measureText(textView.getText().toString());
        float showViewPx = (textView.getWidth() - textView.getPaddingLeft() - textView.getPaddingRight()) * lines;
        return allTextPx > showViewPx;
    }

    /**
     * @Title: setStretch
     * @Description:设置箭头的显示状态与文本的伸缩状态
     * @param status -1:不显示，0：向上箭头，1：向下箭头
     */
    public void setStretch(int status){
        if(status == -1){
            textView.setLines(1);
            if(look_all_desc.getVisibility()==View.VISIBLE){
                look_all_desc.setVisibility(View.GONE);
            }
        }else if(status == 0){
            if(look_all_desc.getVisibility()==View.GONE){
                look_all_desc.setVisibility(View.VISIBLE);
            }
            arrowImage.setImageResource(arrowUpId);
            textView.setSingleLine(false);
            textView.setEllipsize(null);
            look_all_text.setText("收起");
        }else if(status == 1){
            if(look_all_desc.getVisibility()==View.GONE){
                look_all_desc.setVisibility(View.VISIBLE);
            }
            arrowImage.setImageResource(arrowDownId);
            textView.setLines(lines);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            look_all_text.setText("查看全部");
        }
        look_all_desc.setTag(status);
    }
}
