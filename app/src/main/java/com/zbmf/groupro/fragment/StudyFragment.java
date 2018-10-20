package com.zbmf.groupro.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zbmf.groupro.R;

/**
 * Created by xuhao on 2017/2/13.
 */

public class StudyFragment extends GroupBaseFragment {
    private TextView no_message_text;
    public static StudyFragment newInstance() {
        StudyFragment fragment = new StudyFragment();
        return fragment;
    }
    @Override
    protected View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.study_fragment_layout,null);
    }
    @Override
    protected void initView() {
        //初始化控件
        no_message_text=getView(R.id.no_message_text);
        getView(R.id.ll_none).setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {
        //初始化数据
        no_message_text.setText("敬请期待");
    }
}
