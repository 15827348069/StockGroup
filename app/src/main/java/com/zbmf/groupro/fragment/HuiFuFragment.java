package com.zbmf.groupro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zbmf.groupro.R;

/**
 * Created by xuhao on 2017/2/13.
 */

public class HuiFuFragment extends Fragment {
    private View mfrtagment;
    public static HuiFuFragment newInstance() {
        HuiFuFragment fragment = new HuiFuFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mfrtagment==null){
            mfrtagment=inflater.inflate(R.layout.huifu_fragment_layout,null);
        }
        return mfrtagment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
