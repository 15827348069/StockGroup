package com.zbmf.StocksMatch.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zbmf.StocksMatch.MatchApplication;
import com.zbmf.StocksMatch.R;

/**
 * Created by pq
 * on 2018/5/15.
 */

public class ToastUtils extends Toast{
    private static ToastUtils mToastUtils=null;
    private static Toast mToast=null;
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public ToastUtils(Context context) {
        super(context);
    }

    public static Toast getmToast(){
        if (mToast==null){
            synchronized (ToastUtils.class){
                if (mToast==null){
                    mToast=new Toast(MatchApplication.getContext());
                }
            }
        }
        return mToast;
    }

    /**
     * 单例Toast
     * @return
     */
    public static ToastUtils instanceToast(){
        if (mToastUtils==null){
            synchronized (ToastUtils.class){
                if (mToastUtils==null){
                    mToastUtils=new ToastUtils(MatchApplication.getContext());//这里使用ApplicationContext,防止内存泄露
                }
            }
        }
        return mToastUtils;
    }

    /**
     * 带图片的toast  正方形的吐司
     * @param tip 文字提示
     * @param tipImg 提示图片
     * @param time 提示时间
     */
    public static void showSquareImgToast(Context context,CharSequence tip,Drawable tipImg,int time){
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(MatchApplication.getContext()).inflate(R.layout.custom_view, null);
        ImageView toastIv1 = view.findViewById(R.id.toastIv1);
        TextView toastTv1 = view.findViewById(R.id.toastTv1);
        if (tipImg!=null){
            toastIv1.setImageDrawable(tipImg);
        }
        if (!TextUtils.isEmpty(tip)){
            toastTv1.setTextSize(14);
            toastTv1.setText(tip);
        }
        //设置toast的显示时间
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * 设置带动画的吐司  正方形的吐司
     * @param type
     */


    /**
     * 设置正方形的纯文字吐司
     */
      public static void showSquareTvToast(String tv){
          Toast toast = new Toast(MatchApplication.getInstance());
          View view = LayoutInflater.from(MatchApplication.getContext()).inflate(R.layout.custom_view, null);
          ImageView singleIv = view.findViewById(R.id.toastIv1);
          TextView toastTv = view.findViewById(R.id.toastTv1);
          singleIv.setVisibility(View.GONE);
          toastTv.setTextSize(14);
          toastTv.setText(tv);
          toast.setView(view);
          toast.setDuration(Toast.LENGTH_SHORT);
          toast.setGravity(Gravity.CENTER,0,0);
          toast.show();
      }

    /**
     * 单例Toast
     */
    public static void showSingleToast(String tv){
        if (mToast==null){
            getmToast();
        }
        View view = LayoutInflater.from(MatchApplication.getContext()).inflate(R.layout.custom_view, null);
        ImageView singleIv = view.findViewById(R.id.toastIv1);
        TextView toastTv = view.findViewById(R.id.toastTv1);
        singleIv.setVisibility(View.GONE);
        toastTv.setText(tv);
        toastTv.setTextSize(14);
        mToast.setView(view);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 带图片的正方形单利吐司
     * @param tv
     */
    public static void showSquareImgSingleToast(String tv,Drawable tipImg){
        if (mToast==null){
            getmToast();
        }
        View view = LayoutInflater.from(MatchApplication.getContext()).inflate(R.layout.custom_view, null);
        ImageView singleTv = view.findViewById(R.id.toastIv1);
        TextView toastTv1 = view.findViewById(R.id.toastTv1);
        if (tipImg!=null){
            singleTv.setImageDrawable(tipImg);
        }
        if (!TextUtils.isEmpty(tv)){
            toastTv1.setTextSize(14);
            toastTv1.setText(tv);
        }
        //设置toast的显示时间
        mToast.setView(view);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

    /**
     * 设置长方形的带图片的吐司
     *@param type
     */


    /**
     * 设置长方形的带动画效果的吐司
     * @param type
     */


    /**
     * 设置长方形的纯文字吐司
     */
      public static void rectangleSingleToast(String tip){
          if (mToast==null){
              getmToast();
          }
          View view = LayoutInflater.from(MatchApplication.getContext()).inflate(R.layout.rectangle_toast_view, null);
          TextView toastTv1 = view.findViewById(R.id.tv);
          if (!TextUtils.isEmpty(tip)){
              toastTv1.setTextSize(14);
              toastTv1.setText(tip);
          }
          //设置toast的显示时间
          mToast.setView(view);
          mToast.setDuration(Toast.LENGTH_SHORT);
          mToast.setGravity(Gravity.CENTER,0,0);
          mToast.show();
      }


//    public ToastUtils setGravity(){
//
//    }
    public void setDuration(int type){
        if (type==0){
            mToastUtils.setDuration(Toast.LENGTH_SHORT);
        }else if (type==1){
            mToastUtils.setDuration(Toast.LENGTH_LONG);
        }
    }

    public static void showShortToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
    public static void showLongToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
