package com.zbmf.StocksMatch.model;

import com.zbmf.StocksMatch.api.Method;
import com.zbmf.StocksMatch.api.SendParam;
import com.zbmf.StocksMatch.bean.BaseBean;
import com.zbmf.StocksMatch.bean.DealSys;
import com.zbmf.StocksMatch.bean.DealsList;
import com.zbmf.StocksMatch.model.imode.IMatchNewMode;
import com.zbmf.worklibrary.model.CallBack;
import com.zbmf.worklibrary.pullrefreshrecycle.RefreshStatus;
import com.zbmf.worklibrary.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhao
 * on 2017/12/1.
 */

public class MatchNewDiealMode extends BaseMatchMode implements IMatchNewMode {}
