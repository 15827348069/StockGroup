package com.zbmf.StockGroup.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StockGroup.R;
import com.zbmf.StockGroup.adapter.SearchGroupadapter;
import com.zbmf.StockGroup.adapter.SearchParentTagAdapter;
import com.zbmf.StockGroup.api.JSONHandler;
import com.zbmf.StockGroup.api.WebBase;
import com.zbmf.StockGroup.beans.Group;
import com.zbmf.StockGroup.beans.TagBean;
import com.zbmf.StockGroup.callback.ResultCallback;
import com.zbmf.StockGroup.utils.JSONParse;
import com.zbmf.StockGroup.utils.SettingDefaultsManager;
import com.zbmf.StockGroup.utils.ShowActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    List<Group> searchlistData;
    private ListView listView;
    SearchGroupadapter adapter;
    private EditText search_edittext;
    private int page, pages;
    private List<TagBean> tagBeanList ;
    private SearchParentTagAdapter tagAdapter;
    private RecyclerView tagRecyclerView;
    private boolean history,hotsearch,actionsearch;
    private LinearLayout noMessage;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.listView1);
        search_edittext = (EditText) findViewById(R.id.search_edittext);
        tagRecyclerView = getView(R.id.search_rv);
        noMessage=getView(R.id.line_no_search);
        getView(R.id.imb_search_clear).setOnClickListener(this);
    }

    @Override
    public void initData() {
        tagBeanList=new ArrayList<>();
        searchlistData = new ArrayList<>();
        adapter = new SearchGroupadapter(this, searchlistData);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group=searchlistData.get(position);
                SettingDefaultsManager.getInstance().setSearchHistory(new TagBean.ChildrenTag(group.getNick_name(),group.getId(),0));
                ShowActivity.showGroupDetailActivity(SearchActivity.this,group );
            }
        });

        LinearLayoutManager layoutManage = new LinearLayoutManager(getBaseContext());
        layoutManage.setOrientation(LinearLayoutManager.VERTICAL);
        tagRecyclerView.setLayoutManager(layoutManage);
        tagAdapter = new SearchParentTagAdapter(SearchActivity.this, tagBeanList);
        tagAdapter.setOnItemClickLitener(new SearchParentTagAdapter.OnItemClickLitener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onParenItemClick(TagBean.ChildrenTag childrenTag, int position) {
                ShowActivity.showGroupDetailActivity(SearchActivity.this,childrenTag.getId());
            }

            @Override
            public void onDelete(TagBean tagBean) {
                SettingDefaultsManager.getInstance().clearSearchHistory();
                tagBeanList.remove(tagBean);
                tagAdapter.notifyDataSetChanged();
            }
        });
        tagRecyclerView.setAdapter(tagAdapter);
        getSearchHistory();
        getHotSerch();
    }

    @Override
    public void addListener() {
        search_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_SEARCH)) {
                    if(TextUtils.isEmpty(search_edittext.getText())){
                        showToast("请输入搜索条件");
                    }else{
                        actionsearch=true;
                        search_group(search_edittext.getText().toString());
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        search_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(actionsearch){
                    actionsearch=false;
                }
                if (s.length() > 0) {
                    searchlistData.clear();
                    search_group(s.toString());
                } else {
                    getSearchHistory();
                    searchlistData.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSearchHistory();
    }

    private void getSearchHistory() {
        if(tagRecyclerView.getVisibility()==View.GONE){
            tagRecyclerView.setVisibility(View.VISIBLE);
        }
        if(listView.getVisibility()==View.VISIBLE){
            listView.setVisibility(View.GONE);
        }
        if(noMessage.getVisibility()==View.VISIBLE){
            noMessage.setVisibility(View.GONE);
        }
        if(tagBeanList==null){
            tagBeanList=new ArrayList<>();
        }else{
            if(tagBeanList.size()>0&&tagBeanList.get(0).getTag_name().equals("搜索记录")){
                tagBeanList.remove(0);
            }
        }
        SettingDefaultsManager.getInstance().getSearchHistory(new ResultCallback() {
            @Override
            public void onSuccess(Object str) {
                TagBean tagBean = new TagBean();
                tagBean.setTag_name("搜索记录");
                tagBean.setData( (List<TagBean.ChildrenTag>) str);
                tagBeanList.add(0,tagBean);
                if(!history){
                    history=true;
                }
                RushSearchList();
            }

            @Override
            public void onError(String message) {
                if(!history){
                    history=true;
                }
                RushSearchList();
            }
        });
    }
    private void getHotSerch(){
        WebBase.recommendList(6,new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result=obj.optJSONObject("result");
                if(!result.isNull("groups")){
                    TagBean tagBean = new TagBean();
                    tagBean.setTag_name("热门搜索");
                    List<TagBean.ChildrenTag> childrenTags = JSONParse.getGroupTags(result.optJSONArray("groups"));
                    tagBean.setData(childrenTags);
                    tagBeanList.add(tagBean);
                }
                if(!hotsearch){
                    hotsearch=true;
                }
                RushSearchList();
            }

            @Override
            public void onFailure(String err_msg) {
                if(!hotsearch){
                    hotsearch=true;
                }
                RushSearchList();
            }
        });
    }
    private void RushSearchList(){
        if(history&&hotsearch){
            tagAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 搜索圈子
     *
     * @param key
     */
    public void search_group(final String key) {
        WebBase.searchGroup(key, page, new JSONHandler() {
            @Override
            public void onSuccess(JSONObject obj) {
                JSONObject result = obj.optJSONObject("result");
                page = result.optInt("page");
                pages = result.optInt("pages");
                if (!result.isNull("groups")) {
                    searchlistData.clear();
                    List<Group> groups = JSONParse.getGroupList(result.optJSONArray("groups"));
                    if (groups!=null&&groups.size()>0){
//                        int is_private = groups.get(0).getIs_private();
//                        if (is_private==0){//表示公开
                            searchlistData.addAll(groups);
//                        }
                    }
                    if (searchlistData.size() > 0) {
                        tagRecyclerView.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    } else {
                        if(actionsearch){
                            if(noMessage.getVisibility()==View.GONE){
                                noMessage.setVisibility(View.VISIBLE);
                                tagRecyclerView.setVisibility(View.GONE);
                                listView.setVisibility(View.GONE);
                            }
                        }else{
                            getSearchHistory();
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String err_msg) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        search_edittext.setText("");
    }
}
