package com.zbmf.StocksMatch.presenter;

import android.text.TextUtils;

import com.zbmf.StocksMatch.bean.SearchMatchBean;
import com.zbmf.StocksMatch.listener.ISearchView;
import com.zbmf.StocksMatch.model.SearchMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.presenter.BasePresenter;
import com.zbmf.worklibrary.util.Logx;

/**
 * Created by pq
 * on 2018/3/21.
 */

public class SearchPresenter extends BasePresenter<SearchMode,ISearchView> {
    private String mKeyWord;

    public SearchPresenter(/*String keyWord*/) {
        /*mKeyWord = keyWord;*/
    }

    public void setKeyWord(String keyWord){
        this.mKeyWord=keyWord;
    }

    @Override
    public void getDatas() {
        if (!TextUtils.isEmpty(mKeyWord)){
            serachMatch(mKeyWord,1,10);
        }
    }

    @Override
    public SearchMode initMode() {
        return new SearchMode();
    }

    //搜索比赛
    public void serachMatch(String keyWord,int page,int per_page){
        getMode().searchMatch(keyWord,page,per_page, new CallBack<SearchMatchBean>() {

            @Override
            public void onSuccess(SearchMatchBean searchMatchBean) {
                if (getView()!=null){
                    getView().searchMatchResult(searchMatchBean);
                }
            }

            @Override
            public void onFail(String msg) {
                Logx.e(msg);
            }
        });
    }
}
