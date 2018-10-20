package com.zbmf.StocksMatch.widget;

/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.zbmf.StocksMatch.R;
import com.zbmf.StocksMatch.api.WSError;
import com.zbmf.StocksMatch.beans.General;
import com.zbmf.StocksMatch.utils.UiCommon;

/**
 * Wrapper around UserTask & ProgressDialog
 * 
 * @author Lukasz Wisniewski
 */
public abstract class LoadingDialog<Input, Result> extends AsyncTask<Input, WSError, Result>{

	private ProgressDialog mProgressDialog;
	protected Context mActivity;
	private int mLoadingMsg;
	private int mFailMsg;
	private boolean mEnddismiss = true;
	private boolean Cancelable = true;
	private boolean isNoTip = false;

	public LoadingDialog(Context activity, int loadingMsg, int failMsg){
		this.mActivity = activity;
		this.mLoadingMsg = loadingMsg;
		this.mFailMsg = failMsg;
	}
	
	public LoadingDialog(Context activity, boolean Enddismiss, boolean isNoTip){
		this.mActivity = activity;
		this.mEnddismiss = Enddismiss;
		this.isNoTip = isNoTip;
	}
	
	public LoadingDialog(Context activity, int loadingMsg, int failMsg,boolean Enddismiss){
		this.mActivity = activity;
		this.mLoadingMsg = loadingMsg;
		this.mFailMsg = failMsg;
		this.mEnddismiss = Enddismiss;
	}
	
	public LoadingDialog(Context activity, int loadingMsg, int failMsg,boolean Enddismiss,boolean Cancelable){
		this.mActivity = activity;
		this.mLoadingMsg = loadingMsg;
		this.mFailMsg = failMsg;
		this.mEnddismiss = Enddismiss;
		this.Cancelable = Cancelable;
	}

	@Override
	public void onCancelled() {
		failMsg();
		super.onCancelled();
	}

	@Override
	public void onPreExecute() {
		if (this.mEnddismiss){
		String title = "";
		String message = mActivity.getString(mLoadingMsg);
			mProgressDialog = ProgressDialog.show(mActivity, title, message, true, true, null);
			mProgressDialog.setCancelable(Cancelable);
		}
		super.onPreExecute();
	}

	@Override
	public abstract Result doInBackground(Input... params);

	@Override
	public void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (this.mEnddismiss) mProgressDialog.dismiss();
		if(result != null ){
			if(result instanceof General){
				General ret = (General)result;
				if(ret.getCode() == 1004){
					UiCommon.INSTANCE.showTip(mActivity.getString(R.string.err_tip));
					UiCommon.INSTANCE.logout(mActivity);
					return;
				}
			}
			doStuffWithResult(result);
		} else {
			failMsg();
		}
	}
	
	protected void failMsg(){
		if(!this.isNoTip)
			UiCommon.INSTANCE.showTip(mActivity.getString(mFailMsg));
	}
	
	/**
	 * Very abstract function hopefully very meaningful name,
	 * executed when result is other than null
	 * 
	 * @param result
	 * @return
	 */
	public abstract void doStuffWithResult(Result result);
	
	@Override
	protected void onProgressUpdate(WSError... values) {
		UiCommon.INSTANCE.showTip(values[0].getMessage());
		mProgressDialog.dismiss();
		super.onProgressUpdate(values);
	}

}
