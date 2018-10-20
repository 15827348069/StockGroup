package com.zbmf.groupro.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.groupro.R;
import com.zbmf.groupro.adapter.SearchCiecleadapter;
import com.zbmf.groupro.beans.Group;
import com.zbmf.groupro.utils.Pinyin4j;

import java.text.CollationKey;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    List<Group> listData;
    List<Group> searchlistData;
    private ListView listView;
    SearchCiecleadapter adapter;
    private EditText search_edittext;
    private TextView group_title_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }
    private void init(){
        listView = (ListView) findViewById(R.id.listView1);
        group_title_name= (TextView) findViewById(R.id.group_title_name);
        group_title_name.setText("搜索圈子");
        findViewById(R.id.group_title_return).setOnClickListener(this);
        listData=new ArrayList<Group>();
        searchlistData=new ArrayList<Group>();
        adapter=new SearchCiecleadapter(this,searchlistData);
        listView.setAdapter(adapter);
        search_edittext=(EditText) findViewById(R.id.search_edittext);
        search_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId== EditorInfo.IME_ACTION_SEARCH ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_SEARCH)) {
                    search_group(search_edittext.getText().toString());
                    return true;
                }else{
                    return false;
                }
            }
        });
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                searchlistData.clear();
                if(s.length()>0){
                    for (Group circle : listData) {
                        if(circle.getPingyin().contains(Pinyin4j.getHanyuPinyin(s.toString()))){
                            searchlistData.add(circle);
                        }
                        Collections.sort(searchlistData, new CollatorComparator());
                        adapter.notifyDataSetChanged();
                    }
                    if(searchlistData.size()==0){
                        search_group(s.toString());
                    }
                }else{
                    for (Group circle : listData) {
                        searchlistData.add(circle);
                    }
                    Collections.sort(searchlistData, new CollatorComparator());
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }else{
                        adapter = new SearchCiecleadapter(SearchActivity.this, searchlistData);
                        listView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }
//    /**
//     *
//     * @param id
//     * @param name
//     * @param nick_name
//     * @param avatar
//     * @param exclusives
//     */
    public void search_group(String key){
        listData.clear();
        for(int i=0;i<5;i++){
            if(i%2==0){
                listData.add(new Group(i+"",key+i,key+i,"https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D360/sign=c429bad21dd8bc3ed90800ccb28aa6c8/e7cd7b899e510fb3a78c787fdd33c895d0430c44.jpg",0));
            }else{
                listData.add(new Group(i+"",key+i,key+i,"https://ss0.baidu.com/94o3dSag_xI4khGko9WTAnF6hhy/image/h%3D360/sign=c429bad21dd8bc3ed90800ccb28aa6c8/e7cd7b899e510fb3a78c787fdd33c895d0430c44.jpg",1));
            }
        }
        for (Group circle : listData) {
            if(circle.getPingyin().contains(Pinyin4j.getHanyuPinyin(key))){
                searchlistData.add(circle);
            }
            Collections.sort(searchlistData, new CollatorComparator());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.group_title_return:
                finish();
                break;
        }
    }

    private class CollatorComparator implements Comparator {
        Collator collator = Collator.getInstance();

        public int compare(Object element1, Object element2) {
            CollationKey key1 = collator
                    .getCollationKey(((Group) element1).getIndex());
            CollationKey key2 = collator
                    .getCollationKey(((Group) element2).getIndex());
            return key1.compareTo(key2);
        }
    }
}
