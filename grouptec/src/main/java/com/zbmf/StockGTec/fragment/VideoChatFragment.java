package com.zbmf.StockGTec.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.zbmf.StockGTec.R;
import com.zbmf.StockGTec.adapter.MessageAdapter;
import com.zbmf.StockGTec.api.JSONHandler;
import com.zbmf.StockGTec.api.WebBase;
import com.zbmf.StockGTec.beans.ChatMessage;
import com.zbmf.StockGTec.beans.Videos;
import com.zbmf.StockGTec.utils.ChatMessageComparator;
import com.zbmf.StockGTec.utils.DateUtil;
import com.zbmf.StockGTec.utils.JSONparse;
import com.zbmf.StockGTec.view.PullToRefreshListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.zbmf.StockGTec.R.id.listview;


public class VideoChatFragment extends Fragment {

    private List<ChatMessage> messages = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private Videos mVideo;
    private Timer mTimer = null;

    private String time;
    private String lastTime="";
    private ListView mRefreshableView;

    public VideoChatFragment() {
    }

    public static VideoChatFragment newInstance(Videos videos) {
        VideoChatFragment fragment = new VideoChatFragment();
        Bundle args = new Bundle();
        args.putSerializable("video", videos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVideo = (Videos) getArguments().getSerializable("video");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PullToRefreshListView psr = (PullToRefreshListView) view.findViewById(listview);
        mRefreshableView = psr.getRefreshableView();
        messageAdapter = new MessageAdapter(0,getActivity(), messages);
        mRefreshableView.setAdapter(messageAdapter);

        if (mTimer == null)
            mTimer = new Timer();

        mTimer.schedule(mTimerTask, 200, 5000);
    }

    private void getVideoMsg() {
        WebBase.getVideoMsg(mVideo.getVideo_id(), time,lastTime, new JSONHandler(false, getActivity(), "正在加载数据...") {
            @Override
            public void onSuccess(JSONObject obj) {
                ChatMessage chatMessage = JSONparse.getRoomMsg(obj);
                if (chatMessage != null && chatMessage.getMessages() != null) {
                    List<ChatMessage> tempMessages = chatMessage.getMessages();
                    Collections.sort(tempMessages, new ChatMessageComparator());
                    messages.addAll(tempMessages);
                    messageAdapter.notifyDataSetChanged();
                    mRefreshableView.smoothScrollToPosition(messageAdapter.getCount());
                }
            }

            @Override
            public void onFailure(String err_msg) {
                Toast.makeText(getActivity(), err_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    time = DateUtil.getTimes() + "";
                    if(messages.size()>0){
                        lastTime=messages.get(messages.size()-1).getTime();
                    }else{
                        lastTime="";
                    }
                    getVideoMsg();
                }
            });

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mTimer.cancel();
        mTimer=null;
    }
}
