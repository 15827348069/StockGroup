package com.zbmf.StocksMatch.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.adapter.AnnouncementAdapter;
import com.zbmf.StocksMatch.api.Get2Api;
import com.zbmf.StocksMatch.api.Get2ApiImpl;
import com.zbmf.StocksMatch.beans.Announcement;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.utils.DataLoadDirection;
import com.zbmf.StocksMatch.utils.UiCommon;
import com.zbmf.StocksMatch.widget.LoadingDialog;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshBase;
import com.zbmf.StocksMatch.widget.pulltorefresh.PullToRefreshListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 大赛公告
 * @author lulu
 */
public class AnnouncementActivity extends ExActivity  {

    private PullToRefreshListView content_view;
    private TextView tv_title;
    private Get2Api server = null;
    private static final int PAGE_SIZE = 12;//每页显示数量
    private static int PAGE_INDEX = 1;//当前页码
    private List<Announcement> list = new ArrayList<Announcement>();
    private AnnouncementAdapter adapter;
    private MatchBean matchBean;
    private String title="<!DOCTYPE html>\n" +
            "<html lang=\"zh-cn\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no\">\n" +
            "    <title>", title1 = "</title>\n" +
            "        <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/wap/bootstrap/bootstrap.min.css?16825001.css\" media=\"screen\" />\n" +
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"/css/wap/match.css?16825001.css\" media=\"screen\" />\n" +
            "    <script type=\"text/javascript\" src=\"/_src/js/wap/jquery-2.1.1.min.js\"></script>\n" +
            "</head>\n" +
            "<body>";

    private String end = "</body>\n" +
            "</html>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        getData();
        setupView();
    }

    private void getData() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            matchBean = (MatchBean) bundle.getSerializable("matchbean");
        }
    }
    StringBuilder sb;
    private void setupView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText(R.string.board);
        content_view = (PullToRefreshListView) findViewById(R.id.content_view);
        content_view.setMode(PullToRefreshBase.Mode.BOTH);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        adapter = new AnnouncementAdapter(this);
        adapter.setList(list);
        content_view.setAdapter(adapter);
        content_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Announcement announcement = (Announcement) parent.getItemAtPosition(position);
                sb = new StringBuilder();
                String str = sb.append(title).append(announcement.getSubject()).append(title1).append(announcement.getContent()).append(end).toString();

                String path = UiCommon.INSTANCE.DEFAULT_DATA_FILE;
                String fileName = announcement.getTopic_id() + ".html";
                Bundle bundle = new Bundle();
                bundle.putString("title", announcement.getSubject());
                bundle.putString("web_url", str);
                bundle.putInt("soure_act", 8);//加载HTML代码

//                bundle.putString("web_url", "file:///"+path+"/18706055.html");
//                UiCommon.INSTANCE.writeFileToSDFromInput(path,fileName,new ByteArrayInputStream(str.getBytes()));
                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_TEXT, bundle);
//                UiCommon.INSTANCE.showActivity(UiCommon.ACTIVITY_IDX_ACCOUNT_Web,bundle);
            }
        });

        content_view.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetAnnouncements(AnnouncementActivity.this).execute(DataLoadDirection.Refresh, 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetAnnouncements(AnnouncementActivity.this).execute(DataLoadDirection.LoadMore,PAGE_INDEX);
            }
        });

        if(matchBean!=null){
//            tv_title.setText(UiCommon.INSTANCE.subTitle(matchBean.getTitle()));
            if(isFirstIn)
                showDialog(this,R.string.loading);
            new GetAnnouncements(this).execute(DataLoadDirection.Refresh, 1);
        }

    }

    private boolean isFirstIn = true;
    private class GetAnnouncements extends LoadingDialog<Integer, Announcement> {

        private int operation;
        private int page;

        public GetAnnouncements(Context activity) {
            super(activity, R.string.loading, R.string.load_fail1,false);
        }


        @Override
        public Announcement doInBackground(Integer... params) {
            operation = params[0];
            page = params[1];
            Announcement ret = null;

            if (operation == DataLoadDirection.Refresh) {
                page = 1;
            } else {
                page++;
            }
            if(server == null){
                server = new Get2ApiImpl();
            }

            try {
                ret = server.getAnnouncements(matchBean.getId(), page, PAGE_SIZE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ret;
        }

        @Override
        public void onPostExecute(Announcement ret) {
            super.onPostExecute(ret);
            content_view.onRefreshComplete();
            DialogDismiss();
        }

        @Override
        public void doStuffWithResult(Announcement result) {
            if (result != null && result.code != -1) {
                if (result.getStatus()==1) {
                    if(result.getList()!=null){
                        if (operation == DataLoadDirection.Refresh)
                            list.clear();
                        adapter.addList(result.getList());
//                        for (int i=0;i<result.getList().size();i++){
//                        Announcement announcement = tempList.get(i);
//                        String str = sb.append(head).append(announcement.getContent()).append(end).toString();
//                        String path = UiCommon.INSTANCE.DEFAULT_DATA_FILE;
//                        String fileName = announcement.getTopic_id()+".html";
//                        UiCommon.INSTANCE.writeFileToSDFromInput(path,fileName,new ByteArrayInputStream(str.getBytes()));
//                        }
                        /*if (page != result.getPages()) {
                            //还有数据
                            content_view.setMode(PullToRefreshBase.Mode.BOTH);
                        } else {
                            content_view.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }*/

                        if(page>result.getPages())
                            UiCommon.INSTANCE.showTip(getString(R.string.load_comlete));
                        PAGE_INDEX = page;
                    }
                } else {
                    UiCommon.INSTANCE.showTip(result.msg);
                }
            }else {
                UiCommon.INSTANCE.showTip(getString(R.string.load_fail));
            }
        }
    }


}
