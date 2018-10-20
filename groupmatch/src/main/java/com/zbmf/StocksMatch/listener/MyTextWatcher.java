package com.zbmf.StocksMatch.listener;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

public class MyTextWatcher implements TextWatcher {
	
	private EditText editText;
	private WatchListener listener;
	private int MAX_INPUT_TEXT;
	
	public MyTextWatcher(EditText editText,int MAX_INPUT_TEXT){
		this.editText = editText;
		this.MAX_INPUT_TEXT = MAX_INPUT_TEXT;
	}
	
	public void setListener(WatchListener listener){
		this.listener = listener;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		if (listener != null && arg0.length() <= MAX_INPUT_TEXT) {
			listener.afterChanged(arg0);			
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {

	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		Editable editable = editText.getText();  
        int len = editable.length();  
          
        if(len > MAX_INPUT_TEXT)
		{
			int selEndIndex = Selection.getSelectionEnd(editable);
			String str = editable.toString();
			//截取新字符串
			String newStr = str.substring(0,MAX_INPUT_TEXT);
			editText.setText(newStr);
			editable = editText.getText();

			//新字符串的长度
			int newLen = editable.length();
			//旧光标位置超过字符串长度
			if(selEndIndex > newLen)
			{
				selEndIndex = editable.length();
			}
			//设置新光标所在的位置
			Selection.setSelection(editable, selEndIndex);

		}
		if (listener != null) {
			listener.onChanged();
		}
	}
	
	public abstract interface WatchListener{
		public abstract void afterChanged(Editable arg0);
		public abstract void onChanged();
	} 

}
