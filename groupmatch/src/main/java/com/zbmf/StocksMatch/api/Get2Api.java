package com.zbmf.StocksMatch.api;

import com.zbmf.StocksMatch.beans.Actives;
import com.zbmf.StocksMatch.beans.Announcement;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.beans.Group;
import com.zbmf.StocksMatch.beans.MatchBean;
import com.zbmf.StocksMatch.beans.MatchInfo;
import com.zbmf.StocksMatch.beans.PayInfo;
import com.zbmf.StocksMatch.beans.Quotation;
import com.zbmf.StocksMatch.beans.RecommendPic;
import com.zbmf.StocksMatch.beans.Record;
import com.zbmf.StocksMatch.beans.Stock;
import com.zbmf.StocksMatch.beans.StockholdsBean;
import com.zbmf.StocksMatch.beans.User;
import com.zbmf.StocksMatch.beans.VersionInfo;
import com.zbmf.StocksMatch.beans.Yield;

import org.json.JSONException;

/**
 * 接口
 */
public interface Get2Api {

	/**
	 * 会员OAUTH登录
	 * @param user_name 用户名
	 * @param pass_word 密码
	 * @return
	 * @throws JSONException
     * @throws WSError
     */
	public User Login(String user_name,String pass_word) throws JSONException;

	/**
	 * 获取手机验证码
	 * @param mobile
	 * @return
     */
	public General getPhonecode(String mobile) throws JSONException;


	/**
	 * 注册账号
	 * @param mobile
	 * @param password
	 * @param code
	 * @return
	 * @throws JSONException
     */
	public User signphone(String mobile,String password,String code) throws JSONException;

	/**
	 * 获取用户资料
	 * @param auth_token
	 * @return
	 * @throws JSONException
	 */
	public User getuserinfo2(String auth_token) throws JSONException;

	/**
	 * 会员OAUTH登出
	 *
	 * @return General
	 * @author atan
	 */
	public General Logont() throws JSONException, WSError;


	/**
	 * 获取推荐的比赛别列表
	 * @return
	 * @throws Exception
	 */
	public MatchBean getRecommendMatch() throws JSONException;

	/**
	 *获取推荐广告图片
	 * @return
	 * @throws JSONException
     */
	public RecommendPic getRecommendPic() throws JSONException;

	/**
	 * 获取用户参加的比赛
	 * @return
	 * @throws JSONException
     */
	public MatchBean getRunMatches() throws JSONException;

	public MatchBean getRunMatches(String user_id) throws JSONException;

	/**
	 * 用户比赛详情,包括总收益率,周收益率等
	 * @param match_id
	 * @return
	 * @throws JSONException
	 */
	public MatchInfo getMatchMessage(String match_id,String user_id) throws JSONException;

	/**
	 * 全部比赛,只要是没有过期的比赛
	 * @param page
	 * @param per_page
	 * @return
	 * @throws JSONException
	 */
	public MatchBean getMathNoStopList(int page,int per_page) throws JSONException;

	/**
	 * 报名比赛获取手机验证码
	 * @param match_id
	 * @param truename
	 * @param mobile
	 * @return
	 * @throws JSONException
	 */
	public General getVerifyCode(String match_id,String truename,String mobile) throws JSONException;

	/**
	 * 邀请赛中邀请码验证
	 * @param match_id
	 * @param invite_code
	 * @return
	 * @throws JSONException
	 */
	public General checkInviteCode(String match_id,String invite_code) throws JSONException;

	/**
	 * 报名比赛提交
	 * @param match_id
	 * @param truename
	 * @param mobile
	 * @param code 验证码
	 * @return
	 * @throws JSONException
	 */
	public MatchBean applyMatch(String match_id,String truename,String mobile,String code) throws JSONException;

	/**
	 * 获取自选股列表数据
	 * @return
	 * @throws JSONException
	 */
	public Quotation getFocusList() throws JSONException;

	/**
	 * 获取大盘行情数据
	 * @return
	 * @throws JSONException
	 */
	public Quotation getStockRealtimeInfo2() throws JSONException;

	/**
	 * 股票信息
	 * @param symbols
	 * @return
	 * @throws JSONException
	 */
	public Stock getStockRealtimeInfo(String symbols) throws JSONException;

	/**
	 * 赛事的股票信息
	 * @param symbols
	 * @param id 大赛ID
	 * @return
	 * @throws JSONException
	 */
	public Stock getRealtimeInfo(String id,String symbols) throws JSONException;
	/**
	 * 添加自选股
	 * @param symbol
	 * @return
	 * @throws JSONException
	 */
	public General focus2(String symbol) throws JSONException;

