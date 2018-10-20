package com.zbmf.StockGroup.listener;

import android.util.Log;

import com.bokecc.sdk.mobile.live.DWLive;
import com.bokecc.sdk.mobile.live.DWLiveListener;
import com.bokecc.sdk.mobile.live.Exception.DWLiveException;
import com.bokecc.sdk.mobile.live.pojo.Answer;
import com.bokecc.sdk.mobile.live.pojo.ChatMessage;
import com.bokecc.sdk.mobile.live.pojo.PrivateChatInfo;
import com.bokecc.sdk.mobile.live.pojo.QualityInfo;
import com.bokecc.sdk.mobile.live.pojo.Question;
import com.bokecc.sdk.mobile.live.pojo.QuestionnaireInfo;
import com.zbmf.StockGroup.utils.LogUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by xuhao on 2017/7/13.
 */

public class MyLiveListener implements DWLiveListener {
    private onLiveStatus liveStatus;
    public void setStatusListener(onLiveStatus liveStatus){
        this.liveStatus=liveStatus;
    }
    private onStreamEnd streamEnd;
    public void setStreamEnd(onStreamEnd streamEnd){
        this.streamEnd=streamEnd;
    }
    private onUserCountMessage userCountMessage;
    public void setUserCountMessage(onUserCountMessage userCountMessage){
        this.userCountMessage=userCountMessage;
    }
    private onKickOut onKickOut;
    public void setOnKickOut(onKickOut onKickOut){
        this.onKickOut=onKickOut;
    }
    /**
     * 直播状态
     */
    public interface onLiveStatus{
         void onLiveStatus(DWLive.PlayStatus playStatus);
    }
    /**
     * 直播结束
     */
    public interface onStreamEnd{
        void onStreamEnd(boolean b);
    }
    /**
     * 观看直播人数
     */
    public interface onUserCountMessage{
        void onUserCountMessage(int i);
    }

    public interface onInformation{
        void onInformation(String s);
    }
    /**
     * 被踢出直播室
     */
    public interface onKickOut{
        void onKickOut();
    }


    @Override
    public void onQuestion(Question question) {

    }

    @Override
    public void onPublishQuestion(String s) {

    }

    @Override
    public void onAnswer(Answer answer) {

    }

    @Override
    public void onLiveStatus(DWLive.PlayStatus playStatus) {
        if(liveStatus!=null){
            liveStatus.onLiveStatus(playStatus);
        }
    }

    @Override
    public void onStreamEnd(boolean b) {
        if(streamEnd!=null){
            streamEnd.onStreamEnd(b);
        }
    }

    @Override
    public void onHistoryChatMessage(ArrayList<ChatMessage> arrayList) {

    }

    @Override
    public void onPublicChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onSilenceUserChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onPrivateQuestionChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onPrivateAnswerChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onPrivateChat(PrivateChatInfo privateChatInfo) {

    }

    @Override
    public void onPrivateChatSelf(PrivateChatInfo privateChatInfo) {

    }

    @Override
    public void onUserCountMessage(int i) {
        if(userCountMessage!=null){
            userCountMessage.onUserCountMessage(i);
        }
    }

    @Override
    public void onNotification(String s) {

    }

    @Override
    public void onBroadcastMsg(String s) {

    }

    @Override
    public void onInformation(String s) {

    }

    @Override
    public void onException(DWLiveException e) {
        LogUtil.e(e.getMessage());
    }

    @Override
    public void onInitFinished(int i, List<QualityInfo> list) {

    }

    @Override
    public void onKickOut() {
        if(onKickOut!=null){
            onKickOut.onKickOut();
        }
    }

    @Override
    public void onLivePlayedTime(int i) {

    }

    @Override
    public void onLivePlayedTimeException(Exception e) {

    }

    @Override
    public void isPlayedBack(boolean b) {

    }

    @Override
    public void onStatisticsParams(Map<String, String> map) {

    }

    @Override
    public void onCustomMessage(String s) {

    }

    @Override
    public void onBanStream(String s) {

    }

    @Override
    public void onUnbanStream() {

    }

    @Override
    public void onAnnouncement(boolean b, String s) {

    }

    @Override
    public void onRollCall(int i) {

    }

    @Override
    public void onStartLottery(String s) {

    }

    @Override
    public void onLotteryResult(boolean b, String s, String s1, String s2) {

    }

    @Override
    public void onStopLottery(String s) {

    }

    @Override
    public void onVoteStart(int i, int i1) {

    }

    @Override
    public void onVoteStop() {

    }

    @Override
    public void onVoteResult(JSONObject jsonObject) {

    }

    @Override
    public void onQuestionnairePublish(QuestionnaireInfo questionnaireInfo) {

    }

    @Override
    public void onQuestionnaireStop(String s) {

    }
}
