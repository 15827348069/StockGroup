package com.zbmf.worklibrary.presenter;



import com.zbmf.worklibrary.model.BaseMode;
import com.zbmf.worklibrary.baseview.BaseView;


/**
 * Created by xuhao
 * on 2017/11/21.
 */
public abstract class BasePresenter<M extends BaseMode,V extends BaseView> implements IBasePresenter{
     private M mMode;
     private V mView;
     private boolean isFirst=true;
     public abstract void getDatas();
     public abstract M initMode();
     @Override
     public void onDestroy() {
          if(mMode!=null){
               mMode.onDestory();
               mMode=null;
          }
          if(mView!=null){
               mView=null;
          }
     }

     public boolean isFirst() {
          return isFirst;
     }

     public void setFirst(boolean first) {
          isFirst = first;
     }

     @Override
     public void onStart(BaseView view) {
          mView= (V) view;
          mMode=initMode();
     }

     public M getMode() {
          return mMode;
     }


     public V getView(){
          return mView;
     }
}