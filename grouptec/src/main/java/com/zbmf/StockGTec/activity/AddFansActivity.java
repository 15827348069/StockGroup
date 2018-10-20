package com.zbmf.StockGTec.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.FansPriceAdapter;
import com.zbmf.StockGTec.beans.FansPrice;
import com.zbmf.StockGTec.view.GridViewForScrollView;

import java.util.ArrayList;
import java.util.List;

public class AddFansActivity extends ExActivity implements View.OnClickListener{
    private FansPriceAdapter adapter;
    private GridViewForScrollView price_view;
    private List<FansPrice>infolist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fans);
        init();
    }

    private void init() {
        infolist=new ArrayList<>();
        adapter=new FansPriceAdapter(getBaseContext(),infolist);
        price_view= (GridViewForScrollView) findViewById(R.id.fans_price_gridview);
        price_view.setAdapter(adapter);
        getPrice();
    }
    public void getPrice(){
        for(int i=0;i<6;i++){
            FansPrice fp=new FansPrice();
            fp.setIs_checked(false);
            fp.setPrice(i+"0");
            fp.setTitle(i+"天体验");
            infolist.add(fp);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sure_add_fans_button:
                Toast.makeText(getBaseContext(),"加入铁粉",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