	/**
	 * 删除自选股
	 * @param symbol
	 * @return
	 * @throws JSONException
	 */
	public General defocus(String symbol) throws JSONException;

	/**
	 * 用户搜索
	 * @param keyword
	 * @return
	 * @throws JSONException
	 */
	public User searchUsers(String keyword) throws JSONException;

	/**
	 * 比赛搜索
	 * @param keyword
	 * @return
	 * @throws JSONException
	 */
	public MatchBean searchMatch(String keyword) throws JSONException;

	/**
	 * 获取推荐活动
	 * @return
	 * @throws JSONException
	 */
	public Actives getActives()throws JSONException;

	/**
	 * 获取关注的用户
	 * @param page
	 * @param per_page
	 * @return
	 * @throws JSONException
	 */
	public Group getUserGroups(int page,int per_page) throws JSONException;

	/**
	 * 取消/关注
	 * @param group_id
	 * @return
	 * @throws JSONException
	 */
	public General groups_quit(String group_id,String method) throws JSONException;

	/**
	 * 信息
	 * @return
	 * @throws JSONException
	 */
	public User UserMore() throws JSONException;

	/**
	 * 意见反馈
	 * @param content
	 * @return
	 * @throws JSONException
     */
	public General suggests(String content) throws JSONException;

	/**
	 * 亲 你的获奖记录
	 * @param user_id
	 * @param match_id
	 * @param page
	 * @param per_page
	 * @return
	 * @throws JSONException
	 */
	public Record getWinRecords(String user_id,String match_id,int page,int per_page) throws JSONException;

	/**
	 * 炒股大赛公告
	 * @param match_id
	 * @param page
	 * @param per_page
	 * @return
	 * @throws JSONException
	 */
	public Announcement getAnnouncements(String match_id,int page,int per_page)throws JSONException;

	/**
	 * 榜单list
	 * @param match_id
	 * @param order
	 * @param page
	 * @param per_page
	 * @return
	 * @throws JSONException
	 */
	public Yield getYieldList(String match_id,String order, int page, int per_page) throws JSONException;

	/**
	 * 持仓
	 * @param match_id
	 * @param user_id
	 * @param page
	 * @param per_page
	 * @return
	 * @throws JSONException
	 */
	public StockholdsBean getHoldlist(String match_id,String user_id,int page,int per_page) throws JSONException;

	/**
	 * 委托
	 * @param match_id
	 * @param page
	 * @param per_page
	 * @return
	 * @throws JSONException
	 */
	public StockholdsBean getOrderList(String match_id,int page,int per_page) throws JSONException;

	/**
	 * 交易记录
	 * @param match_id
	 * @param user_id
	 * @param page
	 * @param per_page
	 * @return
	 * @throws JSONException
	 */
	public StockholdsBean getDeallogList(String match_id, String user_id,int page, int per_page) throws JSONException;

	/**
	 * 炒股大赛用户详情页
	 * @param user_id
	 * @return
	 * @throws JSONException
	 */
	public User UserInfo(String user_id) throws JSONException;

	/**
	 * STOCK BUY
	 * @param symbol
	 * @param price
	 * @param volumn
	 * @param match_id
	 * @return
     * @throws JSONException
     */
	public General buy(String symbol,String price,String volumn,String match_id) throws JSONException;

	/**
	 * STOCK SELL
	 * @param symbol
	 * @param price
	 * @param volumn
	 * @param match_id
	 * @return
     * @throws JSONException
     */
	public General sell(String symbol,String price,String volumn,String match_id) throws JSONException;

	public User iconupload(String nickname,String avatar) throws JSONException;

	/**
	 * 魔方宝购买单只股票页面
	 * @return
	 * @throws JSONException
	 */
	public PayInfo PayStockTem() throws JSONException;

	/**
	 * 魔方宝购买查看单只股票
	 * @return
	 * @throws JSONException
	 */
	public General PayStock(String user_id,String match_id,String symbol) throws JSONException;

	/**
	 * 合作登陆-sina
	 * @param token
	 * @param openid
	 * @param api_type
	 * @return
	 * @throws JSONException
	 */
	public String getAccessTokenByOpenapi(String token,String openid,String api_type) throws JSONException;

	/**
	 * 合作登陆-webchat
	 * @param code
	 * @return
	 * @throws JSONException
     */
	public String matchWechat(String code) throws JSONException;

	/**
	 * 撤销合作赛赛事委托
	 * @param match_id
	 * @param id
	 * @return
	 * @throws JSONException
     */
	public General withdraw(String match_id,String id) throws JSONException;

	public VersionInfo version() throws JSONException;
	public Stock updateDB() throws JSONException;
}